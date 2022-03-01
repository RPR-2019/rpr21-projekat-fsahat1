package ba.unsa.etf.rpr.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class PasswordController {
    public PasswordField fldPasswd;
    private boolean open;

    public PasswordController(boolean b){
        fldPasswd = new PasswordField();
        open = b;
    }

    public boolean isOpen() {
        return open;
    }

    public void login(ActionEvent actionEvent) throws IOException {
        if(fldPasswd.getText().equals("admin")){
            Stage myStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminpage.fxml"));
            AdminPageController newController = new AdminPageController(open);
            loader.setController(newController);
            Parent root = loader.load();

            myStage.setTitle("Admin page");
            myStage.setScene(new Scene(loader.getRoot(), USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            myStage.show();

            Stage stage = (Stage) fldPasswd.getScene().getWindow();
            stage.close();

            myStage.setOnHiding(e -> {
                open = newController.isOpenElection();
            });
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DemokratiJA");
            alert.setHeaderText("The password you entered is incorrect");
            alert.setContentText("Please try again.");
            alert.show();
            return;
        }
    }

    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) fldPasswd.getScene().getWindow();
        stage.close();
    }
}
