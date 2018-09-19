/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.neurons;

import java.util.ArrayList;
import mygame.brain.FishBrain;
import mygame.brain.NeuronConnection;

/**
 *
 * @author jonas
 */
public class Neuron {

    private FishBrain brain;

    public ArrayList<NeuronConnection> inputNeurons;
    protected NeuronFunction function;
    private double output;

    //TODO klasse zum automatischen einfügen der Gewichte
    //TODO oder Klasse mit weights und Neurons (InputClass)
    public Neuron(ArrayList<Neuron> inputNeurons) {
        this.inputNeurons = new ArrayList<>();
        if (inputNeurons != null) {
            for (Neuron inputNeuron : inputNeurons) {
                this.inputNeurons.add(new NeuronConnection(inputNeuron));
            }
        } else {
            this.inputNeurons = new ArrayList<>();
        }
    }

    public void activationFunction() {
        double sum = 0;
        int count = 0;
        for (NeuronConnection connection : getInputNeurons()) {
            sum += connection.getActivatity();
        }
        setOutput(sum);
    }

    public void backPropagation(double target) {
        if (getInputNeurons() != null) {
            for (NeuronConnection connection : getInputNeurons()) {
                connection.backPropagation(target);
                connection.adaptConnection(-1.0 * (getOutput() - target));
            }
        }

    }

    public void removeConnection(int index) {
        if (getInputNeurons().size() > 0 && getInputNeurons().size() > index && getInputNeurons().get(index) != null) {
            inputNeurons.remove(index);
        }
    }

    public void connect(Neuron neuron) {
        if (!getInputNeurons().contains(neuron)) {
            inputNeurons.add(new NeuronConnection(neuron));
        }
    }

    /**
     * @return the brain
     */
    public FishBrain getBrain() {
        return brain;
    }

    /**
     * @param brain the brain to set
     */
    public void setBrain(FishBrain brain) {
        this.brain = brain;
    }

    /**
     * @return the inputNeurons
     */
    public ArrayList<NeuronConnection> getInputNeurons() {
        return inputNeurons;
    }

    /**
     * @param inputNeurons the inputNeurons to set
     */
    public void setInputNeurons(ArrayList<NeuronConnection> inputNeurons) {
        this.inputNeurons = inputNeurons;
    }

    /**
     * @return the function
     */
    public NeuronFunction getFunction() {
        return function;
    }

    /**
     * @param function the function to set
     */
    public void setFunction(NeuronFunction function) {
        this.function = function;
    }

    /**
     * @return the output
     */
    public double getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(double output) {
        this.output = output;
    }
    

}
