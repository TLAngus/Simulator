/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulator;
import java.awt.Color;
import java.awt.Graphics;
import model.Cell;

/**
 *
 * @author gillesbraun
 */
public class DrawController {
    public static void draw(Simulator sim, Graphics g, int height, int width) {
        int rows = sim.getRows();
        int cols = sim.getCols();
        
        drawGrid(g, height, width, rows, cols);
        
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                Cell cell = sim.getCell(row, col);
                cell.getEntity();
            }
        }
    }
    
    private static void drawGrid(Graphics g, int height, int width, int rows, int cols) {
        double xSideOfCell = (double)width / cols;
        double ySideOfCell = (double)height / rows;
        
        double sideOfCell = Math.min(xSideOfCell, ySideOfCell);
                
        double xOffset = (width - sideOfCell * cols) / 2;
        double yOffset = (height - sideOfCell * rows) / 2;
        
        // Dont draw a grid when the distance between is too small
        if(sideOfCell < 5) {
            return;
        }
        
        g.setColor(Color.LIGHT_GRAY);
        
        for(int r = 0; r <= rows; r++) {
            g.drawLine((int)xOffset, (int)(yOffset + sideOfCell * r), (int)(width - xOffset), (int)(yOffset + sideOfCell * r));
            for(int c = 0; c <= cols; c++) {
                g.drawLine((int)(xOffset + sideOfCell * c), (int)yOffset, (int)(xOffset + sideOfCell * c), (int)(height-yOffset));
            }
        }
        
    }
}
