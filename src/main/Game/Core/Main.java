package Game.Core;

import Game.logic.Engine.SceneCreator;
import Game.logic.UI.StartUI;
import Game.logic.implementClass.Career;
import Game.logic.implementClass.StringReminder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneCreator gamePanel = new SceneCreator();
        Scene scene = new Scene(gamePanel, 1080, 720);
        stage.setTitle("MonsterHunter");
        stage.getIcons().add(new Image("icon_HD.png"));
        stage.setScene(scene);
        gamePanel.setStage(stage);
        gamePanel.openUI(new StartUI(stage, gamePanel));

//        gamePanel.load(Career.swordsman, StringReminder.hillsLayer);
        stage.show();

    }
}