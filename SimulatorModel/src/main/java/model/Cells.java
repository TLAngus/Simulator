/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author gillesbraun
 */
public class Cells implements Iterable<Tuple<Coordinates, Cell>> {

    private Cell[][] cells;

    /**
     * Create a new Cells object with the given rows and columns.
     * @param rows
     * @param cols 
     */
    public Cells(int rows, int cols) {
        cells = new Cell[rows][cols];
        createCells(rows, cols);
    }

    /**
     * Fill the array.
     * @param rows
     * @param cols 
     */
    private void createCells(int rows, int cols) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                setCell(new Cell(), new Coordinates(r, c));
            }
        }
    }

    public void setCell(Cell cell, Coordinates co) {
        co = normalizeCoordinates(co);
        cells[co.row][co.col] = cell;
    }

    public Cell getCell(Coordinates co) {
        co = normalizeCoordinates(co);
        return cells[co.row][co.col];
    }
    
    /**
     * Normalize the coordinates. So every number that is input is translated
     * to real coordinates that exist in the game.
     * @param co Coordinates that may be wrong
     * @return Coordinates that always exist
     */
    public Coordinates normalizeCoordinates(Coordinates co) {
        int r = co.row,
            c = co.col;
        while (r < 0) {
            r += getRows();
        } 

        while (c < 0) {
            c += getCols();
        }
        return new Coordinates(r % getRows(), c % getCols());
    }

    /**
     * Returns a map of all Cells that are in radius (circle) from a specified point.
     * @param radius Radius in cells
     * @param r Row
     * @param c Col
     * @return Map with the cells
     */
    public Map<Coordinates, Cell> getCellsInRadius(int radius, int r, int c) {
        HashMap<Coordinates, Cell> list = new HashMap<>();

        // dont look at every cell, only those where may be possible
        int startR = r - radius;
        int endR = r + radius;

        int startC = c - radius;
        int endC = c + radius;

        for (int row = startR; row <= endR; row++) {
            for (int col = startC; col <= endC; col++) {
                if (Math.pow(col - c, 2) + Math.pow(row - r, 2) < Math.pow(radius, 2) && !(r == row && c == col)) {
                    Coordinates coordinates = new Coordinates(row, col);
                    list.put(normalizeCoordinates(coordinates), getCell(coordinates));
                }
            }
        }
        return list;
    }

    /**
     * Returns a Map with Cells that have entities that are in radius from a point.
     * @param radius Radius
     * @param r Row
     * @param c Col
     * @return Cells that have entities
     */
    public Map<Coordinates, Cell> getCellsWithEntities(int radius, int r, int c) {
        Map<Coordinates, Cell> cells = getCellsInRadius(radius, r, c);
        Map<Coordinates, Cell> withEntities = new HashMap<>();
        for (Map.Entry<Coordinates, Cell> entry : cells.entrySet()) {
            if (entry.getValue().hasEntity()) {
                withEntities.put(entry.getKey(), entry.getValue());
            }
        }
        return withEntities;
    }

    /**
     * Get the closest cell from a position that has an entity.
     * @param radius Radius
     * @param pos Coordinates position
     * @return Cell
     */
    public Tuple<Coordinates, Cell> getClosestCellWithEntity(int radius, Coordinates pos) {
        return getClosestCellWithEntityNotOfType(radius, pos, null);
    }

    /**
     * Get the closest cell from a position that has an entity that is
     * not an instance of the specified Class.
     * @param radius Radius
     * @param pos Coordinates position
     * @param excludeType Class of entity which should be ignored
     * @return Cell
     */
    public Tuple<Coordinates, Cell> getClosestCellWithEntityNotOfType(int radius, Coordinates pos, Class excludeType) {
        Map<Coordinates, Cell> cellsWithEntities = getCellsWithEntities(radius, pos.row, pos.col);
        Tuple<Coordinates, Cell> closest = null;
        Double closestDistance = null;

        for (Map.Entry<Coordinates, Cell> entry : cellsWithEntities.entrySet()) {
            Coordinates currentPos = entry.getKey();
            Cell currentCell = entry.getValue();

            // Calculate distance with pythagoras
            int xDiff = Math.abs(pos.col - currentPos.col);
            int yDiff = Math.abs(pos.row - currentPos.row);
            double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
            boolean isSameType = currentCell.getEntity().getClass().equals(excludeType);
            if ((closestDistance == null || distance < closestDistance)) {
                // when excludetype is not set, ignore it.
                // if it is set, set closest only if it is of a different type
                if (!(excludeType != null && isSameType)) {
                    closestDistance = distance;
                    closest = new Tuple(currentPos, currentCell);
                }

            }
        }
        return closest;
    }

    @Override
    public Iterator<Tuple<Coordinates, Cell>> iterator() {
        return new Iterator<Tuple<Coordinates, Cell>>() {
            private int r = 0, c = -1;

            @Override
            public boolean hasNext() {
                if (++c >= getCols()) {
                    c = 0;
                    if (++r >= getRows()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public Tuple<Coordinates, Cell> next() {
                Coordinates coordinates = new Coordinates(r, c);
                return new Tuple(coordinates, getCell(coordinates));
            }
        };
    }

    public int getRows() {
        return cells.length;
    }

    public int getCols() {
        return cells[0].length;
    }

}
