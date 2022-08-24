package Game.logic.Engine;

import Game.logic.Entities.Character.Player;
import Game.logic.implementClass.gameStatus;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class GameProcess {
    public gameStatus status;
    private final SceneCreator root;
    private ArrayList<Group> maps;
    public Player player;
    private int recentMaps;
    public GameProcess(SceneCreator root) {
        this.status = gameStatus.start;
        this.root = root;
    }

    public void addMap(Group map) {
        this.maps.add(map);
    }

    public void start() {
        this.status = gameStatus.start;
        this.recentMaps = 0;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void stop() {
        this.status = gameStatus.stop;
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
