package Tests.Bills;

import org.junit.jupiter.api.*;

import SpendWise.Logic.Bills.Expense;
import SpendWise.Logic.Bills.OneTime;
import SpendWise.Utils.Enums.ExpenseType;

import static org.junit.Assert.*;
import java.time.LocalDate;

public class ExpenseTest {
    private Expense expense;

    @BeforeEach
    public void setUpClass() {
        expense = new OneTime(500, false, LocalDate.now(), "description", false);
    }

    @Test
    public void testExpense() {
        assertNotNull(expense);
    }

    @Test
    public void testGetType() {
        assertEquals(ExpenseType.ONETIME, expense.getType());
    }

    @Test
    public void testEquals() {
        Expense expense = new OneTime(500, false, LocalDate.now(), "description", false);
        assertTrue(expense.equals(this.expense));
        expense = new OneTime(500, false, LocalDate.now(), "new description", false);
        assertFalse(expense.equals(this.expense));
    }

    @Test
    public void testGetValue() {
        assertEquals(500, expense.getValue(), 0.001);
    }

    @Test
    public void testGetValueDate() {
        assertEquals(500, expense.getValue(LocalDate.now()), 0.001);
    }

    @Test
    public void testSetValue() {
        expense.setValue(1000);
        assertEquals(1000, expense.getValue(), 0.001);
    }

    @Test
    public void testIsEssential() {
        assertFalse(expense.isEssential());
    }

    @Test
    public void testSetEssential() {
        expense.setEssential(true);
        assertTrue(expense.isEssential());
    }

    @Test
    public void testGetDate() {
        assertEquals(LocalDate.now(), expense.getDate());
    }

    @Test
    public void testSetDate() {
        expense.setDate(LocalDate.of(2020, 1, 1));
        assertEquals(LocalDate.of(2020, 1, 1), expense.getDate());
    }

    @Test
    public void testGetDescription() {
        assertEquals("description", expense.getDescription());
    }

    @Test
    public void testSetDescription() {
        expense.setDescription("new description");
        assertEquals("new description", expense.getDescription());
    }

    @Test
    public void testCalculatePayment() {
        assertEquals(500, expense.calculatePayment(), 0.001);
    }
}
