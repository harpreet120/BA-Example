package lahmmp.budget.budgetManagement;

import jdk.jfr.BooleanFlag;
import lahmmp.budget.ConnectionSQL;
import lahmmp.budget.repository.BudgetRepository;
import lahmmp.budget.userManagement.management.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;


//Die Klasse Budget erbt von der Klasse BudgetConnector und hat somit Zugriff auf die Daten aus der Datenbank.
// Zu diesen Daten kommt noch der Freibetrag dazu. (Dieser wird nicht in der Datenbank gespeichert, da er errechnet werden kann.)
//Außerdem wird noch für jedes Budget die Historie (expenses) hinzugefügt.)

public class Budget extends BudgetConnector {

    private float freeAmount;

    private ArrayList<BudgetCosts> expenses;

    public Budget() {

    }

    public Budget(int budgetId, int employeeNumber, float amount,
                  String budgetDescription, Date expirationDate) {
        this.budgetId = budgetId;
        this.employeeNumber = employeeNumber;
        this.plannedAmount = amount;
        this.freeAmount = plannedAmount;
        this.budgetDescription = budgetDescription;
        this.expirationDate = expirationDate;
    }

    private ConnectionSQL connectionSQL = new ConnectionSQL();
    private PreparedStatement prepStmt;

    // Methode um einen Kosteneintrag in der DB hinzuzufügen
    public int addCost(String description, float costHeight, Date date) {
        this.freeAmount = returnFreeAmount(budgetId);

        if (this.plannedAmount >= (this.freeAmount + costHeight)) {
            //ToDatabase
            try {
                prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("INSERT INTO `history`  (`description`, `value`, `date`, `budgetId`) VALUES (?, ?, ?, ?)");
                prepStmt.setString(1, description);
                prepStmt.setFloat(2, costHeight);
                prepStmt.setDate(3, date);
                prepStmt.setInt(4, this.budgetId);
                prepStmt.executeUpdate();
                connectionSQL.connectToDatabase().close();
                prepStmt.close();
                return 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.print("Unallowed Action: Das Budget darf nicht überzogen werden");
        }
        return 0;
    }

    // Methode um einen Forecasteintrag in der DB hinzuzufügen
    public int addForeCast(String description, float costHeight, Date date) {
        this.freeAmount = returnFreeAmount(budgetId);

        if (this.plannedAmount >= (this.freeAmount + costHeight)) {
            //ToDatabase
            try {
                prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("INSERT INTO `Forecast`  (`description`, `value`, `date`, `budgetId`) VALUES (?, ?, ?, ?)");
                prepStmt.setString(1, description);
                prepStmt.setFloat(2, costHeight);
                prepStmt.setDate(3, date);
                prepStmt.setInt(4, this.budgetId);
                prepStmt.executeUpdate();
                connectionSQL.connectToDatabase().close();
                prepStmt.close();
                return 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.print("Unallowed Action: Das Budget darf nicht durch den ForeCast überzogen werden");
        }
        return 0;
    }

    // Methode die das noch verfügbare Budget zurück gibt
    public float returnFreeAmount(int budgetID) {
        float freeAmount;
        float costs = 0;
        float plannedAmount = 0;
        costs = returnCosts(budgetID);
        plannedAmount = returnPlannedAmount(budgetID);
        return freeAmount = plannedAmount - costs;
    }

    // Methode die den geplanten Budgetrahmen zurück gibt
    public float returnPlannedAmount(int budgetID) {
        float plannedAmount = 0;
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("SELECT `plannedAmount` FROM `budget` WHERE `budgetId` = (?)");
            prepStmt.setInt(1, budgetID);
            ResultSet result = prepStmt.executeQuery();
            result.next();
            plannedAmount = result.getFloat("plannedAmount");
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
            result.close();
            return plannedAmount;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Methode die die Summe alle gebuchten Kosten eines Budgets zurück gibt
    public float returnCosts(int budgetID) {
        float costs = 0;
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("SELECT SUM(value) as SUM FROM `history`  WHERE budgetId = (?)");
            prepStmt.setInt(1, budgetID);
            ResultSet result = prepStmt.executeQuery();
            result.next();
            costs = result.getFloat("SUM");
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
            result.close();
            return costs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // Methode die die Summe alle gebuchten Forecasteinträge eines Budgets zurück gibt
    public float returnForecasts(int budgetID) {
        float forecasts = 0;
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("SELECT SUM(value) as SUM FROM `forecast`  WHERE budgetId = (?)");
            prepStmt.setInt(1, budgetID);
            ResultSet result = prepStmt.executeQuery();
            result.next();
            forecasts = result.getFloat("SUM");
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
            result.close();
            return forecasts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // Methode die alle Budgets eines Benutzer zurückgibt
    public int getAllBudgetsByEmployeeNumber(int number) {
        ArrayList<Budget> budgetList = new ArrayList<>();
        ResultSet resultSet;
        Budget budget = new Budget();
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("SELECT * FROM `budget` WHERE `employeeNumber` = (?)");
            prepStmt.setInt(1, number);
            resultSet = prepStmt.executeQuery();
            if(resultSet.next()){
            budget.setEmployeeNumber(resultSet.getInt("employeeNumber"));
            budgetList.add(budget);}
            resultSet.close();
            connectionSQL.connectToDatabase().close();
            prepStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (budgetList.isEmpty()){
            return 0;
        }

        return 1;
    }

    public ArrayList<BudgetCosts> getExpenses() {
        return expenses;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public float getPlannedAmount() {
        return plannedAmount;
    }

    public float getFreeAmount() {
        return freeAmount;
    }

    public String getBudgetDescription() {
        return budgetDescription;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setArchivated(boolean archivated) {
        this.archivated = archivated;
    }


}
