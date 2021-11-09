/**
 * 
 */
package application;

import java.io.File;
<<<<<<< HEAD
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.function.Predicate;

=======
import java.io.IOException;

import javax.json.JsonObject;
>>>>>>> 6497dc4f2eb468ea989664c129d4e5a43853acb4

import application.news.Article;
import application.news.Categories;
import application.news.User;
import application.utils.JsonArticle;
import application.utils.exceptions.ErrorMalFormedArticle;
<<<<<<< HEAD
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
=======
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
>>>>>>> 6497dc4f2eb468ea989664c129d4e5a43853acb4
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
<<<<<<< HEAD
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;
import serverConection.ConnectionManager;

/**
 * @author AngelLucas
=======
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import serverConection.ConnectionManager;
import serverConection.exceptions.ServerCommunicationError;

/**
 * @author ÁngelLucas
>>>>>>> 6497dc4f2eb468ea989664c129d4e5a43853acb4
 *
 */
public class NewsReaderController {

	private NewsReaderModel newsReaderModel = new NewsReaderModel();
<<<<<<< HEAD
	private User usr;

	//TODO add attributes and methods as needed

	public NewsReaderController() {
		//TODO
		//Uncomment next sentence to use data from server instead dummy data
		//newsReaderModel.setDummyData(false);
		//Get text Label
		
	}

		

	private void getData() {
		//TODO retrieve data and update UI
		//The method newsReaderModel.retrieveData() can be used to retrieve data  
=======
	private User usr = null;
	private Scene scene;

	private ObservableList<Article> articles;

	@FXML
	private Text newsUser;

	@FXML
	private Button loginButton;

	@FXML
	private Button logoutButton;

	@FXML
	private ListView<String> headlineList;

	@FXML
	private ComboBox<Categories> categoryFilter;

	@FXML
	private ImageView articleImage;

	@FXML
	private Text articleAbstract;

	@FXML
	private Button articleReadMore;

	@FXML
	private Button articleEdit;

	@FXML
	private Button articleDelete;

	private FilteredList<Article> filteredArticles;

	public NewsReaderController() {
		newsReaderModel.setDummyData(false);
	}

	private void getData() {
		newsReaderModel.retrieveData();
		this.loadArticles();
>>>>>>> 6497dc4f2eb468ea989664c129d4e5a43853acb4
	}

	/**
	 * @return the usr
	 */
	User getUsr() {
<<<<<<< HEAD
		return usr;
	}

	void setConnectionManager (ConnectionManager connection){
		this.newsReaderModel.setDummyData(false); //System is connected so dummy data are not needed
		this.newsReaderModel.setConnectionManager(connection);
		this.getData();
	}
	
=======
		return this.usr;
	}

	void setConnectionManager(ConnectionManager connection) {
		this.newsReaderModel.setDummyData(false); // System is connected so dummy data are not needed
		this.newsReaderModel.setConnectionManager(connection);
		this.getData();
	}

	void setScene(Scene scene) {
		this.scene = scene;
	}

	void updateScene() {
		this.clearScene();
		this.getData();
	}

>>>>>>> 6497dc4f2eb468ea989664c129d4e5a43853acb4
	/**
	 * @param usr the usr to set
	 */
	void setUsr(User usr) {
<<<<<<< HEAD
		
		this.usr = usr;
		//Reload articles
		this.getData();
		//TODO Update UI
	}

=======
		this.usr = usr;
		this.updateScene();
	}

	private void loadArticles() {

		if (usr != null) {
			newsUser.setText("User " + usr.getIdUser());
			loginButton.setDisable(true);
		} else {
			newsUser.setText("");
			loginButton.setDisable(false);
		}

		this.categoryFilter.setItems(newsReaderModel.getCategories());

		articles = newsReaderModel.getArticles();
		ObservableList<String> headlines = FXCollections.observableArrayList();
		for (int i = 0; i < articles.size(); i++) {
			headlines.add(articles.get(i).getTitle());
		}
		this.headlineList.setItems(headlines);

		this.headlineList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				for (int i = 0; i < articles.size(); i++) {
					if (articles.get(i).getTitle() == newValue) {
						Article article = articles.get(i);
						articleAbstract.setText(article.getAbstractText());
						articleImage.setImage(article.getImageData());

						if (usr != null && article.getIdUser() == usr.getIdUser()) {
							articleDelete.setDisable(false);
							articleEdit.setDisable(false);
						} else {
							articleDelete.setDisable(true);
							articleEdit.setDisable(true);
						}

						articleEdit.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								try {
									Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
									FXMLLoader loader = new FXMLLoader(
											getClass().getResource(AppScenes.EDITOR.getFxmlFile()));
									Scene articleScene = new Scene(loader.load());
									ArticleEditController controller = loader.<ArticleEditController>getController();
									controller.setArticle(article);
									controller.setConnectionMannager(newsReaderModel.getConnectionManager());
									controller.setUsr(usr);
									controller.setMainScene(scene);
									controller.setMainController(NewsReaderController.this);
									primaryStage.setScene(articleScene);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						});

						articleDelete.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								try {
									newsReaderModel.getConnectionManager().deleteArticle(article.getIdArticle());
									updateScene();
								} catch (ServerCommunicationError e2) {
									e2.printStackTrace();
								}
							}
						});

						articleReadMore.setDisable(false);
						articleReadMore.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								try {
									Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
									FXMLLoader loader = new FXMLLoader(
											getClass().getResource(AppScenes.NEWS_DETAILS.getFxmlFile()));
									Scene articleScene = new Scene(loader.load());
									ArticleDetailsController controller = loader
											.<ArticleDetailsController>getController();
									controller.setArticle(article);
									controller.setMainScene(scene);
									primaryStage.setScene(articleScene);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						});
					}
				}
			}
		});
	}

	private void clearScene() {
		this.headlineList.getItems().clear();
		this.categoryFilter.getSelectionModel().select(0);
		this.articleAbstract.setText("");
		this.articleImage.setImage(null);
		this.articleDelete.setDisable(true);
		this.articleEdit.setDisable(true);
		this.articleReadMore.setDisable(true);
	}

	@FXML
	void initialize() {
		assert headlineList != null : "fx:id=\"headlineList\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert categoryFilter != null : "fx:id=\"categoryMenu\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert articleImage != null : "fx:id=\"articleImage\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert articleAbstract != null : "fx:id=\"articleAbstract\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert articleReadMore != null : "fx:id=\"articleReadMore\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert articleEdit != null : "fx:id=\"articleEdit\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert articleDelete != null : "fx:id=\"articleDelete\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'NewsReader.fxml'.";
	}

	@FXML
	void updateCategory(ActionEvent event) {
		ObservableList<String> filteredHeadlines = FXCollections.observableArrayList();
		filteredArticles = new FilteredList<>(articles, article -> true);
		Object currentCategory = categoryFilter.getSelectionModel().selectedItemProperty().getValue();
		String strCategory = currentCategory.toString();

		if (strCategory.equals("All")) {
			for (int i = 0; i < articles.size(); i++) {
				filteredHeadlines.add(articles.get(i).getTitle());
			}
		} else {
			filteredArticles.setPredicate(article -> article.getCategory().toString().equals(strCategory));
			for (int i = 0; i < filteredArticles.size(); i++) {
				filteredHeadlines.add(filteredArticles.get(i).getTitle());
			}
		}
		this.headlineList.setItems(filteredHeadlines);
	}

	@FXML
	void exitApp(ActionEvent e) {
		System.exit(0);
	}

	@FXML
	void openLogin(ActionEvent e) {
		try {
			Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.LOGIN.getFxmlFile()));
			Scene loginScene = new Scene(loader.load());
			LoginController controller = loader.<LoginController>getController();
			controller.setConnectionManager(newsReaderModel.getConnectionManager());
			controller.setMainScene(scene);
			controller.setMainController(this);
			primaryStage.setScene(loginScene);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@FXML
	void createArticleAction(ActionEvent e) {
		try {
			Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.EDITOR.getFxmlFile()));
			Scene articleScene = new Scene(loader.load());
			ArticleEditController controller = loader.<ArticleEditController>getController();
			controller.setArticle(null);
			controller.setMainScene(scene);
			controller.setMainController(NewsReaderController.this);

			if (this.usr != null) {
				controller.setUsr(this.usr);
				controller.setConnectionMannager(newsReaderModel.getConnectionManager());
			}

			primaryStage.setScene(articleScene);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@FXML
	void loadArticleAction(ActionEvent e) throws ErrorMalFormedArticle {
		try {
			Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose Article");
			fileChooser.setInitialDirectory(new File("saveNews//"));
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("NEWS", "*.news"));
			File articleFile = fileChooser.showOpenDialog(primaryStage);
			if (articleFile != null) {
				JsonObject articleJson = (JsonObject) JsonArticle.readFile(articleFile.toString());
				Article article = JsonArticle.jsonToArticle(articleJson);

				FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.EDITOR.getFxmlFile()));
				Scene articleScene = new Scene(loader.load());
				ArticleEditController controller = loader.<ArticleEditController>getController();
				controller.setArticle(article);

				if (this.usr != null) {
					controller.setUsr(this.usr);
					controller.setConnectionMannager(newsReaderModel.getConnectionManager());
				}

				controller.setMainScene(scene);
				controller.setMainController(NewsReaderController.this);

				primaryStage.setScene(articleScene);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// Auxiliary methods
	private interface InitUIData<T> {
		void initUIData(T loader);
	}
>>>>>>> 6497dc4f2eb468ea989664c129d4e5a43853acb4

}
