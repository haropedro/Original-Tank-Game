package TankGame;

import TankGame.GameObject.Movable.*;
import TankGame.GameObject.Unmovable.*;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javax.swing.JFrame;

/**
 *
 * @author monal,motiveg
 */
public class TankWorld implements Runnable {

    // Do resource initialization in init() methods further down //

    // JFrame properties //
    private String frame_title;
    private int frame_width, frame_height;
    private int map_width, map_height;

    // Resource paths //
    private String background_path;
    private String wall_path;
    private String breakablewall_path;
    private String health_path;
    private String life_path;
    private String lifeIcon1_path;
    private String lifeIcon2_path;
    private String tank1_path, tank2_path;
    private String projectile_path;
    private String img_paths[];
    private String music_path;
    private String sound_paths[];

    // Map properties //
    private final int NUM_ROWS = 25, NUM_COLS = 25;
    private int[][] mapLayout;

    // Swing parts //
    private Scene scene;

    // Observable //
    private final GameObservable gobs;

    // Active fields //
    private Thread thread;
    private boolean running = false;

    // Game objects //
    private ArrayList<Wall> walls;
    private ArrayList<BreakableWall> bwalls;
    private ArrayList<PowerUp> pups;
    private ArrayList<Projectile> bullets;

    // Player stuff //
    private static Tank tank1;
    private static Tank tank2;
    private KeyInput keyinput1;
    private KeyInput keyinput2;

    // Sound player //
    private GameSounds music;
    private ArrayList<GameSounds> soundplayer;

    // Game window //
    private JFrame frame;

    public static void main(String args[]) {
        TankWorld tankworld = new TankWorld();
        tankworld.start();
    }

    public TankWorld() {
        this.gobs = new GameObservable();
    }

    @Override
    public void run() {
        init();
        try {
        while(running) {
            this.gobs.setChanged();
            this.gobs.notifyObservers();
            tick();
            render();

            Thread.sleep(1000/144);
        }
        } catch (InterruptedException e) {
            Logger.getLogger(TankWorld.class.getName()).log(Level.SEVERE, null, e);
        }

        stop();
    }

    private void tick() {
        System.out.print("Tank 1 --------\t");
        tank1.printTankData();
        System.out.print("Tank 2 --------\t");
        tank2.printTankData();

        this.scene.setProjectiles(bullets);
        System.out.println(bullets.size()+" bullets");
    }

    private void render() {
        this.scene.repaint();
    }

    public synchronized void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // initialize resources and world properties
        initWorldProperties();
        initResourcePaths();

        // initialize scene things here
        this.scene = new Scene(map_width, map_height, frame_width, frame_height,
                                background_path, img_paths);
        setupMap();
        setupPlayers();
        setupSounds();

        // initialize game frame
        setupFrame();
    }

    private void initWorldProperties() {
        this.frame_title = "Tank Game";
        this.frame_width = 800;
        this.frame_height = 822;
        this.map_width = 1600;
        this.map_height = 1600;
    }

    private void initResourcePaths() {
        // IMAGES
        background_path = "Resources/Background.bmp";
        wall_path = "Resources/Wall1.gif";
        breakablewall_path = "Resources/Wall2.gif";
        health_path = "Resources/Health_nontransparent.png";
        life_path = "Resources/Life.gif";
        lifeIcon1_path = "Resources/Life2_p1.gif";
        lifeIcon2_path = "Resources/Life2_p2.gif";
        tank1_path = "Resources/Tank1edit.gif";
        tank2_path = "Resources/Tank2edit.gif";
        projectile_path = "Resources/Shelledit2.gif";
        // 0: empty, 1: empty, 2: wall, 3: breakable wall, 4: health, 5: life, 6: projectile
        img_paths = new String[] {tank1_path, tank2_path, wall_path, breakablewall_path, health_path, life_path, projectile_path};

        // SOUNDS
        music_path = "Resources/Music.wav";
        sound_paths = new String[] {"Resources/Explosion_small.wav", "Resources/Explosion_large.wav", "Resources/unbreakable.wav", "Resources/breakable.wav"};
    }

    public void setupMap() {
        setMapLayout();
        createMapObjects();
    }

    // 0 = empty, 1 = ?, 2 = wall, 3 = breakable wall, 4 = health, 5 = life
    private void setMapLayout() {
        this.mapLayout = new int[][]
        { // current size: 25x25
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, // 0
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 1
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 2
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 3
            {2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2}, // 4
            {2, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 2}, // 5
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 6
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 7
            {2, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 2}, // 8
            {2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2}, // 9
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 10
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 11
            {2, 2, 2, 2, 2, 0, 0, 0, 2, 2, 3, 3, 4, 3, 3, 2, 2, 0, 0, 0, 2, 2, 2, 2, 2}, // 12
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 13
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 14
            {2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2}, // 15
            {2, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 2}, // 16
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 17
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 18
            {2, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 2}, // 19
            {2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2}, // 20
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 21
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 22
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, // 23
            {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, // 24
        };
    }

    private void createMapObjects() {
        walls = new ArrayList<>();
        bwalls = new ArrayList<>();
        pups  = new ArrayList<>();
        BufferedImage objectImage;
        int cell_size = 64;
        int extra = 32;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                // Wall
                if (this.mapLayout[row][col] == 2) {
                    objectImage = setImage(img_paths[2]);
                    walls.add(new Wall(col*cell_size, row*cell_size,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    walls.add(new Wall((col*cell_size)+extra, row*cell_size,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    walls.add(new Wall(col*cell_size, (row*cell_size)+extra,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    walls.add(new Wall((col*cell_size)+extra, (row*cell_size)+extra,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                }
                // Breakable Wall
                if (this.mapLayout[row][col] == 3) {
                    objectImage = setImage(img_paths[3]);
                    bwalls.add(new BreakableWall(col*cell_size, row*cell_size,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    bwalls.add(new BreakableWall((col*cell_size)+extra, row*cell_size,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    bwalls.add(new BreakableWall(col*cell_size, (row*cell_size)+extra,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    bwalls.add(new BreakableWall((col*cell_size)+extra, (row*cell_size)+extra,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                }
                // Health
                // TODO: this is currently a generic powerup
                // NOTE: current setup only has one powerup in the center;
                //       change this function if the map layout is changed
                if (this.mapLayout[row][col] == 4) {
                    objectImage = setImage(img_paths[4]);
                    //pups.add(new PowerUp(col*cell_size, row*cell_size,
                    //        objectImage.getWidth(), objectImage.getHeight(), objectImage));
                    // this is the temporary set up; remove this if a new layout is used
                        pups.add(new PowerUp((col*cell_size)+(extra/2), (row*cell_size)+(extra/2),
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                }
                // Life
                /*
                if (this.mapLayout[row][col] == 5) {
                    objectImage = setImage(img_paths[5]);
                    pups.add(new PowerUp(col*cell_size, row*cell_size,
                            objectImage.getWidth(), objectImage.getHeight(), objectImage));
                }
                */
            }
        } // end loops

        // add each object to the Observable
        walls.forEach((curr) -> {
            this.gobs.addObserver(curr);
        });
        bwalls.forEach((curr) -> {
            this.gobs.addObserver(curr);
        });
        pups.forEach((curr) -> {
            this.gobs.addObserver(curr);
        });

        // add each object to the Scene
        this.scene.setMapObjects(this.walls, this.bwalls, this.pups);
    }

    private void setupPlayers() {
        BufferedImage t1img = setImage(img_paths[0]);
        BufferedImage t2img = setImage(img_paths[1]);

        int tank1_x = 100,
            tank1_y = 100,
            tank2_x = 1500,
            tank2_y = 1500,
            tank2_angle = 180, // face tank2 inwards
            tank_speed = 2;

        tank1 = new Tank(this, t1img, tank1_x, tank1_y, tank_speed,
                KeyEvent.VK_A, KeyEvent.VK_D,
                KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_SPACE);

        tank2 = new Tank(this, t2img, tank2_x, tank2_y, tank_speed,
                KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER);
        tank2.setAngle(tank2_angle); // face tank2 inwards

        // other tank has to be set after instantions
        tank1.setOtherTank(tank2);
        tank2.setOtherTank(tank1);

        // connect key inputs with tanks
        this.keyinput1 = new KeyInput(tank1);
        this.keyinput2 = new KeyInput(tank2);

        // add tanks to observer list
        gobs.addObserver(tank1);
        gobs.addObserver(tank2);

        this.scene.setTanks(tank1, tank2);

        // instantiate bullets list
        this.bullets = new ArrayList<>();

        // set life icons
        this.scene.setLifeIcons(setImage(this.lifeIcon1_path), setImage(this.lifeIcon2_path));
    }

    private void setupSounds() {
        // music; for looped sound, it will play once instantiated
        this.music = new GameSounds(1, this.music_path);

        // game sounds
        GameSounds small_explosion = new GameSounds(2, this.sound_paths[0]);
        GameSounds large_explosion = new GameSounds(2, this.sound_paths[1]);
        GameSounds unbreakable_hit = new GameSounds(2, this.sound_paths[2]);
        GameSounds breakable_hit = new GameSounds(2, this.sound_paths[3]);
        this.soundplayer = new ArrayList<>();
        this.soundplayer.add(small_explosion); // 0
        this.soundplayer.add(large_explosion); // 1
        this.soundplayer.add(unbreakable_hit); // 2
        this.soundplayer.add(breakable_hit); // 3
    }

    private void setupFrame() {
        frame = new JFrame();

        // GAME WINDOW
        this.frame.setTitle(frame_title);
        this.frame.setSize(frame_width, frame_height);
        this.frame.setPreferredSize(new Dimension(frame_width, frame_height));
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this.scene);
        this.frame.setLocationRelativeTo(null);

        this.frame.addKeyListener(keyinput1);
        this.frame.addKeyListener(keyinput2);

        // finalize the frame
        this.frame.pack();
        this.frame.setVisible(true);
    }

    public void addBulletToObservable(Projectile p) {
        this.gobs.addObserver(p);
    }

    // SETTERS //
    private BufferedImage setImage(String filepath) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            System.out.println("Error getting image. " + e.getMessage());
        }
        return img;
    }

    // GETTERS //
    public TankWorld getTankWorld() {
        return this;
    }

    public static Tank getTank(int tankNumber) {
        switch (tankNumber) {
            case 1:
                return tank1;
            case 2:
                return tank2;
            default:
                System.out.println("Tank not found!");
                return null;
        }
    }

    public static ArrayList<Tank> getTanks() {
        ArrayList<Tank> tanks = new ArrayList<>();
        tanks.add(tank1);
        tanks.add(tank2);
        return tanks;
    }

    public ArrayList<Wall> getWalls() {
        return this.walls;
    }

    public int getWallSize(){
        return walls.size();
    }

    public ArrayList<BreakableWall> getBreakableWalls() {
        return this.bwalls;
    }

    public int getBreakableWallSize() {
        return bwalls.size();
    }

    public ArrayList<PowerUp> getPowerUps() {
        return this.pups;
    }

    public ArrayList<Projectile> getProjectile() {
        return bullets;
    }

    public BufferedImage getProjectileImg(){
        BufferedImage projectile = setImage(img_paths[6]);
        return projectile;
    }

    public int getWindowWidth () {
        return this.frame_width;
    }

    public int getWindowHeight() {
        return this.frame_height;
    }

    public int getMapWidth() {
        return this.map_width;
    }

    public int getMapHeight() {
        return this.map_height;
    }

    public GameSounds getSound(int sound_number) {
        return this.soundplayer.get(sound_number);
    }

    public void playSound(int sound_number) {
        if (sound_number >= 0 && sound_number <= 3)
            this.soundplayer.get(sound_number).play();
    }

}
