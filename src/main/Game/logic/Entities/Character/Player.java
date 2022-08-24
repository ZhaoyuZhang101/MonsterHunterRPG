package Game.logic.Entities.Character;

import Game.logic.Entities.Entity;
import Game.logic.Items.Weapon;
import Game.logic.implementClass.*;
import javafx.geometry.Rectangle2D;

import java.util.Set;

public class Player extends Entity {
    //base properties
    public Weapon weapon;
    public int hp;
    public int magic;
    public int defense;
    public int damage;
    public Career career;
    public Set<String> skills;
    public playerActivity activity = playerActivity.idle;
    public boolean up = false;
    public boolean down = false;
    public boolean left = false;
    public boolean right = false;

    // action control
    public int tickCount;
    public int frameCount;
    private int jumpCount = 0;
    private final int jumpLimit = 5;
    private int jumpPhase = 0;
    public int attackCount = -1;
    public final int attackLimit = 5;
    public int attackPhase = -1;
    public int phaseCount = -1;
    public boolean startAttack = false;
    int frames = 10;
    private double grandHeight = 360;
    public boolean startMoving = false;

    public Player(CollisionBox collisionBox, Position pos, int hp, int magic, int defense, int damage, Set<String> skills) {
        this.collisionBox = collisionBox;
        this.pos = pos;
        this.hp = hp;
        this.magic = magic;
        this.defense = defense;
        this.damage = damage;
        this.skills = skills;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void changeActivity(playerActivity act) {
        switch (act) {
            case idle, attack, jump, fall -> this.frameCount = 0;
            case run -> {}
        }
        this.activity = act;
    }

    public void loadAppearance(Career career) {

        this.career = career;
        setImage(StringReminder.animationAddress+career+"/"+this.direction.name+"_idle.png");
        this.appearance.setViewport(new Rectangle2D(135*frameCount, 0, 135, 135));
        getChildren().add(this.appearance);
        setLayoutX(this.root.stage.getScene().getWidth()/2);
        setLayoutY(this.root.stage.getScene().getHeight()-this.root.stage.getScene().getHeight()*0.24);
        this.grandHeight = this.root.stage.getScene().getHeight()-this.root.stage.getScene().getHeight()*0.24;
        System.out.println(this.grandHeight);
    }

    // update the animation of character per frame.
    public void animationUpdate() {
        getChildren().remove(this.appearance);
        try {
            setImage(StringReminder.animationAddress + this.career + "/" + this.direction.name + "_" + activity.name + (!(activity==playerActivity.attack) ? "" : attackPhase + 1) + ".png");
        } catch (Exception e) {
            setImage(StringReminder.animationAddress + this.career + "/" + this.direction.name + "_idle.png");
            throw new Error("动画名称错误或未发现动画文件，错误文件："+ StringReminder.animationAddress + this.career + "/" + this.direction.name + "_" + activity.name + (!startAttack ? "" : attackPhase + 1) + ".png");
        }
        this.appearance.setViewport(new Rectangle2D(135*frameCount, 0, 135, 135));
        getChildren().add(this.appearance);
        this.grandHeight = this.root.stage.getScene().getHeight()-this.root.stage.getScene().getHeight()*0.24;
        setLayoutX(this.root.stage.getScene().getWidth()/2);
    }

    public void attack() {

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

    // update the frame process once per 2 ticks
    public void frame() {
        frameCount=(frameCount+1)%frames;
    }

    //trigger once per 50 miles
    @Override
    public void ticks() {
        super.ticks();
        animationUpdate();
        tickCount = (tickCount + 1) % 30;
        if (tickCount % 2 == 0) {
            frame();
        }
        if (startMoving && activity == playerActivity.jump) {
            if (jumpCount < jumpLimit) {
                jumpCount++;
                setLayoutY(getLayoutY()-(jumpCount*10-jumpCount^2));
                System.out.println(getLayoutY());
            } else {
                changeActivity(playerActivity.idle);
                startMoving = false;
            }
        }

        if (left||right) {
            this.root.mapMove(direction, true);
            if (activity!=playerActivity.jump&&activity!=playerActivity.fall&&activity!=playerActivity.attack) {
                changeActivity(playerActivity.run);
            }
        } else {
            this.root.mapMove(direction, false);
        }

        if (activity!=playerActivity.jump) {
            if (jumpCount>0) {
                jumpCount--;
            }
        }

        if (getLayoutY()<this.grandHeight && activity != playerActivity.jump) {
            setLayoutY(getLayoutY()+(20));
            if (activity!=playerActivity.attack) {
                changeActivity(playerActivity.fall);
            }
        }
        if (getLayoutY()>this.grandHeight && activity != playerActivity.jump) {
            setLayoutY(this.grandHeight);
            if (activity!=playerActivity.attack) {
                changeActivity(playerActivity.idle);
            }
            jumpPhase = 0;
        }
    }
}