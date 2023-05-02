package lahmmp.budget.userManagement.management;

public class ManagementErr {

    private Boolean employeenumberErr;
    private Boolean planntamountErr;
    private Boolean archivatedErr;
    private Boolean dateErr;
    private Boolean budgetIdErr;
    private Boolean noBudgetFoundErr;
    private Boolean noOwnerFoundErr;

    public Boolean isEmployeenumberErr() {
        return this.employeenumberErr;
    }

    public void setEmployeenumberErr(final Boolean employeenumberErr) {
        this.employeenumberErr = employeenumberErr;
    }

    public Boolean isPlanntamountErr() {
        return this.planntamountErr;
    }

    public void setPlanntamountErr(final Boolean planntamountErr) {
        this.planntamountErr = planntamountErr;
    }

    public Boolean isArchivatedErr() {
        return this.archivatedErr;
    }

    public void setArchivatedErr(final Boolean archivatedErr) {
        this.archivatedErr = archivatedErr;
    }

    public Boolean isDateErr() {
        return this.dateErr;
    }

    public void setDateErr(final Boolean dateErr) {
        this.dateErr = dateErr;
    }

    public Boolean isBudgetIdErr() {
        return this.budgetIdErr;
    }

    public void setBudgetIdErr(final Boolean budgetIdErr) {
        this.budgetIdErr = budgetIdErr;
    }

    public Boolean isNoBudgetFoundErr() {return this.noBudgetFoundErr;}

    public void setNoBudgetFoundErr(final Boolean noBudgetFoundErr) {this.noBudgetFoundErr = noBudgetFoundErr;}

    public Boolean isNoOwnerFoundErr() {return this.noOwnerFoundErr;}

    public void setNoOwnerFoundErr(final Boolean noOwnerFoundErr) {this.noOwnerFoundErr = noOwnerFoundErr;}

    public void resetErr() {
        employeenumberErr = false;
        planntamountErr = false;
        archivatedErr = false;
        dateErr = false;
        budgetIdErr = false;
        noBudgetFoundErr = false;
        noOwnerFoundErr = false;
    }

}
