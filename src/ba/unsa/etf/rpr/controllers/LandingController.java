package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.database_organization.VotingDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class LandingController implements Initializable {

    public ImageView imgFlag;

    public LandingController(){
        imgFlag = new ImageView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("images/flag.png");
        imgFlag.setImage(image);
    }

    public void openVoting(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginregister.fxml"));
        LoginRegisterController noviController = new LoginRegisterController();
        loader.setController(noviController);
        Parent root = loader.load();

        myStage.setTitle("Login/register");
        myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.show();
    }

    public void openRegister(ActionEvent actionEvent) {
    }

    public void openAdmin(ActionEvent actionEvent) {
    }

    public void openHelp(ActionEvent actionEvent) {
    }

    public void openAbout(ActionEvent actionEvent) {
    }

    public void openLanguage(ActionEvent actionEvent) {
    }

    public void openResults(ActionEvent actionEvent) {
    }

}
