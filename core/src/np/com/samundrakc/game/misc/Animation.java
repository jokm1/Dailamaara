package np.com.samundrakc.game.misc;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;

/**
 * Created by samundra on 2/1/2016.
 */
public class Animation {
public static boolean state =  false;
    public static  Action simpleAnimation( float x,float y){
        MoveByAction actionMove = Actions.action(MoveByAction.class);
        actionMove.setAmountX(x);
        actionMove.setAmountY(y);
        actionMove.setDuration(1);
        actionMove.setInterpolation(Interpolation.elasticOut);
        return actionMove;
    }

    public static Action shakeAction(){
        MoveToAction move =  Actions.action(MoveToAction.class);
        move.setX(5);
        move.setDuration((float) 0.5);
        return move;
    }

    public static Action rotate360(){
        RotateByAction rotate = Actions.action(RotateByAction.class);
        rotate.setAmount(360f);
        rotate.setDuration(2);
        return rotate;
    }

    public static Action sizeActionPlus(int width,int height,float duration){
        SizeToAction size =  new SizeToAction();
        size.setWidth(width);
        size.setHeight(height);
        size.setDuration(duration);
        return size;
    }

    public static Action repeatAction(Action action){
        RepeatAction repeatAction =  new RepeatAction();
        repeatAction.setAction(action);
        repeatAction.setCount(RepeatAction.FOREVER);
        return  repeatAction;
    }
}