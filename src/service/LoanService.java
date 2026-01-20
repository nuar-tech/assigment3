package service;

import edu.aitu.oop3.db.DatabaseConnection;
import entity.Loan;
import entity.Book;
import entity.Member;
import repository.BookRepository;
import repository.MemberRepository;
import exception.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final FineCalculator fineCalculator;

    public LoanService() {
        this.bookRepository = new BookRepository();
        this.memberRepository = new MemberRepository();
        this.fineCalculator = new FineCalculator();
    }

    // Borrow a book
    public Loan borrowBook(int bookId, int memberId, LocalDate dueDate)
            throws LibraryException, SQLException {

        // Check if book exists and is available
        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new LibraryException("Book not found");
        }
        if (!book.isAvailable()) {
            throw new BookAlreadyOnLoanException(bookId);
        }

        // Check if member exists
        memberRepository.findById(memberId); // Throws MemberNotFoundException if not found

        // Create loan record
        Loan loan = new Loan(bookId, memberId, dueDate);

        // Save to database (simplified - in practice, use LoanRepository)
        String sql = "INSERT INTO loans (book_id, member_id, due_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, bookId);
            pstmt.setInt(2, memberId);
            pstmt.setDate(3, Date.valueOf(dueDate));
            pstmt.executeUpdate();

            // Update book availability
            bookRepository.updateAvailability(bookId, false);

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                loan.setId(rs.getInt(1));
            }
        }

        return loan;
    }

    // Return a book
    public double returnBook(int loanId) throws LibraryException, SQLException {
        String selectSql = "SELECT * FROM loans WHERE id = ? AND return_date IS NULL";
        String updateSql = "UPDATE loans SET return_date = ?, fine_amount = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            // Get the loan
            selectStmt.setInt(1, loanId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                throw new LibraryException("Active loan not found");
            }

            LocalDate dueDate = rs.getDate("due_date").toLocalDate();
            int bookId = rs.getInt("book_id");
            LocalDate returnDate = LocalDate.now();

            // Calculate fine
            double fine = fineCalculator.calculateFine(dueDate, returnDate);

            // Update loan with return date and fine
            updateStmt.setDate(1, Date.valueOf(returnDate));
            updateStmt.setDouble(2, fine);
            updateStmt.setInt(3, loanId);
            updateStmt.executeUpdate();

            // Update book availability
            bookRepository.updateAvailability(bookId, true);

            return fine;
        }
    }

    // Get current loans for a member
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
