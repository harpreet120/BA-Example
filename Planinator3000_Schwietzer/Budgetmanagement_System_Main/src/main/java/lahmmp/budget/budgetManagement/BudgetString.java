package lahmmp.budget.budgetManagement;


import java.util.ArrayList;

//BudgetString wird genutzt, um Falscheingaben in dem Controller abzufangen und sie nicht auf der Webseite sind.

public class BudgetString {

    private String budgetId;
    private String employeeNumber;
    private String freeAmount;
    private String budgetDescription;
    private String expirationDate;
    private String expenses;
    private String archivated;
    private String plannedAmount;



    public BudgetString(String budgetId, String employeeNumber, String amount, String budgetDescription, String expirationDate) {
        this.budgetId = budgetId;
        this.employeeNumber = employeeNumber;
        this.freeAmount = amount;
        this.budgetDescription = budgetDescription;
        this.expirationDate = expirationDate;
    }

    public BudgetString() {
    }

    public String getBudgetId() {
        return this.budgetId;
    }

    public void setBudgetId(final String budgetId) {
        this.budgetId = budgetId;
    }

    public String getEmployeeNumber() {
        return this.employeeNumber;
    }

    public void setEmployeeNumber(final String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getFreeAmount() {
        return this.freeAmount;
    }

    public void setFreeAmount(final String freeAmount) {
        this.freeAmount = freeAmount;
    }

    public String getBudgetDescription() {
        return this.budgetDescription;
    }

    public void setBudgetDescription(final String budgetDescription) {
        this.budgetDescription = budgetDescription;
    }

    public String getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(final String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getExpenses() {
        return this.expenses;
    }

    public void setExpenses(final String expenses) {
        this.expenses = expenses;
    }

    public String getArchivated() {
        return this.archivated;
    }

    public void setArchivated(final String archivated) {
        this.archivated = archivated;
    }

    public String getPlannedAmount() {
        return this.plannedAmount;
    }

    public void setPlannedAmount(final String plannedAmount) {
        this.plannedAmount = plannedAmount;
    }
}
