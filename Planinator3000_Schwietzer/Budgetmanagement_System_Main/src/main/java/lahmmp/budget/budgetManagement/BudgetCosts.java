package lahmmp.budget.budgetManagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;


//BudgetCosts spiegelt die History der Budgets wieder. Diese sind auch mit der Datenbank verbunden durch das @Table

@Entity
@Table(name = "history")
public class BudgetCosts {      //Klasse BudgetCosts entspricht der "Historie eines Budgets" in der DB

    @Id
    @Column(name = "costId")
    private int costId;
    @Column(name = "description")
    private String description;
    @Column(name = "value")
    private float value;
    @Column(name = "date")
    private Date date;
    @Column(name = "budgetId")
    private int budgetId;

    public BudgetCosts(String description, float value, Date date) {
        this.description = description;
        this.value = value;
        this.date = date;
    }

    public BudgetCosts() {
            }

    public int getCostId() {
        return costId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCostId(final int costId) {
        this.costId = costId;
    }

    public int getBudgetId() {
        return this.budgetId;
    }

    public void setBudgetId(final int budgetId) {
        this.budgetId = budgetId;
    }
}
