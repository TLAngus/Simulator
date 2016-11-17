/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import java.util.Map;
import model.Cell;
import model.Cells;
import model.Coordinates;
import model.entities.Entity;
import model.entities.Hitman;
import model.entities.Person;

/**
 *
 * @author gillesbraun
 */
public class Simulator {
    private Cells readState, writeState;

    public Simulator(int rows, int cols) {
        writeState = new Cells(rows, cols);
        readState = new Cells(rows, cols);
        
        setEntity(new Person("Person"), 4, 5);
        setEntity(new Hitman(), 2, 3);
    }

    public Map<Coordinates, Cell> getCellsInRadius(int radius, int r, int c) {
        return readState.getCellsInRadius(radius, r, c);
    }

    public Map<Coordinates, Cell> getCellsWithEntities(int radius, int r, int c) {
        return readState.getCellsWithEntities(radius, r, c);
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
                    // do step and save coordinates of new entity position
                    Coordinates newEntityPos = cell.doEntityStep(readState, r, c);
                    // duplicate entity for safety
                    Entity entity = cell.getEntity().duplicate();
                    
                    Cell newCell = getCell(newEntityPos.getRow(), newEntityPos.getCol()).duplicate();
                    newCell.setEntity(entity);
                    writeState.setCell(newCell, newEntityPos.getRow(), newEntityPos.getCol());
                }
            }
        }
        
        commit();
    }    
    
    public void setEntity(Entity e, int r, int c) {
        Cell cell = readState.getCell(r, c).duplicate();
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
