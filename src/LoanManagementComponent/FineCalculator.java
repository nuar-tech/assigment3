package LoanManagementComponent;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FineCalculator {
    private static final double DAILY_FINE_RATE = 1.0; // $1 per day
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
            return daysOverdue * DAILY_FINE_RATE;
        }
        return 0.0;
    }

    public double calculateCurrentFine(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        return calculateFine(dueDate, today);
    }
}