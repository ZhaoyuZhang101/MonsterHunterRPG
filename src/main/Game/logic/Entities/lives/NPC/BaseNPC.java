package Game.logic.Entities.lives.NPC;

import Game.logic.Entities.Entity;
import Game.logic.Entities.lives.BaseLives;
import Game.logic.implementClass.*;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.List;
import java.util.Set;

/**
 * @className BaseNPC
 * @Description TODO: implement this class
 *              requirement: for doing this class some thing need to be considered:
 *                           1. NPC has dialog window, and can interact with player when player click them.
 * @Author zhang
 * @DATE 2022/8/27 1:25
 **/
public class BaseNPC extends BaseLives {
    public Group dialogBox = new Group();
    public int showBox = -1;

    /**
     * @author Zhang
     * @param collisionBox collision box for npc to trigger dialog
     */
    public BaseNPC(CollisionBox collisionBox, int hp, int magic, int defense, int damage, Set<String> skills) {
        super(collisionBox, hp, magic, defense, damage, skills);
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    mouse_click();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void changeActivity(playerActivity act) {
        if (activity != playerActivity.death) {
            switch (act) {
                case idle -> {
                    this.frames = 4;
                    this.frameCount = 0;
                }
                case run, death, getHurt -> this.frames = 0;
                case jump, fall -> {
                    this.frames = 0;
                    this.frameCount = 0;
                }
                case attack -> {
                    this.frameCount = 0;
                    switch (this.attackPhase) {
                        case 0, 1, 2 -> this.frames = 0;
                    }
                }
            }
            this.activity = act;
        }
    }

    public void mouse_click() throws IOException {
        getChildren().remove(dialogBox);
        dialogBox.getChildren().clear();
        String dialog = getRandomDialog();
        ImageView box = convertImage(StringReminder.UIImageAddress + StringReminder.dialogBox, new CollisionBox(1, 2.5f));
        Text text = new Text(dialog);
        box.setLayoutX(box.getLayoutY()-(root.stage.getScene().getHeight()/10)*0.6);
        box.setLayoutY(box.getLayoutY()-(root.stage.getScene().getHeight()/10)*0.2);
        text.setLayoutX(text.getLayoutY()-(root.stage.getScene().getHeight()/10)*0.5);
        text.setLayoutY(text.getLayoutY()+(root.stage.getScene().getHeight()/10)*0.4);
        dialogBox.getChildren().add(box);
        dialogBox.getChildren().add(text);
        showBox = 100;
        getChildren().add(dialogBox);
    }

    public void update() {
        getChildren().remove(dialogBox);
        getChildren().add(dialogBox);
        System.out.println("update");
    }

    public String getRandomDialog() throws IOException {
        JSONObject npcJson = helperFunc.readJson(StringReminder.npcBaseAddress);
        JSONObject npcContent = npcJson.getJSONObject(career.toString());
        JSONArray array = npcContent.getJSONArray("dialog");
        Random random = new Random();
        int index = random.nextInt(array.length());
        System.out.println(array.get(index).toString());
        return array.get(index).toString();
    }


    @Override
    public void loadAppearance(Career career) {
        this.career = career;

        switch (career.toString()) {
            case "adventure_01", "barkeep", "beggar", "villager_01", "witch" -> frames = 5;
            case "dog", "princess" -> frames = 4;
        }
        setImage(StringReminder.animationAddress+"NPC/"+ career + ".png", collisionBox);
        this.appearance.setViewport(new Rectangle2D(34*frameCount, 0, 34, 34));
        getChildren().add(this.appearance);
        setLayoutX(this.root.stage.getScene().getHeight()/2-this.root.stage.getScene().getHeight()*0.15);
        setLayoutY(this.root.stage.getScene().getHeight()-this.root.stage.getScene().getHeight()*0.24);
        this.grandHeight = this.root.stage.getScene().getHeight()-this.root.stage.getScene().getHeight()*0.24;
        this.height = (this.root.stage.getScene().getHeight()-getLayoutY())/(this.root.stage.getScene().getHeight()/10);
    }

    @Override
    public void setImage(String address, CollisionBox collisionBox) {
        InputStream a = getClass().getResourceAsStream(address);
        assert a != null;
        Image image = new Image(a);
        this.appearance = new ImageView(image);
        this.appearance.setFitWidth(collisionBox.getWidth()*(this.root.stage.getScene().getHeight()/10)*1);
        this.appearance.setFitHeight(collisionBox.getHeight()*(this.root.stage.getScene().getHeight()/10)*1);
        this.appearance.setLayoutY(this.appearance.getLayoutY()+(this.root.stage.getScene().getHeight()/10)*0.9);
    }

    public ImageView convertImage(String address, CollisionBox collisionBox) {
        InputStream a = getClass().getResourceAsStream(address);
        assert a != null;
        Image image = new Image(a);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(collisionBox.getWidth()*(this.root.stage.getScene().getHeight()/10)*1);
        imageView.setFitHeight(collisionBox.getHeight()*(this.root.stage.getScene().getHeight()/10)*1);
        return imageView;
    }


    public void animationUpdate() {
        getChildren().remove(this.appearance);
        try {
            setImage(StringReminder.animationAddress+"NPC/"+ career + ".png", collisionBox);
        } catch (Exception e) {
            setImage(StringReminder.animationAddress+"NPC/"+ career + ".png", collisionBox);
            throw new Error("动画名称错误或未发现动画文件，错误文件："+ StringReminder.animationAddress+"NPC/"+ career + ".png");
        }
        this.appearance.setViewport(new Rectangle2D(34*frameCount, 0, 34, 34));
        getChildren().add(this.appearance);
        if (isPlayer) {
            setLayoutX(this.root.stage.getScene().getHeight() / 2 - this.root.stage.getScene().getHeight() * 0.15);
        }
        this.height = (this.root.stage.getScene().getHeight()-getLayoutY())/(this.root.stage.getScene().getHeight()/10);
    }

    public void NPCAdjustment() {
        double unit = root.stage.getScene().getHeight() / 10;
        for (BaseLives bn : this.root.gameProcess.NPCs) {
            double difference = bn.distance - this.root.gameProcess.player.distance;
            bn.setLayoutX(this.root.gameProcess.player.getLayoutX() + difference * unit);
        }
    }

    @Override
    public void map_ticks() {
        if (root.gameProcess.player == null) {
            return;
        }
        NPCAdjustment();
        if (showBox>0) {
            showBox --;
            update();
        } else if (showBox == 0) {
            dialogBox.getChildren().clear();
            showBox = -1;
        }
    }
}
