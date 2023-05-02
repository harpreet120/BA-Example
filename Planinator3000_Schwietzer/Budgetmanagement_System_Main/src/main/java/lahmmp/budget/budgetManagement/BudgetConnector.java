package lahmmp.budget.budgetManagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

//Diese Klasse ist ein Spiegelbild zur Datenbank.
//Die Attribute haben sind nicht private, damit die Klasse Budget, die von dieser Erbt, auch auf die Attribute zugreifen kann

@Entity
@Table(name = "budget")
public class BudgetConnector {

    @Id
    @Column(name = "budgetId")
    int budgetId;

    @Column(name = "employeeNumber")
    int employeeNumber;

    @Column(name = "plannedAmount")
    float plannedAmount;

    @Column(name = "budgetDescription")
    String budgetDescription;

    @Column(name = "expirationDate")
    Date expirationDate;

    @Column(name = "archivated")
    boolean archivated = false;

    public int getBudgetId() {
        return this.budgetId;
    }

    public void setBudgetId(final int budgetId) {
        this.budgetId = budgetId;
    }

    public int getEmployeeNumber() {
        return this.employeeNumber;
    }

    public void setEmployeeNumber(final int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public float getPlannedAmount() {
        return this.plannedAmount;
    }

    public void setPlannedAmount(final float plannedAmount) {
        this.plannedAmount = plannedAmount;
    }

    public String getBudgetDescription() {
        return this.budgetDescription;
    }

    public void setBudgetDescription(final String budgetDescription) {
        this.budgetDescription = budgetDescription;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(final Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isArchivated() {
        return this.archivated;
    }

    public void setArchivated(final boolean archivated) {
        this.archivated = archivated;
    }
}
