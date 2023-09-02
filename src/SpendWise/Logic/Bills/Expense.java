package SpendWise.Logic.Bills;

import java.io.Serializable;
import java.time.LocalDate;

import SpendWise.Utils.Enums.ExpenseType;

public abstract class Expense implements Serializable {
    private double value;

    private boolean isEssential;

    private LocalDate date;

    private String description;

    public Expense(double value, boolean isEssential, LocalDate date, String description) {
        this.value = value;
        this.isEssential = isEssential;
        this.date = date;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Value: R$" + this.getValue() + " || Type: " + this.getType().getName() + " || Description: "
                + this.getDescription() + " || Date: " + this.getDate();
    }

    public ExpenseType getType() {
        if (this instanceof Fixed) {
            return ExpenseType.FIXED;
        } else if (this instanceof OneTime) {
            return ExpenseType.ONETIME;
        } else if (this instanceof Recurring) {
            return ExpenseType.RECURRING;
        } else {
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof Expense) {
            Expense exp = (Expense) obj;
            return exp.getValue() == value && exp.isEssential() == isEssential
                    && exp.getDate().equals(date) && exp.getDescription().equals(description);
        } else {
            return false;
        }
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    public double getValue(LocalDate date) {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @param isEssential the isEssential to set
     */
    public void setEssential(boolean isEssential) {
        this.isEssential = isEssential;
    }

    /**
     * @return the isEssential
     */
    public boolean isEssential() {
        return isEssential;
    }

    /**
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    public abstract double calculatePayment();
}
