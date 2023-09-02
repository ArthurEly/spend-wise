package Tests.Managers;

import org.junit.jupiter.api.*;
import static org.junit.Assert.*;
import java.time.LocalDate;

import SpendWise.Logic.Bills.Expense;
import SpendWise.Logic.Bills.Fixed;
import SpendWise.Logic.Managers.ExpensesManager;
import SpendWise.Utils.Enums.ExpenseType;

public class ExpensesManagerTest {
    private ExpensesManager expensesManager;

    @BeforeEach
    public void setUpClass() {
        expensesManager = new ExpensesManager();
    }

    @Test
    public void testExpensesManager() {
        assertNotNull(expensesManager);
    }

    @Test
    public void testAddExpense() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(1, expensesManager.getExpenses().size());
    }

    @Test
    public void testAddExpenseFalse() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertFalse(expensesManager.addExpense(exp));
    }

    @Test
    public void testRemoveExpense() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        expensesManager.removeExpense(exp);
        assertEquals(0, expensesManager.getExpenses().size());
    }

    @Test
    public void testRemoveExpenseFalse() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        assertFalse(expensesManager.removeExpense(exp));
    }

    @Test
    public void testGetExpenses() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(1, expensesManager.getExpenses().size());
    }

    @Test
    public void testGetMonthExpenses() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(1, expensesManager.getMonthExpenses(LocalDate.now()).size());
    }

    @Test
    public void testGetMonthExpensesFalse() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(0, expensesManager.getMonthExpenses(LocalDate.now().minusMonths(1)).size());
    }

    @Test
    public void testCalculateMonthExpenses() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(100, expensesManager.calculateMonthExpenses(LocalDate.now()), 0.001);
    }

    @Test
    public void testCalculateMonthExpensesFalse() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(0, expensesManager.calculateMonthExpenses(LocalDate.now().minusMonths(1)), 0.001);
    }

    @Test
    public void testCalculateMonthExpensesOverload() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(100, expensesManager.calculateMonthExpenses(LocalDate.now(), ExpenseType.FIXED), 0.001);
    }

    @Test
    public void testCalculateMonthExpensesOverloadFalse() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(0, expensesManager.calculateMonthExpenses(LocalDate.now(), ExpenseType.RECURRING), 0.001);
    }

    @Test
    public void testCalculateMonthExpensesEssential() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(100, expensesManager.calculateMonthExpenses(LocalDate.now(), true), 0.001);
    }

    @Test
    public void testCalculateMonthExpensesEssentialFalse() {
        Expense exp = new Fixed(100, true, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(0, expensesManager.calculateMonthExpenses(LocalDate.now(), false), 0.001);
    }

    @Test
    public void testCalculateMonthExpensesMonEssential() {
        Expense exp = new Fixed(100, false, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(100, expensesManager.calculateMonthExpenses(LocalDate.now(), false), 0.001);
    }

    @Test
    public void testCalculateMonthExpensesMonEssentialFalse() {
        Expense exp = new Fixed(100, false, LocalDate.now(), "test");
        expensesManager.addExpense(exp);
        assertEquals(0, expensesManager.calculateMonthExpenses(LocalDate.now(), true), 0.001);
    }
}
