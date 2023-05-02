package lahmmp.budget.userManagement.User;

public class UserString {

    private String name;
    private String lastname;
    private String userRole;
    private String password;
    private String username;
    private String employeeNumber;


    public UserString(String name, String lastname, String userRole, String password, String username, String employeeNumber) {
        this.name = name;
        this.lastname = lastname;
        this.userRole = userRole;
        this.password = password;
        this.username = username;
        this.employeeNumber = employeeNumber;
    }

    public UserString(){}

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public String getUserRole() {
        return this.userRole;
    }

    public void setUserRole(final String userRole) {
        this.userRole = userRole;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmployeeNumber() {
        return this.employeeNumber;
    }

    public void setEmployeeNumber(final String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
}
