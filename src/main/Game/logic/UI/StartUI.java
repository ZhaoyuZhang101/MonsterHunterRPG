package Game.logic.UI;

import Game.logic.Engine.SceneCreator;
import Game.logic.UI.Button.GameButton;
import Game.logic.implementClass.Career;
import Game.logic.implementClass.StringReminder;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @className StartUI
 * @Description TODO: implement this class
 *              requirement: for doing this class some thing need to be considered:
 *                           1. it has button to start the game and open the setting UI.
 *                           2. contains exit button to quit game.
 * @Author Zhang
 * @DATE 2022/8/27 1:15
 **/
public class StartUI extends BaseUI{
    private Stage stage;
    public SceneCreator gamePanel;
    public StartUI(Stage stage, SceneCreator gamePanel) {
        this.stage = stage;
        this.gamePanel = gamePanel;
        loadButton();
    }
    public void loadButton() {
        GameButton START_Button = new GameButton("NEW GAME", this.stage);
        GameButton LOAD_Button = new GameButton("LOAD GAME", this.stage);
        GameButton EXIT_Button = new GameButton("EXIT GAME", this.stage);
        getChildren().add(START_Button);
        getChildren().add(LOAD_Button);
        getChildren().add(EXIT_Button);
        START_Button.setLayoutX(this.stage.getScene().getWidth()/2-EXIT_Button.getPrefWidth()/2);
        LOAD_Button.setLayoutX(this.stage.getScene().getWidth()/2-EXIT_Button.getPrefWidth()/2);
        EXIT_Button.setLayoutX(this.stage.getScene().getWidth()/2-EXIT_Button.getPrefWidth()/2);
        START_Button.setLayoutY(this.stage.getScene().getHeight()/2-EXIT_Button.getPrefHeight()*1.5);
        LOAD_Button.setLayoutY(this.stage.getScene().getHeight()/2);
        EXIT_Button.setLayoutY(this.stage.getScene().getHeight()/2+EXIT_Button.getPrefHeight()*1.5);
        START_Button.setOnMouseClicked(mouseEvent -> {
            try {
                gamePanel.load(false, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            deleteSelf();
        });
        LOAD_Button.setOnMouseClicked(mouseEvent -> {
            try {
                gamePanel.load(true, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            deleteSelf();
        });
        EXIT_Button.setOnMouseClicked(mouseEvent -> stage.close());
    }
    public void deleteSelf() {
        this.gamePanel.deleteChild(this);
    }
}