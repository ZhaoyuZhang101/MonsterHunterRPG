package Game.logic.Entities.Platform;

import Game.logic.Entities.Entity;
import Game.logic.implementClass.Career;
import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.StringReminder;
import Game.logic.implementClass.TimeLineController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

/**
 * @className BasePlatform
 * @Description TODO: please implement this class
 *              requirement: for doing this class some thing need to be considered:
 *                           1. platform need distance, height, collisionBox.
 * @Author zhang
 * @DATE 2022/8/27 1:02
 **/
public class BasePlatform extends Entity {
    public double height;
    public double monsterGenerateRate;
    public Career monsterType;
    public Career NPCType;
    public Stage stage;
    public String last = "";
    public String front = "";
    public BasePlatform (Stage stage, double height, double monsterGenerateRate, String monsterType, String NPCType) {
        this.stage = stage;
        this.height = height;
        this.monsterGenerateRate = monsterGenerateRate;
        this.monsterType = switch (monsterType) {
            case ("skeleton") -> Career.skeleton;
            case ("dragon") -> Career.dragon;
            default -> null;
        };
        this.NPCType = switch (NPCType) {
            case ("adventure_01") -> Career.adventure_01;
            case ("barkeep") -> Career.barkeep;
            case ("beggar") -> Career.beggar;
            case ("dog") -> Career.dog;
            case ("princess") -> Career.princess;
            case ("villager_01") -> Career.villager_01;
            case ("witch") -> Career.witch;
            default -> null;
        };;
        TimeLineController timeLineController = new TimeLineController() {
            @Override
            public void tick() throws IOException {
                super.tick();
                ticks();
            }
        };
        this.collisionBox = new CollisionBox(0, 0);
    }

    public void loadAppearance() {
        setImage(StringReminder.platformBaseAddress + StringReminder.grass, collisionBox);
        getChildren().add(appearance);
    }

    public void updateShape() {
    }


    @Override
    public void ticks() throws IOException {
        super.ticks();
        this.updateShape();
    }
}
