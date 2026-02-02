import controller.LibraryController;
import config.FinePolicy;
import entity.Book;
import factory.BookFactory;
import builder.LoanReport;
import entity.Loan;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final LibraryController controller = new LibraryController();

    public static void main(String[] args) {
        System.out.println("=== Library Management System - Milestone 2 ===");
        System.out.println("Using FinePolicy Singleton: " + FinePolicy.getInstance());

        displayMainMenu();
    }

    private static void displayMainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Book Operations");
            System.out.println("2. Member Operations");
            System.out.println("3. Loan Operations");
            System.out.println("4. View Reports");
            System.out.println("5. System Information");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> bookOperations();
                    case 2 -> memberOperations();
                    case 3 -> loanOperations();
                    case 4 -> viewReports();
                    case 5 -> systemInformation();
                    case 0 -> {
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static void bookOperations() {
        while (true) {
            System.out.println("\n=== BOOK OPERATIONS ===");
            System.out.println("1. List all books");
            System.out.println("2. List available books");
            System.out.println("3. Add new book (Factory Pattern Demo)");
            System.out.println("4. Search book by ID");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> listAllBooks();
                    case 2 -> listAvailableBooks();
                    case 3 -> addNewBook();
                    case 4 -> searchBookById();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static void memberOperations() {
        while (true) {
            System.out.println("\n=== MEMBER OPERATIONS ===");
            System.out.println("1. View member loans");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> viewMemberLoans();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static void loanOperations() {
        while (true) {
            System.out.println("\n=== LOAN OPERATIONS ===");
            System.out.println("1. Borrow a book");
            System.out.println("2. Return a book");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> borrowBook();
                    case 2 -> returnBook();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static void viewReports() {
        while (true) {
            System.out.println("\n=== REPORTS (Builder Pattern) ===");
            System.out.println("1. Generate loan report");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> generateLoanReport();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid option!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
            }
        }
    }

    private static void systemInformation() {
        System.out.println("\n=== SYSTEM INFORMATION ===");
        System.out.println("Library Management System - Milestone 2");
        System.out.println("OOP Features Implemented:");
        System.out.println("1. Singleton Pattern: FinePolicy");
        System.out.println("2. Factory Pattern: BookFactory for PrintedBook, Ebook, ReferenceBook");
        System.out.println("3. Builder Pattern: LoanReport");
        System.out.println("4. Generics: CrudRepository<T, ID>");
        System.out.println("5. Inheritance: Book -> PrintedBook, Ebook, ReferenceBook");
        System.out.println("\nCurrent Fine Policy:");
        System.out.println(FinePolicy.getInstance());
    }

    private static void listAllBooks() {
        System.out.println("\n--- All Books ---");

        System.out.println("Feature: Uses BookRepository implementing CrudRepository<Book, Integer>");
        System.out.println("All books would be listed here with their types.");
    }

    private static void listAvailableBooks() {
        System.out.println(controller.listAvailableBooks());
    }

    private static void addNewBook() {
        System.out.println("\n--- Add New Book (Factory Pattern) ---");

        try {
            System.out.println("Select book type:");
            System.out.println("1. Printed Book");
            System.out.println("2. Ebook");
            System.out.println("3. Reference Book");
            System.out.print("Choose type (1-3): ");
            int typeChoice = Integer.parseInt(scanner.nextLine());

            Book.BookType bookType;
            switch (typeChoice) {
                case 1 -> bookType = Book.BookType.PRINTED;
                case 2 -> bookType = Book.BookType.EBOOK;
                case 3 -> bookType = Book.BookType.REFERENCE;
                default -> {
                    System.out.println("Invalid type! Defaulting to Printed Book.");
                    bookType = Book.BookType.PRINTED;
                }
            }

            System.out.print("Enter ISBN: ");
            String isbn = scanner.nextLine();

            System.out.print("Enter Title: ");
            String title = scanner.nextLine();

            System.out.print("Enter Author: ");
            String author = scanner.nextLine();

            System.out.print("Enter Year Published: ");
            int year = Integer.parseInt(scanner.nextLine());

            Book newBook = BookFactory.createBook(bookType, isbn, title, author, year);

            if (bookType == Book.BookType.PRINTED) {
                System.out.print("Enter number of pages: ");
                int pages = Integer.parseInt(scanner.nextLine());
                ((entity.PrintedBook) newBook).setPages(pages);
            } else if (bookType == Book.BookType.EBOOK) {
                System.out.print("Enter file size in MB: ");
                double fileSize = Double.parseDouble(scanner.nextLine());
                ((entity.Ebook) newBook).setFileSizeMB(fileSize);

                System.out.print("Enter format (PDF/EPUB): ");
                String format = scanner.nextLine();
                ((entity.Ebook) newBook).setFormat(format);
            }

            System.out.println("\nBook created successfully using Factory Pattern!");
            System.out.println("Book Type: " + newBook.getBookType());
            System.out.println("Title: " + newBook.getTitle());
            System.out.println("Would be saved to database via BookRepository (Generic Repository)");

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchBookById() {
        try {
            System.out.print("Enter Book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine());

            System.out.println("Feature: Uses BookRepository.findById(" + bookId + ")");
            System.out.println("Book details would be displayed here.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid Book ID!");
        }
    }

    private static void viewMemberLoans() {
        try {
            System.out.print("Enter Member ID: ");
            int memberId = Integer.parseInt(scanner.nextLine());

            System.out.println(controller.viewCurrentLoans(memberId));

        } catch (NumberFormatException e) {
            System.out.println("Invalid Member ID!");
        }
    }

    private static void borrowBook() {
        try {
            System.out.print("Enter Book ID: ");
            int bookId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Member ID: ");
            int memberId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter due date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine();
            LocalDate dueDate = LocalDate.parse(dateStr);

            System.out.println(controller.borrowBook(bookId, memberId, dueDate));

        } catch (NumberFormatException e) {
            System.out.println("Invalid input format!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void returnBook() {
        try {
            System.out.print("Enter Loan ID: ");
            int loanId = Integer.parseInt(scanner.nextLine());

            System.out.println(controller.returnBook(loanId));

        } catch (NumberFormatException e) {
            System.out.println("Invalid Loan ID!");
        }
    }

    private static void generateLoanReport() {
        try {
            System.out.print("Enter Loan ID for report: ");
            int loanId = Integer.parseInt(scanner.nextLine());

            System.out.println("\n=== Generating Loan Report (Builder Pattern) ===");

            Loan sampleLoan = new Loan(1, 1, LocalDate.now().minusDays(10));
            sampleLoan.setId(loanId);
            sampleLoan.setLoanDate(LocalDate.now().minusDays(20));
            sampleLoan.setReturnDate(LocalDate.now().minusDays(5));
            sampleLoan.setFineAmount(15.50);

            LoanReport report = new LoanReport.Builder()
                    .fromLoan(sampleLoan, "Effective Java", "Alice Johnson")
                    .build();

            System.out.println(report.generateReport());
            System.out.println("\nThis report was created using the Builder Pattern.");
            System.out.println("Builder allows step-by-step construction of complex objects.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid Loan ID!");
        }
    }
}