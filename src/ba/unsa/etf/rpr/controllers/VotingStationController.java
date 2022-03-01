package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.classes.PoliticalParty;
import ba.unsa.etf.rpr.classes.Voter;
import ba.unsa.etf.rpr.database_organization.VotingDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayDeque;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class VotingStationController {
    public TableView partiesList;
    public TableColumn partyColumn;
    public TableColumn leaderColumn;
    private VotingDAO dao;
    private Voter voter;
    private PoliticalParty party;

    public VotingStationController(Voter v){
        partiesList = new TableView();
        partyColumn = new TableColumn();
        leaderColumn = new TableColumn();
        dao = VotingDAO.getInstance();
        voter = v;
    }

    @FXML
    public void initialize(){ refresh(); }

    public void refresh(){
        partiesList.setItems(FXCollections.observableArrayList(dao.parties()));
        partyColumn.setCellValueFactory(new PropertyValueFactory<PoliticalParty, String>("name"));
        leaderColumn.setCellValueFactory(new PropertyValueFactory<PoliticalParty,String>("partyLeader"));
    }

    public void openHelp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Voting process");
        alert.setHeaderText("Help");
        alert.setContentText("Select the candidate that you will vote for, and confirm with the cast vote button." +
                "\nIf you wish to mark your ballot as empty/invalid/any other message, please click on the corresponding button.");
        alert.show();
        return;
    }

    public void openAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Voting process");
        alert.setHeaderText("About");
        alert.setContentText("DemokratiJA\nUnofficial voting aid prototype" +
                "Version 0.1, created by Faruk Sahat for the RPR final exam");
        alert.show();
    }

    public void castVote(ActionEvent actionEvent) {
        if(partiesList.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Voting process");
            alert.setHeaderText("Error");
            alert.setContentText("You have not selected any candidates.\nIf you wish to mark your ballot as invalid, click on the corresponding button.");
            alert.show();
            return;
        }
        party =(PoliticalParty) partiesList.getSelectionModel().getSelectedItem();
        //dao.castVote(party, voter);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Voting process");
        alert.setHeaderText("Success");
        alert.setContentText("Thank you for taking part in the democratic process.");
        alert.show();
        Stage stage = (Stage) partiesList.getScene().getWindow();
        stage.close();
    }

    public void invalidateBallot(ActionEvent actionEvent) throws IOException {
        Stage myStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/confirmation.fxml"));
        ConfirmationController noviController = new ConfirmationController();
        loader.setController(noviController);
        Parent root = loader.load();

        myStage.setTitle("Confirmation required");
        myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        myStage.show();

        myStage.setOnHiding(e -> {
            //if(noviController.getAddStatus()){
            //    dao.invalidateBallot(voter);
           //     Stage stage = (Stage) partiesList.getScene().getWindow();
            //    stage.close();
            //}
        });
    }

    public void cancelVoting(ActionEvent actionEvent) {
        Stage stage = (Stage) partiesList.getScene().getWindow();
        stage.close();
    }
}
