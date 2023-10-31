package Game.logic.Effects;

import Game.logic.Engine.SceneCreator;
import Game.logic.implementClass.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * @className EffectPlayer
 * @Description TODO
 * @Author zhang
 * @DATE 2022/10/2 1:38
 **/
public class EffectPlayer extends Parent {
    public int frameCount;
    public ImageView appearance;
    public SceneCreator root;
    public double height = 0;
    public Effects effects;
    public int frame = 0;
    public TimeLineController timeLineController;
    public double distance = 0;
    public double sizeX = 0;
    public double sizeY = 0;
    public CollisionBox collisionBox;
    public EffectPlayer(SceneCreator root, Effects effects, int frame, double distance, double height, CollisionBox collisionBox, double sizeX, double sizeY) {
        this.collisionBox = collisionBox;
        this.root = root;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.effects = effects;
        this.frame = frame;
        this.distance = distance;
        this.height = height;
        timeLineController = new TimeLineController() {
            @Override
            public void tick() throws IOException {
                super.tick();
                ticks();
            }
        };
        loadAppearance();
    }
    public void setImage(String address) {
        InputStream a = getClass().getResourceAsStream(address);
        assert a != null;
        Image image = new Image(a);
        this.appearance = new ImageView(image);
        this.appearance.setFitWidth(collisionBox.getWidth()*(this.root.stage.getScene().getHeight()/5));
        this.appearance.setFitHeight(collisionBox.getHeight()*(this.root.stage.getScene().getHeight()/5));
    }
    public void loadAppearance() {
        if (root.gameProcess.player == null) {
            return;
        }
        double unit = this.root.stage.getScene().getHeight()/10;
        setImage(StringReminder.effectsImageAddress+effects+".png");
        this.appearance.setViewport(new Rectangle2D(sizeX*frameCount, 0, sizeX, sizeY));
        getChildren().add(this.appearance);
        setLayoutX(root.gameProcess.player.getLayoutX() + unit*(this.distance-root.gameProcess.player.distance) + unit*0.5);
        setLayoutY(this.root.stage.getScene().getHeight() - unit*this.height + unit*0.5);
    }

    // update the animation of character per frame.
    public void animationUpdate() {
        double unit = this.root.stage.getScene().getHeight()/10;
        getChildren().remove(this.appearance);
        try {
            setImage(StringReminder.effectsImageAddress+effects+".png");
        } catch (Exception e) {
            setImage(StringReminder.effectsImageAddress+effects+".png");
            throw new Error("动画名称错误或未发现动画文件，错误文件："+ StringReminder.effectsImageAddress+effects+".png");
        }
        this.appearance.setViewport(new Rectangle2D(sizeX*frameCount, 0, sizeX, sizeY));
        getChildren().add(this.appearance);
        setLayoutX(root.gameProcess.player.getLayoutX() + unit*(this.distance-root.gameProcess.player.distance) + unit*0.5);
        setLayoutY(this.root.stage.getScene().getHeight() - unit*this.height + unit*0.5);
    }

    public void ticks() {
        if (root.gameProcess.player == null) {
            return;
        }
        animationUpdate();
        if (frameCount < frame) {
            frameCount ++;
        } else {
            frameCount = 0;
            root.deleteChild(this);
            timeLineController.stop();
        }
    }
}
