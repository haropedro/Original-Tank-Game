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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.image.*;
import java.awt.*;






public class TankRotationExample extends JFrame  {


    
    

   private final GameEventObservable geobv;
    public static void main(String[] args) throws IOException {
       
        // TODO code application logic here
        Thread x;
        TankRotationExample trex = new TankRotationExample();
        trex.init();

        try {
            while (true) {
                trex.geobv.setChanged();
                trex.geobv.notifyObservers();
                Thread.sleep(1000/144);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(TankRotationExample.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public TankRotationExample() {
        this.geobv = new GameEventObservable();
    }

    private void init() {
   setTitle("Tank Rotation");
        Tank t1 = new Tank(0, 0, 0, 0, (short)0);
        try {
            BufferedImage i = ImageIO.read(new File("Tank1.jpg"));
            t1.setImg(i);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        TankControl tc1 = new TankControl(t1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
        this.add(t1);

        this.addKeyListener(tc1);

        this.geobv.addObserver(t1);

        this.setSize(800, 800);
        this.setResizable(false);
        setLocationRelativeTo(null);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
 
    }
    
  





    



    
    



  public void run() {
      while (true) {
          try {
              this.geobv.notifyObservers();
               Thread.sleep(1000/144);
     
            }catch(InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
       }
   }
    
    
}
