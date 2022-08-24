package Game.logic.Entities.Character;


import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.Position;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Wizard extends Player{

    public Wizard(CollisionBox collisionBox, Position pos, int hp, int magic, int defense, int damage, Set<String> skills) {
        super(collisionBox, pos, hp, magic, defense, damage, skills);
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
