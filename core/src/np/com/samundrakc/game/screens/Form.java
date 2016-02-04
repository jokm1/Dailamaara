package np.com.samundrakc.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Timer;
import java.util.TimerTask;

import np.com.samundrakc.game.DailaMaara;
import np.com.samundrakc.game.anchors.Const;
import np.com.samundrakc.game.controllers.FormCtrl;
import np.com.samundrakc.game.misc.Animation;
import np.com.samundrakc.game.misc.Context;
import np.com.samundrakc.game.misc.Utils;

/**
 * Created by samundra on 2/2/2016.
 */
public class Form extends ScreenRules {
    TextField name;
    private TextButton back;

    public TextField getGroup() {
        return group;
    }

    public TextField getName() {
        return name;
    }

    TextField group;

    public Table getTable() {
        return table;
    }

    Table table;
    Button play;
    FormCtrl formCtrl;

    public Form(DailaMaara game) {
        super(game);
        formCtrl = new FormCtrl(this);
        initWidgets();
        selectPlayerForm();
        selectCardForDistrubutor();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        game.clearView();
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        table.setPosition(Utils.inCenter(table, 'x'), Utils.inCenter(table, 'y'));
        stage.addActor(table);
        stage.addActor(selectPlayerTable);

    }

    private void initWidgets() {
        name = new TextField("", Context.skin);
        group = new TextField("", Context.skin);
        Label nameLabel = new Label("Enter your name", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label groupLabel = new Label("Enter your group name", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        play = new TextButton("NEXT", Context.skin);
        play.setBounds(0, 0, play.getWidth(), play.getHeight());
        play.addListener(formCtrl.playButton());
        table = new Table();
        table.add(nameLabel).padTop(5).row();
        table.add(name).padTop(5).row();
        table.add(groupLabel).padTop(5).row();
        table.add(group).padTop(5).row();
        table.add(play).padTop(5);
        table.center();
        table.setFillParent(!true);
    }

    Button[] computer;
    Table selectPlayerTable;

    public Table getSelectPlayerTable() {
        return selectPlayerTable;
    }

    private void selectPlayerForm() {
        computer = new Button[3];
        selectPlayerTable = new Table();
        Label nameLabel = new Label("Select your friend", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        selectPlayerTable.add(nameLabel).colspan(5).expandX().padBottom(10).row();
        for (int i = 0; i < computer.length; i++) {
            computer[i] = new TextButton("Computer " + (i + 1), Context.skin);
            computer[i].setBounds(0, 0, computer[i].getWidth(), computer[i].getHeight());
            computer[i].addListener(formCtrl.computerCtrl(computer[i], i));
            selectPlayerTable.add(computer[i]).pad(5).expandX();
        }
        selectPlayerTable.row();
        back = new TextButton("Back", Context.skin);
        back.setBounds(0, 0, back.getWidth(), back.getHeight());
        back.addListener(formCtrl.backButton());
        selectPlayerTable.add(back).colspan(5).padTop(10);
        selectPlayerTable.center().setFillParent(true);
        selectPlayerTable.setPosition(Context.WIDTH + selectPlayerTable.getWidth(), 0);
    }

    private void selectCardForDistrubutor() {
//        Stack stacks = new Stack();
        final Group stacks = new Group();
        final int[] x = {3};
        final int[] y = {3};
        final int[] gap = {3};
        stacks.setSize(50, 70);
        stacks.setPosition(gap[0],  (50) );
        for (int i = 0; i < Const.TOTAL_NUMBER_OF_CARDS; i++) {
            stacks.addActor(new FormCtrl.BackCover(i));
            stacks.getChildren().get(i).setPosition(gap[0],gap[0]);
//            if(x[0] >=  (Context.WIDTH - 50)){
//                y[0] +=  stacks.getChildren().get(i).getHeight() +gap[0];
//                x[0] = gap[0];
//            }
//            stacks.getChildren().get(i).addAction(Actions.sequence(Animation.simpleAnimation(x[0], y[0])));

//            stacks.getChildren().get(i).addAction(Actions.sequence(Animation.simpleAnimation(x,y) ,Animation.repeatAction(Actions.sequence(Animation.sizeActionPlus(52,72,5),Animation.sizeActionPlus(50,70,5)))));
            stacks.getChildren().get(i).addListener(formCtrl.cardsListener(i,stacks.getChildren().get(i)));
//            x[0] += stacks.getChildren().get(i).getWidth() + gap[0];
        }
        final int[] i = {0};
        final Timer timer =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (i[0] == Const.TOTAL_NUMBER_OF_CARDS) {
                    timer.cancel();
                    return;
                }
                if (x[0] >= (Context.WIDTH - 50)) {
                    y[0] += stacks.getChildren().get(i[0]).getHeight() + gap[0];
                    x[0] = gap[0];
                }
                stacks.getChildren().get(i[0]).addAction(Actions.sequence(Animation.simpleAnimation(x[0], y[0]),Actions.sequence(Animation.sizeActionPlus(60,80,0.2f),Animation.sizeActionPlus(50,70,0.2f))));
//                stacks.getChildren().get(i[0]).addAction(Animation.rotate360());
                x[0] += stacks.getChildren().get(i[0]).getWidth() + gap[0];
                i[0]++;
            }
        }, 100, 100);
        System.out.println("Lets se m i async");
//        stacks.setPosition(gap[0], (Context.HEIGHT / 2) - ((y[0] + 50) / 2));
        stage.addActor(stacks);
    }
}