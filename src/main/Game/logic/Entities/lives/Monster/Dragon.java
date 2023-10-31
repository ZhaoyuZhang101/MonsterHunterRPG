package Game.logic.Entities.lives.Monster;

import Game.logic.hurtEngine.hurtEngine;
import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.playerActivity;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * @className Skeleton
 * @Description TODO
 * @Author zhang
 * @DATE 2022/10/1 19:51
 **/
public class Dragon extends BaseMonster{
    public Dragon(CollisionBox collisionBox, int hp, int magic, int defense, int damage, Set<String> skills) {
        super(collisionBox, hp, magic, defense, damage, skills);
        isHitCanBack = false;
        hitBackRange = 0.5;
        searchRange = 5;
        attackGap = 100;
    }
    @Override
    public void changeActivity(playerActivity act) {
        if (activity != playerActivity.death) {
            switch (act) {
                case idle -> {
                    this.frames = 9;
                    this.frameCount = 0;
                }
                case run -> this.frames = 6;
                case getHurt -> this.frames = 3;
                case death -> this.frames = 9;
                case jump, fall -> {
                    this.frames = 3;
                    this.frameCount = 0;
                }
                case attack -> {
                    this.frameCount = 0;
                    switch (this.attackPhase) {
                        case 0, 1 -> this.frames = 4;
                        case 2 -> this.frames = 5;
                    }
                }
            }
            this.activity = act;
        }
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
        this.appearance.setLayoutY(this.appearance.getLayoutY() - (this.root.stage.getScene().getHeight()/10)*2);
        this.appearance.setLayoutX(this.appearance.getLayoutX() - (this.root.stage.getScene().getHeight()/10)*2);
    }

    public void useSkill(String skillName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.getClass().getMethod(skillName).invoke(this);
    }

    public void impact() {
        System.out.println("impact");
    }

    public void circle_slash() {
        System.out.println("circle_slash");
    }

    @Override
    public void attack() {
        super.attack();
        if (attackCount==-1 && activity != playerActivity.getHurt && activity!=playerActivity.death) {
            int delay = 0;
            startAttack = true;
            if (phaseCount>0) {
                this.attackPhase = (this.attackPhase + 1) % 3;
            } else {
                attackPhase = 0;
            }
            switch (this.attackPhase) {
                case 0, 1 -> delay = 20;
                case 2 -> delay = 30;
            }
            if (Math.abs(root.gameProcess.player.distance - this.distance) <= attackRange && Math.abs(root.gameProcess.player.height - this.height) <= 1) {
                hurtEngine he = new hurtEngine(root);
                he.getHurt(this, root.gameProcess.player, delay);
            }
            changeActivity(playerActivity.attack);
            switch (attackPhase) {
                case 0, 1 -> this.attackCount = 8;
                case 2 -> this.attackCount = 10;
            }
            startMoving = true;
            phaseCount = -1;
        }
    }

    @Override
    public void ticks() throws IOException {
        super.ticks();
        if (attackCount>0) {
            attackCount--;
        } else if (attackCount==0){
            changeActivity(playerActivity.idle);
            startAttack = false;
            startMoving = false;
            attackCount = -1;
            phaseCount = 10;
        }
        if (phaseCount>0) {
            phaseCount--;
        } else if (phaseCount==0){
            attackPhase = -1;
            phaseCount = -1;
        }
    }

}
