/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


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
        return cells[r % getRows()][c % getCols()];
    }
    
    public void setCell(Cell cell, int r, int c) {
        cells[c % getCols()][r % getRows()] = cell;
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
    
    
    
    
}
