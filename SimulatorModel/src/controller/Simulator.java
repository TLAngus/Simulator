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
 * This is Simulator class which handles everything except drawing.
 * @author gillesbraun
 */
public class Simulator {
    private Cells readState, writeState;

    /**
     * Creates a new Game with the specified amount of rows and columns.
     * @param rows
     * @param cols 
     */
    public Simulator(int rows, int cols) {
        writeState = new Cells(rows, cols);
        readState = new Cells(rows, cols);
    }

    /**
     * Creates a new Simulator instance which copies from the given Cells object
     * @param c Cells which are used to copy
     */
    public Simulator(Cells c) {
        readState = c;
        writeState = new Cells(c.getRows(), c.getCols());
    }

    /**
     * Factory method which creates a new Simulator from the given JSON
     * @param json JSON String representing the Simulator
     * @return Simulator
     */
    public static Simulator fromJson(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Entity.class, new EntityAdapter());
        Gson gson = gsonBuilder.create();
        return new Simulator(gson.fromJson(json, Cells.class));
    }

    /**
     * Adds an entity to the game.
     * @param e
     * @param c 
     */
    public void addEntity(Entity e, Coordinates c) {
        getCell(c).add(e);
    }

    /**
     * Returns a JSON representation of the Simulator instance.
     * @return JSON
     */
    public String getJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Entity.class, new EntityAdapter());
        Gson gson = gsonBuilder.create();
        return gson.toJson(readState);
    }

    /**
     * Saves to file.
     * @param file
     * @throws IOException 
     */
    public void saveToFile(String file) throws IOException {
        String json = getJson();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.print(json);
        }
    }

    /**
     * Loads from file. Does not create a new instance, but overwrites the current.
     * @param file String of the file path.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void loadFromFile(String file) throws FileNotFoundException, IOException {
        String l = null, json = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((l = br.readLine()) != null) {
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

    /**
     * Kills the entities that are supposed to be dead.
     */
    private void commit() {
        readState = writeState;
        writeState = new Cells(writeState.getRows(), writeState.getCols());

        for (Tuple<Coordinates, Cell> tuple : readState) {
            Cell cell = tuple.y;
            Entity notToBeKilled = null;
            for (Entity current : cell) {
                // check if any entity should be killed.
                if (notToBeKilled == null || notToBeKilled.getKillPriority() < current.getKillPriority()) {
                    notToBeKilled = current;
                }
            }
            cell.removeEntitiesExcept(notToBeKilled);
        }
    }

    public Iterator<Tuple<Coordinates, Cell>> iterator() {
        return readState.iterator();
    }

    /**
     * Do a full simulation cycle. Each entity moves.
     */
    public void doSimulationCycle() {
        for (Tuple<Coordinates, Cell> tuple : readState) {
            Coordinates coords = tuple.x;
            Cell cell = tuple.y;
            
            for (Entity curr : cell) {
                if (curr != null) {
                    Coordinates doStep = curr.doStep(readState, coords.row, coords.col);
                    moveEntity(curr, doStep);
                    //cell.remove(curr);
                }
            }
        }

        commit();
    }
    
    /**
     * Move an entity from one position to another if there is no entity in the To cell.
     * @param from Coordinates From
     * @param to Coordinates To
     */
    public void changeEntityPosition(Coordinates from, Coordinates to) {
        Cell fromCell = getCell(from);
        Cell toCell = getCell(to);
        if(fromCell.hasEntity() && !toCell.hasEntity()) {
            toCell.add(fromCell.getEntity());
            fromCell.clear();
        }
    }

    /**
     * Moves an entity to a new cell. Possible to add more than one entity.
     * When there are more than one entity in a cell, commit() checks which one survives.
     * @param e
     * @param c 
     */
    private void moveEntity(Entity e, Coordinates c) {
        Cell cell = writeState.getCell(c);
        cell.add(e);
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
