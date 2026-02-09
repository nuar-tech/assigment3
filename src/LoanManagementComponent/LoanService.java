package LoanManagementComponent;

import edu.aitu.oop3.db.DatabaseConnection;
import CatalogComponent.Book;
import CatalogComponent.BookRepository;
import MemberManagementComponent.MemberRepository;
import exception.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final FineCalculator fineCalculator;

    public LoanService() {
        this.bookRepository = new BookRepository();
        this.memberRepository = new MemberRepository();
        this.fineCalculator = new FineCalculator();
    }

    public Loan borrowBook(int bookId, int memberId, LocalDate dueDate)
            throws LibraryException, SQLException {

        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new LibraryException("Book not found");
        }
        if (!book.get().isAvailable()) {
            throw new BookAlreadyOnLoanException(bookId);
        }

        memberRepository.findById(memberId);

        Loan loan = new Loan(bookId, memberId, dueDate);

        String sql = "INSERT INTO loans (book_id, member_id, due_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, bookId);
            pstmt.setInt(2, memberId);
            pstmt.setDate(3, Date.valueOf(dueDate));
            pstmt.executeUpdate();

            bookRepository.updateAvailability(bookId, false);

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                loan.setId(rs.getInt(1));
            }
        }

        return loan;
    }

    public double returnBook(int loanId) throws LibraryException, SQLException {
        String selectSql = "SELECT * FROM loans WHERE id = ? AND return_date IS NULL";
        String updateSql = "UPDATE loans SET return_date = ?, fine_amount = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            selectStmt.setInt(1, loanId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                throw new LibraryException("Active loan not found");
            }

            LocalDate dueDate = rs.getDate("due_date").toLocalDate();
            int bookId = rs.getInt("book_id");
            LocalDate returnDate = LocalDate.now();

            double fine = fineCalculator.calculateFine(dueDate, returnDate);

            updateStmt.setDate(1, Date.valueOf(returnDate));
            updateStmt.setDouble(2, fine);
            updateStmt.setInt(3, loanId);
            updateStmt.executeUpdate();

            bookRepository.updateAvailability(bookId, true);

            return fine;
        }
    }

    public List<Loan> getCurrentLoans(int memberId) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE member_id = ? AND return_date IS NULL";

        try (Connection conn =  DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getInt("id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setMemberId(rs.getInt("member_id"));
                loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
                loan.setDueDate(rs.getDate("due_date").toLocalDate());
                loans.add(loan);
            }
        }
        return loans;
    }
}
