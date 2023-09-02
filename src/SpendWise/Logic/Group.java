package SpendWise.Logic;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Group implements Serializable {

    private Set<User> users;
    private String groupName;

    public Group(String groupName) {
        this.groupName = groupName;
        users = new HashSet<User>();
    }

    public void addUser(User user) {
        users.add(user);
        user.getGroupManager().addGroup(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.getGroupManager().removeGroup(this);
    }

    public Set<User> getUsers() {
        return users;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String toString() {
        return groupName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Group)) {
            return false;
        }
        Group group = (Group) obj;
        return this.groupName.equals(group.getGroupName());
    }
    
    @Override
    public int hashCode() {
        return this.groupName.hashCode();
    }
}
