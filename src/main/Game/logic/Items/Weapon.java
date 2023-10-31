package Game.logic.Items;

import Game.logic.implementClass.WeaponType;
import javafx.scene.Parent;

public class Weapon extends Parent {
    public String name;
    public int damage;
    public WeaponType type;
    public int defense;
    public Weapon(WeaponType type, String name, int damage, int defense) {
        this.name = name;
        this.type = type;
        this.damage = damage;
        this.defense = defense;
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "name='" + name + '\'' +
                ", damage=" + damage +
                ", type=" + type +
                ", defense=" + defense +
                '}';
    }
}
