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
import java.util.Observable;

public class GameEventObservable extends Observable {
public GameEventObservable() {
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }
    
}
