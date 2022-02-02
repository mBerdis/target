package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    ImageView kriz;
    @FXML
    Slider unava;
    @FXML
    AnchorPane panel;

    int perioda=0;
    double xmysi,ymysi,xmysiFix,ymysiFix;
    Random rn=new Random();

    public int galtonBoard(int pozicia, int hladina){
        if (hladina==0) return pozicia;
        else if (rn.nextInt(2)==1) return galtonBoard(pozicia+1,hladina-1);
        return galtonBoard(pozicia-1,hladina-1);
    }

    public void mouseMove(MouseEvent mouseEvent) {
        //x_nová = x_stará*0.9+x_nová_nameraná*0.1;      filtrácia
        //x_nová = x_stará+(x_nová_nameraná-x_stará)*0.1

        xmysi=mouseEvent.getSceneX()-kriz.getFitWidth()/2;           //xmysi
        ymysi=mouseEvent.getSceneY()-kriz.getFitHeight()/2;          //ymysi
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
                    xmysi = xmysi + galtonBoard(0,10) * (unava.getValue()+1);
                    ymysi = ymysi + galtonBoard(0,10) * (unava.getValue()+1);

                }
                kriz.setX(kriz.getX()*0.95+xmysi*0.05);
                kriz.setY(kriz.getY()*0.95+ymysi*0.05);
            }
        }.start();
    }

    public void pifpaf(MouseEvent mouseEvent) {
        Circle c = new Circle(kriz.getX()+kriz.getFitWidth()/2,kriz.getY()+kriz.getFitHeight()/2,10);
        panel.getChildren().add(c);
    }
}
