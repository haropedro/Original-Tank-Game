/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame3;

/**
 *
 * @author harop
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.io.IOException;

public class Tank extends JComponent implements Observer {

    private int x;
    private int y;
    private final int r = 6;
    private int vx;
    private int vy;
    private short angle;
    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    // private boolean shootKeyPressed ;

    public Tank(int x, int y, int vx, int vy, short angle) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;

    }



    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public void setAngle(short angle) {
        this.angle = angle;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    
    //It will set or toggle all the keys for keyboard presses.
    public void toggleUpPressed() {
        this.UpPressed = true;
    }

    public void toggleDownPressed() {
        this.DownPressed = true;
    }

    public void toggleRightPressed() {
        this.RightPressed = true;
    }

    public void toggleLeftPressed() {
        this.LeftPressed = true;
    }
  
// This will untoggle or unset all the keys for keyboard  presses.
    public void unToggleUpPressed() {
        this.UpPressed = false;
    }

    public void unToggleDownPressed() {
        this.DownPressed = false;
    }

    public void unToggleRightPressed() {
        this.RightPressed = false;
    }

    public void unToggleLeftPressed() {
        this.LeftPressed = false;
    }
    
  
  
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), img.getWidth() / 2, img.getHeight() / 2);
        Graphics2D graphic2D = (Graphics2D) g;
        graphic2D.drawImage(img, rotation, null);
        
        System.out.println(this.toString());
    }

    @Override
    public void update(Observable o, Object o1) {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        this.repaint();
    }

    private void rotateLeft() {
        this.angle -= 3;
    }

    private void rotateRight() {
        this.angle += 3;
    }

    private void moveBackwards() {
        vx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = (int) Math.round(r * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(r * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }
    

    
   
    private void checkBorder() {
        if (x < 0) {
            x = 0;
        }
        if (x >= 740) {
            x = 740;
        }
        if (y < 0) {
            y = 0;
        }
        if (y >= 720) {
            y = 720;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }   
}
