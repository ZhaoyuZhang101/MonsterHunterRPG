package Game.logic.implementClass;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static Game.logic.implementClass.helperFunc.readJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class readJsonTest {
    @Test
    public void readJsonTest() throws IOException {
        JSONObject json = readJson("src/test/java/Game/logic/implementClass/test.json");
        assertTrue("test string".equals(json.getString("str_key")));
        assertEquals(123, json.getInt("int_key"));
        assertTrue(json.getBoolean("bool_key"));

        assertTrue(json.getJSONArray("arr_key") != null);
        assertTrue("foo".equals(json.getJSONArray("arr_key").get(0)));
        assertTrue("bar".equals(json.getJSONArray("arr_key").get(1)));

        assertTrue(json.getJSONObject("obj_key") != null);
        assertTrue(json.getJSONObject("obj_key").has("key"));
        assertTrue("val".equals(json.getJSONObject("obj_key").get("key")));
    }
}
