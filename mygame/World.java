/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import mygame.Fish.Fish;
import mygame.Fish.Food;
import mygame.brain.FishBrain;

/**
 *
 * @author jonas
 */
public class World {
//Fish/Fish2.j3o

    public static final String FISH_MODEL_PATH = "Fish/Fish2.j3o";
    public static final String FOOD_MODEL_PATH = "Fish/FishFood_1.j3o";

    public ArrayList<Fish> fish;
    public ArrayList<Food> food;
    Node rootNode;
    AssetManager assetManager;

    int brainAdaptionCount;

    public World(Node rootNode, AssetManager assetManager) {
        fish = new ArrayList<>();
        food = new ArrayList<>();
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        brainAdaptionCount = 0;
    }

    public void nextStep() {
        int i = 0;
        boolean adaptBrain = false;
        if (brainAdaptionCount++ > 50) {
            brainAdaptionCount = 0;
            adaptBrain = true;
        }
        for (Fish fish1 : fish) {
            if (adaptBrain) {
                //System.out.println(i++);
                fish1.adaptBrain();
            }
            fish1.nextStep();
            //  System.out.println((i++) + "Fish " + fish1.hunger);
        }
        // respawnFish();
        reproduce();
        removeDead();
    }

    public void respawnFish() {
        for (Fish fish1 : fish) {
            if (!fish1.isAlive()) {
                //  System.out.println("mygame.World.respawnFish()");
                fish1.setPosition((float) ((Math.random() * 200) - 100),
                        (float) ((Math.random() * 100)),
                        (float) ((Math.random() * 200) - 100));
                fish1.hunger = 0;
                fish1.reproduce = 0;
                fish1.avgHunger = 100;
            }
        }
    }

    public void spawnFish(Vector3f position) {
        Spatial fishModel = assetManager.loadModel(FISH_MODEL_PATH);
        rootNode.attachChild(fishModel);
        if (position == null) {
            fishModel.setLocalTranslation((float) ((Math.random() * 400) - 200),
                    (float) ((Math.random() * 100)),
                    (float) ((Math.random() * 400) - 200));
        } else {
            fishModel.setLocalTranslation(position);
        }
        Fish newFish = new Fish(fishModel, this);
        fish.add(newFish);
    }

    public void spawnFood() {
        Spatial foodModel = assetManager.loadModel(FOOD_MODEL_PATH);
        rootNode.attachChild(foodModel);
        foodModel.setLocalTranslation((float) ((Math.random() * 200) - 100),
                (float) ((Math.random() * 100)),
                (float) ((Math.random() * 200) - 100));
        Food newFood = new Food(200, foodModel);
        food.add(newFood);
    }

    public void removeFish(Fish remove) {
        fish.remove(remove);
    }

    public void removeFish(int remove) {
        fish.remove(remove);
    }

    public void removeFood(Food nextFood) {
        nextFood.amount = 200;
        nextFood.setPosition((float) ((Math.random() * 200) - 100),
                (float) ((Math.random() * 100)),
                (float) ((Math.random() * 200) - 100));
    }

    public void spawnFish(Fish thisFish, Fish nextFish) {
        Fish newFish0 = thisFish.getCopy();
        Fish newFish1 = nextFish.getCopy();
        //newFish.brain = mergeFish(thisFish.brain, nextFish.brain);
        // newFish.brain = mergeFish(thisFish.brain, nextFish.brain);
        rootNode.attachChild(newFish0.getModel());
        rootNode.attachChild(newFish1.getModel());
        fish.add(newFish0);
        fish.add(newFish1);
        spawnFish(thisFish.getPosition());
    }

    public FishBrain mergeFish(FishBrain brain0, FishBrain brain1) {
        FishBrain brainCopy0 = brain0.getBrainCopy();
        FishBrain newBrain = new FishBrain(brain0, brain1);
        newBrain.connectNeurons();
        return newBrain;
    }

    private void reproduce() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < fish.size(); i++) {
            if (fish.get(i).reproduce()) {
                indices.add(i);
            }
        }
        for (Integer index : indices) {
            spawnFish(fish.get(index), fish.get(index).getNextFish());
            // spawnFish(fish.get(index).getPosition());
            fish.get(index).reproduce = 0;
        }

    }

    private void removeDead() {
        boolean someOneDead = true;
        while (someOneDead) {
            int index = 0;
            someOneDead = false;
            for (int i = 0; i < fish.size(); i++) {
                if (!fish.get(i).isAlive()) {
                    index = i;
                    someOneDead = true;
                    break;
                }
            }
            if (someOneDead) {
                fish.get(index).setPosition((float) 0, (float) -5, (float) 0);
                rootNode.detachChild(fish.get(index).getModel());
                boolean removed = fish.remove(fish.get(index));
                if (removed) {
                    System.out.println("mygame.World.removeDead()");
                }
            }
        }
        while (fish.size() < 20) {
            spawnFish(null);

        }
    }

}
