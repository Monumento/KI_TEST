/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.brain;

import mygame.neurons.Neuron;

/**
 *
 * @author jonas
 */
public class NeuronConnection {

    private Neuron neuron;
    private double weight;
    private double rank;

    public NeuronConnection(Neuron neuron) {
        this.neuron = neuron;
        this.weight = (Math.random() / 0.1) - 0.05;
        this.rank = 0;
    }

    public void backPropagation(double target) {
        neuron.backPropagation(target);
    }

    public void adaptConnection(double value) {
        this.weight += value;
    }

    public double getActivatity() {
        return neuron.getOutput() * weight;
    }

    /**
     * @return the neuron
     */
    public Neuron getNeuron() {
        return neuron;
    }

    /**
     * @param neuron the neuron to set
     */
    public void setNeuron(Neuron neuron) {
        this.neuron = neuron;
    }

    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * @return the rank
     */
    public double getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(double rank) {
        this.rank = rank;
    }

}
