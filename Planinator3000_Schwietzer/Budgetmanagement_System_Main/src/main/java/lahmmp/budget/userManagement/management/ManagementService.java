package lahmmp.budget.userManagement.management;

import com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer;
import lahmmp.budget.ConnectionSQL;
import lahmmp.budget.budgetManagement.Budget;
import lahmmp.budget.budgetManagement.BudgetConnector;
import lahmmp.budget.repository.BudgetRepository;
import lahmmp.budget.repository.UserRepository;
import lahmmp.budget.userManagement.User.User;
import lahmmp.budget.userManagement.User.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ManagementService extends Management {

    @Autowired
    private BudgetRepository budgetRepository;

    public ManagementService(){

    }

    public ManagementService(String name, String lastname, UserRoles userRole, String password, String username, int employeeNumber) {
        super( name,  lastname,  userRole,  password,  username,  employeeNumber);
    }

    private ConnectionSQL connectionSQL = new ConnectionSQL();
    private PreparedStatement prepStmt;

    // Methode die prüft ob ein Nutzer ein Owner ist
    public int verifyAsOwner (int employeeNumber){
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("SELECT `userRole` FROM `user` WHERE `employeeNumber`= (?)");
            prepStmt.setInt(1, employeeNumber);
            ResultSet result = prepStmt.executeQuery();
            result.next();
            if(result.getString("userRole").equalsIgnoreCase("owner")){
                connectionSQL.connectToDatabase().close();
                prepStmt.close();
                result.close();
                return 1;
            }
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Methode die den BudgetOwner eines Budgets anpasst
    public int modifyBudgetOwner (int budgetID, int newEmployeeNumber){
        if(verifyAsOwner(newEmployeeNumber)==1) {
            try {
                prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("UPDATE `budget` set `employeeNumber` = (?) where `budgetId` = (?)");
                prepStmt.setInt(1, newEmployeeNumber);
                prepStmt.setInt(2, budgetID);
                prepStmt.executeUpdate();
                connectionSQL.connectToDatabase().close();
                prepStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            return 1; //Fehler das newEmployeeNumber kein Owner ist
        }
        return 0;
    }

    // Methode die den plannedAmmount eines Budgets anpasst
    public void modifyBudgetAmount (int budgetID, float newAmount){

        float costs = returnCosts(budgetID);
        if (newAmount>= costs && newAmount>=0 ){
            try {
                prepStmt = this.connectionSQL.connectToDatabase().prepareStatement( "UPDATE `budget` set `plannedAmount` = (?) where `budgetId` = (?)");
                prepStmt.setFloat(1, newAmount);
                prepStmt.setInt(2, budgetID);
                prepStmt.executeUpdate();
                connectionSQL.connectToDatabase().close();
                prepStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }}
    }

    // Methode die die Beschreibung eines Budgets anpasst
    public void modifyBudgetDescription (int budgetID, String newDescription){
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement( "UPDATE `budget` set `budgetDescription` = (?) where `budgetId` = (?)");
            prepStmt.setString(1, newDescription);
            prepStmt.setInt(2, budgetID);
            prepStmt.executeUpdate();
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Methode die das Ablaufdatum eines Budgets anpasst
    public void modifyBudgetExpirationDate (int budgetID, Date newDate){
        Date today = new Date(Calendar.getInstance().getTime().getTime());

        if (newDate.compareTo(today)>0){
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement( "UPDATE `budget` set `expirationDate` = (?) where `budgetId` = (?)");
            prepStmt.setDate(1, newDate);
            prepStmt.setInt(2, budgetID);
            prepStmt.executeUpdate();
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }}
    }

    // Methode die den Archivierungsstatus eines Budgets anpasst
    public void archiveBudget (int budgetID, Boolean status){
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement( "UPDATE `budget` set `archivated` = (?) where `budgetId` = (?)");
            prepStmt.setBoolean(1, status);
            prepStmt.setInt(2, budgetID);
            prepStmt.executeUpdate();
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Methode die das verfügbare Budget zurückgibt
    public float returnFreeAmount(int budgetID){
        float freeAmount;
        float costs = 0;
        float plannedAmount = 0;

        costs= returnCosts(budgetID);
        plannedAmount= returnPlannedAmount(budgetID);

        return freeAmount = plannedAmount - costs;
    }

    // Methode die das geplante Budget zurückgibt
    public float returnPlannedAmount(int budgetID){
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

    // Methode die die gebuchten Kosten eines Budgets zurückgibt
    public float returnCosts(int budgetID){
        float costs = 0;

        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("SELECT SUM(value) as SUM FROM `history` WHERE budgetId = (?)");
            prepStmt.setInt(1, budgetID);
            ResultSet result = prepStmt.executeQuery();

            result.next();
            costs= result.getFloat("SUM");
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
            result.close();
            return costs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    // Management1------------------------------------------------------------------------------------------------
    // Methode die alle Budgets zurückgibt
    public ArrayList<Budget> getAllBudgets() {
        ArrayList<Budget> budgetList = new ArrayList<>();

        for (BudgetConnector u: budgetRepository.findAll())
        {
            Budget b = new Budget();
            b.setExpirationDate(u.getExpirationDate());
            b.setBudgetDescription(u.getBudgetDescription());
            b.setPlannedAmount(u.getPlannedAmount());
            b.setBudgetId(u.getBudgetId());
            b.setArchivated(u.isArchivated());
            b.setEmployeeNumber(u.getEmployeeNumber());
            budgetList.add(b);
        }

        return budgetList;
    }
    // Management1------------------------------------------------------------------------------------------------

    // Management2------------------------------------------------------------------------------------------------
    //Methode um ein Budget in der DB zu speichern
    public void saveBudget(Budget budget) {
        Date today = new Date(Calendar.getInstance().getTime().getTime());
        Date newDate = budget.getExpirationDate();

        if (verifyAsOwner(budget.getEmployeeNumber()) == 1 && newDate.compareTo(today) > 0 && budget.getPlannedAmount()>=0 ) {

            BudgetConnector budgetConnector = new BudgetConnector();
            budgetConnector.setBudgetDescription(budget.getBudgetDescription());
            budgetConnector.setPlannedAmount(budget.getPlannedAmount());
            budgetConnector.setEmployeeNumber(budget.getEmployeeNumber());
            budgetConnector.setExpirationDate(budget.getExpirationDate());

            this.budgetRepository.save(budgetConnector);
    }}
    // Management2------------------------------------------------------------------------------------------------

    // Management3------------------------------------------------------------------------------------------------
    // Methode die die Beschreibung eines Budgets anpasst
    public void modifyDescription(Budget budgetsearchid, int budgettest) {

        ManagementService mservice = new ManagementService();
        mservice.modifyBudgetDescription(budgettest,budgetsearchid.getBudgetDescription());
    }
    // Methode die den Owner eines Budgets anpasst
    public int modifyEmployeeNumber(Budget budgetsearchid, int budgettest) {

        ManagementService mservice = new ManagementService();
        return mservice.modifyBudgetOwner(budgettest,budgetsearchid.getEmployeeNumber());
    }
    // Methode die den geplanten Budgetrahmen eines Budgets anpasst
    public void modifyPlannedAmount(Budget budgetsearchid, int budgettest) {

        ManagementService mservice = new ManagementService();
        mservice.modifyBudgetAmount(budgettest,budgetsearchid.getPlannedAmount());
    }
    // Methode die den Archivierungsstatus eines Budgets anpasst
    public void modifyisArchivated(Budget budgetsearchid, int budgettest) {

        ManagementService mservice = new ManagementService();
        mservice.archiveBudget(budgettest,budgetsearchid.isArchivated());
    }
    // Methode die das Ablaufdatum eines Budgets anpasst
    public void modifyExpirationDate(Budget budgetsearchid, int budgettest) {

        ManagementService mservice = new ManagementService();
        mservice.modifyBudgetExpirationDate(budgettest,budgetsearchid.getExpirationDate());
    }

    // Management3------------------------------------------------------------------------------------------------

    // Management4------------------------------------------------------------------------------------------------
    // Methode die alle Budgets aber mit weniger Attributen auflistet
    public ArrayList<Budget> getAllBudgetsButWithLessAttributes() {
        ArrayList<Budget> budgetList = new ArrayList<>();

        for (BudgetConnector u: budgetRepository.findAll())
        {
            Budget b = new Budget();
            b.setBudgetDescription(u.getBudgetDescription());
            b.setPlannedAmount(u.getPlannedAmount());
            b.setBudgetId(u.getBudgetId());
            b.setEmployeeNumber(u.getEmployeeNumber());
            budgetList.add(b);
        }

        return budgetList;
    }
    // Management4------------------------------------------------------------------------------------------------

    //Filteroption------------------------------------------------------------------------------------------------
    //  Methode die ein gesuchtes Budget zurückgibt
    public ArrayList<Budget> getBudget(int searchedBudgetID) {
        ArrayList<Budget> budgetList = new ArrayList<>();

        for (BudgetConnector u: budgetRepository.findAll())
        {
            Budget b = new Budget();
            if (u.getBudgetId()==searchedBudgetID){
                b.setBudgetDescription(u.getBudgetDescription());
                b.setPlannedAmount(u.getPlannedAmount());
                b.setBudgetId(u.getBudgetId());
                b.setEmployeeNumber(u.getEmployeeNumber());
                b.setExpirationDate(u.getExpirationDate());
                b.setArchivated(u.isArchivated());
                budgetList.add(b);
                return budgetList;
            }

        }

        return budgetList;
    }

    // Methode, welche die Budgetübersicht einer ID filtert
    public int filterBudget_Uebersicht_Management (Budget budgetid){
        ArrayList<Budget> budgetList = getAllBudgets();
        int result = 0;

        for (Budget b: budgetList) {
            if (b.getBudgetId() == budgetid.getBudgetId()) {
                result = (b.getBudgetId());
                return result;
            }
        }

        return result;
    }
    //Filteroption------------------------------------------------------------------------------------------------
}
