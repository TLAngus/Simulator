
import controller.Simulator;
import model.entities.Person;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gillesbraun
 */
public class Main {
    public static void main(String[] args) {
        Simulator simulator = new Simulator(10, 10);
        simulator.setEntity(new Person("Hasib"), 0, 0);
        
        System.out.println(simulator);
        
        simulator.doSimulationCycle();
        System.out.println(simulator);
        
        simulator.doSimulationCycle();
        System.out.println(simulator);
    }
}
