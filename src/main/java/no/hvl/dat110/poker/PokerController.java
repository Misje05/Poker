package no.hvl.dat110.poker;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PokerController {

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        String username = (String) session.getAttribute("user");
        if (username == null) return "redirect:/login";
        model.addAttribute("username", username);
        model.addAttribute("chips", session.getAttribute("chips"));
        return "index";
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        int chips = DatabaseManager.loginPlayer(username, password);
        if (chips != -1) {
            session.setAttribute("user", username);
            session.setAttribute("chips", chips);
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/register")
    public String register() { return "register"; }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username, @RequestParam String password, Model model) {
        if (DatabaseManager.registerPlayer(username, password)) {
            model.addAttribute("message", "Registration successful! Please login.");
            return "login";
        }
        model.addAttribute("error", "Registration failed.");
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
