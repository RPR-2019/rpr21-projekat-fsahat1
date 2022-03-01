package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.classes.PoliticalParty;
import ba.unsa.etf.rpr.database_organization.VotingDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class PartyRegistrationController {
    public TextField txtPartyName;
    public TextField txtPartyLeader;
    private PoliticalParty party;
    private VotingDAO dao;

    public PartyRegistrationController(){
        txtPartyName = new TextField();
        txtPartyLeader = new TextField();
    }

    public void registerParty(ActionEvent actionEvent) throws IOException {
        party = new PoliticalParty(txtPartyName.getText(), txtPartyLeader.getText());
        ArrayList<PoliticalParty> accepted = dao.parties();
        ArrayList<PoliticalParty> waiting = dao.waitlist();
        if(accepted.contains(party)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration halted");
            alert.setHeaderText("Error - party registered");
            alert.setContentText("This party is already accepted.");
            alert.show();
            return;
        }
        else if(waiting.contains(party)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration halted");
            alert.setHeaderText("Error - party in waiting");
            alert.setContentText("This party is currently being evaluated.");
            alert.show();
            return;
        }
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/confirmation.fxml"));
        ConfirmationController newController = new ConfirmationController();
        loader.setController(newController);
        Parent root = loader.load();

        myStage.setTitle("Confirmation required");
        myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.show();

        myStage.setOnHiding(e -> {
            if(newController.isAddStatus()){
                dao.registerNewParty(party);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration complete");
                alert.setHeaderText("Success");
                alert.setContentText("This party is now in the waitlist.");
                alert.show();
                Stage stage = (Stage) txtPartyName.getScene().getWindow();
                stage.close();
            }
        });
        return;
    }

    public void cancelRegistration(ActionEvent actionEvent) {
        Stage stage = (Stage) txtPartyName.getScene().getWindow();
        stage.close();
    }
}
