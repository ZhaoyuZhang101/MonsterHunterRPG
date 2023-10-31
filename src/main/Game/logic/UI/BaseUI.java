package Game.logic.UI;

import Game.logic.Engine.GameProcess;
import javafx.scene.Parent;

/**
 * @className BaseUI
 * @Description TODO: implement this class
 *              requirement: for doing this class some thing need to be considered:
 *                           1. it has function to control open and close.
 *                           2. contains button and image and title
 * @Author Zhang
 * @DATE 2022/8/27 1:11
 **/
public class BaseUI extends Parent {
    public GameProcess game;

    public BaseUI() {}

    public void setGame(GameProcess game) {
        this.game = game;
    }

    public void ticks() {}
}
