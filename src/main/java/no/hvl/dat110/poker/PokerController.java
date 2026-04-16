package no.hvl.dat110.poker;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PokerController {

    @Autowired
    private PokerService pokerService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        String username = (String) session.getAttribute("user");
        if (username == null) return "redirect:/login";
        model.addAttribute("username", username);
        model.addAttribute("chips", session.getAttribute("chips"));
        model.addAttribute("tables", pokerService.getActiveTables().values());
        return "index";
    }

    @GetMapping("/game/{tableId}")
    public String viewGame(@PathVariable String tableId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("user");
        if (username == null) return "redirect:/login";
        
        PokerTable table = pokerService.getTable(tableId);
        if (table == null) return "redirect:/";
        
        // Add player to table if not present
        boolean joined = false;
        for (Player p : table.getPlayers()) {
            if (p.getName().equals(username)) { joined = true; break; }
        }
        if (!joined) {
            table.addPlayer(new Player(username, (int)session.getAttribute("chips")));
        }
        
        model.addAttribute("table", table);
        model.addAttribute("currentUser", username);
        return "game";
    }

    @PostMapping("/game/{tableId}/start")
    public String startGame(@PathVariable String tableId) {
        PokerTable table = pokerService.getTable(tableId);
        if (table != null) table.startNewRound();
        return "redirect:/game/" + tableId;
    }

    @PostMapping("/game/{tableId}/action")
    public String playerAction(@PathVariable String tableId, 
                             @RequestParam String action, 
                             @RequestParam(defaultValue = "0") int amount,
                             HttpSession session) {
        String username = (String) session.getAttribute("user");
        PokerTable table = pokerService.getTable(tableId);
        if (table != null && username != null) {
            table.handleAction(username, action, amount);
        }
        return "redirect:/game/" + tableId;
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
