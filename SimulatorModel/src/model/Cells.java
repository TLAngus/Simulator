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
    
    public Cells(int rows, int cols) {
        cells = new Cell[rows][cols];
        createCells(rows, cols);
    }
    
    private void createCells(int rows, int cols) {
        for(int c = 0; c < rows; c++) {
            for(int r = 0; r < cols; r++) {
                setCell(new Cell(), c, r);
            }
        }
    }

    public int getRows() {
        return cells.length;
    }
    
    public int getCols() {
        return cells[0].length;
    }
    
    public Cell getCell(int r, int c) {
        do {
            r+= getRows();
        } while(r < 0);
                
        do {
            c+= getCols();
        } while(c < 0);
        
        return cells[r % getRows()][c % getCols()];
    }
    
    public void setCell(Cell cell, int r, int c) {
        do {
            r+= getRows();
        } while(r < 0);
                
        do {
            c+= getCols();
        } while(c < 0);
        cells[r % getRows()][c % getCols()] = cell;
    }

    @Override
    public String toString() {
        String ret = "";
        for(int row = 0; row < getRows(); row++) {
            for(int col = 0; col < getCols(); col++) {
                Cell cell = getCell(row, col);
                if(cell != null) {
                    ret += "[" + cell.toString() + " ("+row+";"+col+")], ";
                }
            }
            ret += "\n";
        }
        ret = ret.substring(0, ret.length()-2);
        return ret.trim();
    }

    public Map<Coordinates, Cell> getCellsInRadius(int radius, int r, int c) {
        HashMap<Coordinates, Cell> list = new HashMap<>();
        
        // dont look at every cell, only those where may be possible
        int startR = r - radius;
        int endR = r + radius;
        
        int startC = c - radius;
        int endC = c + radius;
        
        for(int row = startR; row <= endR; row++) {
            for(int col = startC; col <= endC; col++) {
                if(Math.pow(col - c, 2) + Math.pow(row - r, 2) < Math.pow(radius, 2) && !(r == row && c == col)) {
                    list.put(new Coordinates(row, col), getCell(row, col));
                }
            }
        }
        return list;
    }
    
    public Map<Coordinates, Cell> getCellsWithEntities(int radius, int r, int c) {
        Map<Coordinates, Cell> cells = getCellsInRadius(radius, r, c);
        Map<Coordinates, Cell> withEntities = new HashMap<>();
        for (Map.Entry<Coordinates, Cell> entry : cells.entrySet()) {
            if(entry.getValue().hasEntity()) {
                withEntities.put(entry.getKey(), entry.getValue());
            }
        }
        return withEntities;
    }
    
    public Tuple<Coordinates, Cell> getClosestCellWithEntity(int radius, Coordinates pos) {
        return getClosestCellWithEntityNotOfType(radius, pos, null);
    }
    
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
            if((closestDistance == null || distance < closestDistance)) {
                // when excludetype is not set, ignore it.
                // if it is set, set closest only if it is of a different type
                if(!(excludeType != null && isSameType)) {
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
            private int r = 0, c = 0;
            
            @Override
            public boolean hasNext() {
                if(++c >= getCols()) {
                    c = 0;
                    if(++r >= getRows()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public Tuple<Coordinates, Cell> next() {
                return new Tuple(new Coordinates(r, c), getCell(r, c));
            }
        };
    }
}
