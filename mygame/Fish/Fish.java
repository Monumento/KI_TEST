/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Fish;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Queue;
import mygame.World;
import mygame.brain.FishBrain;

/**
 *
 * @author jonas
 */
public class Fish {

    World world;
    Spatial model;
    public FishBrain brain;
    public double hunger;
    public double reproduce;
    public static final int FEED_SPEED = 10;
    public double avgHunger;
    public int age;

    public Fish(Spatial fishModel, World world) {
        brain = new FishBrain();
        brain.createNeurons();
        this.model = fishModel;
        this.world = world;
        hunger = 0;
        reproduce = 0;
        avgHunger = 100;
        age = 0;
    }

    public void adaptBrain() {
        if (isAlive()) {
            avgHunger += avgHunger + hunger / 2;
            if (avgHunger > 200) {
                brain.connectNeurons();
            } else {
                System.out.println("mygame.Fish.Fish.adaptBrain()");
            }
        }
    }

    public boolean isAlive() {
        return (hunger <= 10000 && age < 100000);
    }

    public void aging() {
        hunger++;
        reproduce++;
        age++;
    }

    public void nextStep() {
        if (isAlive()) {
            aging();
            Vector3f target = getNextFood().getPosition();
            if (reproduce > hunger && reproduce > 1000) {
                target = getNextFish().getPosition();
            }
            //TODO target = 0, backpropagation
            brain.nextStep(model.getLocalTranslation(), target);
            double[] nextStep = brain.getNextStep();
            for (int i = 0; i < nextStep.length; i++) {
                //System.out.println("next step for " + i + " " + nextStep[i]);
            }
            // System.out.println((float) nextStep[0]);
            model.setLocalTranslation(model.getLocalTranslation().add(
                    (float) ((-nextStep[0] + 0.5)),
                    (float) ((-nextStep[1] + 0.5)) //0
                    ,
                     (float) ((-nextStep[2] + 0.5))
            //0
            ));
            //brain.backPorpagation(model.getLocalTranslation(), target);
            eat();

        }
    }

    public boolean reproduce() {
        return (hunger < reproduce && reproduce > 1000 && getNextFishDistance() < 1);
    }

    public void eat() {
        if (getNextFoodDistance() < 1 && getNextFood().amount > 0) {
            getNextFood().amount -= FEED_SPEED;
            hunger -= FEED_SPEED;
        }
        if (getNextFood().amount <= 0) {
            world.removeFood(getNextFood());
        }
    }

    public Vector3f getPosition() {
        return model.getLocalTranslation();
    }

    public void setPosition(Vector3f position) {
        model.setLocalTranslation(position);
    }

    public void setPosition(Float x, Float y, Float z) {
        model.setLocalTranslation(x, y, z);
    }

    public Food getNextFood() {
        Vector3f position = getPosition();
        Vector3f foodPosition = world.food.get(0).getPosition();
        double distance = position.distance(foodPosition);
        double minDist = distance;
        int index = 0;
        for (int i = 0; i < world.food.size(); i++) {
            foodPosition = world.food.get(i).getPosition();
            distance = position.distance(foodPosition);
            if (distance < minDist) {
                index = i;
                minDist = distance;
            }
        }
        return world.food.get(index);
    }

    public double getNextFoodDistance() {
        Vector3f position = getPosition();
        Vector3f foodPosition = world.food.get(0).getPosition();
        double distance = position.distance(foodPosition);
        double minDist = distance;
        for (int i = 0; i < world.food.size(); i++) {
            foodPosition = world.food.get(i).getPosition();
            distance = position.distance(foodPosition);
            if (distance < minDist) {
                minDist = distance;
            }
        }
        return minDist;
    }

    public Fish getNextFish() {
        Vector3f position = getPosition();
        Vector3f fishPosition = world.fish.get(0).getPosition();
        double distance = position.distance(fishPosition);
        double minDist = distance;
        int index = 0;
        for (int i = 0; i < world.fish.size(); i++) {
            fishPosition = world.fish.get(i).getPosition();
            distance = position.distance(fishPosition);
            if (distance < minDist) {
                index = i;
                minDist = distance;
            }
        }
        return world.fish.get(index);
    }

    public double getNextFishDistance() {
        Vector3f position = getPosition();
        Vector3f foodPosition = world.fish.get(0).getPosition();
        double distance = position.distance(foodPosition);
        double minDist = distance;
        for (int i = 0; i < world.fish.size(); i++) {
            foodPosition = world.fish.get(i).getPosition();
            distance = position.distance(foodPosition);
            if (distance < minDist) {
                minDist = distance;
            }
        }
        return minDist;
    }

    public Fish getCopy() {
        Fish fishCopy = new Fish(model.clone(), world);
        fishCopy.brain = brain.getBrainCopy();
        return fishCopy;
    }

    public Spatial getModel() {
        return model;
    }

}
