package Game.logic.Items;

import Game.logic.implementClass.WeaponType;

public class Weapon {
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
