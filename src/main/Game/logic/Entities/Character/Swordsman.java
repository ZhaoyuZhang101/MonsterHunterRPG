package Game.logic.Entities.Character;

import Game.logic.Entities.Character.Player;
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
        System.out.println(act);
        switch (act) {
            case idle -> this.frames = 10;
            case run -> this.frames = 6;
            case jump, fall -> this.frames = 2;
            case attack -> {
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

}
