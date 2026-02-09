package LoanManagementComponent;

import exception.LibraryException;

public class LoanOverdueException extends LibraryException {
    private final double fineAmount;
    public LoanOverdueException(String message, double fineAmount) {
        super(message);
        this.fineAmount = fineAmount;
    }

    public double getFineAmount() {
        return fineAmount;
    }
}