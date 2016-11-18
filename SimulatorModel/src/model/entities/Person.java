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
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Coordinates doStep(Cells cells, int r, int c) {
        return new Coordinates(r, c + 1);
    }

    @Override
    public String toString() {
        return "Person: "+ name;
    }

    @Override
    public Entity duplicate() {
        return new Person(name);
    }

    @Override
    public int getKillPriority() {
        return 0;
    }
    
}
