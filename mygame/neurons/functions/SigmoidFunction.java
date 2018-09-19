/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.neurons.functions;

import mygame.neurons.NeuronFunction;

/**
 *
 * @author jonas
 */
public class SigmoidFunction extends NeuronFunction{

    @Override
    public double activationFunction(double x) {
        return (1.0/(1.0+Math.exp(-x)));
    }
    
}
