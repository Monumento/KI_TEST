package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import org.lwjgl.opengl.Display;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.HashSet;
import mygame.Fish.Fish;
import mygame.brain.FishBrain;

/**
 * Version 0.1.2
 *
 * @author Jonas Weis
 */
public class Main extends SimpleApplication implements AnimEventListener {

    //popups
    double aktuelleZeit;
    static final int INTERVAL = 100;

    boolean naechsterZug = false;

    public Main app = this;

    //historie
    public BitmapText historie;
    public BitmapText displayedText;
    public String oldHistorie;
    public int historieCount = 0;
    public int textPosition = 0;
    public int width;
    public int height;

    private AnimChannel channel;
    //Spielen durch Klicks
    public Vector2f vonFeld;

    public boolean istSpielerSchwarz;
    public int naechsterZugCount;

    private int msgDisplayTime;

    //Table Scene
    private DirectionalLight sun;

    //AI
    public static World world;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    private long msgTime;

    @Override
    public void simpleInitApp() {
        Display.setResizable(true);
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(1000f);
        flyCam.setZoomSpeed(0);
        //sichtweite
        cam.setFrustumFar(9999);
        cam.setLocation(new Vector3f(0, 50, 50));
        //cam swap for player white/balck

        //Frontend Modell und Logik
        initMouseMovement();

        oldHistorie = "";
        width = this.settings.getWidth();
        height = this.settings.getHeight();

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        //Table Scene----------------------
        viewPort.setBackgroundColor(ColorRGBA.White);

        Spatial player = assetManager.loadModel("background/newScene.j3o");
        player.setLocalTranslation(0, -10, 0);
        rootNode.attachChild(player);
        //   TangentBinormalGenerator.generate(player); // for debug if textures black (normal mapped)

        CollisionShape test = new CapsuleCollisionShape();

        world = new World(rootNode, assetManager);
        for (int i = 0; i < 20; i++) {
            world.spawnFish(null);
        }
        for (int i = 0; i < 10; i++) {
            world.spawnFood();
        }

        getCamera().setFrustumFar(999);
        flyCam.setMoveSpeed(100);

        // Sun light
        sun = new DirectionalLight();
        rootNode.addLight(sun);

    }

    /**
     * Init. sykbox
     *
     * @param assetManager
     * @param rootNode
     */
    public void initSky(AssetManager assetManager, Node rootNode) {
        Texture westTex = assetManager.loadTexture("background/west.png");
        Texture eastTex = assetManager.loadTexture("background/east.png");
        Texture northTex = assetManager.loadTexture("background/north.png");
        Texture southTex = assetManager.loadTexture("background/south.png");
        Texture upTex = assetManager.loadTexture("background/top.png");
        Texture downTex = assetManager.loadTexture("background/down.png");
        final Vector3f normalScale = new Vector3f(1, 1, 1);

        Spatial skySpatial = SkyFactory.createSky(assetManager, westTex, eastTex, southTex, northTex, upTex, downTex, normalScale);

        rootNode.attachChild(skySpatial);
    }

    int countFails = 0, countSuccess = 0;
    double n = 200.0;
    long aktuelleZeit2 = 0;

    @Override
    public void simpleUpdate(float tpf) {
        if (System.currentTimeMillis() - aktuelleZeit >= 1) {
            world.nextStep();

            aktuelleZeit = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - aktuelleZeit2 >= 1000) {
            displayMSG("" + world.fish.size(), 1000);
            aktuelleZeit2 = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - msgTime >= msgDisplayTime) {
            if (guiNode.hasChild(displayedText)) {
                guiNode.detachChild(displayedText);
            }
        }

    }

    private void initMouseMovement() {
        inputManager.addMapping("Walk", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "Walk");
        inputManager.addMapping("LClick", new MouseButtonTrigger(0));
        inputManager.addMapping("ScrollUp", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("ScrollDown", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addListener(actionListener, "LClick");
        inputManager.addListener(analogListener, "ScrollUp", "ScrollDown");
    }

    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("ScrollUp")) {
                textPosition += 5;
            } else if (name.equals("ScrollDown")) {
                textPosition -= 5;
            }
            if (historie != null) {
                historie.setLocalTranslation(width - 180, 0.8f * height + textPosition, 0);
                guiNode.detachAllChildren();
                guiNode.attachChild(historie);
            }
        }
    };

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Walk") && !keyPressed) {
                if (!channel.getAnimationName().equals("Walk")) {
                    channel.setAnim("Walk", 0.50f);
                    channel.setLoopMode(LoopMode.Loop);
                }
            }
        }
    };

    public void displayMSG(String msg, int time) {
        if (guiNode.hasChild(displayedText)) {
            guiNode.detachChild(displayedText);
        }
        this.msgDisplayTime = time;
        displayedText = new BitmapText(guiFont, false);
        displayedText.setSize(guiFont.getCharSet().getRenderedSize());
        displayedText.setText(msg);
        displayedText.setLocalTranslation(5, height - 25, 0);

        guiNode.attachChild(displayedText);
        msgTime = System.currentTimeMillis();

    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Walk")) {
            channel.setAnim("stand", 0.50f);
            channel.setLoopMode(LoopMode.DontLoop);
            channel.setSpeed(1f);
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
