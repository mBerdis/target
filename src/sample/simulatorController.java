package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class simulatorController implements Initializable{
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
    @FXML
    Text tirednessText;
    @FXML
    Text windText;
    @FXML
    Text shotsText;
    @FXML
    BorderPane borderPane;
    @FXML
    VBox settings;
    @FXML
    Button switchBtn;
    @FXML
    TextField nameInput;
    @FXML
    ImageView windArrow;

    String windDirection = "NORTH";

    AnchorPane clearPane;
    int perioda=0;
    double xmysi,ymysi,xmysiFix,ymysiFix;
    Random rn=new Random();
    int shot = 0;
    int sumPoints = 0;
    List<Circle> bullets = new ArrayList<>();
    RotateTransition rt;

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("table.fxml"));
    VBox table = fxmlLoader.load();

    public simulatorController() throws IOException {}

    public int galtonBoard(int pozicia, int hladina){
        if (hladina==0) return pozicia;
        else if (rn.nextInt(2)==1) return galtonBoard(pozicia+1,hladina-1);
        return galtonBoard(pozicia-1,hladina-1);
    }

    public void mouseMove(MouseEvent mouseEvent) {
        //x_nov?? = x_star??*0.9+x_nov??_nameran??*0.1;      filtr??cia
        //x_nov?? = x_star??+(x_nov??_nameran??-x_star??)*0.1

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

                    // randomly change wind direction
                    windDirection = generateWindDirection();

                }
                pointer.setX(pointer.getX()*0.95+xmysi*0.05);
                pointer.setY(pointer.getY()*0.95+ymysi*0.05);
            }
        }.start();

        clearPane = panel;
    }

    public String generateWindDirection()
    {
        int rng = (int) (Math.random() * 100);
        // 5% that wind changes
        if (rng > 5)
        {
            // DONT CHANGE WIND DIRECTION
            return windDirection;
        }
        String direction = switch ((int) (Math.random() * 4))
                {
                    case 0 -> "NORTH";
                    case 1 -> "EAST";
                    case 2 -> "WEST";
                    default -> "SOUTH";
                };
        rt = new RotateTransition(Duration.millis(500),windArrow);
        switch (direction)
        {
            case "NORTH" -> rt.setToAngle(-90);
            case "EAST" -> rt.setToAngle(0);
            case "WEST" -> rt.setToAngle(-180);
            default -> rt.setToAngle(90);
        }
        rt.play();
        return direction;
    }

    public void shoot() {
        String windDirection = generateWindDirection();

        int windOffsetX = 0;
        int windOffsetY = 20; // bullet drop / gravity


        switch(windDirection)
        {
            case "NORTH": windOffsetY -= (10 * wind.getValue()); break;
            case "EAST": windOffsetX += (10 * wind.getValue()); break;
            case "WEST": windOffsetX -= (10 * wind.getValue()); break;
            default: windOffsetY += (10 * wind.getValue()); break;
        }

        //double offset = galtonBoard(0,10) * (wind.getValue() * 5);

        Circle c = new Circle(pointer.getX()+ pointer.getFitWidth()/2 + windOffsetX, pointer.getY()+ pointer.getFitHeight()/2 + windOffsetY,7);
        c.setStroke(Color.RED);
        bullets.add(c);
        panel.getChildren().add(c);
        shot++;

        double stredX = (target.getX()+target.getFitWidth()/2);
        double stredY = (target.getY()+target.getFitHeight()/2);
        double distance = Math.sqrt(Math.pow(stredX-c.getCenterX(),2) + Math.pow(stredY-c.getCenterY(),2));
        double oneCircleWidth = target.getFitWidth()/2/10;

        if (distance <= oneCircleWidth)
        {
            updateTextArea(shot,10);
            sumPoints += 10;
        }
        else
        if (distance > oneCircleWidth && distance <= 2 * oneCircleWidth)
        {
            updateTextArea(shot,9);
            sumPoints += 9;
        }
        else
        if (distance > 2 * oneCircleWidth && distance <= 3 * oneCircleWidth) {
            updateTextArea(shot,8);
            sumPoints += 8;
        }
        else
        if (distance > 3 * oneCircleWidth && distance <= 4 * oneCircleWidth) {
            updateTextArea(shot,7);
            sumPoints += 7;
        }
        else
        if (distance > 4 * oneCircleWidth && distance <= 5 * oneCircleWidth) {
            updateTextArea(shot,6);
            sumPoints += 6;
        }
        else
        if (distance > 5 * oneCircleWidth && distance <= 6 * oneCircleWidth) {
            updateTextArea(shot,5);
            sumPoints += 5;
        }
        else
        if (distance > 6 * oneCircleWidth && distance <= 7 * oneCircleWidth) {
            updateTextArea(shot,4);
            sumPoints += 4;
        }
        else
        if (distance > 7 * oneCircleWidth && distance <= 8 * oneCircleWidth) {
            updateTextArea(shot,3);
            sumPoints += 3;
        }
        else
        if (distance > 8 * oneCircleWidth && distance <= 9 * oneCircleWidth) {
            updateTextArea(shot,2);
            sumPoints += 2;
        }
        else
        if (distance > 9 * oneCircleWidth && distance <= 10 * oneCircleWidth) {
            updateTextArea(shot,1);
            sumPoints += 1;
        }
        else
            updateTextArea(shot,0);

        if (gameEndedCheck())
        {
            // game has ended, disable shooting
            pointer.setVisible(false);
            panel.setOnMouseClicked(null);
            // show stats
            saveData();
            return;
        }

    }

    private void saveData()
    {
        float priemer = (float) sumPoints /  shot;

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("Stats.txt",true))) {
            if (nameInput.getText().equals("")) writer.write("\nUnknown;" + sumPoints + ";" + shot + ";" + priemer);
            else writer.write("\n" + nameInput.getText() + ";" + sumPoints + ";" + shot + ";" + priemer);
        }
        catch(IOException e){   // Handle and print exception
            System.out.println("There was an error, while writing to file: "+e.getMessage());
        }
    }

    private void updateTextArea(int shot, int points){
        textarea.appendText(shot +". strela: " + points +"b\n");
    }

    public void reset()
    {
        for (Circle c : bullets)
        {
            panel.getChildren().remove(c);
        }
        pointer.setVisible(true);
        textarea.clear();
        nameInput.clear();
        shot = 0;
        panel.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override public void handle(MouseEvent e)
            {
                shoot();
            }
        });
    }

    private boolean gameEndedCheck()
    {
        int allShots = (int) numberOfShots.getValue();

        return shot >= allShots;
    }

    public void updateTiredness(){
        tirednessText.setText(String.valueOf((int) tiredness.getValue()));
    }
    public void updateWind(){
        windText.setText(String.valueOf((int) wind.getValue()));
    }
    public void updateShots(){
        shotsText.setText(String.valueOf((int) numberOfShots.getValue()));
    }
    public void switchToTable(){
        borderPane.getChildren().remove(borderPane.getRight());
        borderPane.setRight(table);
        switchBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                switchToSettings();
            }
        });
        switchBtn.setText("Nastavenia");
    }
    public void switchToSettings(){
        borderPane.getChildren().remove(borderPane.getRight());
        borderPane.setRight(settings);
        switchBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override public void handle(ActionEvent e)
            {
                switchToTable();
            }
        });
        switchBtn.setText("Tabu??ka");

    }
}
