package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.classes.PoliticalParty;
import ba.unsa.etf.rpr.database_organization.VotingDAO;
import ba.unsa.etf.rpr.report.PrintReport;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {
    public TableColumn tblParty;
    public TableColumn tblLeader;

    public TableView tableWaitlist;
    public CheckBox btnPolling = new CheckBox();
    private VotingDAO dao;
    boolean openElection = false;

    public AdminPageController(boolean b){
        tblLeader = new TableColumn();
        tblParty = new TableColumn();
        btnPolling.setSelected(b);
        dao = VotingDAO.getInstance();
    }

    public boolean isOpenElection() {
        return openElection;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
    }

    public void refresh(){
        tableWaitlist.setItems(FXCollections.observableArrayList(dao.waitlist()));
        tblParty.setCellValueFactory(new PropertyValueFactory<PoliticalParty, String>("name"));
        tblLeader.setCellValueFactory(new PropertyValueFactory<PoliticalParty,String>("partyLeader"));
    }

    public void toggleElection(ActionEvent actionEvent) {
        openElection = btnPolling.isSelected();
    }

    public void approveApplication(ActionEvent actionEvent) {
        if(!tableWaitlist.getSelectionModel().isEmpty()){
            PoliticalParty party = (PoliticalParty) tableWaitlist.getSelectionModel().getSelectedItem();
            dao.addElectableParty(party);
        }
        refresh();
    }

    public void denyApplication(ActionEvent actionEvent) {
        if(!tableWaitlist.getSelectionModel().isEmpty()){
            PoliticalParty party = (PoliticalParty) tableWaitlist.getSelectionModel().getSelectedItem();
            dao.deleteParty(party);
        }
        refresh();
    }

    public void loadFromFile(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose input file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("party list files", "txt"));
        File chosen = chooser.showOpenDialog(tableWaitlist.getScene().getWindow());
        if(chosen==null) return;
        dao.writeFromFile(chosen);
    }

    public void outputIntoFile(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose output file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("results files", "elres"));
        File chosen = chooser.showSaveDialog(tableWaitlist.getScene().getWindow());
        if(chosen==null) return;
        dao.writeToFile(chosen);
    }

    public void getReport(ActionEvent actionEvent) {
        try {
            new PrintReport().showReport(dao.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }

    }

    public void openAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DemokratiJA");
        alert.setHeaderText("About");
        alert.setContentText("DemokratiJA\nUnofficial voting aid prototype" +
                "Version 0.1, created by Faruk Sahat for the RPR final exam");
        alert.show();
    }

    public void exitProgram(ActionEvent actionEvent) {
        Stage stage = (Stage) btnPolling.getScene().getWindow();
        stage.close();
    }

    public void resetElection(ActionEvent actionEvent) {
        dao.resetElection();
    }

}
