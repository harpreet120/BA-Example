package lahmmp.budget.budgetManagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

//Diese Klasse ist ein Spiegelbild zu der Datenbank. Deshalb hat sie @Entity und @Table, damit einfache SQL abfragen schnell automatisch erstellt werden können.

@Entity
@Table(name = "forecast")
public class Forecast {

    @Id // Das Attribut @Id zeigt an, wo der Primary Key in der Datenbank ist
    @Column(name = "foreCastId") // Mit @Column wird der Name der Spalte in der Datenkank beschieben  
    private int foreCastId;
    @Column(name = "description")
    private String description;
    @Column(name = "value")
    private float value;
    @Column(name = "date")
    private Date date;
    @Column(name = "budgetId")
    private int budgetId;

    public Forecast(int foreCastId, String description, float value, Date date, int budgetId) {
        this.foreCastId = foreCastId;
        this.description = description;
        this.value = value;
        this.date = date;
        this.budgetId = budgetId;
    }

    public Forecast() {

    }

    public int getForeCastId() {
        return this.foreCastId;
    }

    public void setForeCastId(int id) {
        this.foreCastId = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription( String description) {
        this.description = description;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(final float value) {
        this.value = value;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public int getBudgetId() {
        return this.budgetId;
    }

    public void setBudgetId(final int budgetId) {
        this.budgetId = budgetId;
    }
}