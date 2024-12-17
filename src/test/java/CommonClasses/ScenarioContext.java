package CommonClasses;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private static final Map<String, Object> context = new HashMap<>();

    public static void put(String key, Object value) {
        context.put(key, value);
    }

    public static Object get(String key) {
        return context.get(key);
    }
}
