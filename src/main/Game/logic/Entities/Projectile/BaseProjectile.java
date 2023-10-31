package Game.logic.Entities.Projectile;

import Game.logic.Entities.Entity;

/**
 * @className BaseProjectile
 * @Description TODO: implement this class
 *              requirement: for doing this class some thing need to be considered:
 *                           1. projectile should have property of degree, to control the actual direction of shooting
 *                           2. need a property of distance and height (definition, distance from the start point, the start point is the edge of the right most game window(only when game start).
 *                           and the unit is meters(1 meter stands for the 1/15 width of the window))
 *                           3. length and height (use collisionBox class)
 *                           4. need some function, like cause damage, movement. (basically, when projectile has been shoot it will keep moving till it shoot something or move out of the game)
 *                           5. power. means how far could it shoot. (it might be effected from gravity[considering].)
 * @Author zhang
 * @DATE 2022/8/27 0:50
 **/
public class BaseProjectile extends Entity {
}
