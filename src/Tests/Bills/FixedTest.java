package Tests.Bills;

import org.junit.jupiter.api.*;

import SpendWise.Logic.Bills.Fixed;

import static org.junit.Assert.*;
import java.time.LocalDate;

public class FixedTest {
    private Fixed fixed;
    private double value = 500f;

    @BeforeEach
    public void setUpClass() {
        fixed = new Fixed(value, false, LocalDate.now().minusMonths(3), "description");
    }

    @Test
    public void testFixed() {
        assertNotNull(fixed);
    }

    @Test
    public void testTotalValue() {
        assertEquals(value * 3, fixed.totalValue(), 0.001);
    }

    @Test
    public void testTotalValueDate() {
        assertEquals(value * 3, fixed.totalValue(LocalDate.now()), 0.001);
    }

    @Test
    public void testIsDateInBetween() {
        LocalDate date = LocalDate.now();
        LocalDate startDate = date.minusMonths(3);
        LocalDate endDate = date.plusMonths(3);
        assertTrue(fixed.isDateInBetween(date, startDate, endDate));
        assertFalse(fixed.isDateInBetween(date, startDate, date.minusMonths(1)));
        assertFalse(fixed.isDateInBetween(date, endDate.plusDays(1), endDate.plusMonths(3)));
    }

    @Test
    public void testValueInMonth() {
        assertEquals(value, fixed.valueInMonth(LocalDate.now()), 0.001);
    }

    @Test
    public void testValueInMonthDateBeforeStart() {
        assertEquals(0, fixed.valueInMonth(LocalDate.now().minusMonths(4)), 0.001);
    }

    @Test
    public void testUpdateValueDate() {
        fixed.updateValue(1000, LocalDate.now().plusMonths(1));
        assertEquals(value * 4, fixed.totalValue(LocalDate.now().plusMonths(1)), 0.001);
    }

    @Test
    public void testUpdateValue() {
        fixed.updateValue(1000);
        assertEquals(value * 4, fixed.totalValue(LocalDate.now().plusMonths(1)), 0.001);
    }

    @Test
    public void testGetLastDate() {
        assertEquals(LocalDate.now().minusMonths(3), fixed.getLastDate());
    }

    @Test
    public void testCalculatePayment() {
        assertEquals(value, fixed.calculatePayment(), 0.001);
    }

    @Test
    public void testTotalValueDateBeforeStart() {
        assertEquals(0, fixed.totalValue(LocalDate.now().minusMonths(4)), 0.001);
    }

    @Test
    public void testTotalValueDateAfterEnd() {
        assertEquals(value * 7, fixed.totalValue(LocalDate.now().plusMonths(4)), 0.001);
    }
}
