package Game.logic.Entities.Character;

import Game.logic.Entities.Entity;
import Game.logic.Items.Weapon;
import Game.logic.implementClass.*;
import javafx.geometry.Rectangle2D;

import java.util.Set;

public class Player extends Entity {
    public Weapon weapon;
    public int hp;
    public int magic;
    public int defense;
    public int damage;
    public int tickCount;
    public int frameCount;
    private int jumpCount = 0;
    private final int jumpLimit = 5;

    private int attackCount = -1;
    private final int attackLimit = 5;
    public int attackPhase = -1;
    public int phaseCount = -1;
    public boolean startAttack = false;
    int frames = 10;
    private boolean startMoving = false;
    public Career career;
    public Set<String> skills;
    public playerActivity activity = playerActivity.idle;
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
        this.frameCount = 0;
        switch (act) {
            case idle -> System.out.println("idle");
            case run -> System.out.println("run");
            case jump, fall -> System.out.println("jump");
            case attack -> System.out.println("attack");
        }
        this.activity = act;
    }

    public void loadAppearance(Career career) {
        this.career = career;
        setImage(StringReminder.animationAddress+career+"/"+this.direction.name+"_idle.png");
        this.appearance.setViewport(new Rectangle2D(135*frameCount, 0, 135, 135));
        getChildren().add(this.appearance);
        setLayoutX(this.root.stage.getScene().getWidth()/2);
        setLayoutY(this.root.stage.getScene().getHeight()/2);
        System.out.println(this.root.stage.getScene().getHeight());
    }

    public void animationUpdate() {
        getChildren().remove(this.appearance);
        System.out.println(this.direction.name + "_"+activity.name+(!startAttack?"":attackPhase+1)+".png");
        setImage(StringReminder.animationAddress + this.career + "/" + this.direction.name + "_"+activity.name+(!startAttack?"":attackPhase+1)+".png");
        this.appearance.setViewport(new Rectangle2D(135*frameCount, 0, 135, 135));
        getChildren().add(this.appearance);
        setLayoutX(this.root.stage.getScene().getWidth()/2);
        setLayoutY(this.root.stage.getScene().getHeight()/2);
    }

    public void attack() {
        if (attackCount==-1) {
            startAttack = true;
            if (phaseCount>0) {
                this.attackPhase = (this.attackPhase + 1) % 3;
                System.out.println("------------------------------------>");
            } else {
                attackPhase = 0;
            }
            System.out.println("----------------"+attackPhase);
            changeActivity(playerActivity.attack);
            switch (attackPhase) {
                case 0, 1 -> this.attackCount = 8;
                case 2 -> this.attackCount = 10;
            }
            startMoving = true;
            phaseCount = -1;
        }
    }

    public void moveUp() {
        if (activity!=playerActivity.fall) {
            changeActivity(playerActivity.jump);
        }
        startMoving = true;
    }
    public void moveDown() {
    }
    public void moveLeft() {
        this.direction = Direction.left;
        changeActivity(playerActivity.run);
        startMoving = true;
    }
    public void moveRight() {
        this.direction = Direction.right;
        changeActivity(playerActivity.run);
        startMoving = true;
    }
    public void stop() {
        if (activity!=playerActivity.jump&&activity!=playerActivity.fall&&attackCount==-1) {
            changeActivity(playerActivity.idle);
            startMoving = false;
        }
    }

    public void frame() {
        frameCount=((frameCount+1))%frames;
    }

    @Override
    public void ticks() {
        super.ticks();
        animationUpdate();
        tickCount = (tickCount+1) %30;
        if (tickCount%2==0) {
            frame();
        }
        if (attackCount>0) {
            attackCount--;
        } else if (attackCount==0){
            System.out.println("pause");
            changeActivity(playerActivity.idle);
            startAttack = false;
            startMoving = false;
            attackCount = -1;
            phaseCount = 10;
        }
        if (phaseCount>0) {
            System.out.println(phaseCount);
            phaseCount--;
        } else if (phaseCount==0){
            attackPhase = -1;
            phaseCount = -1;
        }
        if (startMoving&&activity==playerActivity.jump){
            if (jumpCount<jumpLimit) {
                jumpCount++;
            } else {
                changeActivity(playerActivity.fall);
            }
        }else if (startMoving&&activity==playerActivity.fall){
            if (jumpCount>0) {
                jumpCount--;
            } else {
                changeActivity(playerActivity.idle);
                startMoving = false;
            }
        }
    }
}