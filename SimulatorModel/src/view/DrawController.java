/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Simulator;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import model.Cell;
import model.Coordinates;
import model.Tuple;
import model.entities.Entity;
import model.entities.Hitman;
import model.entities.Person;

/**
 *
 * @author gillesbraun
 */
public class DrawController {

    private static int rows, cols, sideOfCell, xOffset, yOffset, width, height;
    private static MovingEntity movingEntity;

    public static void draw(Simulator sim, Graphics g, int pHeight, int pWidth) {
        rows = sim.getRows();
        cols = sim.getCols();

        width = pWidth;
        height = pHeight;

        sideOfCell = Math.min(width / cols, height / rows);

        xOffset = (width - sideOfCell * cols) / 2;
        yOffset = (height - sideOfCell * rows) / 2;

        drawGrid(g);

        for (Iterator<Tuple<Coordinates, Cell>> iterator = sim.iterator();
                iterator.hasNext();) {
            
            Tuple<Coordinates, Cell> next = iterator.next();
            Cell cell = next.y;
            int col = next.x.col;
            int row = next.x.row;

            if (cell.size() > 0) {
                Entity e = cell.get(0);
                int xPos = (int) (xOffset + col * sideOfCell);
                int yPos = (int) (yOffset + row * sideOfCell);
                drawEntity(e, g, xPos, yPos);
            }
        }
        if(movingEntity != null) {
           drawEntity(
                   movingEntity.entity, 
                   g, 
                   movingEntity.position.x - movingEntity.mouseOffset.x, 
                   movingEntity.position.y - movingEntity.mouseOffset.y,
                   false);
        }
    }

    public static Coordinates getCoordinatesForXY(Point p) {
        int r = (p.y - yOffset) / sideOfCell;
        int c = (p.x - xOffset) / sideOfCell;
        return new Coordinates(r, c);
    }

    private static void highlightCell(Graphics g, int x, int y) {
        g.setColor(Color.GREEN);
        g.drawRect(x, y, sideOfCell, sideOfCell);
    }

    private static void drawEntity(Entity e, Graphics g, int x, int y) {
        drawEntity(e, g, x, y, true);
    }
    private static void drawEntity(Entity e, Graphics g, int x, int y, boolean hideIfMoving) {
        g.setColor(Color.RED);
        if(hideIfMoving && movingEntity != null && e == movingEntity.entity) {
            return;
        }
        if (e instanceof Person) {
            Polygon polygon = new Polygon();
            polygon.addPoint(x, y + sideOfCell);
            polygon.addPoint(x + sideOfCell / 2, y + sideOfCell / 2);
            polygon.addPoint(x + sideOfCell, y + sideOfCell);
            g.fillPolygon(polygon);
            g.fillOval(x + sideOfCell / 4, y + sideOfCell / 4, sideOfCell / 2, sideOfCell / 2);
        } else if (e instanceof Hitman) {
            g.drawLine(x, y, x + sideOfCell, y + sideOfCell);
            g.drawLine(x, y + sideOfCell, x + sideOfCell, y);
        }
    }

    private static void drawGrid(Graphics g) {

        // Dont draw a grid when the distance between is too small
        if (sideOfCell < 5) {
            return;
        }

        g.setColor(Color.LIGHT_GRAY);

        for (int r = 0; r <= rows; r++) {
            g.drawLine((int) xOffset, (int) (yOffset + sideOfCell * r), (int) (width - xOffset), (int) (yOffset + sideOfCell * r));
            for (int c = 0; c <= cols; c++) {
                g.drawLine((int) (xOffset + sideOfCell * c), (int) yOffset, (int) (xOffset + sideOfCell * c), (int) (height - yOffset));
            }
        }

    }
    
    public static void startMovingEntity(Entity e, Point p) {
        int yMouseDiff = (p.y - yOffset) % sideOfCell;
        int xMouseDiff = (p.x - xOffset) % sideOfCell;
        
        movingEntity = new MovingEntity(e, new Point(xMouseDiff, yMouseDiff), p);
    }
    
    public static void moveMovingEntity(Point newPosition) {
        movingEntity.position = newPosition;
    }
    
    public static void stopMovingEntity() {
        movingEntity = null;
    }
    
    private static class MovingEntity {
        private Entity entity;
        private Point mouseOffset, position;

        public MovingEntity(Entity currentlyMoving, Point currentlyMovingOffset, Point currentlyMovingPos) {
            this.entity = currentlyMoving;
            this.mouseOffset = currentlyMovingOffset;
            this.position = currentlyMovingPos;
        }
    }
}
