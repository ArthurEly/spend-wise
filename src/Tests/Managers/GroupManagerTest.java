package Tests.Managers;

import org.junit.jupiter.api.*;
import static org.junit.Assert.*;

import SpendWise.Logic.Managers.GroupManager;

public class GroupManagerTest {
    private GroupManager groupManager;

    @BeforeEach
    public void setUpClass() {
        groupManager = new GroupManager();
    }

    @Test
    public void testGroupManager() {
        assertNotNull(groupManager);
    }

    @Test
    public void testCreateGroup() {
        assertNotEquals(null, groupManager.createGroup("group1"));
    }

    @Test
    public void testCreateGroupFalse() {
        groupManager.createGroup("group1");
        assertEquals(null, groupManager.createGroup("group1"));
    }

    @Test
    public void testAddGroup() {
        groupManager.createGroup("group1");
        groupManager.createGroup("group2");
        assertEquals(2, groupManager.getGroups().size());
    }

    @Test
    public void testAddGroupFalse() {
        groupManager.createGroup("group1");
        groupManager.createGroup("group2");
        assertEquals(2, groupManager.getGroups().size());
        groupManager.addGroup(groupManager.findGroup("group1"));
        assertEquals(2, groupManager.getGroups().size());
    }

    @Test
    public void testRemoveGroup() {
        groupManager.createGroup("group1");
        groupManager.createGroup("group2");
        groupManager.removeGroup(groupManager.findGroup("group1"));
        assertEquals(1, groupManager.getGroups().size());
    }

    @Test
    public void testRemoveGroupFalse() {
        groupManager.createGroup("group1");
        groupManager.createGroup("group2");
        groupManager.removeGroup(groupManager.findGroup("group3"));
        assertEquals(2, groupManager.getGroups().size());
    }

    @Test
    public void testFindGroup() {
        groupManager.createGroup("group1");
        groupManager.createGroup("group2");
        assertEquals("group1", groupManager.findGroup("group1").getGroupName());
    }

    @Test
    public void testFindGroupFalse() {
        groupManager.createGroup("group1");
        groupManager.createGroup("group2");
        assertEquals(null, groupManager.findGroup("group3"));
    }

    @Test
    public void testGetGroups() {
        groupManager.createGroup("group1");
        groupManager.createGroup("group2");
        assertEquals(2, groupManager.getGroups().size());
    }
}
