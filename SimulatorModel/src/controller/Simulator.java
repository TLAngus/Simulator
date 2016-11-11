/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Cell;

/**
 *
 * @author gillesbraun
 */
public class Simulator {
    private Cell[][] cells;

    public Simulator(int cols, int rows) {
        cells = new Cell[cols][rows];
    }
}
