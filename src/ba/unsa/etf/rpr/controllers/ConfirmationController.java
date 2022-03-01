package ba.unsa.etf.rpr.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConfirmationController {

    public Button btnOk;
    boolean addStatus = false;

    public boolean isAddStatus() {
        return addStatus;
    }

    public void clkOk(ActionEvent actionEvent) {
        addStatus = true;
        clkCancel(new ActionEvent());
    }

    public void clkCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }
}
