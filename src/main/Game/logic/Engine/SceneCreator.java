package Game.logic.Engine;

import Game.logic.implementClass.*;
import Game.logic.Entities.Character.Guardian;
import Game.logic.Entities.Character.Player;
import Game.logic.Entities.Character.Swordsman;
import Game.logic.Entities.Character.Wizard;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SceneCreator extends Parent {
    public GameProcess gameProcess;
    public Stage stage;
    public SceneCreator() {
        //initialize game process
        gameProcess = new GameProcess(this);
    }

    public void load() throws IOException {
        loadPlayer(Career.swordsman);
        setKeyEvent();
    }

    public void setKeyEvent() {
        this.stage.getScene().setOnKeyPressed(this::onKeyPressed);
        this.stage.getScene().setOnKeyReleased(this::onKeyReleased);
    }

    public void onKeyPressed(KeyEvent e) {
        if (gameProcess.player!=null && gameProcess.status.equals(gameStatus.start)) {
            if (e.getCode() == KeyCode.UP) {
                gameProcess.player.moveUp();
            } else if (e.getCode() == KeyCode.DOWN) {
                gameProcess.player.moveDown();
            } else if (e.getCode() == KeyCode.LEFT) {
                gameProcess.player.moveLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                gameProcess.player.moveRight();
            } else if (e.getCode() == KeyCode.SPACE) {
                gameProcess.player.attack();
            }
        }
    }

    public void onKeyReleased(KeyEvent e) {
        KeyCode[] keys = new KeyCode[] {KeyCode.UP,KeyCode.LEFT,KeyCode.DOWN,KeyCode.RIGHT};
        if (Arrays.asList(keys).contains(e.getCode())) {
            gameProcess.player.stop();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void readMap(String mapName) throws IOException {
        JSONObject mapJson = helperFunc.readJson(StringReminder.mapAddress);
        JSONObject maps = mapJson.getJSONObject(mapName).getJSONObject("mapContent");
        for (String i: maps.keySet()) {
            JSONArray map = maps.getJSONArray(i);
            for (Object l:map) {
                for (Character w: l.toString().toCharArray()) {
                    System.out.println(w);
                }
            }
        }
    }

    public void loadPlayer(Career career) throws IOException {
        JSONObject mapJson = helperFunc.readJson(StringReminder.playerValueAddress);
        JSONObject playerValue = mapJson.getJSONObject(career.toString());
        int hp = playerValue.getInt("hp");
        int damage = playerValue.getInt("damage");
        int defense = playerValue.getInt("defense");
        int magic = playerValue.getInt("magic");
        JSONArray collisionJson = playerValue.getJSONArray("collision");
        JSONArray skillJson = playerValue.getJSONArray("skill");
        CollisionBox collisionBox = new CollisionBox(collisionJson.getFloat(0),collisionJson.getFloat(1));
        Set<String> skills = new HashSet<>();
        for (int i=0;i<skillJson.length();i++){
            skills.add(skillJson.getString(i));
        }
        Player player = switch (career) {
            case swordsman -> new Swordsman(collisionBox, null, hp, magic, defense, damage, skills);
            case wizard -> new Wizard(collisionBox, null, hp, magic, defense, damage, skills);
            case guardian -> new Guardian(collisionBox, null, hp, magic, defense, damage, skills);
        };
        this.gameProcess.setPlayer(player);
        player.setRoot(this);
        player.loadAppearance(career);
        getChildren().add(player);
    }

}
