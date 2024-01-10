package statistics.calculator;

import entities.user.User;

public interface CalculateRevenueStrategy<U extends User> {
    /**
     * Calculate the revenue given (normal user) or made (artists/hosts)
     * @param user The suer for which we calculate
     */
    void calculateRevenue(U user);
}
