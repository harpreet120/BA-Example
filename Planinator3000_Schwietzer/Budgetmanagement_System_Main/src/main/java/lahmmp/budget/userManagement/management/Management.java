package lahmmp.budget.userManagement.management;

import lahmmp.budget.userManagement.User.User;
import lahmmp.budget.userManagement.User.UserRoles;


public class Management extends User {

    //Employeenumber auch automatisch generieren lassen?
    //extends User
    private UserRoles role = UserRoles.MANAGEMENT;

    public Management(){

    }

    public Management(String name, String lastname, UserRoles userRole, String password, String username, int employeeNumber) {
        super( name,  lastname,  userRole,  password,  username,  employeeNumber);
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}
