package lahmmp.budget.userManagement.owner;

import lahmmp.budget.ConnectionSQL;
import lahmmp.budget.budgetManagement.Budget;
import lahmmp.budget.budgetManagement.BudgetConnector;
import lahmmp.budget.budgetManagement.BudgetCosts;
import lahmmp.budget.budgetManagement.Forecast;
import lahmmp.budget.repository.BudgetRepository;
import lahmmp.budget.repository.ForecastRepository;
import lahmmp.budget.repository.HistoryRepository;
import lahmmp.budget.userManagement.User.UserRoles;
import lahmmp.budget.userManagement.management.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

// Für Thymeleaf damit diese Klasse ein Service ist
@Service
public class OwnerService {

    @Autowired
    private BudgetRepository budgetRepository;

    private ConnectionSQL connectionSQL = new ConnectionSQL();
    private PreparedStatement prepStmt;
    private Owner myOwner;

    //Methode damit nur die Budgets mit der passenden Employee Nummer ausgegeben werden
    public boolean checkOwner (int budgetid, int employeenumber) {
        ArrayList<Budget> budgetList = getAllBudgetsOwner(employeenumber);

        for ( Budget budget : budgetList){
            if(budget.getBudgetId()==budgetid){
                return true;
            }
        }
        return false;
    }

    // Methode zur Überprüfung, ob das Budget archiviert ist
    public boolean checkArchivated (int budgetid) {
        ArrayList<Budget> budgetList = getBudgetOwner(budgetid);
        boolean test = budgetList.get(0).isArchivated();
        boolean test2 = false;
            if((budgetList.get(0).isArchivated())==false){
                return true;
            }
        return false;
    }


    // Owner 1 --------------------------------------------------------------------
    // Listet alle Budgets eines Owners auf
    public ArrayList<Budget> getAllBudgetsOwner(int sessionUserEmployeeNumber) {
        ArrayList<Budget> budgetList = new ArrayList<>();

        for (BudgetConnector u : budgetRepository.findAll()) {
            Budget b = new Budget();
            if (u.getEmployeeNumber() == sessionUserEmployeeNumber) {
                b.setExpirationDate(u.getExpirationDate());
                b.setBudgetDescription(u.getBudgetDescription());
                b.setPlannedAmount(u.getPlannedAmount());
                b.setBudgetId(u.getBudgetId());
                b.setArchivated(u.isArchivated());
                b.setEmployeeNumber(u.getEmployeeNumber());
                budgetList.add(b);
            }
        }

        return budgetList;
    }

    // Gibt ein Budget Objekt zurück um zu prüfen ob der Benutzer Owner hiervon ist. Dies passiert in der Methode die diese aufruft
    public ArrayList<Budget> getBudgetOwner(int searchedBudgetID) {
        ArrayList<Budget> budgetList = new ArrayList<>();

        for (BudgetConnector u : budgetRepository.findAll()) {
            Budget b = new Budget();
            if (u.getBudgetId() == searchedBudgetID) {
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

    public int filterBudget_Uebersicht_Owner(Budget budgetid, int sessionUserEmployeeNumber) {
        ArrayList<Budget> budgetList = getAllBudgetsOwner(sessionUserEmployeeNumber);
        int result = 0;

        for (Budget b : budgetList) {
            if (b.getBudgetId() == budgetid.getBudgetId()) {
                result = (budgetid.getBudgetId());
                return result;
            }
        }

        return result;
    }

    //Ende Owner 1 -----------------------------------------------------------
    @Autowired
    private HistoryRepository historyRepository;

    // Owner 2 -------------------------------------------------------------------------
    public ArrayList<BudgetCosts> getAllCosts(int sessionUserEmployeeNumber) {
        ArrayList<BudgetCosts> costList = new ArrayList<>();
        ArrayList<Budget> budgetList = getAllBudgetsOwner(sessionUserEmployeeNumber);
        ArrayList<Integer> budgetListCompare = new ArrayList<>();

        for (Budget bud : budgetList) {
            budgetListCompare.add(bud.getBudgetId());
        }

        for (BudgetCosts costs : historyRepository.findAll()) {
            for (Integer i : budgetListCompare) {
                BudgetCosts c = new BudgetCosts();
                if (i == costs.getBudgetId()) {
                    c.setCostId(costs.getCostId());
                    c.setDescription(costs.getDescription());
                    c.setValue(costs.getValue());
                    c.setDate(costs.getDate());
                    c.setBudgetId(costs.getBudgetId());
                    costList.add(c);
                }
            }
        }
        return costList;
    }

    public ArrayList<BudgetCosts> getCostsByBudgetId(int budgetID) {
        ArrayList<BudgetCosts> costList = new ArrayList<>();

        for (BudgetCosts costs : historyRepository.findAll()) {
            BudgetCosts c = new BudgetCosts();
            if (budgetID == costs.getBudgetId()) {
                c.setCostId(costs.getCostId());
                c.setDescription(costs.getDescription());
                c.setValue(costs.getValue());
                c.setDate(costs.getDate());
                c.setBudgetId(costs.getBudgetId());
                costList.add(c);
            }
        }

        return costList;
    }

    public void addCosts(BudgetCosts newCost, int budgetId) {

        ManagementService mservice = new ManagementService();

        float freeamount = mservice.returnFreeAmount(budgetId);
        if (freeamount >= newCost.getValue()) {

            try {
                prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("INSERT INTO history (`budgetId`, `description`, `value`, `date`) VALUES (?,?,?,?)");
                prepStmt.setInt(1, newCost.getBudgetId());
                prepStmt.setString(2, newCost.getDescription());
                prepStmt.setFloat(3, newCost.getValue());
                prepStmt.setDate(4, newCost.getDate());
                prepStmt.executeUpdate();
                connectionSQL.connectToDatabase().close();
                prepStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public void modifyCosts(BudgetCosts newCost, int budgetId) {

        ManagementService mservice = new ManagementService();

        float costs = mservice.returnCosts(budgetId);

        String newDescription = "Korrektur: "+ newCost.getDescription();

        if ((costs+newCost.getValue())>= 0) {

            try {
                prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("INSERT INTO history (`budgetId`, `description`, `value`, `date`) VALUES (?,?,?,?)");
                prepStmt.setInt(1, newCost.getBudgetId());
                prepStmt.setString(2, newDescription);
                prepStmt.setFloat(3, newCost.getValue());
                prepStmt.setDate(4, newCost.getDate());
                prepStmt.executeUpdate();
                connectionSQL.connectToDatabase().close();
                prepStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
    // Owner 2 -------------------------------------------------------------------------


    @Autowired
    private ForecastRepository forecastRepository;

    // Owner 3 -------------------------------------------------------------------------
    public ArrayList<Forecast> getAllForecasts(int sessionUserEmployeeNumber) {
        ArrayList<Forecast> forecastList = new ArrayList<>();
        ArrayList<Budget> budgetList = getAllBudgetsOwner(sessionUserEmployeeNumber);
        ArrayList<Integer> budgetListCompare = new ArrayList<>();

        for (Budget bud : budgetList) {
            budgetListCompare.add(bud.getBudgetId());
        }

        for (Forecast forecast : forecastRepository.findAll()) {
            for (Integer i : budgetListCompare) {
                Forecast f = new Forecast();
                if (i == forecast.getBudgetId()) {
                    f.setForeCastId(forecast.getForeCastId());
                    f.setDescription(forecast.getDescription());
                    f.setValue(forecast.getValue());
                    f.setDate(forecast.getDate());
                    f.setBudgetId(forecast.getBudgetId());
                    forecastList.add(f);
                }
            }
        }
        return forecastList;
    }

    public ArrayList<Forecast> getForecastsByBudgetId(int budgetID) {
        ArrayList<Forecast> forecastList = new ArrayList<>();

        for (Forecast forecast : forecastRepository.findAll()) {
            Forecast f = new Forecast();
            if (budgetID == forecast.getBudgetId()) {
                f.setForeCastId(forecast.getForeCastId());
                f.setDescription(forecast.getDescription());
                f.setValue(forecast.getValue());
                f.setDate(forecast.getDate());
                f.setBudgetId(forecast.getBudgetId());
                forecastList.add(f);
            }
        }

        return forecastList;
    }

    public void addForecasts(Forecast newForecast, int budgetId) {
        try {
            prepStmt = this.connectionSQL.connectToDatabase().prepareStatement("INSERT INTO forecast (`budgetId`, `description`, `value`, `date`) VALUES (?,?,?,?)");
            prepStmt.setInt(1, newForecast.getBudgetId());
            prepStmt.setString(2, newForecast.getDescription());
            prepStmt.setFloat(3, newForecast.getValue());
            prepStmt.setDate(4, newForecast.getDate());
            prepStmt.executeUpdate();
            connectionSQL.connectToDatabase().close();
            prepStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Owner 3 -------------------------------------------------------------------------
}
