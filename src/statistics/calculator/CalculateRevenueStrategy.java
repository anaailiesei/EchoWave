package statistics.calculator;

import entities.user.User;

public interface CalculateRevenueStrategy {
    /**
     * Calculate the revenue given (normal user) or made (artists/hosts)
     */
    void calculateRevenue();
}
