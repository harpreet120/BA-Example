package lahmmp.budget.userManagement.owner;

import lahmmp.budget.ConnectionSQL;
import lahmmp.budget.budgetManagement.Budget;
import lahmmp.budget.userManagement.User.User;
import lahmmp.budget.userManagement.User.UserRoles;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Owner extends User {
    //Kontruktor
    public Owner(String name, String lastname, UserRoles userRole, String password, String username, int employeeNumber) {
        super(name, lastname, userRole, password, username, employeeNumber);
    }

    //Attribute
    private static UserRoles roles = UserRoles.OWNER;
    private ArrayList<Budget> budgetList = new ArrayList<Budget>();

    public ArrayList<Budget> getBudgetList() {
        return budgetList;
    }

    private  ConnectionSQL connectionSQL = new ConnectionSQL();
    private  PreparedStatement prepStmt;

}
