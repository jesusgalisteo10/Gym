package com.example.view;

import com.example.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button adminWayButton;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * Sets the action to navigate to the admin way (main page).
     *
     * @throws Exception If an error occurs while changing the scene to the main page.
     */
    public void setAdminWayButton() throws Exception {
        App.currentController.changeScene(Scenes.MAINPAGE, null);
    }

}
