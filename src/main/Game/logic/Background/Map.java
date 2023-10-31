package Game.logic.Background;

import Game.logic.Engine.SceneCreator;
import Game.logic.Entities.Platform.BasePlatform;
import Game.logic.Entities.Platform.Grass;
import Game.logic.Entities.Platform.StoneBrick;
import Game.logic.Entities.lives.BaseLives;
import Game.logic.Entities.lives.Character.Guardian;
import Game.logic.Entities.lives.Character.Player;
import Game.logic.Entities.lives.Character.Swordsman;
import Game.logic.Entities.lives.Character.Wizard;
import Game.logic.Entities.lives.Monster.BaseMonster;
import Game.logic.Entities.lives.Monster.Dragon;
import Game.logic.Entities.lives.Monster.Skeleton;
import Game.logic.implementClass.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static Game.logic.implementClass.Career.*;

/**
 * @className Map
 * @Description TODO
 * @Author zhang
 * @DATE 2022/9/11 11:55
 **/
public class Map extends Background {
    public ArrayList<BasePlatform> basePlatforms = new ArrayList<>();
    public Map(Stage stage, SceneCreator root, String mapName) throws IOException {
        super(stage, root);
        loadMap(mapName);
    }

    //give a map name and load map data from json and show on the stage.
    public void loadMap(String mapName) throws IOException {
        updateHW();
        JSONObject mapJson = helperFunc.readJson(StringReminder.mapAddress);
        JSONArray maps = mapJson.getJSONObject(mapName).getJSONArray("mapContent");
        int difficulty = mapJson.getJSONObject(mapName).getInt("difficulty");
        double unit = (this.SceneH/10);
        for (Object jo: maps) {
            BasePlatform platform = generatePlatform(jo.toString());
            platform.setLayoutY(this.SceneH - (platform.height*unit));
            platform.setLayoutX(this.SceneH/2 + unit*platform.distance);
            if (platform.monsterType != null) {
                double possibility = platform.monsterGenerateRate*100;
                Random rand = new Random();
                int randInt = rand.nextInt(100);
                if (randInt<=possibility) {
                    BaseLives monster = root.loadLives(platform.monsterType, false, false, false);
                    monster.distance = platform.distance;
                    monster.setLayoutY(platform.getLayoutY() - unit * 2);
                    monster.setRoot(root);
                    Random random = new Random();
                    monster.level = random.nextInt(difficulty*3+1, difficulty*3+3);
                    System.out.println("produce monster---------------------");
                    root.gameProcess.monsters.add(monster);
                }
            }
            if (platform.NPCType != null) {
                BaseLives npc = root.loadLives(platform.NPCType, false, true, false);
                npc.distance = platform.distance;
                npc.setLayoutY(platform.getLayoutY() - unit * 3);
                npc.setRoot(root);
                Random random = new Random();
                npc.level = random.nextInt(difficulty*3+1, difficulty*3+3);
                System.out.println("produce npc---------------------");
                root.gameProcess.NPCs.add(npc);
            }
            basePlatforms.add(platform);
            getChildren().add(platform);
        }
    }

    public BasePlatform generatePlatform(String platformContent) throws IOException {
        JSONObject platformJson = new JSONObject(platformContent);
        String platformType = platformJson.getString("platformType");
        double distance = platformJson.getDouble("distance");
        double height = platformJson.getDouble("height");
        double monsterGenerateRate = platformJson.getDouble("monsterGenerateRate");
        String monsterType = platformJson.getString("monsterType");
        String NPCType = platformJson.getString("NPCType");
        String last = platformJson.getString("last");
        String front = platformJson.getString("front");
        BasePlatform basePlatform = new BasePlatform(stage, height,monsterGenerateRate, monsterType, NPCType);

        switch (platformType) {
            case ("Grass") -> {
                basePlatform = new Grass(stage, height,monsterGenerateRate, monsterType, NPCType);
            }
            case ("StoneBrick") -> {
                basePlatform = new StoneBrick(stage, height,monsterGenerateRate, monsterType, NPCType);
            }
        }
        basePlatform.distance = distance;
        basePlatform.last = last;
        basePlatform.front = front;
        basePlatform.setRoot(this.root);
        basePlatform.loadAppearance();
        return basePlatform;
    }


    @Override
    public void ticks() {
        updateHW();
        mapAdjustment();
    }

    public void mapAdjustment() {
        double unit = stage.getScene().getHeight() / 10;
        if (this.root.map != null&&this.root.gameProcess.player!=null) {
            for (BasePlatform i : this.root.map.basePlatforms) {
                double difference = i.distance - this.root.gameProcess.player.distance;
                i.setLayoutX(this.root.gameProcess.player.getLayoutX() + difference * unit + unit * 1.5);
                i.setLayoutY(stage.getScene().getHeight() - i.height*unit);

            }
        }
    }
    @Override
    public void moveLeft() {
//        setLayoutX(getLayoutX()+((this.SceneH/1000)*this.layer));
    }

    @Override
    public void moveRight() {
//        setLayoutX(getLayoutX()-((this.SceneH/1000)*this.layer));
//        System.out.println("-------->"+Math.round((this.SceneH/1000)*this.layer));
    }
}
