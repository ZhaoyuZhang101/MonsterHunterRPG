package Game.logic.implementClass;

import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public interface helperFunc {
    static JSONObject readJson(String address) throws IOException {
        char[] line = new char[10000];
        InputStreamReader input =new InputStreamReader(new FileInputStream(address), StandardCharsets.UTF_8);
        int len =input.read(line);
        String text =new String(line,0,len);
        return new JSONObject(text);
    }
}
