package lahmmp.budget.userManagement.administrator;

import lahmmp.budget.userManagement.User.User;
import lahmmp.budget.userManagement.User.UserRoles;

public class Administrator extends User {

    private UserRoles ADMINISTRATOR;

    public Administrator (String name, String lastname, UserRoles userRole, String password, String username, int employeeNumber) {
        super(name, lastname, userRole, password, username, employeeNumber);
    }

    public UserRoles getADMINISTRATOR() {
        return this.ADMINISTRATOR;
    }

    public void setADMINISTRATOR(final UserRoles ADMINISTRATOR) {
        this.ADMINISTRATOR = ADMINISTRATOR;
    }
}
