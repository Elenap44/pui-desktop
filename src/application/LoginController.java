package application;

/**
 * @author ÁngelLucas
 * Elena Maria Perez Perez
 * Remedios Pastor Molines
 * Abel Horvath
 */

import application.news.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import serverConection.ConnectionManager;

public class LoginController {

	private LoginModel loginModel = new LoginModel();
	private Scene mainScene;
	private NewsReaderController mainController;

	private User loggedUsr = null;

	private String username;
	private String password;

	@FXML
	private Button loginButton;

	@FXML
	private TextField userName;

	@FXML
	private PasswordField psswrd;

	@FXML
	private Text loginError;

	public LoginController() {
		loginModel.setDummyData(false);
	}

	User getLoggedUsr() {
		return loggedUsr;

	}

	void setConnectionManager(ConnectionManager connection) {
		this.loginModel.setConnectionManager(connection);
	}

	void setMainScene(Scene scene) {
		this.mainScene = scene;
	}

	void setMainController(NewsReaderController c) {
		this.mainController = c;
	}

	@FXML
	void initialize() {
		assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'Login.fxml'.";
	}

	@FXML
	void loginAction(ActionEvent e) {
		username = userName.getText().toString();
		password = psswrd.getText().toString();

		User usr = loginModel.validateUser(username, password);
		if (usr == null) {
			System.out.print("credentials do not match");
			loginError.setText("These credentials do not match");
		} else {
			loggedUsr = usr;
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
	    	stage.close();
		}
	}

	@FXML
	void cancel(ActionEvent e) {
		Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		primaryStage.close();
	}
}