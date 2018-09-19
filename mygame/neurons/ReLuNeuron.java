/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.neurons;

import java.util.ArrayList;
import mygame.brain.NeuronConnection;
import mygame.neurons.functions.SigmoidFunction;

/**
 *
 * @author jonas
 */
public class ReLuNeuron extends Neuron{

    public ReLuNeuron(ArrayList<Neuron> inputNeurons){
        super(inputNeurons);
        function = new SigmoidFunction();
    } 
    
    public void setOutput(){
        
    }

    @Override
    public void activationFunction() {
        double sum = 0;
        for(NeuronConnection connection: getInputNeurons()){
            sum += connection.getActivatity();
        }
        setOutput(getFunction().activationFunction(sum));
    }
    

    
}
