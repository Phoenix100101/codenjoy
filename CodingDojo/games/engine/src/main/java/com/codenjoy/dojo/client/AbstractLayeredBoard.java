package com.codenjoy.dojo.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.CharElements;
import com.codenjoy.dojo.services.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

public abstract class AbstractLayeredBoard<E extends CharElements> {
    protected int size;
    protected char[][][] field;

    public AbstractLayeredBoard forString(String boardString) {
        if (boardString.indexOf("layer") != -1) {
            JSONObject source = new JSONObject(boardString);
            JSONArray layers = source.getJSONArray("layers");

            return forString(layers.getString(0), layers.getString(1));
        } else {
            return forString(new String[]{boardString});
        }
    }

    public AbstractLayeredBoard forString(String... layers) {
        String board = layers[0].replaceAll("\n", "");
        size = (int) Math.sqrt(board.length());
        field = new char[layers.length][size][size];

        for (int i = 0; i < layers.length; ++i) {
            board = layers[i].replaceAll("\n", "");

            char[] temp = board.toCharArray();
            for (int y = 0; y < size; y++) {
                int dy = y * size;
                for (int x = 0; x < size; x++) {
                    field[i][x][y] = temp[dy + x];
                }
            }
        }

        return this;
    }

    public abstract E valueOf(char ch);

    public int size() {
        return size;
    }

    // TODO подумать над этим, а то оно так долго все делается
    public static Set<Point> removeDuplicates(Collection<Point> all) {
        Set<Point> result = new TreeSet<Point>();
        for (Point point : all) {
            result.add(point);
        }
        return result;
    }

    public List<Point> get(int numLayer, E... elements) {
        List<Point> result = new LinkedList<Point>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (E element : elements) {
                    if (valueOf(field[numLayer][x][y]).equals(element)) {
                        result.add(pt(x, y));
                    }
                }
            }
        }
        return result;
    }

    public boolean isAt(int numLayer, int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return getAt(numLayer, x, y).equals(element);
    }

    public E getAt(int numLayer, int x, int y) {
        return valueOf(field[numLayer][x][y]);
    }

    public String boardAsString(int numLayer) {
        StringBuffer result = new StringBuffer();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                result.append(field[numLayer][x][y]);
            }
            result.append("\n");
        }
        return result.toString();
    }

    public int countLayers() {
        return field.length;
    }

    @Override
    public String toString() {
        String result = "Board:";
        for (int i = 0; i < countLayers(); i++) {
            result += "\n" + boardAsString(i);
        }
        return result;
    }

    public boolean isAt(int numLayer, int x, int y, E... elements) {
        for (E c : elements) {
            if (isAt(numLayer, x, y, c)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNear(int numLayer, int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return false;
        }
        return isAt(numLayer, x + 1, y, element) || isAt(numLayer, x - 1, y, element) || isAt(numLayer, x, y + 1, element) || isAt(numLayer, x, y - 1, element);
    }

    public int countNear(int numLayer, int x, int y, E element) {
        if (pt(x, y).isOutOf(size)) {
            return 0;
        }
        int count = 0;
        if (isAt(numLayer, x - 1, y, element)) count++;
        if (isAt(numLayer, x + 1, y, element)) count++;
        if (isAt(numLayer, x, y - 1, element)) count++;
        if (isAt(numLayer, x, y + 1, element)) count++;
        return count;
    }

    public List<E> getNear(int numLayer, int x, int y) {
        List<E> result = new LinkedList<E>();

        int radius = 1;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (pt(x + dx, y + dy).isOutOf(size)) {
                    continue;
                }
                result.add(getAt(numLayer, x + dx, y + dy));
            }
        }

        return result;
    }

    public boolean isOutOfField(int x, int y) {
        return pt(x, y).isOutOf(size);
    }

    public void set(int numLayer, int x, int y, char ch) {
        field[numLayer][x][y] = ch;
    }

    public char[][] getField(int numLayer) {
        return field[numLayer];
    }
}