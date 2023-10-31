package Game.logic.Entities.Platform;

import Game.logic.implementClass.CollisionBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @className StoneBrick
 * @Description TODO
 * @Author zhang
 * @DATE 2022/8/27 1:47
 **/
public class StoneBrick extends BasePlatform{
    public StoneBrick (Stage stage, double height, double monsterGenerateRate, String monsterType, String NPCType) {
        super(stage, height, monsterGenerateRate, monsterType, NPCType);
        // remind: 体积盒在这里设置
        this.collisionBox = new CollisionBox(0.5F, 3);
    }

}
