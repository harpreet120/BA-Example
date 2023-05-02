package lahmmp.budget.userManagement.owner;

public class OwnerErr {
    private Boolean budgetIdErr;
    private Boolean valueErr;
    private Boolean dateErr;
    private Boolean noBudgetFoundErr;

    public Boolean isBudgetIdErr() {
        return this.budgetIdErr;
    }

    public void setBudgetIdErr(final Boolean budgetIdErr) {
        this.budgetIdErr = budgetIdErr;
    }

    public Boolean isValueErr() {
        return this.valueErr;
    }

    public void setValueErr(final Boolean valueErr) {
        this.valueErr = valueErr;
    }

    public Boolean isDateErr() {
        return this.dateErr;
    }

    public void setDateErr(final Boolean dateErr) {
        this.dateErr = dateErr;
    }

    public Boolean isNoBudgetFoundErr() {return this.noBudgetFoundErr;}

    public void setNoBudgetFoundErr(final Boolean noBudgetFoundErr) {this.noBudgetFoundErr = noBudgetFoundErr;}

    public void resetErr() {
        budgetIdErr = false;
        valueErr = false;
        dateErr = false;
        noBudgetFoundErr = false;
    }
}
