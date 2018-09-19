/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.neurons;

/**
 *
 * @author jonas
 */
public class LinearFunction extends NeuronFunction {

    double scalar, constant;

    public LinearFunction(double scalar, double constant) {
        this.scalar = scalar;
        this.constant = constant;
    }

    @Override
    public double activationFunction(double x) {
        return scalar * x + constant;
    }

}
