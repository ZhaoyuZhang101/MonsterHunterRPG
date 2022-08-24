package Game.main;

import Game.logic.Engine.SceneCreator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneCreator gamePanel = new SceneCreator();
        Scene scene = new Scene(gamePanel, 1080, 720);
        stage.setTitle("MonsterHunter");
        stage.setScene(scene);
        gamePanel.setStage(stage);
        gamePanel.load();
        stage.show();

    }
}