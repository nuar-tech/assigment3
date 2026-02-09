package LoanManagementComponent;

import edu.aitu.oop3.db.DatabaseConnection;
import java.sql.*;

public class FinePolicy {
    private static FinePolicy instance;
    private double dailyRate;
    private double maxFine;
    private int graceDays;

    private FinePolicy() {
        loadPolicy();
    }

    public static FinePolicy getInstance() {
        if (instance == null) {
            instance = new FinePolicy();
        }
        return instance;
    }

    private void loadPolicy() {
        String sql = "SELECT daily_rate, max_fine, grace_days FROM fine_policy LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                dailyRate = rs.getDouble("daily_rate");
                maxFine = rs.getDouble("max_fine");
                graceDays = rs.getInt("grace_days");
            } else {
                dailyRate = 1.0;
                maxFine = 50.0;
                graceDays = 3;
            }

        } catch (SQLException e) {
            System.out.println("Using default fine policy");
            dailyRate = 1.0;
            maxFine = 50.0;
            graceDays = 3;
        }
    }
    public double getDailyRate() { return dailyRate; }
    public double getMaxFine() { return maxFine; }
    public int getGraceDays() { return graceDays; }

    @Override
    public String toString() {
        return String.format("FinePolicy: $%.2f per day, max $%.2f, %d grace days",
                dailyRate, maxFine, graceDays);
    }
}
