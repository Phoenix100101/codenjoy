package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Point;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 14:45
 */
public interface Field {
    boolean isBarrier(int x, int y);

    boolean tryToDrill(Hero hero, int x, int y);

    boolean isPit(int x, int y);

    Point getFreeRandom();

    boolean isLadder(int x, int y);

    boolean isPipe(int x, int y);

    boolean isFree(int x, int y);

    boolean isFullBrick(int x, int y);

    boolean isHeroAt(int x, int y);

    boolean isBrick(int x, int y);

    boolean isEnemyAt(int x, int y);
}
