package Game.logic.hurtEngine;

import Game.logic.Effects.EffectPlayer;
import Game.logic.Engine.SceneCreator;
import Game.logic.Entities.lives.BaseLives;
import Game.logic.implementClass.CollisionBox;
import Game.logic.implementClass.Effects;
import Game.logic.implementClass.TimeLineController;
import Game.logic.implementClass.playerActivity;

import java.io.IOException;

/**
 * @className hurtEngine
 * @Description TODO
 * @Author zhang
 * @DATE 2022/10/2 3:35
 **/
public class hurtEngine {
    int delay = 0;
    SceneCreator root;
    BaseLives attacker;
    BaseLives victim;
    TimeLineController timeLineController;
    public hurtEngine(SceneCreator root) {
        this.root = root;
        timeLineController = new TimeLineController() {
            @Override
            public void tick() throws IOException {
                super.tick();
                ticks();
            }
        };
    }
    public void getHurt(BaseLives attacker, BaseLives victim, int delay) {
        this.delay = delay;
        this.attacker = attacker;
        this.victim = victim;
    }

    public void startHurt() {
        if (Math.abs(attacker.distance - victim.distance) <= attacker.attackRange && Math.abs(attacker.height - victim.height) <= 1) {
            victim.hp -= attacker.damage;
            victim.changeActivity(playerActivity.getHurt);
            EffectPlayer effect = new EffectPlayer(this.root, Effects.bleeding, 22, victim.distance, victim.height, new CollisionBox(1, 1),100, 100);
            this.root.addChild(effect);
            if (victim.hp <= 0) {
                victim.death();
                if (attacker.isPlayer) {
                    attacker.EXP += victim.level*10;
                    if (attacker.EXP >= attacker.EXPLimit) {
                        attacker.EXP = attacker.EXP - attacker.EXPLimit;
                        attacker.levelUp();
                    }
                }
                System.out.println("dead");
            }
        }
    }

    public void ticks() {
        if (delay > 0) {
            delay --;
        } else {
            startHurt();
            timeLineController.stop();
        }
    }
}
