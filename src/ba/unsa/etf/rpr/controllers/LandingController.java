package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.database_organization.VotingDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class LandingController implements Initializable {

    public ImageView imgFlag;
    boolean open = false;
    public LandingController(){
        imgFlag = new ImageView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("images/flag.png");
        imgFlag.setImage(image);
    }

    public void openVoting(ActionEvent actionEvent) throws IOException {
        if(open == false){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DemokratiJA");
            alert.setHeaderText("Early access error");
            alert.setContentText("The polls have not been opened yet. Please, try again later.");
            alert.show();
            return;
        }
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginregister.fxml"));
        LoginRegisterController newController = new LoginRegisterController();
        loader.setController(newController);
        Parent root = loader.load();

        myStage.setTitle("Login/register");
        myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.show();
    }

    public void openRegister(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/partyregistration.fxml"));
        PartyRegistrationController newController = new PartyRegistrationController();
        loader.setController(newController);
        Parent root = loader.load();

        myStage.setTitle("Register your party");
        myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.show();
    }

    public void openAdmin(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/password.fxml"));
        PasswordController newController = new PasswordController(open);
        loader.setController(newController);
        Parent root = loader.load();

        myStage.setTitle("Register your party");
        myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.show();

        myStage.setOnHiding(e -> {
            open = newController.isOpen();
        });
    }

    public void openHelp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DemokratiJA");
        alert.setHeaderText("Help");
        alert.setContentText("If you wish to vote for existing parties, select the first option." +
                "\nIf you wish to register your party for the upcoming election, select the second option." +
                "\nIf you have already voted/registered, follow the results by selecting the last option.");
        alert.show();
        return;
    }

    public void openAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DemokratiJA");
        alert.setHeaderText("About");
        alert.setContentText("DemokratiJA\nUnofficial voting aid prototype" +
                "Version 0.1, created by Faruk Sahat for the RPR final exam");
        alert.show();
    }

    public void openLanguage(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DemokratiJA");
        alert.setHeaderText("Unfortunately. . .");
        alert.setContentText("Other languages are not yet supported in this version.");
        alert.show();
    }

    public void openResults(ActionEvent actionEvent) {
    }

}
