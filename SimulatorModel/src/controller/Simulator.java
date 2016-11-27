/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import model.Cell;
import model.Cells;
import model.Coordinates;
import model.Tuple;
import model.entities.Entity;
import model.entities.EntityAdapter;
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
        
        setEntity(new Person("Person"), new Coordinates(5, 4));
        setEntity(new Person("Person"), new Coordinates(5, 5));
        setEntity(new Hitman(), new Coordinates(0, 2));
        commit();
    }
    
    public void addEntity(Entity e, Coordinates c) {
        getCell(c).add(e);
    }
    
    public void saveToFile(String file) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Entity.class, new EntityAdapter());
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(readState);
        
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.print(json);
        }
    }
    
    public void loadFromFile(String file) throws FileNotFoundException, IOException {
        String l = null, json = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        while((l = br.readLine()) != null) {
            json += l;
        }
        br.close();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Entity.class, new EntityAdapter());
        Gson gson = gsonBuilder.create();
        readState = gson.fromJson(json, Cells.class);
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
        
        for (Tuple<Coordinates, Cell> tuple : readState) {
            Entity notToBeKilled = null;
            for(Iterator<Entity> iterator = tuple.y.iterator(); iterator.hasNext();) {
                Entity current = iterator.next();
                // check if any entity should be killed.
                if(notToBeKilled == null || notToBeKilled.getKillPriority() < current.getKillPriority()) {
                    notToBeKilled = current;
                }
            }
            tuple.y.removeEntitiesExcept(notToBeKilled);
        }
    }

    public Iterator<Tuple<Coordinates, Cell>> iterator() {
        return readState.iterator();
    }
        
    public void doSimulationCycle() {
        int cols = readState.getCols();
        int rows = readState.getRows();
        
        for (Tuple<Coordinates, Cell> tuple : readState) {
            Coordinates coords = tuple.x;
            
            Cell cell = readState.getCell(coords);
            for(Iterator<Entity> iterator = cell.iterator(); iterator.hasNext();) {
                Entity curr = iterator.next();
                if(curr != null) {
                    Coordinates doStep = curr.doStep(readState, coords.row, coords.col);
                    moveEntity(curr, doStep);
                    //cell.remove(curr);
                }
            }                
        }
        
        commit();
    }
    
    private void moveEntity(Entity e, Coordinates c) {
        Cell cell = writeState.getCell(c);
        cell.add(e);
    }
    
    public void setEntity(Entity e, Coordinates c) {
        Cell cell = readState.getCell(c).duplicate();
        cell.add(e);
        writeState.setCell(cell, c);
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

    public Cell getCell(Coordinates c) {
        return readState.getCell(c);
    }
}
