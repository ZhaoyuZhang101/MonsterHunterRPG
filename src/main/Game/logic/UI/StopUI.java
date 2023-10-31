package Game.logic.UI;

import Game.logic.Engine.SceneCreator;
import Game.logic.UI.Button.GameButton;
import Game.logic.implementClass.Career;
import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.StringReminder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @className StopUI
 * @Description TODO: implement this class
 *              requirement: for doing this class some thing need to be considered:
 *                           1. it has button to resume the game, save and load, exit game
 * @Author Zhang
 * @DATE 2022/8/27 1:15
 **/
public class StopUI extends BaseUI{
    private Stage stage;
    public SceneCreator gamePanel;
    public StopUI(Stage stage, SceneCreator gamePanel) {
        this.stage = stage;
        this.gamePanel = gamePanel;
        loadButton();
    }
    public void loadButton() {
        ImageView background = setImage(StringReminder.UIImageAddress + StringReminder.border_shadow, new CollisionBox(10, 15));
        this.getChildren().add(background);
        GameButton CONTI_Button = new GameButton("CONTINUE", this.stage);
        GameButton START_Button = new GameButton("NEW GAME", this.stage);
        GameButton SAVE_Button = new GameButton("SAVE GAME", this.stage);
        GameButton BACK_TO_MAIN = new GameButton("MAIN MENU", this.stage);
        getChildren().add(START_Button);
        getChildren().add(SAVE_Button);
        getChildren().add(CONTI_Button);
        getChildren().add(BACK_TO_MAIN);
        START_Button.setLayoutX(this.stage.getScene().getWidth()/2-CONTI_Button.getPrefWidth()/2);
        SAVE_Button.setLayoutX(this.stage.getScene().getWidth()/2-CONTI_Button.getPrefWidth()/2);
        CONTI_Button.setLayoutX(this.stage.getScene().getWidth()/2-CONTI_Button.getPrefWidth()/2);
        BACK_TO_MAIN.setLayoutX(this.stage.getScene().getWidth()/2-CONTI_Button.getPrefWidth()/2);
        START_Button.setLayoutY(this.stage.getScene().getHeight()/2);
        SAVE_Button.setLayoutY(this.stage.getScene().getHeight()/2+CONTI_Button.getPrefHeight()*1.5);
        CONTI_Button.setLayoutY(this.stage.getScene().getHeight()/2-CONTI_Button.getPrefHeight()*1.5);
        BACK_TO_MAIN.setLayoutY(this.stage.getScene().getHeight()/2+CONTI_Button.getPrefHeight()*3);
        START_Button.setOnMouseClicked(mouseEvent -> {
            try {
                this.gamePanel.clearChildren();
                gamePanel.load(false, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            deleteSelf();
        });
        CONTI_Button.setOnMouseClicked(mouseEvent -> {
            gamePanel.gameProcess.start();
            deleteSelf();
        });

        SAVE_Button.setOnMouseClicked(mouseEvent -> {
            gamePanel.gameProcess.save();
        });

        BACK_TO_MAIN.setOnMouseClicked(mouseEvent -> {
            gamePanel.clearChildren();
            BaseUI startUI = new StartUI(stage, gamePanel);
            gamePanel.addChild(startUI);
        });
    }


    public ImageView setImage(String address, CollisionBox collisionBox) {
        InputStream a = getClass().getResourceAsStream(address);
        assert a != null;
        Image image = new Image(a);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(collisionBox.getWidth()*(this.gamePanel.stage.getScene().getHeight()/10));
        imageView.setFitHeight(collisionBox.getHeight()*(this.gamePanel.stage.getScene().getHeight()/10));
        return imageView;
    }

    public void deleteSelf() {
        this.gamePanel.deleteChild(this);
    }
}
