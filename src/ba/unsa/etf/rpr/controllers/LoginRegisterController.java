package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.classes.VoteStatus;
import ba.unsa.etf.rpr.classes.Voter;
import ba.unsa.etf.rpr.database_organization.VotingDAO;
import exceptions.NonexistantVoterException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class LoginRegisterController implements Initializable {
    public TextField fldNameSurname;
    public TextField fldID;

    private VotingDAO votingDAO=null;

    private Voter voter = null;

    public LoginRegisterController(){
        fldNameSurname = new TextField();
        fldID = new TextField();
        votingDAO = VotingDAO.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void enterStation(ActionEvent actionEvent) throws IOException {
        boolean valid = true;
        AtomicBoolean voted = new AtomicBoolean(false);
        AtomicBoolean exists = new AtomicBoolean(false);
        Platform.runLater(()-> {
            try {
                System.out.println("evo nas");
                if(votingDAO.checkVoter(fldID.getText())==0) voted.set(true);
            } catch (NonexistantVoterException e) {
                exists.set(false);
            }
            System.out.println("stigli smo");
        });
        if(fldNameSurname.textProperty().isEmpty().get()){
            fldNameSurname.getStyleClass().removeAll("validField");
            fldNameSurname.getStyleClass().add("nonValidField");
            valid = false;
        }
        else{
            fldNameSurname.getStyleClass().add("validField");
            fldNameSurname.getStyleClass().removeAll("nonValidField");
        }
        if(fldID.textProperty().isEmpty().get()){
            fldID.getStyleClass().removeAll("validField");
            fldID.getStyleClass().add("nonValidField");
            valid = false;
        }
        else{
            fldID.getStyleClass().add("validField");
            fldID.getStyleClass().removeAll("nonValidField");
        }
        if(!valid){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Voting login/registration");
            alert.setHeaderText("Information Error");
            alert.setContentText("The data you have entered is invalid, please try again");
            alert.show();
            return;
        }
        if(exists.get()){
            if(voted.get()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Voting login/registration");
                alert.setHeaderText("Previous Vote Registered");
                alert.setContentText("You have already voted in this voting period, thank you for participating.");
                alert.show();
                cancel(new ActionEvent());
            }
            else{
                voter = new Voter(fldNameSurname.getText(), fldID.getText());
                Stage myStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/votingstation.fxml"));
                VotingStationController noviController = new VotingStationController(voter);
                loader.setController(noviController);
                Parent root = loader.load();

                myStage.setTitle("Voting");
                myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                myStage.show();
                cancel(new ActionEvent());
            }
        }
        voter = new Voter(fldNameSurname.getText(), fldID.getText());
        votingDAO.addVoter(voter);
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/votingstation.fxml"));
        VotingStationController noviController = new VotingStationController(voter);
        loader.setController(noviController);
        Parent root = loader.load();

        myStage.setTitle("Voting");
        myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.show();
        cancel(new ActionEvent());
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) fldNameSurname.getScene().getWindow();
        stage.close();
    }
}
