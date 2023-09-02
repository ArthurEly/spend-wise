package SpendWise.Logic.Bills;

import java.time.LocalDate;

public class OneTime extends Expense {
    private boolean isPaid;

    public OneTime(double value, boolean isEssential, LocalDate date, String description, boolean isPaid) {
        super(value, isEssential, date, description);
        this.isPaid = isPaid;
    }

    public OneTime(double value, boolean isEssential, LocalDate date, String description) {
        super(value, isEssential, date, description);
        this.isPaid = false;
    }

    @Override
    public double calculatePayment() {
        return this.isPaid ? 0 : this.getValue();
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void pay() {
        this.isPaid = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OneTime) {
            OneTime oneTime = (OneTime) obj;
            return super.equals(obj) && oneTime.isPaid() == isPaid;
        } else {
            return false;
        }
    }

}
