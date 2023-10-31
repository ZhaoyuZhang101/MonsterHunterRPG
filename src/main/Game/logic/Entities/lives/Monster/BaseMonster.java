package Game.logic.Entities.lives.Monster;

import Game.logic.Entities.Entity;
import Game.logic.Entities.Platform.BasePlatform;
import Game.logic.Entities.lives.BaseLives;
import Game.logic.implementClass.*;
import javafx.geometry.Rectangle2D;

import java.util.Random;
import java.util.Set;

/**
 * @className BaseMonster
 * @Description TODO: please implement this class
 *              requirement: for doing this class some thing need to be considered:
 *                           1. it is similar to player class but only difference is that it has AI(simple AI, like when the player close to monster it will run to player and attack automatically)
 *                           2. when it dead, the treasures will come out.
 * @Author zhang
 * @DATE 2022/8/27 1:05
 **/
public class BaseMonster extends BaseLives {
    public Direction autoWalkDirection = Direction.right;
    public double searchRange = 3;
    public int attackGap = 50;
    public boolean attackEnd = false;
    public BaseMonster(CollisionBox collisionBox, int hp, int magic, int defense, int damage, Set<String> skills) {
        super(collisionBox, hp, magic, defense, damage, skills);
    }
    @Override
    public void loadAppearance(Career career) {
        this.career = career;
        setImage(StringReminder.animationAddress + career + "/" + this.direction.name + "_idle.png", collisionBox);
        appearance.setViewport(new Rectangle2D(135*frameCount, 0, 135, 135));
        getChildren().add(this.appearance);
        setLayoutX(this.root.stage.getScene().getHeight()/2-this.root.stage.getScene().getHeight()*0.15);
        setLayoutY(this.root.stage.getScene().getHeight()-this.root.stage.getScene().getHeight()*0.24);
        this.grandHeight = this.root.stage.getScene().getHeight()-this.root.stage.getScene().getHeight()*0.24;
        this.height = (this.root.stage.getScene().getHeight()-getLayoutY())/(this.root.stage.getScene().getHeight()/10);
    }

    public void monsterAdjustment() {
        double unit = root.stage.getScene().getHeight() / 10;
        for (BaseLives bm : this.root.gameProcess.monsters) {
            double difference = bm.distance - this.root.gameProcess.player.distance;
            bm.setLayoutX(this.root.gameProcess.player.getLayoutX() + difference * unit);
        }
    }

    @Override
    public void map_ticks() {
        if (root.gameProcess.player == null) {
            return;
        }
        monsterAdjustment();
        AILogic();
        if (attackEnd) {
            attackGap-=1;
            if (attackGap <= 0) {
                attackEnd = false;
            }
        }

    }

    public void playerDetector() {
        if (root.gameProcess.player.distance > this.distance+0.5 && root.gameProcess.player.distance < this.distance-0.5 + searchRange) {
            if (activity != playerActivity.getHurt) {
                moveRight(true);
                if (canMove) {
                    this.distance += 0.01;
                }
            }
        } else if (root.gameProcess.player.distance < this.distance-0.5 && root.gameProcess.player.distance > this.distance+0.5 - searchRange) {
            if (activity != playerActivity.getHurt) {
                moveLeft(true);
                if (canMove) {
                    this.distance -= 0.01;
                }
            }
        } else if (root.gameProcess.player.distance > this.distance-0.5 + searchRange || root.gameProcess.player.distance < this.distance+0.5 - searchRange) {
            this.randomWalk();
        } else {
            if (!attackEnd) {
                attack();
                attackEnd = true;
                attackGap = 50;
            }
        }
    }

    public void randomWalk() {
        if (onPlatform!=null) {
            if (autoWalkDirection == Direction.right) {
                if (this.distance < onPlatform.distance + onPlatform.collisionBox.getWidth() - 0.2) {
                    moveRight(true);
                    if (canMove) {
                        this.distance += 0.01;
                    } else {
                        moveLeft(true);
                        autoWalkDirection = Direction.left;
                    }
                } else {
                    autoWalkDirection = Direction.left;
                }
            } else {
                if (this.distance > onPlatform.distance + 0.2) {
                    moveLeft(true);
                    if (canMove) {
                        this.distance -= 0.01;
                    } else {
                        moveRight(true);
                        autoWalkDirection = Direction.right;
                    }
                } else {
                    autoWalkDirection = Direction.right;
                }
            }
        }
    }

    public void AILogic() {
        this.playerDetector();
    }


    @Override
    public void attack() {
    }
}
