package Game.logic.UI;

import Game.logic.Engine.SceneCreator;
import Game.logic.UI.Button.GameButton;
import Game.logic.implementClass.Career;
import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.StringReminder;
import Game.logic.implementClass.helperFunc;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * @className StartUI
 * @Description TODO: implement this class
 *              requirement: for doing this class some thing need to be considered:
 *                           1. it has button to start the game and open the setting UI.
 *                           2. contains exit button to quit game.
 * @Author Zhang
 * @DATE 2022/8/27 1:15
 **/
public class GameOverUI extends BaseUI{
    private Stage stage;
    public SceneCreator gamePanel;
    public GameOverUI(Stage stage, SceneCreator gamePanel) {
        this.stage = stage;
        this.gamePanel = gamePanel;
        loadButton();
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


    public void loadButton() {
        Text text = new Text("GAME OVER");
        text.setScaleX(3);
        text.setScaleY(3);
        ImageView background = setImage(StringReminder.UIImageAddress + StringReminder.border_shadow, new CollisionBox(10, 15));

        GameButton START_Button = new GameButton("NEW GAME", this.stage);
        GameButton LOAD_Button = new GameButton("LOAD GAME", this.stage);
        GameButton EXIT_Button = new GameButton("EXIT GAME", this.stage);

        START_Button.setTextFill(Color.WHITE);
        LOAD_Button.setTextFill(Color.WHITE);
        EXIT_Button.setTextFill(Color.WHITE);
        text.setFill(Color.WHITE);

        getChildren().add(background);
        getChildren().add(START_Button);
        getChildren().add(LOAD_Button);
        getChildren().add(EXIT_Button);
        getChildren().add(text);

        START_Button.setLayoutX(this.stage.getScene().getWidth()/2-EXIT_Button.getPrefWidth()/2);
        LOAD_Button.setLayoutX(this.stage.getScene().getWidth()/2-EXIT_Button.getPrefWidth()/2);
        EXIT_Button.setLayoutX(this.stage.getScene().getWidth()/2-EXIT_Button.getPrefWidth()/2);
        text.setLayoutX(this.stage.getScene().getWidth()/2-text.getScaleX()/2);
        START_Button.setLayoutY((2*this.stage.getScene().getHeight())/3-EXIT_Button.getPrefHeight());
        LOAD_Button.setLayoutY((2*this.stage.getScene().getHeight())/3);
        EXIT_Button.setLayoutY((2*this.stage.getScene().getHeight())/3+EXIT_Button.getPrefHeight());
        text.setLayoutY((2*this.stage.getScene().getHeight())/3-EXIT_Button.getPrefHeight()*2);
        START_Button.setOnMouseClicked(mouseEvent -> {
            try {
                this.gamePanel.clearChildren();
                gamePanel.load(false, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            deleteSelf();
        });
        EXIT_Button.setOnMouseClicked(mouseEvent -> stage.close());

        LOAD_Button.setOnMouseClicked(mouseEvent -> {
            try {
                gamePanel.clearChildren();
                gamePanel.load(true, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void deleteSelf() {
        this.gamePanel.deleteChild(this);
    }
}