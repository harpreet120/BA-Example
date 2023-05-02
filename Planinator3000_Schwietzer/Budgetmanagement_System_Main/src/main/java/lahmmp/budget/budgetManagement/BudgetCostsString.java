package lahmmp.budget.budgetManagement;

//Diese Klasse wird genutzt, um falscheingaben auf der Webseite in dem Controller abzufangen.
// So kann auf der Webseite alles Mögliche eingegeben werden, da über Strigns die Informationen übertragen werden.
// Erst im Controller wird versucht die Informationen in den jeweiligen Datentyp zu konvertieren und bei Bedarf ein Fehler geworden
public class BudgetCostsString {

    private String costId;
    private String description;
    private String value;
    private String date;
    private String budgetId;

    public BudgetCostsString() {
    }

    public String getCostId() {
        return this.costId;
    }

    public void setCostId(final String costId) {
        this.costId = costId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getBudgetId() {
        return this.budgetId;
    }

    public void setBudgetId(final String budgetId) {
        this.budgetId = budgetId;
    }
}
