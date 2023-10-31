package Game.logic.Entities.lives;

import Game.logic.Effects.EffectPlayer;
import Game.logic.Entities.Entity;
import Game.logic.Entities.Platform.BasePlatform;
import Game.logic.Items.Weapon;
import Game.logic.UI.BaseUI;
import Game.logic.UI.GameOverUI;
import Game.logic.implementClass.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class BaseLives extends Entity {
    //base properties
    public int level = 1;
    public int EXP = 0;
    public int EXPLimit = level*30;
    public double attackRange = 1;
    public Weapon weapon;
    public Career career;
    public Set<String> skills;
    public playerActivity activity = playerActivity.idle;
    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;
    public boolean isPlayer = false;
    public boolean isNPC = false;
    public BasePlatform onPlatform = null;
    // action control
    public int tickCount;
    public int frameCount;
    public int jumpCount = 0;
    public final int jumpLimit = 5;
    public int jumpPhase = 0;
    public int attackCount = -1;
    public final int attackLimit = 5;
    public int attackPhase = -1;
    public int phaseCount = -1;
    public boolean startAttack = false;
    public int frames = 10;
    public double grandHeight = 0;
    public boolean startMoving = false;
    public boolean canMove = false;
    public EffectPlayer effect;
    public boolean isHitCanBack = false;
    public double hitBackRange = 0.0;

    public boolean gameOverUIOpened = false;

    public BaseLives(CollisionBox collisionBox, int hp, int magic, int defense, int damage, Set<String> skills) {
        this.collisionBox = collisionBox;
        this.hp = hp;
        this.magic = magic;
        this.defense = defense;
        this.damage = damage;
        this.skills = skills;
    }

    @Override
    public void setImage(String address, CollisionBox collisionBox) {
        System.out.println(address);
        InputStream a = getClass().getResourceAsStream(address);
        assert a != null;
        Image image = new Image(a);
        this.appearance = new ImageView(image);
        this.appearance.setFitWidth(collisionBox.getWidth()*(this.root.stage.getScene().getHeight()/10)*3);
        this.appearance.setFitHeight(collisionBox.getHeight()*(this.root.stage.getScene().getHeight()/10)*3);
    }

    public void levelUp() {
        this.level += 1;
        EXPLimit = level*30;
        this.hp += (int) (this.level/2);
        this.hpLimit = this.hp;
        this.damage += (int) (this.level/2);
        EffectPlayer effectPlayer = new EffectPlayer(root, Effects.levelUp, 13, this.distance+0.5, this.height + 0.5, new CollisionBox(1,0.5f),16, 100);
        this.root.addChild(effectPlayer);
    }

    /**
     * @Author zhang
     * @Description //TODO: when the entity attack some one, this method will be triggered, and the hp will reduced according to the damage input
     * @Date 0:41 2022/8/27
     * @Param [entity, damage]: entity for where the damage comes from, damage for how much damage that this entity would get.
     * @return boolean: if this entity has been hurt.
     **/
    public boolean getHurt(Entity damageSource, int damage, int delay) {
        double unit = this.root.stage.getScene().getHeight() / 10;
        System.out.println("get damaged");
        changeActivity(playerActivity.getHurt);
        effect = new EffectPlayer(this.root, Effects.bleeding, 22, this.distance, this.height, new CollisionBox(1, 1),100, 100);
        this.root.addChild(effect);
        hp -= damage;
        return false;
    }

    public void death() {
        changeActivity(playerActivity.death);
        if (isPlayer) {
            if (!gameOverUIOpened) {
                gameOverUIOpened = true;
                BaseUI ui = new GameOverUI(root.stage, root);
                root.addChild(ui);
            }
        }
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void changeActivity(playerActivity act) {
        switch (act) {
            case idle, attack, jump, fall, getHurt -> this.frameCount = 0;
            case run -> {}
        }
        this.activity = act;
    }

    public void loadAppearance(Career career) {
        this.career = career;
        setImage(StringReminder.animationAddress+career+"/"+this.direction.name+"_idle.png",collisionBox);
        this.appearance.setViewport(new Rectangle2D(135*frameCount, 50, 135, 50));
        getChildren().add(this.appearance);
        setLayoutX(this.root.stage.getScene().getHeight()/2-this.root.stage.getScene().getHeight()*0.15);
        setLayoutY(this.root.stage.getScene().getHeight()-this.root.stage.getScene().getHeight()*0.5);
        this.grandHeight = this.root.stage.getScene().getHeight()-this.root.stage.getScene().getHeight()*0.24;
        this.height = (this.root.stage.getScene().getHeight()-getLayoutY())/(this.root.stage.getScene().getHeight()/10);
    }

    // update the animation of character per frame.
    public void animationUpdate() {
        getChildren().remove(this.appearance);
        try {
            setImage(StringReminder.animationAddress + this.career + "/" + this.direction.name + "_" + activity.name + (!(activity==playerActivity.attack) ? "" : attackPhase + 1) + ".png", this.collisionBox);
        } catch (Exception e) {
            setImage(StringReminder.animationAddress + this.career + "/" + this.direction.name + "_idle.png", this.collisionBox);
            throw new Error("动画名称错误或未发现动画文件，错误文件："+ StringReminder.animationAddress + this.career + "/" + this.direction.name + "_" + activity.name + (!startAttack ? "" : attackPhase + 1) + ".png");
        }
        this.appearance.setViewport(new Rectangle2D(135*frameCount, 0, 135, 135));
        getChildren().add(this.appearance);
        if (isPlayer) {
            setLayoutX(this.root.stage.getScene().getHeight() / 2 - this.root.stage.getScene().getHeight() * 0.15);
        }
        this.height = (this.root.stage.getScene().getHeight()-getLayoutY())/(this.root.stage.getScene().getHeight()/10);
    }

    public void attack() {

    }

    public void hitBack(Direction direction) {
        if (isHitCanBack) {
            double unit = this.root.stage.getScene().getHeight() / 10;
            if (direction == Direction.left) {
                this.setLayoutX(this.getLayoutX() - (unit * hitBackRange));
            } else {
                this.setLayoutX(this.getLayoutX() + (unit * hitBackRange));
            }
        }
    }

    public void moveUp(boolean start) {
        if (start) {
            this.up = true;
            if (getLayoutY() < this.grandHeight) {
                jumpPhase++;
            }
            if (jumpCount <= 0 && jumpPhase <= 3) {
                changeActivity(playerActivity.jump);
                this.jumpCount = 0;
                startMoving = true;
                startAttack = false;
            }
        }else {
            this.up = false;
        }
    }
    public void moveDown(boolean start) {
    }
    public void moveLeft(boolean start) {
        if (start) {
            this.left = true;
            this.direction = Direction.left;
            changeActivity(playerActivity.run);
            startMoving = true;
            startAttack = false;
        } else {
            if (activity != playerActivity.jump && activity != playerActivity.fall && attackCount == -1) {
                changeActivity(playerActivity.idle);
                startMoving = false;
            }
            this.left = false;
        }
    }
    public void moveRight(boolean start) {
        if (start) {
            this.right = true;
            this.direction = Direction.right;
            startMoving = true;
            startAttack = false;
        } else {
            if (activity != playerActivity.jump && activity != playerActivity.fall && attackCount == -1) {
                changeActivity(playerActivity.idle);
                startMoving = false;
            }
            this.right = false;
        }
    }



    public void collisionDetector() {
        double unit = this.root.stage.getScene().getHeight() / 10;
        double currentHeight = -1;
        double leftCurrentDistance = 999999;
        double rightCurrentDistance = -1;
        BasePlatform frontPlatform = null;
        for (BasePlatform i: this.root.map.basePlatforms) {
            double dist = i.distance;
            double height = i.height;
            CollisionBox collision = i.collisionBox;
            if (this.distance>=dist&&this.distance<=(dist+collision.getWidth()+0.5)) {
                if (this.height-1.6>height) {
                    if (height>currentHeight) {
                        currentHeight = height;
                        onPlatform = i;
                    }
                }
            }
            if (direction == Direction.right) {
                if (dist < leftCurrentDistance && (this.distance <= dist&& (height < this.height && height > this.height - 1.5))) {
                    leftCurrentDistance = dist;
                    frontPlatform = i;
                }
            } else if (direction == Direction.left) {
                if (dist > rightCurrentDistance && (this.distance >= dist +collision.getWidth() && (height < this.height && height > this.height - 1.5))) {
                    rightCurrentDistance = dist;
                    frontPlatform = i;
                }
            }
        }
        if (frontPlatform != null) {
            if (direction == Direction.right) {
                canMove = !(frontPlatform.distance - 0.5 <= this.distance);

            } else if (direction == Direction.left) {
                canMove = !(frontPlatform.distance + frontPlatform.collisionBox.getWidth() +1 >= this.distance);
            } else {
                canMove = true;
            }
        } else {
            canMove = true;
        }
        if (currentHeight != -1) {
            this.grandHeight = this.root.stage.getScene().getHeight() - (currentHeight * unit) - unit * 1.9;
        } else {
            this.grandHeight = this.root.stage.getScene().getHeight() + (5 * unit);
        }

    }

    // update the frame process once per 2 ticks
    public void frame() {
        frameCount=(frameCount+1)%frames;
        if (frameCount == (frames-1) ) {
            if (this.activity == playerActivity.getHurt) {
                frameCount = 0;
                changeActivity(playerActivity.idle);
            } else if (this.activity == playerActivity.death) {
                root.deleteChild(this);
                if (isPlayer) {
                    root.gameProcess.player = null;
                } else {
                    root.gameProcess.monsters.remove(this);
                }
            }
        }
    }

    public void map_ticks() {}
    //trigger once per 50 miles
    @Override
    public void ticks() throws IOException {
        super.ticks();
        collisionDetector();
        animationUpdate();
        tickCount = (tickCount + 1) % 30;
        if (tickCount % 2 == 0) {
            frame();
        }
        this.height = (this.root.stage.getScene().getHeight()-getLayoutY())/(this.root.stage.getScene().getHeight()/10);
        if (this.height <= -1) {
            death();
        }
        if (this.activity != playerActivity.getHurt && this.activity != playerActivity.death) {
            if (startMoving && activity == playerActivity.jump) {
                if (jumpCount < jumpLimit) {
                    jumpCount++;
                    setLayoutY(getLayoutY() - (this.root.stage.getScene().getHeight() / 30));
                } else {
                    changeActivity(playerActivity.idle);
                    startMoving = false;
                }
            }
            if (left || right) {
                if (isPlayer) {
                    this.root.mapMove(direction, this.canMove);
                }
                if (activity != playerActivity.jump && activity != playerActivity.fall && activity != playerActivity.attack) {
                    changeActivity(playerActivity.run);
                }
            } else {
                if (isPlayer) {
                    this.root.mapMove(direction, false);
                }
            }

            if (activity != playerActivity.jump) {
                if (jumpCount > 0) {
                    jumpCount--;
                }
            }
        }

        if (isPlayer) {
            if (onPlatform != null) {
                if (!Objects.equals(onPlatform.last, "")) {
                    root.gameProcess.save();
                    root.clearChildren();
                    root.load(true, onPlatform.last);
                }
            }
        }

        if (getLayoutY()<this.grandHeight && activity != playerActivity.jump) {
            setLayoutY(getLayoutY()+(int)(this.root.stage.getScene().getHeight()/30)-1);
            if (activity!=playerActivity.attack && this.activity != playerActivity.death) {
                changeActivity(playerActivity.fall);
            }
        }
        if (getLayoutY()>this.grandHeight && activity != playerActivity.jump && this.activity != playerActivity.death) {
            setLayoutY(this.grandHeight);
            if (activity!=playerActivity.attack) {
                changeActivity(playerActivity.idle);
            }
            jumpPhase = 0;
        }
    }
}