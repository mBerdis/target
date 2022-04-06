package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    ImageView pointer;
    @FXML
    Slider tiredness;
    @FXML
    Slider wind;
    @FXML
    Slider numberOfShots;
    @FXML
    AnchorPane panel;
    @FXML
    ImageView target;
    @FXML
    TextArea textarea;

    AnchorPane clearPane;
    int perioda=0;
    double xmysi,ymysi,xmysiFix,ymysiFix;
    Random rn=new Random();
    int shot = 0;
    List<Circle> bullets = new ArrayList<>();

    public int galtonBoard(int pozicia, int hladina){
        if (hladina==0) return pozicia;
        else if (rn.nextInt(2)==1) return galtonBoard(pozicia+1,hladina-1);
        return galtonBoard(pozicia-1,hladina-1);
    }

    public void mouseMove(MouseEvent mouseEvent) {
        //x_nová = x_stará*0.9+x_nová_nameraná*0.1;      filtrácia
        //x_nová = x_stará+(x_nová_nameraná-x_stará)*0.1

        xmysi=mouseEvent.getSceneX()- pointer.getFitWidth()/2;           //xmysi
        ymysi=mouseEvent.getSceneY()- pointer.getFitHeight()/2;          //ymysi
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                perioda++;
                if (perioda % 10 == 0){
    /*              int rozptyl = (int) (10 * (unava.getValue()+1));
                    xmysi +=  rn.nextInt(2*rozptyl)-rozptyl;
                    ymysi +=  rn.nextInt(2*rozptyl)-rozptyl;
    */
                    xmysi = xmysi + galtonBoard(0,10) * (tiredness.getValue()+1);
                    ymysi = ymysi + galtonBoard(0,10) * (tiredness.getValue()+1);

                }
                pointer.setX(pointer.getX()*0.95+xmysi*0.05);
                pointer.setY(pointer.getY()*0.95+ymysi*0.05);
            }
        }.start();

        clearPane = panel;
    }

    public void shoot(MouseEvent mouseEvent) {
        if (gameEndedCheck())
        {
            // game has ended, disable shooting
            pointer.setVisible(false);

            // show stats

            return;
        }

        double offset = galtonBoard(0,10) * (wind.getValue() * 5);

        Circle c = new Circle(pointer.getX()+ pointer.getFitWidth()/2 + offset, pointer.getY()+ pointer.getFitHeight()/2 + offset,7);
        c.setStroke(Color.RED);
        bullets.add(c);
        panel.getChildren().add(c);
        shot++;

        double stredX = (target.getX()+target.getFitWidth()/2);
        double stredY = (target.getY()+target.getFitHeight()/2);
        double distance = Math.sqrt(Math.pow(stredX-c.getCenterX(),2) + Math.pow(stredY-c.getCenterY(),2));
        double oneCircleWidth = target.getFitWidth()/2/10;

        if (distance <= oneCircleWidth) updateTextArea(shot,10);
        else
        if (distance > oneCircleWidth && distance <= 2 * oneCircleWidth) updateTextArea(shot,9);
        else
        if (distance > 2 * oneCircleWidth && distance <= 3 * oneCircleWidth) updateTextArea(shot,8);
        else
        if (distance > 3 * oneCircleWidth && distance <= 4 * oneCircleWidth) updateTextArea(shot,7);
        else
        if (distance > 4 * oneCircleWidth && distance <= 5 * oneCircleWidth) updateTextArea(shot,6);
        else
        if (distance > 5 * oneCircleWidth && distance <= 6 * oneCircleWidth) updateTextArea(shot,5);
        else
        if (distance > 6 * oneCircleWidth && distance <= 7 * oneCircleWidth) updateTextArea(shot,4);
        else
        if (distance > 7 * oneCircleWidth && distance <= 8 * oneCircleWidth) updateTextArea(shot,3);
        else
        if (distance > 8 * oneCircleWidth && distance <= 9 * oneCircleWidth) updateTextArea(shot,2);
        else
        if (distance > 9 * oneCircleWidth && distance <= 10 * oneCircleWidth) updateTextArea(shot,1);
        else
            updateTextArea(shot,0);

    }
    private void updateTextArea(int shot, int points){
        textarea.appendText(shot +". strela: " + points +" bodov \n");
    }

    public void reset()
    {
        for (Circle c : bullets)
        {
            panel.getChildren().remove(c);
        }
        pointer.setVisible(true);
        textarea.clear();
        shot = 0;
    }

    private boolean gameEndedCheck()
    {
        int allShots = (int) numberOfShots.getValue();

        if (shot >= allShots)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
