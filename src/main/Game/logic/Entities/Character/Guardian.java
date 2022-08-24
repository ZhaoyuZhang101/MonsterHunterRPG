package Game.logic.Entities.Character;


import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.Position;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class Guardian extends Player {

    public Guardian(CollisionBox collisionBox, Position pos, int hp, int magic, int defense, int damage, Set<String> skills) {
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

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Guardian player = new Guardian(null, null, 0, 0, 0, 0, new HashSet<>());
        player.useSkill("circle_slash");
    }
}
