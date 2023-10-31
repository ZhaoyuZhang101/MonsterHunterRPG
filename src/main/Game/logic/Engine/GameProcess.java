package Game.logic.Engine;

import Game.logic.Background.Map;
import Game.logic.Entities.lives.BaseLives;
import Game.logic.Entities.lives.Character.Player;
import Game.logic.Entities.lives.Monster.BaseMonster;
import Game.logic.implementClass.gameStatus;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GameProcess {
    public gameStatus status;
    private final SceneCreator root;
    private Map map;
    public BaseLives player = null;
    public ArrayList<BaseLives> monsters = new ArrayList<>();
    public ArrayList<BaseLives> NPCs = new ArrayList<>();
    private int recentMaps;
    public GameProcess(SceneCreator root) {
        this.status = gameStatus.start;
        this.root = root;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void start() {
        this.status = gameStatus.start;
        this.recentMaps = 0;
    }

    public BaseLives setPlayer(BaseLives player) {
        this.player = player;
        return player;
    }

    public void stop() {
        this.status = gameStatus.stop;
    }

    public void save() {
        JSONObject playerData = new JSONObject();
        JSONObject playerStatus = new JSONObject();
        playerStatus.put("career", root.gameProcess.player.career);
        playerStatus.put("level", root.gameProcess.player.level);
        playerStatus.put("hp", root.gameProcess.player.hp);
        playerStatus.put("damage", root.gameProcess.player.damage);
        playerStatus.put("hp_limit", root.gameProcess.player.hpLimit);
        playerStatus.put("magic", root.gameProcess.player.magic);
        playerStatus.put("magic_limit", root.gameProcess.player.magicLimit);
        playerStatus.put("defense", root.gameProcess.player.defense);
        playerStatus.put("exp", root.gameProcess.player.EXP);
        playerStatus.put("exp_limit", root.gameProcess.player.EXPLimit);
        JSONObject recentProcess = new JSONObject();
        recentProcess.put("map", root.mapName);
        playerData.put("playerStatus", playerStatus);
        playerData.put("recentProcess", recentProcess);

        try {
            Files.writeString(Paths.get("src/main/resources/SavedData.json"), playerData.toString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameOver() {
        this.status = gameStatus.over;
    }

    public void missionClear() {
        this.status = gameStatus.succeed;
    }

    public void resume() {
        this.status = gameStatus.start;
    }


}
