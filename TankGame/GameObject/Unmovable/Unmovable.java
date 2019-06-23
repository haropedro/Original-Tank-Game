package TankGame.GameObject.Unmovable;

import TankGame.GameObject.GameObject;

import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;


public class Unmovable extends GameObject {

    public Unmovable() {}

    public Unmovable(BufferedImage img, ImageObserver observer) {
        super(img, observer);
    }

    public Unmovable(int x, int y, BufferedImage img, ImageObserver observer) {
        super(x, y, img, observer);
    }

    public Unmovable(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, width, height, img);
    }

  }
