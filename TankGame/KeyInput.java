/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import TankGame.GameObject.Movable.Tank;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

public class KeyInput extends Observable implements KeyListener {

    private final Tank tank;
    private final int upKey;
    private final int downKey;
    private final int leftKey;
    private final int rightKey;
    private final int shootKey;


    public KeyInput(Tank tank) {
        this.tank = tank;
        this.upKey = tank.getUpKey();
        this.downKey = tank.getDownKey();
        this.leftKey = tank.getLeftKey();
        this.rightKey = tank.getRightKey();
        this.shootKey = tank.getShootKey();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == this.upKey) {
            this.tank.switchUpOn();
        }
        if (key == this.downKey) {
            this.tank.switchDownOn();
        }
        if (key == this.leftKey) {
            this.tank.switchLeftOn();
        }
        if (key == this.rightKey) {
            this.tank.switchRightOn();
        }
        if (key == this.shootKey) {
            this.tank.switchShootOn();
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == this.upKey) {
            this.tank.switchUpOff();
        }
        if (key == this.downKey) {
            this.tank.switchDownOff();
        }
        if (key == this.leftKey) {
            this.tank.switchLeftOff();
        }
        if (key == this.rightKey) {
            this.tank.switchRightOff();
        }
        if (key == this.shootKey) {
            this.tank.switchShootOff();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
