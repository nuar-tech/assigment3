package controller;

import entity.Book;
import entity.Member;
import entity.Loan;
import repository.BookRepository;
import repository.MemberRepository;
import service.LoanService;
import exception.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LibraryController {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanService loanService;

    public LibraryController() {
        this.bookRepository = new BookRepository();
        this.memberRepository = new MemberRepository();
        this.loanService = new LoanService();
    }

    // User Story: Borrow a book
    public String borrowBook(int bookId, int memberId, LocalDate dueDate) {
        try {
            Loan loan = loanService.borrowBook(bookId, memberId, dueDate);
            return "Book borrowed successfully. Loan ID: " + loan.getId();
        } catch (BookAlreadyOnLoanException e) {
            return "Error: " + e.getMessage();
        } catch (MemberNotFoundException e) {
            return "Error: " + e.getMessage();
        } catch (LibraryException | SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    // User Story: Return a book
    public String returnBook(int loanId) {
        try {
            double fine = loanService.returnBook(loanId);
            if (fine > 0) {
                return String.format("Book returned. Fine: $%.2f", fine);
            }
            return "Book returned successfully. No fine.";
        } catch (LibraryException | SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    // User Story: View current loans per member
    public String viewCurrentLoans(int memberId) {
        try {
            List<Loan> loans = loanService.getCurrentLoans(memberId);
            if (loans.isEmpty()) {
                return "No current loans for this member.";
            }

            StringBuilder sb = new StringBuilder("Current Loans:\n");
            for (Loan loan : loans) {
                sb.append(String.format("  Loan ID: %d, Book ID: %d, Due: %s\n",
                        loan.getId(), loan.getBookId(), loan.getDueDate()));
            }
            return sb.toString();
        } catch (SQLException e) {
            return "Error retrieving loans: " + e.getMessage();
        }
    }

    // User Story: List available books
    public String listAvailableBooks() {
        try {
            List<Book> books = bookRepository.findAvailableBooks();
            if (books.isEmpty()) {
                return "No available books.";
            }

            StringBuilder sb = new StringBuilder("Available Books:\n");
            for (Book book : books) {
                sb.append(String.format("  ID: %d, Title: %s, Author: %s\n",
                        book.getId(), book.getTitle(), book.getAuthor()));
            }
            return sb.toString();
        } catch (SQLException e) {
            return "Error retrieving books: " + e.getMessage();
        }
    }
}
