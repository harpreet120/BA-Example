package lahmmp.budget.userManagement.User;

import javax.persistence.*;

@Entity
@Table(name ="user")
public class User {

    @Column(name = "name")
    private String name;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "userRole")
    @Enumerated(EnumType.STRING)
    private UserRoles userRole;
    @Column(name = "password")
    private String password;
    @Column(name = "username")
    private String username; //firmenintern eigene Richtlienen festlegbar
    @Id
    @Column(name = "employeeNumber")
    private int employeeNumber;

    public User(String name, String lastname, UserRoles userRole, String password, String username, int employeeNumber) {
        this.name = name;
        this.lastname = lastname;
        this.userRole = userRole;
        this.password = password;
        this.username = username;
        this.employeeNumber = employeeNumber;
    }

    public User() {
    }

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

    public UserRoles getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoles userRole) {
        this.userRole = userRole;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
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

}
