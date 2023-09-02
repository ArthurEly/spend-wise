package SpendWise.Logic.Managers;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import SpendWise.Logic.Group;

public class GroupManager implements Serializable {
    private Set<Group> groups;

    public GroupManager() {
        this.groups = new HashSet<Group>();
    }

    private boolean isGroupValid(String groupName) {
        
        Group group = findGroup(groupName);
        if (group == null) {
            return false;
        } else if (groups.contains(group)) {
            return true;
        }
        else {
            return false;
        }
    }

    public Group createGroup(String groupName) {
        if (groupName == null) {
            return null;
        }
        if (isGroupValid(groupName)) {
            return null;
        }

        Group group = new Group(groupName);
        groups.add(group);
        return group;
    }
    
    public void addGroup(Group group) {
        if (group == null) {
            return;
        }
        if (isGroupValid(group.getGroupName())) {
            return;
        }

        // Groups work in a "communist" way, so there is no leader in a group
        // Once you're added to a group, you'll have the same rights as everyone else
        groups.add(group);
    }

    public void removeGroup(Group group) {
        if (group == null) {
            return;
        }
        if (!isGroupValid(group.getGroupName())) {
            return;
        }
        
        groups.remove(group);
    }

    public Group findGroup(String groupName){
        for (Group group : groups) {
            if (group.getGroupName().equals(groupName)) {
                return group;
            }
        }
        return null;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }
}
