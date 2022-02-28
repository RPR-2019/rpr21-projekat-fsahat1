package ba.unsa.etf.rpr;

import ba.unsa.etf.rpr.controllers.LandingController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/landing.fxml"));
        LandingController noviController = new LandingController();
        loader.setController(noviController);
        Parent root = loader.load();

        myStage.setTitle("DemokratiJA");
        myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
