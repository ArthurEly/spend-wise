package SpendWise.Logic.Bills;

import java.util.ArrayList;
import java.time.LocalDate;

import SpendWise.Utils.Dates;
import SpendWise.Utils.Triple;

public class Fixed extends Expense {
    private ArrayList<Triple<LocalDate, LocalDate, Double>> valueHistory;

    public Fixed(double value, boolean isEssential, LocalDate date, String description) {
        super(value, isEssential, date, description);
        this.valueHistory = new ArrayList<Triple<LocalDate, LocalDate, Double>>();
        this.valueHistory.add(new Triple<LocalDate, LocalDate, Double>(date, LocalDate.MAX.minusDays(1), value));
    }

    public double totalValue() {
        double sum = 0;
        LocalDate today = LocalDate.now();
        long totalMonths = this.getDate().until(today).toTotalMonths();
        for (long i = 0; i < totalMonths; i++) {
            sum += this.valueInMonth(this.getDate().plusMonths(i));
        }
        return sum;
    }

    public double totalValue(LocalDate date) {
        double sum = 0;
        long totalMonths = this.getDate().until(date).toTotalMonths();
        if (totalMonths < 0) {
            return 0;
        }

        for (long i = 0; i < totalMonths; i++) {
            sum += this.valueInMonth(this.getDate().plusMonths(i));
        }
        return sum;
    }

    public boolean isDateInBetween(LocalDate date, LocalDate startDate, LocalDate endDate) {
        LocalDate rangeStart = Dates.monthStart(startDate);
        LocalDate rangeEnd = Dates.monthEnd(endDate);
        return date.isAfter(rangeStart) && date.isBefore(rangeEnd);
    }

    public double valueInMonth(LocalDate date) {
        for (Triple<LocalDate, LocalDate, Double> triple : this.valueHistory) {
            if (isDateInBetween(date, triple.getFirst(), triple.getSecond())) {
                return triple.getThird();
            }
        }
        return 0; // No value found, dont add anything
    }

    public void updateValue(double newValue, LocalDate date) {
        this.setValue(newValue);
        Triple<LocalDate, LocalDate, Double> lastUpdate = this.valueHistory.get(this.valueHistory.size() - 1);
        lastUpdate.setSecond(date);
        this.valueHistory.add(new Triple<LocalDate, LocalDate, Double>(date, LocalDate.MAX.minusDays(1), newValue));
    }

    public void updateValue(double newValue) {
        this.updateValue(newValue, LocalDate.now());
    }

    public LocalDate getLastDate() {
        return this.valueHistory.get(this.valueHistory.size() - 1).getFirst();
    }

    @Override
    public double calculatePayment() {
        return this.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Fixed) {
            Fixed other = (Fixed) obj;
            return super.equals(other) && this.valueHistory.equals(other.valueHistory);
        }
        return false;
    }

    @Override
    public double getValue() {
        return this.valueInMonth(LocalDate.now());
    }

    @Override
    public double getValue(LocalDate date) {
        return this.valueInMonth(date);
    }

    @Override
    public LocalDate getDate() {
        LocalDate today = LocalDate.now();
        for (Triple<LocalDate, LocalDate, Double> triple : this.valueHistory) {
            LocalDate rangeStart = triple.getFirst().minusDays(1);
            LocalDate rangeEnd = triple.getSecond().plusDays(1);
            if (rangeStart.isBefore(today) && rangeEnd.isAfter(today)) {
                return triple.getFirst();
            }
        }
        return super.getDate();
    }

    /**
     * @return the valueHistory
     */
    public ArrayList<Triple<LocalDate, LocalDate, Double>> getValueHistory() {
        return valueHistory;
    }
}
