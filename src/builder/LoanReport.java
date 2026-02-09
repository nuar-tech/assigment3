package builder;

import LoanManagementComponent.Loan;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LoanReport {
    private final int loanId;
    private final String bookTitle;
    private final String memberName;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private final double fine;

    private LoanReport(Builder builder) {
        this.loanId = builder.loanId;
        this.bookTitle = builder.bookTitle;
        this.memberName = builder.memberName;
        this.loanDate = builder.loanDate;
        this.dueDate = builder.dueDate;
        this.fine = builder.fine;
    }

    public int getLoanId() { return loanId; }
    public String getBookTitle() { return bookTitle; }
    public String getMemberName() { return memberName; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public double getFine() { return fine; }

    public static class Builder {
        private int loanId;
        private String bookTitle;
        private String memberName;
        private LocalDate loanDate;
        private LocalDate dueDate;
        private double fine;

        public Builder loanId(int loanId) {
            this.loanId = loanId;
            return this;
        }

        public Builder bookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
            return this;
        }

        public Builder memberName(String memberName) {
            this.memberName = memberName;
            return this;
        }

        public Builder loanDate(LocalDate loanDate) {
            this.loanDate = loanDate;
            return this;
        }

        public Builder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder fine(double fine) {
            this.fine = fine;
            return this;
        }

        public Builder fromLoan(Loan loan, String bookTitle, String memberName) {
            this.loanId = loan.getId();
            this.bookTitle = bookTitle;
            this.memberName = memberName;
            this.loanDate = loan.getLoanDate();
            this.dueDate = loan.getDueDate();
            this.fine = loan.getFineAmount();
            return this;
        }

        public LoanReport build() {
            return new LoanReport(this);
        }
    }

    public String generateReport() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LoanReport report = new LoanReport.Builder().loanId(1001).bookTitle("Effective java").memberName("Alice").build();
        return String.format(
                "Loan Report #%d\n" +
                        "Book: %s\n" +
                        "Member: %s\n" +
                        "Loaned: %s\n" +
                        "Due: %s\n" +
                        "Fine: $%.2f\n",
                report.loanId, report.bookTitle, report.memberName,
                loanDate.format(fmt), dueDate.format(fmt), fine
        );
    }
}
