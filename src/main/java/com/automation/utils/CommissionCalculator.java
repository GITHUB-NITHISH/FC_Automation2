package com.automation.utils;
 
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
 
public class CommissionCalculator {
 
    /**
     * Calculates Net Revenue, Gross Profit, Commission, and Commissionable Amount
     * dynamically based on the populated fields from the AccountManager Excel data.
     *
     * @param totalCustomerCost       Total cost to the customer
     * @param tax                     Tax amount
     * @param carrierCost             Carrier cost amount
     * @param shipmentAmountStr       Value from 'ShipmentAmount' column (Flat Fee $)
     * @param revenueStr              Value from 'Revenue' column (Revenue %)
     * @param grossProfitStr          Value from 'GrossProfit' column (GP %)
     * @param opCostRevenuePercentStr Value from 'OpCostRevenuePercent' column (Ops %)
     * @param opCostPerShipmentStr    Value from 'OpCostPerShipmentCost' column (Ops $)
     * @return CommissionResult containing the calculated values
     */
    public static CommissionResult calculate(
            double totalCustomerCost,
            String tax,
            double carrierCost,
            String shipmentAmountStr,
            String revenueStr,
            String grossProfitStr,
            String opCostRevenuePercentStr,
            String opCostPerShipmentStr) {
 
    	BigDecimal taxAmount = BigDecimal.valueOf(totalCustomerCost).multiply(parseToBigDecimal(tax).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        BigDecimal nr = BigDecimal.valueOf(totalCustomerCost).subtract(taxAmount);
        BigDecimal gp = nr.subtract(BigDecimal.valueOf(carrierCost));
 
        BigDecimal shipmentAmount = parseToBigDecimal(shipmentAmountStr);
        BigDecimal revenuePct = parseToBigDecimal(revenueStr).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal grossProfitPct = parseToBigDecimal(grossProfitStr).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal opCostPct = parseToBigDecimal(opCostRevenuePercentStr).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal opCostDollar = parseToBigDecimal(opCostPerShipmentStr);
 
        boolean hasShipmentAmount = isPopulated(shipmentAmountStr);
        boolean hasRevenue = isPopulated(revenueStr);
        boolean hasGrossProfit = isPopulated(grossProfitStr);
        boolean hasOpsPct = isPopulated(opCostRevenuePercentStr);
        boolean hasOpsDollar = isPopulated(opCostPerShipmentStr);
 
        BigDecimal opsAmount = BigDecimal.ZERO;
        if (hasOpsDollar) {
            opsAmount = opCostDollar;
        } else if (hasOpsPct) {
                opsAmount = nr.multiply(opCostPct);
        }

 
        BigDecimal commission = BigDecimal.ZERO;
        BigDecimal commissionableAmount = BigDecimal.ZERO;
 
        // Based on the updated formulas (omitting noncommissionable charges):
        if (hasShipmentAmount && !hasRevenue && !hasGrossProfit) {
            // Scenarios 1, 2, 3: Only Flat Fee
            commission = shipmentAmount;
            commissionableAmount = BigDecimal.ZERO; // No noncommissionable amount for flat fee
        } 
        else if (hasRevenue && !hasShipmentAmount) {
            // Scenarios 4, 5, 6: Only Revenue %
          	 = nr.subtract(opsAmount);
            commission = commissionableAmount.multiply(revenuePct);
        }
        else if (hasGrossProfit && !hasShipmentAmount) {
            // Scenarios 7, 8, 9: Only GP %
            commissionableAmount = gp.subtract(opsAmount);
            commission = commissionableAmount.multiply(grossProfitPct);
        }
        else if (hasRevenue && hasShipmentAmount) {
            // Scenarios 10, 11, 12: Revenue % + Flat Fee
            commissionableAmount = nr.subtract(opsAmount);
            commission = commissionableAmount.multiply(revenuePct).add(shipmentAmount);
        }
        else if (hasGrossProfit && hasShipmentAmount) {
            // Scenarios 13, 14, 15: GP % + Flat Fee
            commissionableAmount = gp.subtract(opsAmount);
            commission = commissionableAmount.multiply(grossProfitPct).add(shipmentAmount);
        }
 
        // Rounding to 2 decimal places for final output
        nr = nr.setScale(2, RoundingMode.HALF_UP);
        gp = gp.setScale(2, RoundingMode.HALF_UP);
        opsAmount = opsAmount.setScale(2, RoundingMode.HALF_UP);
        commission = commission.setScale(2, RoundingMode.HALF_UP);
        commissionableAmount = commissionableAmount.setScale(2, RoundingMode.HALF_UP);
 
        return new CommissionResult(
                opsAmount.doubleValue(),
                nr.doubleValue(),
                gp.doubleValue(),
                commission.doubleValue(),
                commissionableAmount.doubleValue()
        );
    }
 
    /**
     * Convenience method to accept the raw ScenarioContext test data Map directly.
     */
    public static CommissionResult calculateFromMap(
            double totalCustomerCost,
            String tax,
            double carrierCost,
            Map<String, String> testData) {
            return calculate(
                totalCustomerCost, 
                tax, 
                carrierCost,
                testData.get("ShipmentAmount$"),
                testData.get("Revenue%"),
                testData.get("GrossProfit%"),
                testData.get("OpRevenue%"),
                testData.get("OpPerShipment$")
        );
    }
 
    private static boolean isPopulated(String value) {
        return value != null && !value.trim().isEmpty() && !value.trim().equalsIgnoreCase("MISSING") && !value.trim().equalsIgnoreCase("null");
    }
 
    private static BigDecimal parseToBigDecimal(String value) {
        if (!isPopulated(value)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
 
    public static class CommissionResult {
        private double opsAmount;
        private double netRevenue;
        private double grossProfit;
        private double commission;
        private double commissionableAmount;
 
        public CommissionResult(double opsAmount, double netRevenue, double grossProfit, double commission, double commissionableAmount) {
            this.opsAmount = opsAmount;
            this.netRevenue = netRevenue;
            this.grossProfit = grossProfit;
            this.commission = commission;
            this.commissionableAmount = commissionableAmount;
        }
 
        public double getOpsAmount() { return opsAmount; }
        public double getNetRevenue() { return netRevenue; }
        public double getGrossProfit() { return grossProfit; }
        public double getCommission() { return commission; }
        public double getCommissionableAmount() { return commissionableAmount; }
        
        /**
         * Returns a Map of calculated values ready to be written back to Excel.
         * Keys must match your Excel column header names exactly.
         */
        public Map<String, String> toExcelDataMap() {
            Map<String, String> map = new HashMap<>();
            map.put("Commission",           String.valueOf(commission));
            map.put("CommissionableAmount", String.valueOf(commissionableAmount));
            map.put("NetRevenue",           String.valueOf(netRevenue));
            map.put("GrossProfit",          String.valueOf(grossProfit));
            map.put("OPSAmount",            String.valueOf(opsAmount));
            return map;
        }
 
        @Override
        public String toString() {
            return "CommissionResult{" +
                    "opsAmount=" + opsAmount +
                    ", netRevenue=" + netRevenue +
                    ", grossProfit=" + grossProfit +
                    ", commission=" + commission +
                    ", commissionableAmount=" + commissionableAmount +
                    '}';
        }
    }
}