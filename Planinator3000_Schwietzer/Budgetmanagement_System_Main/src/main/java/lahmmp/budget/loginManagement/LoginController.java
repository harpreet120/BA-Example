package lahmmp.budget.loginManagement;


import lahmmp.budget.budgetManagement.Budget;
import lahmmp.budget.budgetManagement.BudgetConnector;
import lahmmp.budget.userManagement.User.User;
import lahmmp.budget.userManagement.User.UserRoles;
import lahmmp.budget.userManagement.User.UserService;
import lahmmp.budget.userManagement.administrator.AdminErr;
import lahmmp.budget.userManagement.management.ManagementErr;
import lahmmp.budget.userManagement.owner.OwnerErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowWebServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;


@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // Methode welche einen Nutzer bei Klick auf logout zurück auf die Loginseite bringt
    @GetMapping("/logout")
    public String logout(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "redirect:/";
    }

    // Loginseite und Methode welche einen nicht autorisierten Nutzer auf die Loginseite bringt
    @GetMapping("/")
    public String testUserForm(Model model, HttpSession session) {
        User user = new User();
        session.setAttribute("userLogin", user);
        model.addAttribute("user", user);
        return "index";
    }

    // Methode die schaut, welche Rolle die Person hat, welche sich eingeloggt hat. Dementsprechend wird diese weitergeleitet.
    @PostMapping("/testUser")
    public String testUser(@ModelAttribute("user") User user, HttpSession session) {
        if (userService.getUserByUsernamePassword(user) != null) {
            user = userService.getUserByUsernamePassword(user);
            session.setAttribute("userLogin", user);
            if (user.getUserRole() == UserRoles.MANAGEMENT) {
                ManagementErr err = new ManagementErr();
                session.setAttribute("err", err);
                return "redirect:/Budget_Uebersicht_Management";
            } else if (user.getUserRole() == UserRoles.ADMINISTRATOR) {
                AdminErr err = new AdminErr();
                session.setAttribute("err", err);
                return "redirect:/User_anlegen";
            } else if (user.getUserRole() == UserRoles.OWNER) {
                OwnerErr err = new OwnerErr();
                session.setAttribute("err", err);
                return "redirect:/Budget_Uebersicht_Owner";
            }
        }
        return "redirect:/";
    }

    // Methode die prüft ob ein Benutzer eine Seite x aufrufen darf
    public boolean testAccess(HttpSession session, UserRoles role) {
        User testAccessUser = (User) session.getAttribute("userLogin");
        if (testAccessUser.getUserRole() == role) {
            return true;
        }
        return false;
    }

}

