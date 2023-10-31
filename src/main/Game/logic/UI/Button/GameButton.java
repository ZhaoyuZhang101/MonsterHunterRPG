package Game.logic.UI.Button;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameButton extends Button {
    public Stage stage;
    public ImageView stop = null;
    public GameButton(String s, Stage stage) {
        this.setText(s);
        this.stage = stage;
        setPrefHeight(this.stage.getScene().getHeight()/10);
        setPrefWidth(this.stage.getScene().getHeight()/5);
        setFont(Font.font(this.stage.getScene().getHeight()/40));
        setBackground(null);
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setFont(Font.font(getScene().getHeight()/40-4));
                if (stop!=null) {
                    stop.setFitWidth(stage.getScene().getHeight()/12);
                    stop.setFitHeight(stage.getScene().getHeight()/12);
                }
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setFont(Font.font(getScene().getHeight()/40));
                if (stop!=null) {
                    stop.setFitWidth(stage.getScene().getHeight()/10);
                    stop.setFitHeight(stage.getScene().getHeight()/10);
                }
            }
        });
    }
    public void setImage(ImageView image) {
        this.stop = image;
        stop.setFitWidth(this.stage.getScene().getHeight()/10);
        stop.setFitHeight(this.stage.getScene().getHeight()/10);
    }
}
