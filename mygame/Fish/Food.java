/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.Fish;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.sun.javafx.geom.Vec3f;

/**
 *
 * @author jonas
 */
public class Food {

    public int amount;
    public Spatial model;

    public Food(int amount, Spatial model) {
        this.amount = amount;
        this.model = model;
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

}
