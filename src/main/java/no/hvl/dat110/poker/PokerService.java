package no.hvl.dat110.poker;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PokerService {
    private Map<String, PokerTable> activeTables = new ConcurrentHashMap<>();

    public PokerService() {
        // Initialize with a default room
        activeTables.put("high-stakes", new PokerTable("high-stakes"));
        activeTables.put("casual", new PokerTable("casual"));
    }

    public PokerTable getTable(String id) {
        return activeTables.get(id);
    }

    public Map<String, PokerTable> getActiveTables() {
        return activeTables;
    }
}
