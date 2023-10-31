package Game.logic.Entities.Platform;

import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.StringReminder;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @className Grass
 * @Description TODO
 * @Author zhang
 * @DATE 2022/8/27 1:47
 **/
public class Grass extends  BasePlatform{
    public Grass(Stage stage, double height, double monsterGenerateRate, String monsterType, String NPCType) {
        super(stage, height, monsterGenerateRate, monsterType, NPCType);
        // remind: 体积盒在这里设置
        this.collisionBox = new CollisionBox(1f, 4);
    }

    @Override
    public void updateShape() {
        this.appearance.setFitWidth(collisionBox.getWidth() * (this.root.stage.getScene().getHeight() / 10));
        this.appearance.setFitHeight(collisionBox.getHeight() * (this.root.stage.getScene().getHeight() / 10));
    }

}
