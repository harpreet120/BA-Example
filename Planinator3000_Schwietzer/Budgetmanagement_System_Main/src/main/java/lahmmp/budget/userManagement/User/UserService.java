package lahmmp.budget.userManagement.User;

import lahmmp.budget.ConnectionSQL;
import lahmmp.budget.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

// Für Thymeleaf, damit diese Klasse als ein Service gesehen wird
@Service
public class UserService implements UserServiceInterface {

    private ConnectionSQL connectionSQL = new ConnectionSQL();

    @Autowired
    private UserRepository userRepository;

    public void getUser() {
        Statement statement;
        ResultSet resultSet;
        int counter = 0;
        try {
            statement = connectionSQL.connectToDatabase().createStatement();
            resultSet = statement.executeQuery("SELECT * FROM user");
            while (resultSet.next()) {
                String userID = resultSet.getString("UserID");
                String name = resultSet.getString("LoginDaten");
                String rolle = resultSet.getString("Rolle");
                counter++;
                String outPut = counter + ". employeeNumber: " + userID + "| name: " + name + "| userRole: " + rolle + "\n";//"| lastname: " + lastname + "| password: " + password + "| username: " + username ;
                System.out.println(outPut);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Methoden aus dem Interface. Durch sie lassen sich alle User leicht ausgeben
    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    // Methode aus dem Interface. Durch sie lässt sich ein User in der Datenbank speichern
    @Override
    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    // Eine Methode zur Überprüfung beim login. Ein Beispiel User wird mit Benutzername und Passwort mitgegeben und in der Datenbank überprüft, ob es existiert
    public User getUserByUsernamePassword(User beispiel) {
        PreparedStatement prepstatement;
        ResultSet resultSet;
        User result = new User();
        if (checkIfUserExist(beispiel) == true) {
            try {
                prepstatement = connectionSQL.connectToDatabase().prepareStatement("SELECT `name`,`lastname`,`username`,`userRole`,`password`,`employeeNumber` FROM `db_planinator`.`user` WHERE `username` = (?) AND `password` = (?)");
                prepstatement.setString(1, beispiel.getUsername());
                prepstatement.setString(2, beispiel.getPassword());
                resultSet = prepstatement.executeQuery();
                resultSet.next();
                result.setName(resultSet.getString(1));
                result.setLastname(resultSet.getString(2));
                result.setUsername(resultSet.getString(3));
                result.setUserRole(UserRoles.valueOf(resultSet.getString(4)));
                result.setPassword(resultSet.getString(5));
                result.setEmployeeNumber(resultSet.getInt(6));
                resultSet.close();
                prepstatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return result;
    }

    //Eine Methode zur Überprüfung, ob ein User bereits existiert.
    public boolean checkIfUserExist(User beispiel) {
        PreparedStatement prepstatement;
        ResultSet resultSet;
        try {
            prepstatement = connectionSQL.connectToDatabase().prepareStatement("SELECT `name`,`lastname`,`username`,`userRole`,`password`,`employeeNumber` FROM `db_planinator`.`user` WHERE `username` = (?) AND `password` = (?)");
            prepstatement.setString(1, beispiel.getUsername());
            prepstatement.setString(2, beispiel.getPassword());
            resultSet = prepstatement.executeQuery();
            if (!resultSet.next()) {
                prepstatement.close();
                resultSet.close();
                return false;
            } else {
                prepstatement.close();
                resultSet.close();
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    // Methode die anhand der UnserId den User eindeutig in der Datenbank findet und zurück gibt
    public User getUserByID(int userId) {
        PreparedStatement prepstatement;
        ResultSet resultSet;
        User result = new User();
        try {
            prepstatement = connectionSQL.connectToDatabase().prepareStatement("SELECT `name`,`lastname`,`username`,`userRole`,`password`,`employeeNumber` FROM `db_planinator`.`user` WHERE `employeeNumber` = (?) ");
            prepstatement.setInt(1, userId);
            resultSet = prepstatement.executeQuery();
            resultSet.next();
            result.setName(resultSet.getString(1));
            result.setLastname(resultSet.getString(2));
            result.setUsername(resultSet.getString(3));
            result.setUserRole(UserRoles.valueOf(resultSet.getString(4)));
            result.setPassword(resultSet.getString(5));
            result.setEmployeeNumber(resultSet.getInt(6));
            resultSet.close();
            prepstatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
