/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.neurons;

import java.util.ArrayList;
import mygame.neurons.functions.InvertFunction;

/**
 *
 * @author jonas
 */
public class InvertNeuron extends Neuron {
    
    public InvertNeuron(ArrayList<Neuron> inputNeurons) {
        super(inputNeurons);
        function = new InvertFunction();
    }
    
}
