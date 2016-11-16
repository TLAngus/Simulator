/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulator;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import model.Cell;
import model.entities.Entity;
import model.entities.Person;

/**
 *
 * @author gillesbraun
 */
public class DrawController {
    public static void draw(Simulator sim, Graphics g, int height, int width) {
        int rows = sim.getRows();
        int cols = sim.getCols();
        
        double sideOfCell = Math.min(width / cols, height / rows);
                
        double xOffset = (width - sideOfCell * cols) / 2;
        double yOffset = (height - sideOfCell * rows) / 2;
        
        drawGrid(g, height, width, rows, cols);
        
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                Cell cell = sim.getCell(row, col);
                Entity e = cell.getEntity();
                int xPos = (int)(xOffset + col * sideOfCell);
                int yPos = (int)(yOffset + row * sideOfCell);
                drawEntity(e, g, xPos, yPos, (int)sideOfCell);
            }
        }
    }
    
    private static void drawEntity(Entity e, Graphics g, int x, int y, int sideOfCell) {
        if(e instanceof Person) {
            Person person = (Person)e;
            Polygon polygon = new Polygon();
            polygon.addPoint(x, y + sideOfCell);
            polygon.addPoint(x + sideOfCell / 2, y + sideOfCell / 2);
            polygon.addPoint(x + sideOfCell, y + sideOfCell);
            g.setColor(Color.RED);
            g.fillPolygon(polygon);
            
            g.fillOval(x + sideOfCell / 4, y + sideOfCell / 4, sideOfCell / 2, sideOfCell / 2);
        }
    }
    
    private static void drawGrid(Graphics g, int height, int width, int rows, int cols) {
        double sideOfCell = Math.min(width / cols, height / rows);
                
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
