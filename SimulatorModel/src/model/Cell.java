/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Iterator;
import model.entities.Entity;

/**
 *
 * @author gillesbraun
 */
public class Cell implements Iterable<Entity> {
    private ArrayList<Entity> entities = new ArrayList<>();
    
    public Cell duplicate() {
        Cell cell = new Cell();
        cell.entities = entities;
        return cell;
    }
    
    public void removeEntitiesExcept(Entity e) {
        entities = new ArrayList<>();
        entities.add(e);
    }

    public void doEntityStep(Cells cells, int row, int col) {
        for (Entity e : entities) {
            e.doStep(cells, row, col);
        }
    }
    
    public Entity getEntity() {
        // returns the first entity or null
        return entities.size() > 0 ? entities.get(0) : null;
    }
    
    public boolean hasEntity() {
        return entities.size() > 0 && entities.get(0) != null;
    }

    @Override
    public String toString() {
        if(hasEntity())
            return "Entity: " + getEntity().toString();
        return "";
    }

    public int size() {
        return entities.size();
    }

    public boolean isEmpty() {
        return entities.isEmpty();
    }

    public boolean contains(Object o) {
        return entities.contains(o);
    }

    public int indexOf(Object o) {
        return entities.indexOf(o);
    }

    public Entity get(int index) {
        return entities.get(index);
    }

    public boolean add(Entity e) {
        return entities.add(e);
    }

    public boolean remove(Object o) {
        return entities.remove(o);
    }

    public void clear() {
        entities.clear();
    }

    public Iterator<Entity> iterator() {
        return entities.iterator();
    }
}
