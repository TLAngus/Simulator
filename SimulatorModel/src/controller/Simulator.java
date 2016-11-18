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
import model.Tuple;
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
        setEntity(new Person("Person"), 9, 6);
        setEntity(new Person("Person"), 5, 8);
        setEntity(new Hitman(), 2, 3);
        setEntity(new Hitman(), 7, 8);
        setEntity(new Hitman(), 8, 4);
        commit();
    }

    public Tuple<Coordinates, Cell> getClosestCellWithEntity(int radius, Coordinates pos) {
        return readState.getClosestCellWithEntity(radius, pos);
    }

    public Tuple<Coordinates, Cell> getClosestCellWithEntityNotOfType(int radius, Coordinates pos, Class excludeType) {
        return readState.getClosestCellWithEntityNotOfType(radius, pos, excludeType);
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
                if(cell != null && cell.hasEntity() && !cell.getEntity().isDead()) {
                    // do step and save coordinates of new entity position
                    Coordinates newEntityPos = cell.doEntityStep(readState, r, c);
                    // duplicate entity to destroy reference
                    Entity entity = cell.getEntity().duplicate();
                    
                    moveEntity(entity, newEntityPos);
                }
            }
        }
        
        commit();
    }
    
    private void moveEntity(Entity e, Coordinates newPos) {
        Cell cell = getCell(newPos.row, newPos.col).duplicate();
        
        // get entity that is at the position where the new one is moved to
        Entity entityBefore = cell.getEntity();
        if(entityBefore != null) {
            int before = entityBefore.getKillPriority();
            int moveTo = e.getKillPriority();
            if(moveTo > before) {
                setEntity(e, newPos.row, newPos.col);
            }
            entityBefore.setDead();
        } else {
            setEntity(e, newPos.row, newPos.col);
        }
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
