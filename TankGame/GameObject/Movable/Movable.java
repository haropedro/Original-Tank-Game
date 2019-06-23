package TankGame.GameObject.Movable;

import TankGame.GameObject.GameObject;
import java.awt.image.BufferedImage;


public class Movable extends GameObject {

    protected int speed;

    public Movable(){
    }

    public Movable(BufferedImage img, int x, int y, int speed){
        super(x, y, img, null);
        this.speed = speed;
    }

}
