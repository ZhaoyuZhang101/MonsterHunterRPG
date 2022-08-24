package Game.logic.implementClass;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    static ImageView getImageViewFromAddress(Parent p, String address) {
        InputStream a = p.getClass().getResourceAsStream(address);
        assert a != null;
        Image image = new Image(a);
        return new ImageView(image);
    }
}
