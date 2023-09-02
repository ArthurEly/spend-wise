package Tests;

import org.junit.jupiter.api.*;

import SpendWise.Logic.Group;
import SpendWise.Logic.User;

import static org.junit.Assert.*;

public class GroupTest {
    private Group group;
    private final static String GROUP_NAME = "groupName";

    @BeforeEach
    public void setUpClass() {
        group = new Group(GROUP_NAME);
    }

    @Test
    public void testGroup() {
        Group group = new Group(GROUP_NAME);
        assertNotNull(group);
    }

    @Test
    public void testGroupName() {
        assertEquals(GROUP_NAME, group.getGroupName());
        group = new Group("newGroupName");
        assertEquals("newGroupName", group.getGroupName());
    }

    @Test
    public void testAddUser() {
        User user = new User("username", "name", "email", "password", 1000, 100);
        group.addUser(user);
        assertTrue(group.getUsers().contains(user));
    }

    @Test
    public void testRemoveUser() {
        User user = new User("username", "name", "email", "password", 1000, 100);
        group.addUser(user);
        group.removeUser(user);
        assertFalse(group.getUsers().contains(user));
    }

    @Test
    public void testEquals() {
        Group group = new Group(GROUP_NAME);
        assertTrue(group.equals(this.group));
        group = new Group("newGroupName");
        assertFalse(group.equals(this.group));
    }

    @Test
    public void testHashCode() {
        Group group = new Group(GROUP_NAME);
        assertEquals(group.hashCode(), this.group.hashCode());
        group = new Group("newGroupName");
        assertNotEquals(group.hashCode(), this.group.hashCode());
    }
}
