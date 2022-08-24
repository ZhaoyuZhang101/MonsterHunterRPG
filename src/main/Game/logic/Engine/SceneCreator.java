package Game.logic.Engine;

import Game.logic.Entities.Character.Guardian;
import Game.logic.Entities.Character.Player;
import Game.logic.Entities.Character.Swordsman;
import Game.logic.Entities.Character.Wizard;
import Game.logic.implementClass.*;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

// this class is used to manage all the logic concerned about scene building, and game preparation and setting.
public class SceneCreator extends Parent {
    private int playerTickCount = 0;
    private boolean mapMoveStart = false;
    private Direction mapMoveDirection;
    public GameProcess gameProcess;
    public Stage stage;
    public List<ImageView> backgroundLayer = new ArrayList<>();
    public SceneCreator() {
        //initialize game process
        gameProcess = new GameProcess(this);
    }

    // preparation load for game.
    public void load() throws IOException {
        TimeLineController timeLineController = new TimeLineController() {
            @Override
            public void tick() {
                super.tick();
                ticks();
            }
        };
        backgroundLoad(StringReminder.hillsLayer, 1, 4);
        loadPlayer(Career.swordsman);
        backgroundLoad(StringReminder.hillsLayer, 5, 6);
        setKeyEvent();
    }

    public void setKeyEvent() {
        this.stage.getScene().setOnKeyPressed(this::onKeyPressed);
        this.stage.getScene().setOnKeyReleased(this::onKeyReleased);
    }

    public void onKeyPressed(KeyEvent e) {
        if (gameProcess.player!=null && gameProcess.status.equals(gameStatus.start)) {
            if (e.getCode() == KeyCode.UP) {
                gameProcess.player.moveUp(true);
            } else if (e.getCode() == KeyCode.DOWN) {
                gameProcess.player.moveDown(true);
            } else if (e.getCode() == KeyCode.LEFT) {
                gameProcess.player.moveLeft(true);
            } else if (e.getCode() == KeyCode.RIGHT) {
                gameProcess.player.moveRight(true);
            } else if (e.getCode() == KeyCode.SPACE) {
                gameProcess.player.attack();
            }
        }
    }

    public void onKeyReleased(KeyEvent e) {
        if (gameProcess.player!=null && gameProcess.status.equals(gameStatus.start)) {
            if (e.getCode() == KeyCode.UP) {
                gameProcess.player.moveUp(false);
            } else if (e.getCode() == KeyCode.DOWN) {
                gameProcess.player.moveDown(false);
            } else if (e.getCode() == KeyCode.LEFT) {
                gameProcess.player.moveLeft(false);
            } else if (e.getCode() == KeyCode.RIGHT) {
                gameProcess.player.moveRight(false);
            } else if (e.getCode() == KeyCode.SPACE) {
                gameProcess.player.attack();
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //give a map name and load map data from json and show on the stage.
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

    public void backgroundLoad(String backgroundName, int layerStart, int layerEnd) {
        for (int i=layerStart; i<=layerEnd; i++) {
            ImageView imageView = helperFunc.getImageViewFromAddress(this, StringReminder.hillLayerBaseAddress+StringReminder.hillsLayer+i+".png");
            this.backgroundLayer.add(imageView);
            getChildren().add(imageView);
            imageView.setFitHeight(this.stage.getScene().getHeight());
            imageView.setFitWidth(this.stage.getScene().getHeight()*1.5);
        }

    }

    public void mapMove(Direction direction, boolean move) {
        this.mapMoveStart = move;
        this.mapMoveDirection = direction;
        if (move) {
            System.out.println("move");

        }
    }

    public void ticks() {
        for (int i=0; i<6; i++) {
            ImageView image  = (ImageView) this.backgroundLayer.toArray()[i];
            image.setFitHeight(this.stage.getScene().getHeight());
            image.setFitWidth(this.stage.getScene().getHeight()*1.5);
        }
        playerTickCount = (playerTickCount + 1)%5;
        if (playerTickCount==0) {
            this.gameProcess.player.ticks();
        }

        if (mapMoveStart) {
            switch (mapMoveDirection) {
                case left -> {
                    for (int i = 0; i < 6; i++) {
                        ImageView image = (ImageView) this.backgroundLayer.toArray()[i];
                        image.setLayoutX(image.getLayoutX() + (i) + 0.1);
                    }
                }
                case right -> {
                    for (int i = 0; i < 6; i++) {
                        ImageView image = (ImageView) this.backgroundLayer.toArray()[i];
                        image.setLayoutX(image.getLayoutX() - (i) - 0.1);
                    }
                }
            }
        }
    }

    // create player by giving career and load player data from json to player class.
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
