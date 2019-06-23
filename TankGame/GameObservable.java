package TankGame;

import TankGame.GameObject.GameObject;
import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 *CREDIT: PlaneGame - GameEvents Class
 */
public class GameObservable extends Observable{
    private int type;
    private Object caller, target;

    public void setCollision(GameObject caller, GameObject target)
    {
        type = 1;
        this.caller = caller;
        this.target = target;

        setChanged();

        this.notifyObservers(this);
    }

    public void setKeys(KeyEvent key)
    {
        type = 2;
        this.target = key;
        setChanged();

        notifyObservers(this);
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }

    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }

    public Object getCaller()
    {
        return caller;
    }

    public Object getTarget()
    {
        return target;
    }

}
