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

/**
 *
 * @author gillesbraun
 */
public class Hitman extends Entity {
    private static final int VIEW_RADIUS = 4;

    @Override
    public Coordinates doStep(Cells cells, int r, int c) {
        Map<Coordinates, Cell> cellsWithEntities = cells.getCellsWithEntities(VIEW_RADIUS, r, c);
        for (Map.Entry<Coordinates, Cell> entry : cellsWithEntities.entrySet()) {
            Coordinates coords = entry.getKey();
            int x = coords.getCol() - c;
            int y = coords.getRow() - r;
            if(x > 0 && y > 0) { // move down right
                c+=1;
                r+=1;
                System.out.println("down right");
            } else if(x > 0 && y < 0) { // move up right
                c+=1;
                r-=1;
                System.out.println("up right");
            } else if(x < 0 && y > 0) { // move down left
                r+=1;
                c-=1;
                System.out.println("down left");
            } else if(x < 0 && y < 0) { // move up left
                r-=1;
                c-=1;
                System.out.println("up left");
            } else {
                if(x == 0) {
                    if(y > 0) { // straight down
                        r+=2;
                        System.out.println("staright down");
                    } else {
                        r-=2;
                        System.out.println("staright up");
                    }
                } 
                
                if(y == 0) {
                    if(x > 0) { // straight right
                        c+=2;
                        System.out.println("staright right");
                    } else {
                        c-=2;
                        System.out.println("staright left");
                    }
                }
                
            }            
        }
        if(cellsWithEntities.isEmpty()) {
            return new Coordinates(r+2, c+2);
        }
        return new Coordinates(r, c);
    }

    @Override
    public String toString() {
        return "Hitman";
    }

    @Override
    public Entity duplicate() {
        return new Hitman();
    }
    
}
