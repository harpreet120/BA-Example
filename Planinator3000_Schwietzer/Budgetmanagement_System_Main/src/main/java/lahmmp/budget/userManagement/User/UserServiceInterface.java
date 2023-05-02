package lahmmp.budget.userManagement.User;

import java.util.List;

public interface UserServiceInterface {
    List<User> getAllUser();
    void saveUser(User user);
}
