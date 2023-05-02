package lahmmp.budget.userManagement.management;

import lahmmp.budget.budgetManagement.Budget;
import lahmmp.budget.budgetManagement.BudgetString;
import lahmmp.budget.loginManagement.LoginController;
import lahmmp.budget.userManagement.User.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.ArrayList;


@Controller
public class ManagementController {

    int searchedBudgetID = 0;

    @Autowired
    private ManagementService managementService;

    LoginController testAccess = new LoginController();

    // Management1------------------------------------------------------------------------------------------------
    //Methode um die Errors bei Seitenwechsel zurückzusetzen
    @GetMapping("/Budget_Uebersicht_Management_Reset")
    public String Budget_Uebersicht_Management_Reset(Model model, HttpSession session) {
        ManagementErr err = (ManagementErr) session.getAttribute("err");
        err.resetErr();
        session.setAttribute("err",err);
        return "redirect:/Budget_Uebersicht_Management";
    }

    //Methode welche die übersicht des Managements zurück gibt
    @GetMapping("/Budget_Uebersicht_Management")
    public String Budget_Uebersicht_Management(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            BudgetString budgetid = new BudgetString();
            model.addAttribute("budgetid", budgetid);
            model.addAttribute("listBudget", managementService.getAllBudgets());
            return "management1";
        }
        return "redirect:/";
    }

    //Methode welche die eine gefilterte Sicht auf die Budgets zurück gibt
    @GetMapping("/Budget_Uebersicht_Management_byID")
    public String Budget_Uebersicht_Management_byID(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            ManagementErr err = (ManagementErr) session.getAttribute("err");
            BudgetString budgetid = new BudgetString();
            if (searchedBudgetID == 0) {
                model.addAttribute("budgetid", budgetid);
                model.addAttribute("listBudget", managementService.getAllBudgets());
                err.setNoBudgetFoundErr(false);

            } else if (searchedBudgetID == -5) {
                err.setNoBudgetFoundErr(true);
                model.addAttribute("budgetid", budgetid);
                model.addAttribute("listBudget", managementService.getAllBudgets());
            }
            else {
                err.setNoBudgetFoundErr(false);
                model.addAttribute("budgetid", budgetid);
                model.addAttribute("listBudget", managementService.getBudget(searchedBudgetID));
            }
            session.setAttribute("err",err);

            return "management1";
        }
        return "redirect:/";
    }

    //Methode die das Filtern nach der BudegtId vorbereitet
    @PostMapping("/filterBudget_Uebersicht_Management")
    public String filterBudget_Uebersicht_Management(@ModelAttribute("budgetid") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            ManagementErr err = (ManagementErr) session.getAttribute("err");

            Budget budgetid = new Budget();

            try {
                budgetid.setBudgetId(Integer.valueOf(budgetString.getBudgetId()));
                err.setBudgetIdErr(false);
                if (budgetid.getBudgetId() == 0) {
                    searchedBudgetID = 0;
                    err.setBudgetIdErr(false);
                    session.setAttribute("err", err);
                    return "redirect:/Budget_Uebersicht_Management_byID";
                }
            } catch (NumberFormatException e) {
                err.setBudgetIdErr(true);
                session.setAttribute("err",err);
                searchedBudgetID = 0;
                return "redirect:/Budget_Uebersicht_Management_byID";
            }

            if (managementService.filterBudget_Uebersicht_Management(budgetid)==0){
                searchedBudgetID = -5;
                return "redirect:/Budget_Uebersicht_Management_byID";
            }
            searchedBudgetID = (managementService.filterBudget_Uebersicht_Management(budgetid));
            return "redirect:/Budget_Uebersicht_Management_byID";
        }
        return "redirect:/";
    }
    // Management1------------------------------------------------------------------------------------------------

    // Management2------------------------------------------------------------------------------------------------
    //Methode um die Errors bei Seitenwechsel zurückzusetzen
    @GetMapping("/Budget_anlegen_Reset")
    public String Budget_anlegen_Reset(Model model, HttpSession session) {
        ManagementErr err = (ManagementErr) session.getAttribute("err");
        err.resetErr();
        session.setAttribute("err",err);
        return "redirect:/Budget_anlegen";
    }

    //Methode die das Speichern eines Budgets vorbereitet
    @GetMapping("/Budget_anlegen")
    public String Budget_anlegen(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            BudgetString budget = new BudgetString();
            model.addAttribute("budget", budget);
            return "management2";
        }
        return "redirect:/";
    }

    //Methode die neue Budgets speichert und anschließend auf "/Budget_anlegen" verweist
    @PostMapping("/saveBudget")
    public String saveBudget(@ModelAttribute("budget") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            ManagementErr err = (ManagementErr) session.getAttribute("err");
            Budget budget = new Budget();


            budget.setBudgetDescription(budgetString.getBudgetDescription());
            try {
                budget.setEmployeeNumber(Integer.valueOf(budgetString.getEmployeeNumber()));
                err.setEmployeenumberErr(false);
            } catch (NumberFormatException e) {
                err.setEmployeenumberErr(true);
            }
            try {
                budget.setExpirationDate(Date.valueOf(budgetString.getExpirationDate()));
                err.setDateErr(false);
            } catch (IllegalArgumentException e) {
                err.setDateErr(true);
            }
            try {
                budget.setPlannedAmount(Float.valueOf(budgetString.getPlannedAmount()));
                err.setPlanntamountErr(false);
            } catch (NumberFormatException e) {
                err.setPlanntamountErr(true);
            }
            session.setAttribute("err",err);
            if (err.isEmployeenumberErr() ||
                err.isDateErr() ||
                err.isPlanntamountErr()) {
                return "redirect:/Budget_anlegen";
            }

            managementService.saveBudget(budget);
            return "redirect:/Budget_anlegen";
        }
        return "redirect:/";
    }
    // Management2------------------------------------------------------------------------------------------------


    // Management3------------------------------------------------------------------------------------------------
    ArrayList<Budget> budgettestlist = null;
    int budgetid = 1;

    //Methode um die Errors bei Seitenwechsel zurückzusetzen
    @GetMapping("/Budget_anpassen_Reset")
    public String Budget_anpassen_Reset(Model model, HttpSession session) {
        ManagementErr err = (ManagementErr) session.getAttribute("err");
        err.resetErr();
        session.setAttribute("err",err);
        return "redirect:/Budget_anpassen";
    }

    //Methode die das Anpassen von Budgets vorbereitet
    @GetMapping("/Budget_anpassen")
    public String Budget_anpassen(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            BudgetString budgetsearchid = new BudgetString();
            model.addAttribute("budgetsearchid", budgetsearchid);
            model.addAttribute("listbudgetsearchid", managementService.getBudget(budgetid));

            return "management3";
        }
        return "redirect:/";
    }

    //Methode die nach bestimmten Budgets suchen lässt
    @PostMapping("/searchBudget")
    public String searchBudget(@ModelAttribute("budgetsearchid") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            ManagementErr err = (ManagementErr) session.getAttribute("err");
            Budget budgetsearchid = new Budget();

            try {
                budgetsearchid.setBudgetId(Integer.valueOf(budgetString.getBudgetId()));
                err.setBudgetIdErr(false);
            } catch (NumberFormatException e) {
                err.setBudgetIdErr(true);
            }


            if (managementService.filterBudget_Uebersicht_Management(budgetsearchid)==0){
                err.setNoBudgetFoundErr(true);
                session.setAttribute("err",err);
                budgetid = 0;
                return "redirect:/Budget_anpassen";
            }

            budgetid = (managementService.filterBudget_Uebersicht_Management(budgetsearchid));
            err.setNoBudgetFoundErr(false);
            session.setAttribute("err",err);
            return "redirect:/Budget_anpassen";
        }
        return "redirect:/";
    }

    //Methode welche die Beschreibung eines Budgets ändern lässt
    @PostMapping("/modifyBudgetDescription")
    public String modifyBudgetDescription(@ModelAttribute("budgetsearchid") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            Budget budget = new Budget();

            budget.setBudgetDescription(budgetString.getBudgetDescription());

            managementService.modifyDescription(budget, budgetid);
            return "redirect:/Budget_anpassen";
        }
        return "redirect:/";
    }

    //Methode welche die Mitarbeiternummer eines Budgets ändern lässt
    @PostMapping("/modifyEmployeeNumber")
    public String modifyEmployeeNumber(@ModelAttribute("budgetsearchid") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            ManagementErr err = (ManagementErr) session.getAttribute("err");
            Budget budget = new Budget();
            try {
                budget.setEmployeeNumber(Integer.valueOf(budgetString.getEmployeeNumber()));
                err.setEmployeenumberErr(false);
            } catch (NumberFormatException e) {
                err.setEmployeenumberErr(true);
            }
            session.setAttribute("err",err);
            if (err.isEmployeenumberErr()) {
                return "redirect:/Budget_anpassen";
            }
            if(managementService.modifyEmployeeNumber(budget, budgetid)==0){
                err.setNoOwnerFoundErr(false);
                return "redirect:/Budget_anpassen";
            }
            else {
                err.setNoOwnerFoundErr(true);
                return "redirect:/Budget_anpassen";
            }
        }
        return "redirect:/";
    }

    //Methode welche den verfügbaren Budget-Rahmen ändern lässt
    @PostMapping("/modifyPlannedAmount")
    public String modifyPlannedAmount(@ModelAttribute("budgetsearchid") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            ManagementErr err = (ManagementErr) session.getAttribute("err");
            Budget budget = new Budget();
            try {
                budget.setPlannedAmount(Float.valueOf(budgetString.getPlannedAmount()));
                err.setPlanntamountErr(false);
            } catch (NumberFormatException e) {
                err.setPlanntamountErr(true);
            }
            session.setAttribute("err",err);
            if (err.isEmployeenumberErr()) {
                return "redirect:/Budget_anpassen";
            }
            managementService.modifyPlannedAmount(budget, budgetid);
            return "redirect:/Budget_anpassen";
        }
        return "redirect:/";
    }

    //Methode welche das Attribut "Archivated" eines Budgets ändern lässt
    @PostMapping("/modifyidArchivated")
    public String modifyidArchivated(@ModelAttribute("budgetsearchid") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            ManagementErr err = (ManagementErr) session.getAttribute("err");
            Budget budget = new Budget();
            try {
                budget.setArchivated(Boolean.valueOf(budgetString.getArchivated()));
                err.setArchivatedErr(false);
            } catch (Exception e) {
                err.setArchivatedErr(true);
            }
            session.setAttribute("err",err);
            if (err.isArchivatedErr()) {
                return "redirect:/Budget_anpassen";
            }
            managementService.modifyisArchivated(budget, budgetid);
            return "redirect:/Budget_anpassen";
        }
        return "redirect:/";
    }

    //Methode welche das Ablaufdatum eines Budgets ändern lässt
    @PostMapping("/modifyExpirationDate")
    public String modifyExpirationDate(@ModelAttribute("budgetsearchid") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            ManagementErr err = (ManagementErr) session.getAttribute("err");
            Budget budget = new Budget();
            try {
                budget.setExpirationDate(Date.valueOf(budgetString.getExpirationDate()));
                err.setDateErr(false);
            } catch (IllegalArgumentException e) {
                err.setDateErr(true);
            }
            if (err.isDateErr()) {
                return "redirect:/Budget_anpassen";
            }
            managementService.modifyExpirationDate(budget, budgetid);
            return "redirect:/Budget_anpassen";
        }
        return "redirect:/";
    }

    // Management3------------------------------------------------------------------------------------------------


    // Management4------------------------------------------------------------------------------------------------
    //Methode um die Errors bei Seitenwechsel zurückzusetzen
    @GetMapping("/Soll_Ist_Vergleich_Management_Reset")
    public String Soll_Ist_Vergleich_Management_Reset(Model model, HttpSession session) {
        ManagementErr err = (ManagementErr) session.getAttribute("err");
        err.resetErr();
        session.setAttribute("err",err);
        return "redirect:/Soll_Ist_Vergleich_Management";
    }

    //Methode welche alle Budgets für den Soll-Ist-Vergleich zurückgibt
    @GetMapping("/Soll_Ist_Vergleich_Management")
    public String Soll_Ist_Vergleich_Management(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            Budget budgetid = new Budget();
            model.addAttribute("budgetid", budgetid);
            model.addAttribute("listBudget", managementService.getAllBudgetsButWithLessAttributes());
            return "management4";
        }
        return "redirect:/";
    }

    //Methode welche den Soll-Ist-Vergleich nach der Budget Id filtert
    @GetMapping("/Soll_Ist_Vergleich_Management_byID")
    public String Soll_Ist_Vergleich_Management_byID(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            Budget budgetid = new Budget();
            if (searchedBudgetID == 0) {
                model.addAttribute("budgetid", budgetid);
                model.addAttribute("listBudget", managementService.getAllBudgetsButWithLessAttributes());
            } else {
                model.addAttribute("budgetid", budgetid);
                model.addAttribute("listBudget", managementService.getBudget(searchedBudgetID));
            }

            return "management4";
        }
        return "redirect:/";
    }

    //Vorbereitende Methode zur Filterung des Soll-Ist-Vergleich
    @PostMapping("/filterSoll_Ist_Vergleich_Management")
    public String filterSoll_Ist_Vergleich_Management(@ModelAttribute("budgetid") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.MANAGEMENT)) {
            ManagementErr err = (ManagementErr) session.getAttribute("err");
            Budget budget = new Budget();
            try {
                budget.setBudgetId(Integer.valueOf(budgetString.getBudgetId()));
                err.setBudgetIdErr(false);
            } catch (NumberFormatException e) {
                err.setBudgetIdErr(true);
            }
            if (budgetString.getBudgetId().equals("0")){
                err.setNoBudgetFoundErr(false);
                session.setAttribute("err",err);
                searchedBudgetID = 0;
                return "redirect:/Soll_Ist_Vergleich_Management_byID";
            }
            if (managementService.filterBudget_Uebersicht_Management(budget)==0){
                err.setNoBudgetFoundErr(true);
                session.setAttribute("err",err);
                searchedBudgetID = 0;
                return "redirect:/Soll_Ist_Vergleich_Management_byID";
            }
            err.setNoBudgetFoundErr(false);
            session.setAttribute("err",err);
            searchedBudgetID = (managementService.filterBudget_Uebersicht_Management(budget));
            return "redirect:/Soll_Ist_Vergleich_Management_byID";
        }
        return "redirect:/";
    }
    // Management4------------------------------------------------------------------------------------------------

}
