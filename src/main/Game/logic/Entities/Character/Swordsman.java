package Game.logic.Entities.Character;

import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.Position;
import Game.logic.implementClass.playerActivity;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Swordsman extends Player {

    public Swordsman(CollisionBox collisionBox, Position pos, int hp, int magic, int defense, int damage, Set<String> skills) {
        super(collisionBox, pos, hp, magic, defense, damage, skills);
    }

    @Override
    public void changeActivity(playerActivity act) {
        switch (act) {
            case idle -> {
                this.frames = 10;
                this.frameCount = 0;
            }
            case run -> this.frames = 6;
            case jump, fall -> {
                this.frames = 2;
                this.frameCount = 0;
            }
            case attack -> {
                this.frameCount = 0;
                switch (this.attackPhase) {
                    case 0, 1 -> this.frames=4;
                    case 3 -> this.frames=5;
                }
            }
        }
        this.activity = act;
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
        if (attackCount==-1) {
            startAttack = true;
            if (phaseCount>0) {
                this.attackPhase = (this.attackPhase + 1) % 3;
            } else {
                attackPhase = 0;
            }
            changeActivity(playerActivity.attack);
            switch (attackPhase) {
                case 0, 1 -> this.attackCount = 8;
                case 2 -> this.attackCount = 10;
            }
            startMoving = true;
            phaseCount = -1;
            System.out.println(this.frameCount+", "+this.frames);
        }
    }

    @Override
    public void ticks() {
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
