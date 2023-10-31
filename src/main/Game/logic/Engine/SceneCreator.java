package Game.logic.Engine;

import Game.logic.Background.Background;
import Game.logic.Entities.lives.BaseLives;
import Game.logic.Entities.lives.Character.Guardian;
import Game.logic.Entities.lives.Character.Player;
import Game.logic.Entities.lives.Character.Swordsman;
import Game.logic.Entities.lives.Character.Wizard;
import Game.logic.Background.Map;
import Game.logic.Entities.lives.Monster.BaseMonster;
import Game.logic.Entities.lives.Monster.Dragon;
import Game.logic.Entities.lives.Monster.Skeleton;
import Game.logic.Entities.lives.NPC.BaseNPC;
import Game.logic.UI.BaseUI;
import Game.logic.UI.CharacterMainUI;
import Game.logic.implementClass.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * this class is used to manage all the logic concerned about scene building, and game preparation and setting.
 */
public class SceneCreator extends Parent {
    private int playerTickCount = 0;
    private boolean mapMoveStart = false;
    private Direction mapMoveDirection;
    public GameProcess gameProcess;
    public Stage stage;
    public List<Background> backgroundLayer = new ArrayList<>();
    public Map map;
    public String mapName = "map1";
    public double SceneH;
    public double SceneW;
    public BaseUI CharacterMainUi;
    public SceneCreator() {
        //initialize game process
        gameProcess = new GameProcess(this);

        TimeLineController timeLineController = new TimeLineController() {
            @Override
            public void tick() throws IOException {
                super.tick();
                ticks();
            }
        };
    }


    public void openUI(BaseUI UI_name) {
        getChildren().add(UI_name);
    }

    public void deleteChild(Node node) {
        getChildren().remove(node);
    }
    public void addChild(Node node) {
        getChildren().add(node);
    }

    /**
     * preparation load for game.
     */
    public void load(boolean isLoad, String mapName) throws IOException {
        JSONObject playerData = helperFunc.readJson(StringReminder.savedDataAddress);
        JSONObject mapInfo;
        if (!Objects.equals(mapName, "")) {
            JSONObject mapJson = helperFunc.readJson(StringReminder.mapAddress);
            this.mapName = mapName;
            mapInfo = mapJson.getJSONObject(mapName);
        } else {
            if (isLoad) {
                mapName = playerData.getJSONObject("recentProcess").getString("map");
                JSONObject mapJson = helperFunc.readJson(StringReminder.mapAddress);
                mapInfo = mapJson.getJSONObject(mapName);
                this.mapName = mapName;
            } else {
                JSONObject mapJson = helperFunc.readJson(StringReminder.mapAddress);
                mapInfo = mapJson.getJSONObject("map1");
                this.mapName = "map1";
            }

        }

        Career career = Career.valueOf(playerData.getJSONObject("playerStatus").getString("career"));
        String backName = mapInfo.getString("background");
        String storyName = mapInfo.getString("story");
        System.out.println("--------->"+backName);
        backgroundLoad(backName, 1, 4);
        this.map = new Map(this.stage, this, this.mapName);
        this.map.setLayer(5);
        loadLives(career, true, false, isLoad);
        getChildren().add(this.map);
        backgroundLoad(backName, 6, 6);
        CharacterMainUi = new CharacterMainUI(gameProcess.player, this);
        getChildren().add(CharacterMainUi);
        setKeyEvent();
        gameProcess.start();
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

    public void backgroundLoad(String backgroundName, int layerStart, int layerEnd) {
        for (int i=layerStart; i<=layerEnd; i++) {
            String address = "";
            if (Objects.equals(backgroundName, StringReminder.hillsLayer)) {
                address = StringReminder.hillLayerBaseAddress;
            } else {
                address = StringReminder.forestLayerBaseAddress;
            }
            System.out.println(address+backgroundName+i+".png");
            ImageView imageView = helperFunc.getImageViewFromAddress(this, address+backgroundName+i+".png");
            imageView.setFitHeight(this.stage.getScene().getHeight());
            imageView.setFitWidth(this.stage.getScene().getHeight()*1.5);
            Background background = new Background(this.stage, this);
            background.setImage(imageView);
            background.setLayer(i);
            getChildren().add(background);
            this.backgroundLayer.add(background);
        }
    }

    public void updateHW() {
        this.SceneH = this.stage.getScene().getHeight();
        this.SceneW = this.stage.getScene().getWidth();
    }

    public void mapMove(Direction direction, boolean move) {
        this.mapMoveStart = move;
        this.mapMoveDirection = direction;
    }


    public void clearChildren() {
        getChildren().clear();
        gameProcess.monsters.clear();
        gameProcess.NPCs.clear();
        map.basePlatforms.clear();
        map.timeLineController.stop();
        for (Background bg: backgroundLayer) {
            bg.timeLineController.stop();
        }
        backgroundLayer.clear();
        gameProcess.stop();
    }

    /**
     * This it controls the time of the game, one tick means one second in game,
     * it will be triggered infinite times, therefore if you want to write AI or character moving,
     * this is would be the most important method.
     */
    public void ticks() throws IOException {
        if (gameProcess.player == null || gameProcess.status==gameStatus.stop) {
            return;
        }
        this.updateHW();
        for (int i=0; i<5; i++) {
            Background background  = (Background) this.backgroundLayer.toArray()[i];
            ArrayList<ImageView> imageViews = background.getImage();
            for (ImageView imageView: imageViews) {
                imageView.setFitHeight(this.stage.getScene().getHeight());
                imageView.setFitWidth(this.stage.getScene().getHeight() * 1.5);
            }
        }
        playerTickCount = (playerTickCount + 1)%5;
        if (playerTickCount==0) {
            this.gameProcess.player.ticks();
            try {
                for (BaseLives bl : this.gameProcess.monsters) {
                    bl.ticks();
                }
                for (BaseLives bn : this.gameProcess.NPCs) {
                    bn.ticks();
                }
            } catch (Exception ignored) {
            }
        }

        for (BaseLives bl: this.gameProcess.monsters) {
            bl.map_ticks();
        }

        for (BaseLives bn: this.gameProcess.NPCs) {
            bn.map_ticks();
        }


        if (mapMoveStart) {
            switch (mapMoveDirection) {
                case left -> {
                    if (this.gameProcess.player.minus_distance()) {
                        for (int i = 0; i < 5; i++) {
                            Background background = (Background) this.backgroundLayer.toArray()[i];
                            background.moveLeft();
                        }
                        map.moveLeft();
                    }
                }
                case right -> {
                    this.gameProcess.player.add_distance();
                    for (int i = 0; i < 5; i++) {
                        Background background = (Background) this.backgroundLayer.toArray()[i];
                        background.moveRight();
                    }
                    map.moveRight();
                }
            }
        }
    }

    /**
     * create player by giving career and load player data from json to player class.
     */
    public BaseLives loadLives(Career career, Boolean isPlayer, Boolean isNPC, Boolean isLoad) throws IOException {
        int hp = 1;
        int damage = 0;
        int defense = 0;
        int magic = 0;
        CollisionBox collisionBox = new CollisionBox(1, 1);
        Set<String> skills = new HashSet<>();
        if (! isNPC) {
            JSONObject mapJson;
            if (isPlayer) {
                mapJson = helperFunc.readJson(StringReminder.playerValueAddress);
            } else {
                mapJson = helperFunc.readJson(StringReminder.monsterValueAddress);
            }
            JSONObject playerValue = mapJson.getJSONObject(career.toString());
            hp = playerValue.getInt("hp");
            damage = playerValue.getInt("damage");
            defense = playerValue.getInt("defense");
            magic = playerValue.getInt("magic");
            JSONArray collisionJson = playerValue.getJSONArray("collision");
            JSONArray skillJson = playerValue.getJSONArray("skill");
            collisionBox = new CollisionBox(collisionJson.getFloat(0), collisionJson.getFloat(1));
            for (int i = 0; i < skillJson.length(); i++) {
                skills.add(skillJson.getString(i));
            }
        }
        BaseLives lives = switch (career) {
            case swordsman -> new Swordsman(collisionBox, hp, magic, defense, damage, skills);
            case wizard -> new Wizard(collisionBox, hp, magic, defense, damage, skills);
            case guardian -> new Guardian(collisionBox, hp, magic, defense, damage, skills);
            case skeleton -> new Skeleton(collisionBox, hp, 0, defense, damage, skills);
            case dragon -> new Dragon(collisionBox, hp, 0, defense, damage, skills);
            case adventure_01, dog, barkeep, beggar, princess, witch, villager_01 -> new BaseNPC(collisionBox, hp, 0, defense, damage, skills);
        };
        if (isLoad && isPlayer) {
            JSONObject playerValue = helperFunc.readJson(StringReminder.savedDataAddress).getJSONObject("playerStatus");
            lives.hp = playerValue.getInt("hp");
            lives.hpLimit = playerValue.getInt("hp_limit");
            lives.magic = playerValue.getInt("magic");
            lives.magicLimit = playerValue.getInt("magic_limit");
            lives.defense = playerValue.getInt("defense");
            lives.EXP = playerValue.getInt("exp");
            lives.EXPLimit = playerValue.getInt("exp_limit");
            lives.level = playerValue.getInt("level");
        } else {
            lives.hpLimit = hp;
            lives.magicLimit = magic;
        }
        lives.isPlayer = isPlayer;
        lives.isNPC = isNPC;

        if (isPlayer) {
            this.gameProcess.setPlayer(lives);
        }
        lives.setRoot(this);
        lives.loadAppearance(career);
        getChildren().add(lives);
        return lives;
    }

}
