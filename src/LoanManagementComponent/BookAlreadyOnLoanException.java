package LoanManagementComponent;

import exception.LibraryException;

public class BookAlreadyOnLoanException extends LibraryException {
    public BookAlreadyOnLoanException(String message) {
        super(message);
    }
    public BookAlreadyOnLoanException(int bookId) {
        super("Book with ID " + bookId + " is already on loan");
    }
}