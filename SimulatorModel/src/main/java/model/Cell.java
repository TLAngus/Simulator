/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import model.entities.Entity;

/**
 *
 * @author gillesbraun
 */
public class Cell implements Iterable<Entity> {
    private ArrayList<Entity> entities = new ArrayList<>();
    
    /**
     * Removes all entities from this cell except the one passed as parameter.
     * @param e 
     */
    public void removeEntitiesExcept(Entity e) {
        entities = new ArrayList<>();
        entities.add(e);
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
        return "no entity";
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
        entities.removeAll(Collections.singleton(null));
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
