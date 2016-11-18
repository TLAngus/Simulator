/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author gillesbraun
 */
public class Coordinates {
    public final int row, col;

    public Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    @Override
    public String toString() {
        return row+", "+col;
    }
    
    
}
