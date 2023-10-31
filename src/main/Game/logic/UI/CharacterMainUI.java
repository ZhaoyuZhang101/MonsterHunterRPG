package Game.logic.UI;

import Game.logic.Engine.GameProcess;
import Game.logic.Engine.SceneCreator;
import Game.logic.Entities.lives.BaseLives;
import Game.logic.Entities.lives.Character.Player;
import Game.logic.UI.Button.GameButton;
import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.StringReminder;
import Game.logic.implementClass.TimeLineController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;

/**
 * @className CharacterMainUI
 * @Description TODO: implement this class
 *              requirement: for doing this class some thing need to be considered:
 *                           1. in this class it needs to get the information of recent player like hp, equipment, magic and show them.
 *                           2. it has button to stop the game
 * @Author Zhang
 * @DATE 2022/8/27 1:19
 **/
public class CharacterMainUI extends BaseUI{
    public BaseLives player;
    public GameProcess game;
    public SceneCreator root;
    public int headFrameCount = 0;
    public int headFrames = 10;
    public ImageView head;
    public int gapCountLimit = 50;
    public int gapCount = 0;
    public Group heart = new Group();
    public Group crystal = new Group();
    public CharacterMainUI(BaseLives player, SceneCreator root) {
        this.player = player;
        this.root  =root;
        updateUI();
        TimeLineController timeLineController = new TimeLineController() {
            @Override
            public void tick() throws IOException {
                super.tick();
                ticks();
            }
        };
    }
    public ImageView setImage(String address, CollisionBox collisionBox) {
        InputStream a = getClass().getResourceAsStream(address);
        assert a != null;
        Image image = new Image(a);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(collisionBox.getWidth()*(this.root.stage.getScene().getHeight()/10));
        imageView.setFitHeight(collisionBox.getHeight()*(this.root.stage.getScene().getHeight()/10));
        return imageView;
    }

    public void updateUI() {
        double unit = this.root.stage.getScene().getHeight()/10;
        getChildren().clear();
        head = setImage(StringReminder.UIImageAddress + player.career+"_head.png", new CollisionBox(1, 1));
        head.setViewport(new Rectangle2D(20*headFrameCount, 0, 20, 20));
        getChildren().add(head);
        root.requestFocus();

        GameButton StopButton = new GameButton("", root.stage);
        ImageView imageView = setImage(StringReminder.UIImageAddress + StringReminder.stop, new CollisionBox(5, 5));
        StopButton.setImage(imageView);
        StopButton.setLayoutX(unit*7);
        StopButton.setLayoutY(unit*0.1);
        this.getChildren().add(imageView);
        imageView.setLayoutX(unit*7);
        imageView.setLayoutY(unit*0.1);
        StopButton.setOnMouseClicked(mouseEvent -> {
            BaseUI ui = new StopUI(root.stage, root);
            root.addChild(ui);
            root.gameProcess.stop();
            System.out.println("open");
        });
        Text lv_text = new Text("LV: " + player.level);
        lv_text.setLayoutX(unit * 0.27);
        lv_text.setLayoutY(unit * 0.27);
        lv_text.setScaleX(unit * 0.02);
        lv_text.setScaleY(unit * 0.02);
        lv_text.setFill(Color.WHITE);
        getChildren().add(lv_text);

        Text exp_text = new Text("" + player.EXP+"/" + player.EXPLimit);
        exp_text.setLayoutX(unit * 0.27);
        exp_text.setLayoutY(unit * 0.6);
        exp_text.setScaleX(unit * 0.02);
        exp_text.setScaleY(unit * 0.02);
        exp_text.setFill(Color.WHITE);
        getChildren().add(exp_text);
        ImageView border1 = setImage(StringReminder.UIImageAddress + StringReminder.border1, new CollisionBox(1, 1));
        getChildren().add(border1);
        getChildren().add(heart);
        getChildren().add(crystal);
        getChildren().add(StopButton);
        updateHeart();
        updateCrystal();
    }

    protected void updateHeart () {
        heart.getChildren().clear();
        double unit = this.root.stage.getScene().getHeight()/10;
        for (int i=0; i<=player.hpLimit; i++) {
            ImageView heartImage = setImage(StringReminder.UIImageAddress + StringReminder.heartLimit, new CollisionBox(0.5f, 0.5f));
            heartImage.setLayoutX(unit*i*0.3+unit);
            heart.getChildren().add(heartImage);
        }
        for (int i=0; i<=player.hp; i++) {
            ImageView heartImage = setImage(StringReminder.UIImageAddress + StringReminder.heart, new CollisionBox(0.5f, 0.5f));
            heartImage.setLayoutX(unit*i*0.3+unit);
            heart.getChildren().add(heartImage);
        }

    }

    protected void updateCrystal () {
        crystal.getChildren().clear();
        double unit = this.root.stage.getScene().getHeight()/10;
        for (int i=0; i<=player.magicLimit; i++) {
            ImageView crystalImage = setImage(StringReminder.UIImageAddress + StringReminder.crystalLimit, new CollisionBox(0.5f, 0.5f));
            crystalImage.setLayoutX(unit*i*0.3+unit);
            crystalImage.setLayoutY(unit*0.5);
            crystal.getChildren().add(crystalImage);
        }
        for (int i=0; i<=player.magic; i++) {
            ImageView crystalImage = setImage(StringReminder.UIImageAddress + StringReminder.crystal, new CollisionBox(0.5f, 0.5f));
            crystalImage.setLayoutX(unit*i*0.3+unit);
            crystalImage.setLayoutY(unit*0.5);
            crystal.getChildren().add(crystalImage);
        }

    }

    public void updateHead() {
        head.setViewport(new Rectangle2D(20*headFrameCount, 0, 20, 20));
        updateUI();

    }

    @Override
    public void ticks() {
        gapCount = (gapCount + 1) % gapCountLimit;
        if (gapCount == 0) {
            headFrameCount = (headFrameCount + 1) % headFrames;
            updateHead();
        }
    }
}
