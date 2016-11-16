/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.entities.Entity;

/**
 *
 * @author gillesbraun
 */
public class Cell {
    private Entity entity = null;

    public Entity getEntity() {
        return entity;
    }
    
    public boolean hasEntity() {
        return entity != null;
    }

    public Coordinates doEntityStep(Cells cells, int row, int col) {
        if (entity != null) {
            Coordinates coords = entity.doStep(cells, row, col);
            return coords;
        }
        return null;
    }
    
    /**
     * Used for creating a copy of an entity
     * @param e Entity to be returned as a copy
     * @return Copy of Entity
     */
    private Entity returnEntity(Entity e) {
        return e;
    }

    @Override
    public String toString() {
        if(entity != null)
            return "Entity: " + entity.toString();
        return "";
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
