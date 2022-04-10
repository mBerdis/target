package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class tableController implements Initializable
{
    @FXML
    VBox vbox;
    @FXML
    HBox header;

    private void readData()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("Stats.txt"));
            while (reader.ready())
            {
                String line = reader.readLine();
                HBox child = new HBox();
                child.setSpacing(15);
                child.setAlignment(Pos.TOP_LEFT);
                String[] elements = line.split(";");
                for (String str: elements)
                {
                    Text element = new Text();
                    element.getStyleClass().add("tableText");
                    element.setText(str);
                    element.setFill(Color.GRAY);
                    child.getChildren().add(element);
                }
                vbox.getChildren().add(child);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        readData();
    }
}
