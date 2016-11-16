/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Cell;
import model.Cells;
import model.Coordinates;
import model.entities.Entity;

/**
 *
 * @author gillesbraun
 */
public class Simulator {
    private Cells readState, writeState;

    public Simulator(int rows, int cols) {
        writeState = new Cells(rows, cols);
        commit();
        readState = new Cells(rows, cols);
    }
    
    private void commit() {
        readState = writeState;
        writeState = new Cells(writeState.getRows(), writeState.getCols());
    }
        
    public void doSimulationCycle() {
        int cols = readState.getCols();
        int rows = readState.getRows();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = readState.getCell(r, c);
                if(cell != null && cell.hasEntity()) {
                    Coordinates newEntityPos = cell.doEntityStep(readState, r, c);
                    Entity entity = cell.takeEntity();
                    Cell cell1 = new Cell();
                    cell1.setEntity(entity);
                    writeState.setCell(cell1, newEntityPos.getRow(), newEntityPos.getCol());
                }
            }
        }
        
        commit();
    }
    
    
    
    public void setEntity(Entity e, int r, int c) {
        Cell cell = readState.getCell(r, c);
        if(cell == null) {
            cell = new Cell();
        }
        cell.setEntity(e);
        writeState.setCell(cell, r, c);
    }

    @Override
    public String toString() {
        return readState.toString();
    }
    
    public int getRows() {
        return readState.getRows();
    }

    public int getCols() {
        return readState.getCols();
    }

    public Cell getCell(int r, int c) {
        return readState.getCell(r, c);
    }
}
