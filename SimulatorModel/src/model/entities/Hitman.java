/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import java.util.Map;
import model.Cell;
import model.Cells;
import model.Coordinates;
import model.Tuple;

/**
 *
 * @author gillesbraun
 */
public class Hitman extends Entity {
    private static final int VIEW_RADIUS = 4;

    @Override
    public Coordinates doStep(Cells cells, int r, int c) {
        Tuple<Coordinates, Cell> closestCellWithEntity = 
                cells.getClosestCellWithEntityNotOfType(
                        VIEW_RADIUS, 
                        new Coordinates(r, c), 
                        this.getClass());
        
        if (closestCellWithEntity != null) {
            Coordinates coords = closestCellWithEntity.x;
            int x = coords.col - c;
            int y = coords.row - r;
            if(x > 0 && y > 0) { // move down right
                c+=1;
                r+=1;
            } else if(x > 0 && y < 0) { // move up right
                c+=1;
                r-=1;
            } else if(x < 0 && y > 0) { // move down left
                r+=1;
                c-=1;
            } else if(x < 0 && y < 0) { // move up left
                r-=1;
                c-=1;
            } else {
                if(x == 0) {
                    if(y > 0) { // straight down
                        r+=2;
                    } else { // up
                        r-=2;
                    }
                } 
                
                if(y == 0) {
                    if(x > 0) { // straight right
                        c+=2;
                    } else { // left
                        c-=2;
                    }
                }
                
            }            
        }
        else {
            c+=2;
            r+=2;
        }
        return new Coordinates(r, c);
    }

    @Override
    public Entity duplicate() {
        return new Hitman();
    }

    @Override
    public int getKillPriority() {
        return 5;
    }
    
}
