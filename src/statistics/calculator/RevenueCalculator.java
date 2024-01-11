package statistics.calculator;

public final class RevenueCalculator {
    /**
     * Calculates the revenue with the specified calculation strategy
     *
     * @param revenueStrategy The strategy that should be used
     */
    public void calculateRevenue(final CalculateRevenueStrategy revenueStrategy) {
        revenueStrategy.calculateRevenue();
    }
}
