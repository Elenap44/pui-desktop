package application;


import application.news.User;
import javafx.scene.Scene;
import serverConection.ConnectionManager;



public class LoginController {
//TODO Add all attribute and methods as needed 
	private LoginModel loginModel = new LoginModel();
	
	private Scene mainScene;
	private NewsReaderController mainController;
	
	private User loggedUsr = null;

	private String username;
	private String password;
	
	/*@FXML
	private Button loginButton;

	@FXML
	private TextField userField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Text loginError;*/
	
	public LoginController (){
	//Uncomment next sentence to use data from server instead dummy data
	loginModel.setDummyData(false);
	}
	
	User getLoggedUsr() {
		return loggedUsr;
		
	}
		
	void setConnectionManager (ConnectionManager connection) {
		this.loginModel.setConnectionManager(connection);
	}
	
	void setMainScene(Scene scene) {
		this.mainScene = scene;
	}

	void setMainController(NewsReaderController c) {
		this.mainController = c;
	}
	
	/*@FXML
	void initialize() {
		assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'Login.fxml'.";
	}

	@FXML
	void loginAction(ActionEvent e) {
		username = userField.getText().toString();
		password = passwordField.getText().toString();

		User usr = loginModel.validateUser(username, password);
		if (usr == null) {
			System.out.print("credentials do not match");
			loginError.setText("These credentials do not match");
		} else {
			loggedUsr = usr;
			Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			mainController.setUsr(loggedUsr);
			primaryStage.setScene(mainScene);
		}
	}

	@FXML
	void cancelAction(ActionEvent e) {
		Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		primaryStage.setScene(mainScene);
	}*/
}