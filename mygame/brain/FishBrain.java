/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.brain;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.neurons.LinearFunction;
import mygame.neurons.Neuron;
import mygame.neurons.NeuronFunction;
import mygame.neurons.ReLuNeuron;
import mygame.neurons.SigmoidBasicNeuron;
import mygame.neurons.functions.InvertFunction;
import mygame.neurons.functions.ReLuFunction;
import mygame.neurons.functions.SigmoidFunction;

/**
 *
 * @author jonas
 */
public class FishBrain {

    //input neurons (distance to next food source)
    //output neurons control direction
    //TODO backprop/backperculation
    private ArrayList<Neuron> inputNeurons;
    //TODO many hidden layers
    private ArrayList<Neuron> neurons;
    private ArrayList<Neuron> outputNeurons;

    public FishBrain() {
        neurons = new ArrayList<>();
        inputNeurons = new ArrayList<>();
        outputNeurons = new ArrayList<>();

    }

    public FishBrain(FishBrain brain0, FishBrain brain1) {
        neurons = new ArrayList<>();
        inputNeurons = new ArrayList<>();
        outputNeurons = new ArrayList<>();

        inputNeurons.add(new Neuron(null));
        inputNeurons.add(new Neuron(null));
        inputNeurons.add(new Neuron(null));

        neurons.add(new SigmoidBasicNeuron(null));
        neurons.add(new SigmoidBasicNeuron(null));
        neurons.add(new SigmoidBasicNeuron(null));

        outputNeurons.add(new SigmoidBasicNeuron(null));
        outputNeurons.add(new SigmoidBasicNeuron(null));
        outputNeurons.add(new SigmoidBasicNeuron(null));

        for (int i = 0; i < neurons.size(); i++) {
            neurons.get(i).connect(inputNeurons.get(i));
        }

        for (int i = 0; i < outputNeurons.size(); i++) {
            outputNeurons.get(i).connect(neurons.get(i));
        }

    }

    public void createNeurons() {
        //input
        getInputNeurons().add(new Neuron(null));
        getInputNeurons().add(new Neuron(null));
        getInputNeurons().add(new Neuron(null));

        getNeurons().add(new SigmoidBasicNeuron(null));
        getNeurons().add(new SigmoidBasicNeuron(null));
        getNeurons().add(new SigmoidBasicNeuron(null));

        getOutputNeurons().add(new SigmoidBasicNeuron(null));
        getOutputNeurons().add(new SigmoidBasicNeuron(null));
        getOutputNeurons().add(new SigmoidBasicNeuron(null));

        for (int i = 0; i < getNeurons().size(); i++) {
            getNeurons().get(i).connect(getInputNeurons().get(i));
        }

        for (int i = 0; i < getOutputNeurons().size(); i++) {
            getOutputNeurons().get(i).connect(getNeurons().get(i));
        }
    }

    public void connectNeurons() {
        //  System.out.println("Neurons");
        Neuron hidden = getNeurons().get(getRndIndex(getNeurons().size()));
        Neuron out = getOutputNeurons().get(getRndIndex(getOutputNeurons().size()));
        if (Math.random() < 0.5) {
            if (Math.random() < 0.5) {
                hidden.connect(getInputNeurons().get(getRndIndex(getInputNeurons().size())));
            } else if (Math.random() < 0.99) {
                hidden.removeConnection(getRndIndex(hidden.getInputNeurons().size()));
            } else {
                hidden.setFunction(getFunction(getRndIndex(4)));
            }
        } else {
            if (Math.random() < 0.5) {
                out.connect(getNeurons().get(getRndIndex(getNeurons().size())));
            } else if (Math.random() < 0.99) {
                out.removeConnection(getRndIndex(out.getInputNeurons().size()));
            } else {
                hidden.setFunction(getFunction(getRndIndex(4)));
            }
        }

    }

    public NeuronFunction getFunction(int index) {
        System.out.println("getFunction" + index);
        switch (index) {
            case 0:
                return new InvertFunction();
            case 1:
                return new ReLuFunction();
            case 2:
                return new SigmoidFunction();
            case 3:
                System.out.println("Linear");
                return new LinearFunction(1,Math.random()-0.5);
        }
        return new SigmoidFunction();
    }

    public int getRndIndex(int max) {
        int ranmdom = (int) (Math.random() * (max));
        return ranmdom;
    }

    public void nextStep(Vector3f positionFish, Vector3f positionFood) {

        double distance[] = getDistance(positionFish, positionFood);

        int counter = 0;

        for (Neuron neuron : getInputNeurons()) {
            //   System.out.println("distance für " + counter + " ist " + distance[counter]);
            neuron.setOutput(distance[counter++]);

        }
        for (Neuron neuron : getNeurons()) {
            neuron.activationFunction();
            //System.out.println("distance für " + counter + " ist " + distance[counter]);
            //neuron.setOutput(distance[counter++]);
        }
        for (Neuron neuron : getOutputNeurons()) {
            neuron.activationFunction();
        }
    }

    public double[] getNextStep() {
        double[] sum = new double[getOutputNeurons().size()];
        int counter = 0;
        for (Neuron neuron : getOutputNeurons()) {
            sum[counter++] = neuron.getOutput();
        }
        return sum;
    }

    public void backPorpagation(Vector3f positionFish, Vector3f positionFood) {
        double distance[] = getDistance(positionFish, positionFood);
        int counter = 0;
        //output neuronen
        for (Neuron neuron : getOutputNeurons()) {
            //   System.out.println("neuron" +counter);
            double target = 0;
            if (distance[counter] > 0.1) {
                target = 0;
            } else if (distance[counter] < -0.1) {
                target = 1;
            }
            counter++;
            neuron.backPropagation(target);
        }
    }

    public double[] getDistance(Vector3f positionFish, Vector3f positionFood) {
        //get distance Food
        float foodX = positionFood.getX();
        float foodY = positionFood.getY();
        float foodZ = positionFood.getZ();
        //get distance Fish
        float fishX = positionFish.getX();
        float fishY = positionFish.getY();
        float fishZ = positionFish.getZ();

        double distance[] = new double[3];
        distance[0] = fishX - foodX;
        distance[1] = fishY - foodY;
        distance[2] = fishZ - foodZ;
        return distance;
    }

    public void oldCode2() {
        //input
        getInputNeurons().add(new Neuron(null));
        getInputNeurons().add(new Neuron(null));
        getInputNeurons().add(new Neuron(null));

        //hidden
        ArrayList<Neuron> inputs = new ArrayList<>();
        inputs.add(getInputNeurons().get(0));
        Neuron hiddenNeuron = new SigmoidBasicNeuron(inputs);
        getNeurons().add(hiddenNeuron);

        inputs = new ArrayList<>();
        inputs.add(getInputNeurons().get(0));
        inputs.add(getInputNeurons().get(1));
        inputs.add(getInputNeurons().get(2));
        hiddenNeuron = new SigmoidBasicNeuron(inputs);
        getNeurons().add(hiddenNeuron);

        inputs = new ArrayList<>();
        inputs.add(getInputNeurons().get(2));
        inputs.add(getInputNeurons().get(0));
        hiddenNeuron = new SigmoidBasicNeuron(inputs);
        getNeurons().add(hiddenNeuron);

        //output
        inputs = new ArrayList<>();
        inputs.add(getNeurons().get(0));
        Neuron outputNeuron = new SigmoidBasicNeuron(inputs);
        getOutputNeurons().add(outputNeuron);

        inputs = new ArrayList<>();
        inputs.add(getNeurons().get(0));
        inputs.add(getNeurons().get(1));
        inputs.add(getNeurons().get(2));
        outputNeuron = new SigmoidBasicNeuron(inputs);
        getOutputNeurons().add(outputNeuron);

        inputs = new ArrayList<>();
        inputs.add(getNeurons().get(0));
        inputs.add(getNeurons().get(2));
        outputNeuron = new SigmoidBasicNeuron(inputs);
        getOutputNeurons().add(outputNeuron);
    }

    /**
     * @return the inputNeurons
     */
    public ArrayList<Neuron> getInputNeurons() {
        return inputNeurons;
    }

    /**
     * @return the neurons
     */
    public ArrayList<Neuron> getNeurons() {
        return neurons;
    }

    /**
     * @return the outputNeurons
     */
    public ArrayList<Neuron> getOutputNeurons() {
        return outputNeurons;
    }

    /**
     * @param inputNeurons the inputNeurons to set
     */
    public void setInputNeurons(ArrayList<Neuron> inputNeurons) {
        this.inputNeurons = inputNeurons;
    }

    /**
     * @param neurons the neurons to set
     */
    public void setNeurons(ArrayList<Neuron> neurons) {
        this.neurons = neurons;
    }

    /**
     * @param outputNeurons the outputNeurons to set
     */
    public void setOutputNeurons(ArrayList<Neuron> outputNeurons) {
        this.outputNeurons = outputNeurons;
    }

    public FishBrain getBrainCopy() {
       FishBrain brainCopy = new FishBrain();
       brainCopy.inputNeurons = (ArrayList < Neuron >)inputNeurons.clone();
       brainCopy.neurons = (ArrayList < Neuron >)neurons.clone();
       brainCopy.outputNeurons = (ArrayList < Neuron >)outputNeurons.clone();
       return brainCopy;
    }

}
