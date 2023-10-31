package Game.logic.Entities;

import Game.logic.Engine.SceneCreator;
import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.Direction;
import Game.logic.implementClass.Position;
import Game.logic.implementClass.TimeLineController;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class Entity extends Parent {
    public int hp;
    public int hpLimit;
    public int damage;
    public int magic;
    public int magicLimit;
    public int defense;
    public double height;
    public int distanceCount = 0;
    public double distance = 0;
    public CollisionBox collisionBox = null;
    public Position pos = null;
    public int speed = 1;
    public TimeLineController timeLineController;
    public SceneCreator root = null;
    public ImageView appearance;
    public Direction direction = Direction.right;
    public Entity() {
    }

    public void ticks() throws IOException {

    }

    public void setCollisionBox(CollisionBox collisionBox) {
        this.collisionBox = collisionBox;
    }

    public void setRoot(SceneCreator root) {
        this.root = root;
    }

    public void setImage(String address, CollisionBox collisionBox) {
        if (root != null) {
            InputStream a = getClass().getResourceAsStream(address);
            assert a != null;
            Image image = new Image(a);
            this.appearance = new ImageView(image);
            this.appearance.setFitWidth(collisionBox.getWidth() * (this.root.stage.getScene().getHeight() / 10));
            this.appearance.setFitHeight(collisionBox.getHeight() * (this.root.stage.getScene().getHeight() / 10));
        }
    }


    public void add_distance() {
        this.distanceCount += Math.round((root.SceneH/1000)*5);
        double unit = (root.SceneH/10);
        this.distance = this.distanceCount/unit;
    }
    public boolean minus_distance() {
        double unit = (root.SceneH/10);
        if (this.distanceCount > 0) {
            this.distanceCount -= Math.round((root.SceneH/1000)*5);
            this.distance = this.distanceCount/unit;
            return true;
        } else {
            return false;
        }
    }

    public void delete() {
        this.timeLineController.stop();
    }
}
