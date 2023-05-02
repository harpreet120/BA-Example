package lahmmp.budget.userManagement.administrator;

import lahmmp.budget.ConnectionSQL;
import lahmmp.budget.budgetManagement.Budget;
import lahmmp.budget.userManagement.User.User;
import lahmmp.budget.userManagement.User.UserRoles;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static lahmmp.budget.userManagement.User.UserRoles.*;

@Service // Für Thymeleaf, damit diese Klasse ein Service ist
public class AdministratorService {

    private ConnectionSQL connectionSQL = new ConnectionSQL();

    // Methode zur anpassung der Rolle anhand der Employee Number
    public Integer changeRoleByEmployeeNumber(int employeeNumber, UserRoles newRole) {
        Budget budget = new Budget();
        PreparedStatement prepStmt;
        if (budget.getAllBudgetsByEmployeeNumber(employeeNumber)==0){
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("UPDATE `user` set `userRole` = (?) WHERE `employeeNumber` = (?)");
            prepStmt.setString(1, newRole.toString());
            prepStmt.setInt(2, employeeNumber);
            prepStmt.executeUpdate();
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }}
        return null;
    }

    // Sucht alle User mit anhand von Name und Nachname
    public ArrayList<User> searchUserByName(String name, String lastname) {
        ArrayList<User> user = new ArrayList<User>();

        if(name.isEmpty() && lastname.isEmpty()){
            return null;
        }

        PreparedStatement prepStmt;
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("SELECT `name`, `lastname`, `username`, `userRole`, `password`, `employeeNumber`" +
                    " FROM `user` WHERE `name` = (?) and `lastname` = (?) ");
            prepStmt.setString(1, name);
            prepStmt.setString(2, lastname);
            ResultSet result = prepStmt.executeQuery();

            while (result.next()) {
                String name1 = result.getString("name");
                String lastname1 = result.getString("lastname");
                String username1 = result.getString("username");
                UserRoles role1 = UserRoles.valueOf(result.getString("userRole").toUpperCase()); //kann man in der DB auch ein Enum speichern? @Paul Sevecke
                String password1 = result.getString("password");
                int employeeNumber1 = result.getInt("employeeNumber");


                User user1 = new User(name1, lastname1, role1, password1, username1, employeeNumber1);

                user.add(user1);
            }
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
            result.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }


}
