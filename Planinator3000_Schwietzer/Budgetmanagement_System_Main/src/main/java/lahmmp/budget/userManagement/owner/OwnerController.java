package lahmmp.budget.userManagement.owner;


import lahmmp.budget.budgetManagement.*;
import lahmmp.budget.loginManagement.LoginController;
import lahmmp.budget.userManagement.User.User;
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
import java.util.zip.DataFormatException;

//Der Controller wird genutzt, um Aufrufe von der Webseite entgegenzunehmen und zu bearbeiten
@Controller
public class OwnerController {

    @Autowired
    OwnerService ownerService;

    int searchedBudgetID = 0;

    LoginController testAccess = new LoginController();

    // Owner 1 --------------------------------------------------------
    ArrayList<Budget> budgetList;

    //Methode um die Errors bei Seitenwechsel zurückzusetzen
    @GetMapping("/Budget_Uebersicht_Reset")
    public String Budget_Uebersicht_Reset(Model model, HttpSession session) {
        OwnerErr err = (OwnerErr) session.getAttribute("err");
        err.resetErr();
        session.setAttribute("err",err);
        return "redirect:/Budget_Uebersicht_Owner";
    }

    //Methode um die Budget Übersicht eines Owners anzuzeigen
    @GetMapping("/Budget_Uebersicht_Owner")
    public String Budget_Uebersicht_Owner(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            BudgetString budgetid = new BudgetString();
            User sessionUser = (User) session.getAttribute("userLogin");
            model.addAttribute("budgetid", budgetid);
            model.addAttribute("listBudget", ownerService.getAllBudgetsOwner(sessionUser.getEmployeeNumber()));
            return "owner";
        }
        return "redirect:/";
    }

    //Methode das Budget mit der ID X anzuzeigen falls es dem Owner gehört
    @GetMapping("/Budget_Uebersicht_Owner_byID")
    public String Budget_Uebersicht_Management_byID(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            BudgetString budgetid = new BudgetString();
            User sessionUser = (User) session.getAttribute("userLogin");
            if (searchedBudgetID == 0) {
                model.addAttribute("budgetid", budgetid);
                model.addAttribute("listBudget", ownerService.getAllBudgetsOwner(sessionUser.getEmployeeNumber()));
            } else {
                model.addAttribute("budgetid", budgetid);
                model.addAttribute("listBudget", ownerService.getBudgetOwner(searchedBudgetID));
            }
            return "owner";
        }
        return "redirect:/";
    }

    //Methode um die Budgets zu filtern
    @PostMapping("/filterBudget_Uebersicht_Owner")
    public String filterBudget_Uebersicht_Management(@ModelAttribute("budgetid") BudgetString budgetString, HttpSession session, Model model) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            OwnerErr err = (OwnerErr) session.getAttribute("err");
            User sessionUser = (User) session.getAttribute("userLogin");

            Budget budgetid = new Budget();

            try {
                budgetid.setBudgetId(Integer.valueOf(budgetString.getBudgetId()));
                err.setBudgetIdErr(false);
            } catch (NumberFormatException e) {
//                e.printStackTrace();
                err.setBudgetIdErr(true);
            }
            if (budgetid.getBudgetId()==0){
                err.setNoBudgetFoundErr(false);
                session.setAttribute("err",err);
                searchedBudgetID = 0;
                return "redirect:/Budget_Uebersicht_Owner_byID";
            }
            if (ownerService.filterBudget_Uebersicht_Owner(budgetid,sessionUser.getEmployeeNumber())==0){
                err.setNoBudgetFoundErr(true);
                session.setAttribute("err",err);
                searchedBudgetID = 0;
                return "redirect:/Budget_Uebersicht_Owner_byID";
            }
            err.setNoBudgetFoundErr(false);
            session.setAttribute("err",err);
            searchedBudgetID = (ownerService.filterBudget_Uebersicht_Owner(budgetid, sessionUser.getEmployeeNumber()));
            return "redirect:/Budget_Uebersicht_Owner_byID";
        }
        return "redirect:/";
    }
    // Ende Owner 1 --------------------------------------------------------------

    //Owner 2 ---------------------------------------------------------------------------------
    //Methode um die Errors bei Seitenwechsel zurückzusetzen
    @GetMapping("/KostenUebersicht_Owner_Reset")
    public String KostenUebersicht_Owner_Reset (Model model, HttpSession session) {
        OwnerErr err = (OwnerErr) session.getAttribute("err");
        err.resetErr();
        session.setAttribute("err",err);
        return "redirect:/Kosten_Uebersicht_Owner";
    }

    // Methode um die Kostenübersicht für alle Budgets eines Owner anzuzeigen
    @GetMapping("/Kosten_Uebersicht_Owner")
    public String Kosten_Uebersicht_Owner(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
//            OwnerErr err = (OwnerErr) session.getAttribute("err");
            BudgetCosts budgetCost = new BudgetCosts();
            User sessionUser = (User) session.getAttribute("userLogin");
            model.addAttribute("budgetCost", budgetCost);
            model.addAttribute("listCosts", ownerService.getAllCosts(sessionUser.getEmployeeNumber()));
            return "/owner2";
        }
        return "redirect:/";
    }

    // Methode um die Kostenübersicht für ein Budget X eines Owner anzuzeigen
    @GetMapping("/Kosten_Uebersicht_Owner_byID")
    public String Kosten_Uebersicht_Owner_byID(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            OwnerErr err = (OwnerErr) session.getAttribute("err");
            session.setAttribute("err",err);
            BudgetCosts budgetCost = new BudgetCosts();
            User sessionUser = (User) session.getAttribute("userLogin");
            if (searchedBudgetID == 0) {
                model.addAttribute("budgetCost", budgetCost);
                model.addAttribute("listCosts", ownerService.getAllCosts(sessionUser.getEmployeeNumber()));
            } else {
                model.addAttribute("budgetCost", budgetCost);
                model.addAttribute("listCosts", ownerService.getCostsByBudgetId(searchedBudgetID));
            }

            return "/owner2";
        }
        return "redirect:/";
    }

    // Methode um die Kostenübersicht eines Owner zu filtern
    @PostMapping("/filterKosten_Uebersicht_Owner")
    public String filterKosten_Uebersicht_Owner(@ModelAttribute("budgetid") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            User sessionUser = (User) session.getAttribute("userLogin");
            OwnerErr err = (OwnerErr) session.getAttribute("err");
            Budget budgetid = new Budget();
            try {
                budgetid.setBudgetId(Integer.valueOf(budgetString.getBudgetId()));
                err.setBudgetIdErr(false);
            } catch (NumberFormatException e) {
//                e.printStackTrace();
                err.setBudgetIdErr(true);
            }
            if (budgetid.getBudgetId()==0){
                err.setNoBudgetFoundErr(false);
                session.setAttribute("err",err);
                searchedBudgetID = 0;
                return "redirect:/Kosten_Uebersicht_Owner_byID";
            }
            if (ownerService.filterBudget_Uebersicht_Owner(budgetid,sessionUser.getEmployeeNumber())==0){
                err.setNoBudgetFoundErr(true);
                session.setAttribute("err",err);
                searchedBudgetID = 0;
                return "redirect:/Kosten_Uebersicht_Owner_byID";
            }
            err.setNoBudgetFoundErr(false);
            session.setAttribute("err",err);
            searchedBudgetID = (ownerService.filterBudget_Uebersicht_Owner(budgetid, sessionUser.getEmployeeNumber()));
            return "redirect:/Kosten_Uebersicht_Owner_byID";
        }
        return "redirect:/";
    }

    //Methode die neue Kostenübersicht anzuzeigen
    @GetMapping("/Kosten_Uebersicht_Owner2")
    public String Kosten_Uebersicht_Owner2(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            BudgetCostsString budgetCostsString = new BudgetCostsString();
            User sessionUser = (User) session.getAttribute("userLogin");
            model.addAttribute("budgetCost", budgetCostsString);
            model.addAttribute("listCosts", ownerService.getCostsByBudgetId(searchedBudgetID));
            return "/owner2";
        }
        return "redirect:/";
    }

    //Methode um neue Kosten hinzuzufügen
    @PostMapping("/addKosten_Uebersicht_Owner")
    public String addKosten_Uebersicht_Owner(@ModelAttribute("budgetCosts") BudgetCostsString budgetCostsString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            OwnerErr err = (OwnerErr) session.getAttribute("err");
            BudgetCosts budgetCosts = new BudgetCosts();
            budgetCosts.setDescription(budgetCostsString.getDescription());
            try {
                budgetCosts.setBudgetId(Integer.valueOf(budgetCostsString.getBudgetId()));
                err.setBudgetIdErr(false);
            } catch (NumberFormatException e) {
                err.setBudgetIdErr(true);
            }
            try {
                budgetCosts.setValue(Float.valueOf(budgetCostsString.getValue()));
                err.setValueErr(false);
            } catch (NumberFormatException e) {
                err.setValueErr(true);
            }
            try {
                budgetCosts.setDate(Date.valueOf(budgetCostsString.getDate()));
                err.setDateErr(false);
            } catch (IllegalArgumentException e) {
                err.setDateErr(true);
            }
            session.setAttribute("err",err);
            if (err.isBudgetIdErr() || err.isDateErr() || err.isValueErr()){
                return "redirect:/Kosten_Uebersicht_Owner";
            }
            User sessionUser = (User) session.getAttribute("userLogin");
            int budgetid = budgetCosts.getBudgetId();
            Budget budget = new Budget(budgetid,0,0,null,null);
            if (ownerService.filterBudget_Uebersicht_Owner(budget,sessionUser.getEmployeeNumber())==0){
                err.setNoBudgetFoundErr(true);
                session.setAttribute("err",err);
                return "redirect:/Kosten_Uebersicht_Owner";
            }
            err.setNoBudgetFoundErr(false);
            session.setAttribute("err",err);
            int employeenumber = sessionUser.getEmployeeNumber();
            if (ownerService.checkOwner(budgetid, employeenumber) == true && ownerService.checkArchivated(budgetid) == true) {
                ownerService.addCosts(budgetCosts, budgetCosts.getBudgetId());
                searchedBudgetID = budgetCosts.getBudgetId();
                return "redirect:/Kosten_Uebersicht_Owner2";
            }
            return "redirect:/Kosten_Uebersicht_Owner";
        }
        return "redirect:/";
    }

    //Methode um eine Kostenkorrekturbuchung hinzuzufügen
    @PostMapping("/modifyKosten_Uebersicht_Owner")
    public String modifyKosten_Uebersicht_Owner(@ModelAttribute("budgetCosts") BudgetCostsString budgetCostsString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            OwnerErr err = (OwnerErr) session.getAttribute("err");
            BudgetCosts budgetCosts =  new BudgetCosts();
            budgetCosts.setDescription(budgetCostsString.getDescription());
            try {
                budgetCosts.setBudgetId(Integer.valueOf(budgetCostsString.getBudgetId()));
                err.setBudgetIdErr(false);
            } catch (NumberFormatException e) {
                err.setBudgetIdErr(true);
            }
            try {
                budgetCosts.setValue(Float.valueOf(budgetCostsString.getValue()));
                err.setValueErr(false);
            } catch (NumberFormatException e) {
                err.setValueErr(true);
            }
            try {
                budgetCosts.setDate(Date.valueOf(budgetCostsString.getDate()));
                err.setDateErr(false);
            } catch (IllegalArgumentException e) {
                err.setDateErr(true);
            }
            session.setAttribute("err",err);
            if (err.isBudgetIdErr() || err.isDateErr() || err.isValueErr()){
                return "redirect:/Kosten_Uebersicht_Owner";
            }
            User sessionUser = (User) session.getAttribute("userLogin");
            int budgetid = budgetCosts.getBudgetId();
            Budget budget = new Budget(budgetid,0,0,null,null);
            if (ownerService.filterBudget_Uebersicht_Owner(budget,sessionUser.getEmployeeNumber())==0){
                err.setNoBudgetFoundErr(true);
                session.setAttribute("err",err);
                return "redirect:/Kosten_Uebersicht_Owner";
            }
            err.setNoBudgetFoundErr(false);
            session.setAttribute("err",err);
            int employeenumber = sessionUser.getEmployeeNumber();
            if (ownerService.checkOwner(budgetid, employeenumber) == true && ownerService.checkArchivated(budgetid) == true) {
                ownerService.modifyCosts(budgetCosts, budgetCosts.getBudgetId());
                searchedBudgetID = budgetCosts.getBudgetId();
                return "redirect:/Kosten_Uebersicht_Owner2";
            }
            return "redirect:/Kosten_Uebersicht_Owner";
        }
        return "redirect:/";
    }


    //Ende Owner 2 -----------------------------------------------------------------------------

    //Owner 3 ----------------------------------------------------------------------------------
    //Methode um die Errors bei Seitenwechsel zurückzusetzen
    @GetMapping("/Forecast_Uebersicht_Owner_Reset")
    public String Forecast_Uebersicht_Owner_Reset (Model model, HttpSession session) {
        OwnerErr err = (OwnerErr) session.getAttribute("err");
        err.resetErr();
        session.setAttribute("err",err);
        return "redirect:/Forecast_Uebersicht_Owner";
    }

    //Methode um die Forecastübersicht für die Budgets eines Owners anzuzeigen
    @GetMapping("/Forecast_Uebersicht_Owner")
    public String Forecast_Uebersicht_Owner(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            Forecast forecast = new Forecast();
            User sessionUser = (User) session.getAttribute("userLogin");
            model.addAttribute("forecast", forecast);
            model.addAttribute("listBudget", ownerService.getAllBudgetsOwner(sessionUser.getEmployeeNumber()));
            model.addAttribute("listForecast", ownerService.getAllForecasts(sessionUser.getEmployeeNumber()));
            return "/owner3";
        }
        return "redirect:/";
    }

    //Methode um die Forecastübersicht für das Budget X eines Owners anzuzeigen
    @GetMapping("/Forecast_Uebersicht_Owner_byID")
    public String Forecast_Uebersicht_Owner_byID(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            Forecast forecast = new Forecast();
            User sessionUser = (User) session.getAttribute("userLogin");
            if (searchedBudgetID == 0) {
                model.addAttribute("forecast", forecast);
                model.addAttribute("listBudget", ownerService.getAllBudgetsOwner(sessionUser.getEmployeeNumber()));
                model.addAttribute("listForecast", ownerService.getAllForecasts(sessionUser.getEmployeeNumber()));
            } else {
                model.addAttribute("forecast", forecast);
                model.addAttribute("listBudget", ownerService.getBudgetOwner(searchedBudgetID));
                model.addAttribute("listForecast", ownerService.getForecastsByBudgetId(searchedBudgetID));
            }

            return "/owner3";
        }
        return "redirect:/";
    }

    //Methode um die Forecastübersicht eines Owners zufiltern
    @PostMapping("/filterForecast_Uebersicht_Owner")
    public String filterForecast_Uebersicht_Owner(@ModelAttribute("budgetid") BudgetString budgetString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            User sessionUser = (User) session.getAttribute("userLogin");
            OwnerErr err = (OwnerErr) session.getAttribute("err");
            Budget budgetid =  new Budget();
            try {
                budgetid.setBudgetId(Integer.valueOf(budgetString.getBudgetId()));
                err.setBudgetIdErr(false);
            } catch (NumberFormatException e) {
//                e.printStackTrace();
                err.setBudgetIdErr(true);
            }
            if (budgetid.getBudgetId()==0){
                err.setNoBudgetFoundErr(false);
                session.setAttribute("err",err);
                searchedBudgetID = 0;
                return "redirect:/Forecast_Uebersicht_Owner_byID";
            }
            if (ownerService.filterBudget_Uebersicht_Owner(budgetid,sessionUser.getEmployeeNumber())==0){
                err.setNoBudgetFoundErr(true);
                session.setAttribute("err",err);
                searchedBudgetID = 0;
                return "redirect:/Forecast_Uebersicht_Owner_byID";
            }
            err.setNoBudgetFoundErr(false);
            session.setAttribute("err",err);
            searchedBudgetID = (ownerService.filterBudget_Uebersicht_Owner(budgetid, sessionUser.getEmployeeNumber()));
            return "redirect:/Forecast_Uebersicht_Owner_byID";
        }
        return "redirect:/";
    }

    //Methode die neue Forecastübersicht anzuzeigen
    @GetMapping("/Forecast_Uebersicht_Owner2")
    public String Forecast_Uebersicht_Owner2(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            Forecast forecast = new Forecast();
            User sessionUser = (User) session.getAttribute("userLogin");
            model.addAttribute("forecast", forecast);
            model.addAttribute("listBudget", ownerService.getBudgetOwner(searchedBudgetID));
            model.addAttribute("listForecast", ownerService.getForecastsByBudgetId(searchedBudgetID));
            return "/owner3";
        }
        return "redirect:/";
    }

    //Methode um einen neuen Forecastwert/Forecastkorrekturwert zu buchen
    @PostMapping("/addForecast_Uebersicht_Owner")
    public String addForecast_Uebersicht_Owner(@ModelAttribute("budgetCosts") ForecastString forecastString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.OWNER)) {
            OwnerErr err = (OwnerErr) session.getAttribute("err");
            Forecast forecast = new Forecast();
            try {
                forecast.setBudgetId(Integer.valueOf(forecastString.getBudgetId()));
                err.setBudgetIdErr(false);
            } catch (NumberFormatException e) {
                err.setBudgetIdErr(true);
            }
            try {
                forecast.setValue(Integer.valueOf(forecastString.getValue()));
                err.setValueErr(false);
            } catch (NumberFormatException e) {
                err.setValueErr(true);
            }
            try {
                forecast.setDate(Date.valueOf(forecastString.getDate()));
                err.setDateErr(false);
            } catch (IllegalArgumentException e) {
                err.setDateErr(true);
            }
            forecast.setDescription(forecastString.getDescription());
            session.setAttribute("err",err);
            if (err.isBudgetIdErr() || err.isDateErr() || err.isValueErr()){
                return "redirect:/Forecast_Uebersicht_Owner";
            }
            User sessionUser = (User) session.getAttribute("userLogin");
            int budgetid = forecast.getBudgetId();
            Budget budget = new Budget(budgetid,0,0,null,null);
            if (ownerService.filterBudget_Uebersicht_Owner(budget,sessionUser.getEmployeeNumber())==0){
                err.setNoBudgetFoundErr(true);
                session.setAttribute("err",err);
                return "redirect:/Forecast_Uebersicht_Owner";
            }
            err.setNoBudgetFoundErr(false);
            session.setAttribute("err",err);
            int employeenumber = sessionUser.getEmployeeNumber();
            if (ownerService.checkOwner(budgetid, employeenumber) == true && ownerService.checkArchivated(budgetid) == true) {
                ownerService.addForecasts(forecast, forecast.getBudgetId());
                searchedBudgetID = forecast.getBudgetId();
                return "redirect:/Forecast_Uebersicht_Owner2";
            }
            return "redirect:/Forecast_Uebersicht_Owner";
        }
        return "redirect:/";
    }

//    private OwnerErr isError()

    //Ende Owner 3 -----------------------------------------------------------------------------
}
