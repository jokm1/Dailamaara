package np.com.samundrakc.game.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import java.util.Timer;
import java.util.TimerTask;

import np.com.samundrakc.game.controllers.subControllers.Audio;
import np.com.samundrakc.game.controllers.subControllers.Sound;

/**
 * Created by samundra on 2/10/2016.
 */
public class MessageBox {

    private final Stage stage;
    private final Table table;
    private String message;
    private boolean inMiddle = false;

    public boolean isInMiddle() {
        return inMiddle;
    }

    public MessageBox setInMiddle(boolean inMiddle) {
        this.inMiddle = inMiddle;
        return this;
    }

    public Table getTable() {
        return table;
    }

    public String getMessage() {
        return message;
    }

    public MessageBox setMessage(String message) {
        this.message = message;
        label.setText(this.message);
        Label l = new Label(message, Context.getInstance().getSkin());
        table.setWidth(l.getWidth() + 50);
        table.setHeight(l.getHeight() + 10);
        return this;
    }

    private OnOkButtonClicked okButtonClicked = null;

    public MessageBox(Stage stage, String message) {
        this.stage = stage;
        table = new Table();
        this.message = message;
        init();
    }

    public MessageBox(Stage stage, String message, OnOkButtonClicked okButtonClicked) {
        this.stage = stage;
        table = new Table();
        this.okButtonClicked = okButtonClicked;
        this.message = message;
        init();
    }


    Label label;
    TextButton okButton;
    private String okButtonText = "Done";

    public String getOkButtonText() {
        return okButtonText;
    }

    public MessageBox setOkButtonText(String okButtonText) {
        this.okButtonText = okButtonText;
        okButton.setText(okButtonText);
        return this;
    }

    public MessageBox init() {
        label = new Label(this.message, Context.getInstance().getSkin());
        table.add(label).pad(30).expandX().row();
        table.setWidth(label.getWidth() + 100);
        table.setHeight(label.getHeight() + 10);
        if (okButtonClicked != null) {
            okButton = new TextButton(getOkButtonText(), Context.getInstance().getSkin());
            okButton.setBounds(0, 0, okButton.getWidth(), okButton.getHeight());
            okButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    okButtonClicked.run();
                    hide();
                    return true;
                }
            });
            table.add(okButton).colspan(5).padTop(10);
            table.setSize(label.getWidth() + 100, label.getHeight() + okButton.getHeight() + 100);
        }
        table.setBackground(new NinePatchDrawable(MessageBox.getNinePatch("ng.9.png")));
        table.center();
        table.setY(Context.HEIGHT + table.getHeight());
        if (inMiddle) {
            table.setY(Context.HEIGHT / 2);
        }
        table.setX(Context.WIDTH / 2 - (table.getWidth() / 2));
        this.stage.addActor(table);

        return this;
    }

    public MessageBox show() {
        Sound.getInstance().play(Audio.AUDIO.NEW_MESSAGE);
        if (inMiddle)
            table.addAction(Animation.simpleAnimation(Context.WIDTH / 2 - (table.getWidth() / 2), Context.HEIGHT / 2 - (table.getHeight() / 2)));
        else
            table.addAction(Animation.simpleAnimation(Context.WIDTH / 2 - (table.getWidth() / 2), Context.HEIGHT - (table.getHeight() / 2 + 20)));
        return this;
    }

    public MessageBox hide() {
        table.addAction(Animation.simpleAnimation(Context.WIDTH / 2 - (table.getWidth() / 2), Context.HEIGHT + table.getHeight()));
        return this;
    }

    public interface OnOkButtonClicked {
        void run();
    }


    public MessageBox autoHide(final float seconds, final OnOkButtonClicked onOkButtonClicked) {
        final Timer timer = new Timer();
        final int[] count = {0};
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (count[0] == seconds) {
                    hide();
                    timer.cancel();
                    if (onOkButtonClicked != null) {
                        onOkButtonClicked.run();
                    }
                }
                count[0]++;
            }
        }, 0, 1000);

        return this;
    }

    public static NinePatch getNinePatch(String name) {
        final Texture t = new Texture(Gdx.files.internal(name));
        return new NinePatch(new TextureRegion(t, 1, 1, t.getWidth() - 2, t.getHeight() - 2), 10, 10, 10, 10);
    }
}
