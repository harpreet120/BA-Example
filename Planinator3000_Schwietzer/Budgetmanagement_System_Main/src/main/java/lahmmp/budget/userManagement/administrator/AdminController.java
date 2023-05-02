package lahmmp.budget.userManagement.administrator;

import lahmmp.budget.loginManagement.LoginController;
import lahmmp.budget.userManagement.User.User;
import lahmmp.budget.userManagement.User.UserRoles;
import lahmmp.budget.userManagement.User.UserService;
import lahmmp.budget.userManagement.User.UserString;
import lahmmp.budget.userManagement.management.ManagementErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Locale;

//Der Controller wird genutzt, um Aufrufe von der Webseite entgegenzunehmen und zu bearbeiten
@Controller
public class AdminController {


    @Autowired
    private UserService userService;

    LoginController testAccess = new LoginController();

    //Methode um die Seite zur Rollenanpassung aufzurufen
    @GetMapping("/Rolle_anpassen")
    public String Rolle_anpassen(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.ADMINISTRATOR)) {
            User user = new User();
            model.addAttribute("user", user);
            model.addAttribute("listUser", userService.getAllUser());
            return "admin2";
        }
        return "redirect:/";
    }

    //Methode um die Errors bei Seitenwechsel zurückzusetzen
    @GetMapping("/Rolle_anpassen_Reset")
    public String Rolle_anpassen_Reset(Model model, HttpSession session) {
        AdminErr err = (AdminErr) session.getAttribute("err");
        err.resetErr();
        session.setAttribute("err",err);
        return "redirect:/Rolle_anpassen";
    }

    //Methode um die Errors bei Seitenwechsel zurückzusetzen
    @GetMapping("/User_anlegen_Reset")
    public String User_anlegen_Reset(Model model, HttpSession session) {
        AdminErr err = (AdminErr) session.getAttribute("err");
        err.resetErr();
        session.setAttribute("err",err);
        return "redirect:/User_anlegen";
    }

    //Methode um die Seite zur User Erstellung aufzurufen
    @GetMapping("/User_anlegen")
    public String saveUserForm(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.ADMINISTRATOR)) {
            User user = new User();
            model.addAttribute("user", user);
            return "admin1";
        }
        return "redirect:/";
    }

    //Methode um einen Nutzer abzuspeichern in der DB
    @PostMapping("/saveUser")
    public String saveUserForm(@ModelAttribute("user") UserString userString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.ADMINISTRATOR)) {
            AdminErr err = (AdminErr) session.getAttribute("err");
            User user = new User();
            user.setName(userString.getName());
            user.setUsername(userString.getUsername());
            user.setLastname(userString.getLastname());
            user.setPassword(userString.getPassword());
            try {
                user.setUserRole(UserRoles.valueOf(userString.getUserRole().toUpperCase()));
                err.setRoleErr(false);
            } catch (IllegalArgumentException e) {
                err.setRoleErr(true);
            }
            try {
                user.setEmployeeNumber(Integer.valueOf(userString.getEmployeeNumber()));
                err.setEmployeenumberErr(false);
            } catch (NumberFormatException e) {
                err.setEmployeenumberErr(true);
            }
            session.setAttribute("err",err);
            if (err.isEmployeenumberErr() ||
                err.isRoleErr()) {
                return "redirect:/User_anlegen";
            }
            if(!userService.checkIfUserExist(user)) {
                userService.saveUser(user);
                err.setEmployeenumberErr(false);
            } else {
                err.setEmployeenumberErr(true);
                /*Fehlermeldung*/
            }
            session.setAttribute("err",err);
            return "redirect:/User_anlegen";
        }
        return "redirect:/";
    }

    ArrayList<User> userlist;

    @Autowired
    private AdministratorService administratorService;

    //Methode um die Benutzerliste nach Namen zu filtern
    @PostMapping("/searchUserByName")
    public String searchUserByName(@ModelAttribute("user") User user, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.ADMINISTRATOR)) {
            String name = user.getName();
            String lastname = user.getLastname();
            userlist = administratorService.searchUserByName(name, lastname);
            return "redirect:/Rolle_anpassen_Gefiltert";
        }
        return "redirect:/";
    }


    //Methode um die gefilterte Sicht anzuzeigen
    @GetMapping("/Rolle_anpassen_Gefiltert")
    public String Rolle_anpassen_Gefiltert(Model model, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.ADMINISTRATOR)) {
            AdminErr err = (AdminErr) session.getAttribute("err");
            User user = new User();
            model.addAttribute("user", user);
            if (userlist == (null)) {
                err.setEmployeenameErr(false);
                session.setAttribute("err",err);
                return "redirect:/Rolle_anpassen";
            }
            if (userlist.isEmpty()) {
                err.setEmployeenameErr(true);
                session.setAttribute("err",err);
                return "redirect:/Rolle_anpassen";
            } else {
                err.setEmployeenameErr(false);
                session.setAttribute("err",err);
                model.addAttribute("listUser", userlist);
            }
            userlist = new ArrayList<User>();
            return "admin2";
        }
        return "redirect:/";
    }

    //Methode welche Attribute eines Users in der DB ändert
    @PostMapping("/modifyUser")
    public String modifyUser(@ModelAttribute("user") UserString userString, HttpSession session) {
        if (testAccess.testAccess(session, UserRoles.ADMINISTRATOR)) {
            AdminErr err = (AdminErr) session.getAttribute("err");
            User user = new User();

            try {
                user.setEmployeeNumber(Integer.valueOf(userString.getEmployeeNumber()));
                err.setEmployeenumberErr(false);
            } catch (NumberFormatException e) {
                err.setEmployeenumberErr(true);
            }
            try {
                user.setUserRole(UserRoles.valueOf(userString.getUserRole()));
                err.setRoleErr(false);
            } catch (Exception e) {
                err.setRoleErr(true);
            }
            session.setAttribute("err",err);
            if (err.isRoleErr() ||
                err.isEmployeenumberErr()) {
                return "redirect:/Rolle_anpassen";
            }

            int number = user.getEmployeeNumber();
            UserRoles role = user.getUserRole();
            if (number != 0 && role != null) {
                administratorService.changeRoleByEmployeeNumber(number, role);

                User user2 = userService.getUserByID(number);
                String name = user2.getName();
                String lastname = user2.getLastname();
                userlist = administratorService.searchUserByName(name, lastname);
                return "redirect:/Rolle_anpassen_Gefiltert";
            }
            return "redirect:/Rolle_anpassen";
        }
        return "redirect:/";
    }


}
