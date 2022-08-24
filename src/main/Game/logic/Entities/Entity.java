package Game.logic.Entities;

import Game.logic.Engine.SceneCreator;
import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.Direction;
import Game.logic.implementClass.Position;
import Game.logic.implementClass.TimeLineController;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class Entity extends Parent {
    public int hp;
    public int damage;
    public int magic;
    public int defense;
    public CollisionBox collisionBox = null;
    public Position pos = null;
    public int speed = 1;
    public TimeLineController timeLineController;
    public SceneCreator root;
    public ImageView appearance;
    public Direction direction = Direction.right;
    public Entity() {
    }

    public void ticks() {

    }

    public void setRoot(SceneCreator root) {
        this.root = root;
    }

    public void setImage(String address) {
        InputStream a = getClass().getResourceAsStream(address);
        assert a != null;
        Image image = new Image(a);
        this.appearance = new ImageView(image);
        this.appearance.setFitWidth(this.root.stage.getScene().getHeight()*0.3);
        this.appearance.setFitHeight(this.root.stage.getScene().getHeight()*0.3);
    }

    public void delete() {
        this.timeLineController.stop();
    }
}
