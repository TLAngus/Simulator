/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import model.entities.Entity;


/**
 *
 * @author gillesbraun
 */
public class Cells {
    
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
}
