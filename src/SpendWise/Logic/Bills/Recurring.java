package SpendWise.Logic.Bills;

import java.time.LocalDate;

public class Recurring extends Expense {
    private LocalDate endDate;

    public Recurring(double value, boolean isEssential, LocalDate date, String description, LocalDate endDate) {
        super(value, isEssential, date, description);
        this.endDate = endDate;
    }

    /**
     * @return the endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double totalValue() {
        LocalDate today = LocalDate.now();
        long totalMonthToPay = this.getDate().until(this.endDate).toTotalMonths();
        long monthsPayed = this.getDate().until(today).toTotalMonths();
        if (monthsPayed >= totalMonthToPay) {
            return 0;
        }
        return this.getValue() * (totalMonthToPay - monthsPayed);
    }

    public double totalPayed() {
        LocalDate today = LocalDate.now();
        long totalMonthToPay = this.getDate().until(this.endDate).toTotalMonths();
        long monthsPayed = this.getDate().until(today).toTotalMonths();
        if (monthsPayed >= totalMonthToPay) {
            return this.getValue() * totalMonthToPay;
        }
        return this.getValue() * monthsPayed;
    }

    public double totalCost() {
        return this.getValue() * this.getDate().until(this.endDate).toTotalMonths();
    }

    @Override
    public double calculatePayment() {
        LocalDate today = LocalDate.now();
        if (this.endDate.isBefore(today)) {
            return 0;
        }
        return this.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Recurring) {
            Recurring recurring = (Recurring) obj;
            return super.equals(obj) && recurring.getEndDate().equals(endDate);
        } else {
            return false;
        }
    }
}
