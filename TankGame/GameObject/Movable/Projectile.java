package TankGame.GameObject.Movable;

import TankGame.TankWorld;
import TankGame.GameObject.Unmovable.BreakableWall;
import TankGame.GameObject.Unmovable.Wall;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

public class Projectile extends Movable implements Observer {

    private final Tank p1 = TankWorld.getTank(1);
    private final Tank p2 = TankWorld.getTank(2);
    private final BufferedImage bullet;
    private int theta;
    private int damage;
    private TankWorld obj;
    public int xSize;
    public int ySize;
    public static Tank currentTank;
    public boolean visible;

    public Projectile(TankWorld tw, BufferedImage img, int speed, Tank t, int dmg) {
        super(img, t.getTankCenterX(), t.getTankCenterY(), speed);
        bullet = img;
        damage = dmg;
        xSize = img.getWidth(null);
        ySize = img.getHeight(null);
        currentTank = t;
        theta = currentTank.getAngle();
        visible = true;

        this.obj = tw;
    }

    public void setTankWorld(TankWorld tw) {
        this.obj = tw;
    }

    public static Tank getTank() {
        return currentTank;
    }

    @Override
    public void update(Observable o, Object arg) {
        update();
    }

    public void draw(ImageObserver iobs, Graphics2D g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(theta), 0, 0);
        g.drawImage(bullet, rotation, iobs);
    }

    public int getTheta() {
        return this.theta;
    }

    @Override
    public boolean isVisible () {
        return this.visible;
    }

    //@Override
    public void update() {
        y += Math.round(speed * Math.sin(Math.toRadians(theta)));
        x += Math.round(speed * Math.cos(Math.toRadians(theta)));

        if (p1.collision(this) && visible && currentTank != p1 && visible && p1.coolDown <= 0) {
            if (visible) {
                obj.playSound(3);// breakable collision sound
                obj.getSound(3).getClip().setFramePosition(0);
            }
            visible = false;
            p1.bulletDamage(damage);
        } else if (p2.collision(this) && visible && currentTank != p2 && visible && p2.coolDown <= 0) {
            if (visible) {
                obj.playSound(3);// breakable collision sound
                obj.getSound(3).getClip().setFramePosition(0);
            }
            visible = false;
            p2.bulletDamage(damage);
        } else
            for (int i = 0; i < obj.getWallSize(); i++){
            Wall tempWall = obj.getWalls().get(i);
            if ((tempWall.getWallRectangle().intersects(this.x, this.y, this.width, this.height)) && visible) {
                this.visible = false;
                obj.playSound(2);// unbreakable collision sound
                obj.getSound(2).getClip().setFramePosition(0);
            }

            for (int j = 0; j < obj.getBreakableWallSize(); j++){
            BreakableWall tempWall2 = obj.getBreakableWalls().get(j);
            if((tempWall2.getWallRectangle().intersects(this.x, this.y, this.width, this.height)) && visible){
                obj.getBreakableWalls().remove(j);
                tempWall2.breakWall();
                this.visible = false;
                obj.playSound(3);// breakable collision sound
                obj.getSound(3).getClip().setFramePosition(0);
            }
            }
        }
    }
}
