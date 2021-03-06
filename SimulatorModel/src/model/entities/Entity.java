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
public abstract class Entity {
    
    public abstract int getKillPriority();
    
    public abstract Entity duplicate();
    
    public abstract Coordinates doStep(Cells cells, int r, int c);
}
