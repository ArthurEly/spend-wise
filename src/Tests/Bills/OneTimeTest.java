package Tests.Bills;

import org.junit.jupiter.api.*;

import SpendWise.Logic.Bills.OneTime;

import static org.junit.Assert.*;
import java.time.LocalDate;

public class OneTimeTest {
    private OneTime oneTime;

    @BeforeEach
    public void setUpClass() {
        oneTime = new OneTime(500, false, LocalDate.now(), "description", false);
    }

    @Test
    public void testOneTime() {
        assertNotNull(oneTime);
    }

    @Test
    public void testIsPaid() {
        assertFalse(oneTime.isPaid());
    }

    @Test
    public void testPay() {
        oneTime.pay();
        assertTrue(oneTime.isPaid());
    }

    @Test
    public void testCalculatePayment() {
        assertEquals(500, oneTime.calculatePayment(), 0.001);
        oneTime.pay();
        assertEquals(0, oneTime.calculatePayment(), 0.001);
    }
}
