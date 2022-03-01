package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.classes.PoliticalParty;
import ba.unsa.etf.rpr.classes.Voter;
import ba.unsa.etf.rpr.database_organization.VotingDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayDeque;

public class VotingStationController {
    public TableView partiesList;
    public TableColumn partyColumn;
    public TableColumn leaderColumn;
    private VotingDAO dao;
    private Voter voter;

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
    }

    public void openAbout(ActionEvent actionEvent) {
    }

    public void castVote(ActionEvent actionEvent) {
    }

    public void invalidateBallot(ActionEvent actionEvent) {
    }

    public void cancelVoting(ActionEvent actionEvent) {
    }
}
