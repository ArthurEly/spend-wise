package Tests;

import org.junit.jupiter.api.*;

import SpendWise.Logic.User;

import static org.junit.Assert.*;

public class UserTest {
    private User user;

    @BeforeEach
    public void setUpClass() {
        user = new User("username", "name", "email", "password", 1000, 100);
    }

    @Test
    public void testUser() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void testUsername() {
        assertEquals("username", user.getUsername());
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());
    }

    @Test
    public void testName() {
        assertEquals("name", user.getName());
        user.setName("newName");
        assertEquals("newName", user.getName());
    }

    @Test
    public void testEmail() {
        assertEquals("email", user.getEmail());
        user.setEmail("newEmail");
        assertEquals("newEmail", user.getEmail());
    }

    @Test
    public void testIncome() {
        assertEquals(1000, user.getIncome(), 0.001);
        user.setIncome(2000);
        assertEquals(2000, user.getIncome(), 0.001);
    }

    @Test
    public void testMonthlyLimit() {
        assertEquals(100, user.getMonthlyLimit(), 0.001);
        user.setMonthlyLimit(200);
        assertEquals(200, user.getMonthlyLimit(), 0.001);
    }

    @Test
    public void testCheckPassword() {
        assertTrue(user.checkPassword("password"));
        assertFalse(user.checkPassword("wrongPassword"));
    }

    @Test
    public void testChangePassword() {
        assertTrue(user.changePassword("password", "newPassword"));
        assertFalse(user.changePassword("wrongPassword", "newPassword"));
        assertTrue(user.checkPassword("newPassword"));
    }

    @Test
    public void testPasswordSize() {
        assertEquals(8, user.getPasswordSize());
    }

    @Test
    public void testEquals() {
        User user = new User("username", "name", "email", "password", 1000, 100);
        assertTrue(user.equals(this.user));
        user = new User("newUsername", "name", "email", "password", 1000, 100);
        assertFalse(user.equals(this.user));
    }

    @Test
    public void testHashCode() {
        User user = new User("username", "name", "email", "password", 1000, 100);
        assertEquals(user.hashCode(), this.user.hashCode());
        user = new User("newUsername", "name", "email", "password", 1000, 100);
        assertNotEquals(user.hashCode(), this.user.hashCode());
    }
}
