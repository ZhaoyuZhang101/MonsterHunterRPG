package Game.logic.Entities.lives.Character;

import Game.logic.Entities.Entity;
import Game.logic.Entities.Platform.BasePlatform;
import Game.logic.Entities.lives.BaseLives;
import Game.logic.Items.Weapon;
import Game.logic.implementClass.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

public class Player extends BaseLives {
    public Player(CollisionBox collisionBox, int hp, int magic, int defense, int damage, Set<String> skills) {
        super(collisionBox, hp, magic, defense, damage, skills);
    }
}