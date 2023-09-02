package SpendWise.Logic.Managers;

import java.io.Serializable;
import java.util.Hashtable;

import SpendWise.Logic.User;

public class UserManager implements Serializable {
    private Hashtable<String, User> users;
    private User loggedUser;

    public UserManager() {
        this.users = new Hashtable<String, User>();
        this.loggedUser = new User();
    }

    public boolean changeUsername(String username, String newUsername) {
        if (!this.users.containsKey(username)) {
            return false;
        }
        User user = this.users.get(username);
        user.setUsername(newUsername);
        this.users.remove(username);
        this.users.put(newUsername, user);
        return true;
    }

    public boolean checkUsernameAvailability(String username) {
        return !this.users.containsKey(username);
    }

    public void removeUser(User user) {
        this.users.remove(user.getUsername());
    }

    public void removeUser(String username) {
        this.users.remove(username);
    }

    public void addUser(User user) {
        this.users.put(user.getUsername(), user);
    }

    public void addUser(String username, User user) {
        this.users.put(username, user);
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }

    public void clearLoggedUser() {
        this.loggedUser = new User();
    }

    public boolean createUser(User user) {

        if (this.users.containsKey(user.getUsername())) {
            return false;
        }
        this.users.put(user.getUsername(), user);
        return true;
    }

    public boolean validateLogin(String username, String password) {
        if (!this.users.containsKey(username)) {
            return false;
        }
        User user = this.users.get(username);

        boolean isValid = user.checkPassword(password);

        if (isValid) {
            this.loggedUser = user;
        }

        return isValid;
    }

    public Hashtable<String, User> getUsers() {
        return this.users;
    }

    public User getUser(String username) {
        return this.users.get(username);
    }
}
