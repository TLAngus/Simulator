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
        if(Math.random() >= 0.5) {
            c++;
        }
        r++;
        return new Coordinates(r, c);
    }

    @Override
    public String toString() {
        return "Person: "+ name;
    }
    
}
