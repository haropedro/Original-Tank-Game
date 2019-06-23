package TankGame.GameObject.Unmovable;

import TankGame.GameObject.Movable.Tank;
import TankGame.TankWorld;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;


public class Wall extends Unmovable implements Observer {
    Rectangle wallRect;
    private int height, width;

    public Wall(int x, int y, int width, int height, BufferedImage img) {
        super(x, y, width, height, img);
        this.height = img.getHeight();
        this.width = img.getWidth();
        wallRect = new Rectangle(x,y,width,height);
    }

    public void draw(Graphics g) {
        g.drawImage(this.img, this.x, this.y, this);
    }

    @Override
    public void update(Observable o, Object arg) {
        update();
    }

    public Rectangle getWallRectangle() {
        return wallRect;
    }

    public void update(){
        Tank p1 = TankWorld.getTank(1);
        Tank p2 = TankWorld.getTank(2);

        if (p1.collision(this)) {
            if (p1.x > (x)) {
                p1.x += 3;
            } else if (p1.x < (this.x)) {
                p1.x -= 3;
            }
            if (p1.y > (this.y)) {
                p1.y += 3;
            } else if (p1.y < this.y) {
                p1.y -= 3;
            }
        }
        if (p2.collision(this)) {
            if (p2.x > (x)) {
                p2.x += 3;
            } else if (p2.x < (this.x)) {
                p2.x -= 3;
            }
            if (p2.y > (this.y)) {
                p2.y += 3;
            } else if (p2.y < this.y) {
                p2.y -= 3;
            }
        }

    }


}
