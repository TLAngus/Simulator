/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import model.Cells;
import model.Coordinates;

/**
 *
 * @author gillesbraun
 */
public class Person extends Entity {
    
    @Override
    public Coordinates doStep(Cells cells, int r, int c) {
        return new Coordinates(r, c+1);
    }

    @Override
    public String toString() {
        return "Person";
    }

    @Override
    public Entity duplicate() {
        return new Person();
    }

    @Override
    public int getKillPriority() {
        return 0;
    }
    
}
