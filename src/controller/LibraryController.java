package controller;

import CatalogComponent.Book;
import LoanManagementComponent.BookAlreadyOnLoanException;
import LoanManagementComponent.Loan;
import CatalogComponent.BookRepository;
import MemberManagementComponent.MemberNotFoundException;
import MemberManagementComponent.MemberRepository;
import LoanManagementComponent.LoanService;
import LoanManagementComponent.FinePolicy;
import CatalogComponent.BookFactory;
import builder.LoanReport;
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

    public String showFinePolicy() {
        FinePolicy policy = FinePolicy.getInstance();
        return String.format(
                "Fine Policy (Singleton):\n" +
                        "Daily Rate: $%.2f\n" +
                        "Max Fine: $%.2f\n" +
                        "Grace Days: %d",
                policy.getDailyRate(), policy.getMaxFine(), policy.getGraceDays()
        );
    }

    public String createBookUsingFactory(Book.BookType type, String isbn, String title,
                                         String author, int year) {
        Book book = BookFactory.createBook(type, isbn, title, author, year);
        return String.format(
                "Book created using Factory Pattern:\n" +
                        "Type: %s\n" +
                        "Title: %s\n" +
                        "Author: %s\n" +
                        "ISBN: %s",
                book.getBookType(), book.getTitle(), book.getAuthor(), book.getIsbn()
        );
    }

    public String generateLoanReport(int loanId, String bookTitle, String memberName) {
        try {
            List<Loan> loans = loanService.getCurrentLoans(1);
            Loan loan = null;

            if (!loans.isEmpty()) {
                loan = loans.get(0);
                loan.setId(loanId);
            } else {
                loan = new Loan(1, 1, LocalDate.now().plusDays(14));
                loan.setId(loanId);
                loan.setFineAmount(5.75);
            }

            LoanReport report = new LoanReport.Builder()
                    .fromLoan(loan, bookTitle, memberName)
                    .build();

            return "Loan Report (Builder Pattern):\n\n" + report.generateReport();

        } catch (SQLException e) {
            return "Error generating report: " + e.getMessage();
        }
    }

    public String listAllBooksWithTypes() {
        try {
            List<Book> books = bookRepository.findAll();
            if (books.isEmpty()) {
                return "No books in library.";
            }

            StringBuilder sb = new StringBuilder("All Books (via Generic Repository):\n");
            for (Book book : books) {
                sb.append(String.format("  ID: %d, Type: %-10s, Title: %s\n",
                        book.getId(), book.getBookType(), book.getTitle()));
            }
            return sb.toString();
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String demoMilestone2Features() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MILESTONE 2 FEATURES DEMO ===\n\n");

        sb.append("1. SINGLETON PATTERN:\n");
        sb.append(showFinePolicy()).append("\n\n");

        sb.append("2. FACTORY PATTERN:\n");
        sb.append(createBookUsingFactory(
                Book.BookType.PRINTED,
                "978-0132350884",
                "Clean Code",
                "Robert Martin",
                2008
        )).append("\n\n");

        sb.append("3. BUILDER PATTERN:\n");
        sb.append(generateLoanReport(1001, "Effective Java", "Alice Johnson")).append("\n\n");

        sb.append("4. GENERICS:\n");
        sb.append("BookRepository implements CrudRepository<Book, Integer>\n");
        sb.append("Methods: save(), findById(), findAll(), delete()\n");

        return sb.toString();
    }
}