package SpendWise.Logic.Managers;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import SpendWise.Logic.Bills.Expense;
import SpendWise.Logic.Bills.Fixed;
import SpendWise.Logic.Bills.OneTime;
import SpendWise.Logic.Bills.Recurring;
import SpendWise.Utils.Dates;
import SpendWise.Utils.Enums.ExpenseType;

public class ExpensesManager implements Serializable {
    private Set<Expense> expenses;

    public ExpensesManager() {
        this.expenses = new HashSet<Expense>();
    }

    public boolean addExpense(Expense exp) {
        return expenses.add(exp);
    }

    public boolean removeExpense(Expense exp) {
        return expenses.remove(exp);
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public Set<Expense> getMonthExpenses(LocalDate date) {
        Set<Expense> monthExpenses = new HashSet<Expense>();
        for (Expense exp : expenses) {
            switch (exp.getType()) {
                case FIXED: {
                    Fixed fixed = (Fixed) exp;
                    LocalDate startOfMonth = Dates.monthStart(fixed.getDate());
                    boolean isAfterStart = date.isAfter(startOfMonth);
                    if (isAfterStart) {
                        monthExpenses.add(fixed);
                    }
                    break;
                }
                case ONETIME: {
                    OneTime oneTime = (OneTime) exp;
                    if (oneTime.getDate().getMonth() == date.getMonth()) {
                        monthExpenses.add(oneTime);
                    }
                    break;
                }
                case RECURRING: {
                    Recurring recurring = (Recurring) exp;
                    LocalDate startOfMonth = Dates.monthStart(recurring.getDate());
                    LocalDate endOfMonth = Dates.monthEnd(recurring.getDate());
                    boolean isAfterStart = date.isAfter(startOfMonth);
                    boolean isBeforeEnd = date.isBefore(endOfMonth);
                    if (isAfterStart && isBeforeEnd) {
                        monthExpenses.add(recurring);
                    }
                    break;
                }
            }
        }
        return monthExpenses;
    }

    private double getExpenseValue(Expense exp) {
        if (exp instanceof Fixed) {
            Fixed fixed = (Fixed) exp;
            return fixed.getValue();
        } else {
            return exp.getValue();
        }
    }

    public double calculateMonthExpenses(LocalDate date) {
        Set<Expense> monthExpenses = getMonthExpenses(date);
        double totalExpenses = 0;
        for (Expense exp : monthExpenses) {
            totalExpenses += getExpenseValue(exp);
        }
        return totalExpenses;
    }

    public double calculateMonthExpenses(LocalDate date, ExpenseType type) {
        Set<Expense> monthExpenses = getMonthExpenses(date);
        double totalExpenses = 0;
        for (Expense exp : monthExpenses) {
            if (exp.getType() == type) {
                totalExpenses += getExpenseValue(exp);
            }
        }
        return totalExpenses;
    }

    public double calculateMonthExpenses(LocalDate date, boolean essential) {
        Set<Expense> monthExpenses = getMonthExpenses(date);
        double totalExpenses = 0;
        for (Expense exp : monthExpenses) {
            if (exp.isEssential() == essential) {
                totalExpenses += getExpenseValue(exp);
            }
        }
        return totalExpenses;
    }
}
