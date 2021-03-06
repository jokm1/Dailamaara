package np.com.samundrakc.game.controllers.form;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Timer;
import java.util.TimerTask;

import np.com.samundrakc.game.anchors.Const;
import np.com.samundrakc.game.anchors.Game;
import np.com.samundrakc.game.controllers.CardSelection;
import np.com.samundrakc.game.controllers.subControllers.Sound;
import np.com.samundrakc.game.misc.Animation;
import np.com.samundrakc.game.misc.Context;
import np.com.samundrakc.game.misc.MessageBox;
import np.com.samundrakc.game.misc.Utils;
import np.com.samundrakc.game.screens.Form;
import np.com.samundrakc.game.screens.MenuScreen;
import np.com.samundrakc.game.screens.LoadingScreen;

/**
 * Created by samundra on 2/3/2016.
 */
public class FormCtrl {
    private Form view;
    private Game game;
    private boolean isCardDistubuted = false;

    public boolean isCardDistubuted() {
        return isCardDistubuted;
    }

    public void setIsCardDistubuted(boolean isCardDistubuted) {
        this.isCardDistubuted = isCardDistubuted;
    }

    public Form getView() {
        return view;
    }

    public Game getGame() {
        return game;
    }

    MessageBox msg;

    public FormCtrl(Form view, Game game) {
        this.view = view;
        this.game = game;
        msg = new MessageBox(view.getStage(), "Pop up message");
    }

    public InputListener playButton() {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("play button clicked");
                if (Utils.isEmpty(view.getName().getText())) {
                    autoHideMessage("Please enter your name").autoHide(1, null);
                    return false;
                }
                if (Utils.isEmpty(view.getGroup().getText())) {
                    autoHideMessage("Please enter your group name").autoHide(1, null);
                    return false;
                }
                Sound.getInstance().play(Sound.AUDIO.BUTTON_TOUCH);
                view.getTable().addAction(Animation.simpleAnimation(-Context.WIDTH, Utils.inCenter(view.getTable(), 'y')));
                view.getSelectPlayerTable().addAction(Animation.simpleAnimation(1, 1));
                view.getPref().setString("name", view.getName().getText());
                view.getPref().setString("group", view.getGroup().getText());
                return super.touchDown(event, x, y, pointer, button);
            }
        };
    }

    public MessageBox autoHideMessage(String message) {
        return msg.setMessage(message).show();
    }

    public InputListener computerCtrl(final int i) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Sound.getInstance().play(Sound.AUDIO.BUTTON_TOUCH);
                view.getSelectPlayerTable().addAction(Animation.simpleAnimation(-(Context.WIDTH + 500), 0));
                view.getStacks().addAction(Animation.simpleAnimation(3, (Context.HEIGHT - (Context.HEIGHT - 50))));
                view.getStacksChild().addAction(Animation.simpleAnimation(3, (Context.HEIGHT - (Context.HEIGHT - 50))));
//                game.createGroups(view.getName().getText(), view.getGroup().getText(), i + 1);
                if (view.getPref().getInt("rememberFriend") == 1) {
                    view.getPref().setInt("friend", i);
                }
                game.getGroup().clear();
                game.createGroups(view.getName().getText(), view.getGroup().getText(), i + 1);
                if (isCardSelected) return true;
                if (isAllCardShareProcessDone) return true;
                cardShareProcess(null);
                return true;
            }
        };
    }

    public boolean isCardShareProcessDone() {
        return isCardShareProcessDone;
    }

    public void setIsCardShareProcessDone(boolean isCardShareProcessDone) {
        this.isCardShareProcessDone = isCardShareProcessDone;
    }

    public boolean isAllCardShareProcessDone() {
        return isAllCardShareProcessDone;
    }

    public void setIsAllCardShareProcessDone(boolean isAllCardShareProcessDone) {
        this.isAllCardShareProcessDone = isAllCardShareProcessDone;
    }

    private boolean isCardShareProcessDone = false;
    private boolean isAllCardShareProcessDone = false;

    public void cardShareProcess(final Callback callback) {
        if (!isCardShareProcessDone) {
            autoHideMessage("Please wait while card is distributed").autoHide(2, null);
            getView().getMenu().setTouchable(Touchable.disabled);
            getView().getMenu().setDisabled(true);
            isCardShareProcessDone = true;
        }
        final int[] xx = {3};
        final int[] yy = {3};
        final int[] gap = {3};
        final int[] i = {0};
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (i[0] == Const.TOTAL_NUMBER_OF_CARDS) {
                    timer.cancel();
                    if (!isAllCardShareProcessDone) {
                        autoHideMessage("You can select any card now").autoHide(2, null);
                        getView().getMenu().setTouchable(Touchable.enabled);
                        getView().getMenu().setDisabled(false);
                        isAllCardShareProcessDone = true;
                    }
                    view.getStacks().setTouchable(Touchable.enabled);
                    Button button = new TextButton("Back", Context.getInstance().getSkin());
                    button.setPosition((Context.WIDTH / 2 - button.getWidth()), -button.getHeight());
                    button.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            Sound.getInstance().play(Sound.AUDIO.BUTTON_TOUCH);
                            view.getStacks().setPosition((Context.WIDTH + 500), 50);
                            view.getStacksChild().setPosition((Context.WIDTH + 500), 50);
                            view.getSelectPlayerTable().addAction(Animation.simpleAnimation((0), 0));
                            return super.touchDown(event, x, y, pointer, button);
                        }
                    });
                    view.getStacks().addActor(button);
                    setIsCardSelected(false);
                    if (callback != null) {
                        callback.run();
                    }
                    return;
                }
                if (xx[0] >= (Context.WIDTH - 50)) {
                    yy[0] += 70 + gap[0];
                    xx[0] = gap[0];
                }
                Sound.getInstance().play(Sound.AUDIO.CARD_SHARE);
                view.getStacks().getChildren().get(i[0]).addAction(Actions.sequence(Animation.simpleAnimation(xx[0], yy[0]), Actions.sequence(Animation.sizeActionPlus(60, 80, 0.2f), Animation.sizeActionPlus(50, 70, 0.2f))));
                xx[0] += view.getStacks().getChildren().get(i[0]).getWidth() + gap[0];
                i[0]++;
            }
        }, 100, 100);
    }

    public InputListener backButton() {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Sound.getInstance().play(Sound.AUDIO.BUTTON_TOUCH);
                view.getTable().addAction(Animation.simpleAnimation(Utils.inCenter(view.getStacks(), 'x'), Utils.inCenter(view.getTable(), 'y')));
                view.getSelectPlayerTable().addAction(Animation.simpleAnimation((Context.WIDTH), 0));
                return super.touchDown(event, x, y, pointer, button);
            }
        };
    }

    public boolean isCardSelected() {
        return isCardSelected;
    }

    public void setIsCardSelected(boolean isCardSelected) {
        this.isCardSelected = isCardSelected;
    }

    private boolean isCardSelected = false;

    public InputListener cardsListener(final int i, final Actor actor) {
        return new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                actor.addAction(Actions.sequence(Animation.sizeActionPlus(60, 80, 0.5f), Animation.sizeActionPlus(50, 70, 0.5f)));
                return super.mouseMoved(event, x, y);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isCardSelected) return true;
                if (isCardDistubuted) return true;
                Sound.getInstance().play(Sound.AUDIO.CARD_TOUCHED);
                new CardSelection(FormCtrl.this).start(i, actor);
                return true;
            }
        };
    }

    private float getXDiffToPin(float xA, float xB) {
        if (xB > xA) {
            return -(xB - xA);
        } else {
            return (xA - xB);
        }
    }

    public InputListener remeberFriend() {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Sound.getInstance().play(Sound.AUDIO.BUTTON_TOUCH);
                if (!view.getRemberFriendCheckBox().isChecked()) {
                    view.getPref().setInt("rememberFriend", 1);
                } else {
                    view.getPref().setInt("rememberFriend", 0);
                }
                return true;
            }
        };
    }

    public InputListener menuCtrl() {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                view.getDailaMaara().setScreen(new MenuScreen(view.getDailaMaara()));
                view.getDailaMaara().setScreen(new LoadingScreen(view.getDailaMaara()).otherScreen(new MenuScreen(view.getDailaMaara())));
//                LoadingScreen splashScreen = new LoadingScreen(view.getDailaMaara());
//                view.getDailaMaara().setScreen(splashScreen);
//                splashScreen.nextScreenAssestsChecker(new MenuScreen(view.getDailaMaara()));
                return super.touchDown(event, x, y, pointer, button);
            }
        };
    }

    public static class BackCover extends Image {
        public void setIsCardFlipped(boolean isCardFlipped) {
            this.isCardFlipped = isCardFlipped;
        }

        private boolean isCardFlipped = false;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private int index;
        private int width = 50, height = 70;

        public BackCover(int index) {
            super(Context.getInstance().getCARDS_BACK_COVER());
            super.setSize(width, height);
            this.index = index;
        }
    }

    public interface Callback {
        void run();
    }
}
