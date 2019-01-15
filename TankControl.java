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


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.io.IOException;

public class TankControl extends Observable implements KeyListener  {
    
    private final int up;
    private final int down;
    private final int left;
    private final int right;
   
    private final Tank t1;
    
    
    
public TankControl(Tank t1, int up, int down, int left, int right) {
        this.t1 = t1;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;

    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed ==  up) {
            this.t1.toggleUpPressed();
        }
        if (keyPressed == down) {
            this.t1.toggleDownPressed();
        }
        if (keyPressed == left ) {
            this.t1.toggleLeftPressed();
        }
        if (keyPressed  == right) {
            this.t1.toggleRightPressed();
        }
        
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased == up) {
            this.t1.unToggleUpPressed();
        }
        if (keyReleased == down) {
            this.t1.unToggleDownPressed();
        }
        if (keyReleased == left) {
            this.t1.unToggleLeftPressed();
        }
        if (keyReleased  == right) {
            this.t1.unToggleRightPressed();
        }
        
       
    }

}

    
    

