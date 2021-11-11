/**
 * 
 */
package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.function.Predicate;

import javax.json.JsonObject;

import application.news.Article;
import application.news.Categories;
import application.news.User;
import application.utils.JsonArticle;
import application.utils.exceptions.ErrorMalFormedArticle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;
import serverConection.ConnectionManager;
import serverConection.exceptions.ServerCommunicationError;

/**
 * @author AngelLucas
 *
 */
public class NewsReaderController {

	private NewsReaderModel newsReaderModel = new NewsReaderModel();
	private User usr;

	//TODO add attributes and methods as needed
	
	private Scene scene;

	private ObservableList<Article> articles;
	private FilteredList<Article> filteredArticles;
	

	@FXML
	private Button readMoreButton;
	
	@FXML
	private Text newsUser;


	@FXML
	private ListView<String> headlineList;

	@FXML
	private ComboBox<Categories> categoryFilter;

	@FXML
	private ImageView articleImage;

	@FXML
	private TextArea articleAbstract;

	@FXML
	private MenuItem loadArticle;

	@FXML
	private MenuItem login;

	@FXML
	private MenuItem newArticle;
	
	@FXML
	private MenuItem edit;
	
	@FXML
	private MenuItem delete;

	@FXML
	private MenuItem exit;

	public NewsReaderController() {
		//TODO
		//Uncomment next sentence to use data from server instead dummy data
		newsReaderModel.setDummyData(false);
		//Get text Label
		
	}

		

	private void getData() {
		//TODO retrieve data and update UI
		//The method newsReaderModel.retrieveData() can be used to retrieve data  
		newsReaderModel.retrieveData();
		this.loadArticles();
	}

	/**
	 * @return the usr
	 */
	User getUsr() {
		return usr;
	}

	void setConnectionManager (ConnectionManager connection){
		this.newsReaderModel.setDummyData(false); //System is connected so dummy data are not needed
		this.newsReaderModel.setConnectionManager(connection);
		this.getData();
	}
	
	/**
	 * @param usr the usr to set
	 */
	void setUsr(User usr) {
		
		this.usr = usr;
		//Reload articles
		this.getData();
		//TODO Update UI
		this.updateScene();
	}
	
	void setScene(Scene scene) {
		this.scene = scene;
	}

	void updateScene() {
		this.clearScene();
		this.getData();
	}
	
	private void loadArticles() {

		if (usr != null) {
			newsUser.setText("User " + usr.getIdUser());
			login.setDisable(true);
		} else {
			newsUser.setText("");
			login.setDisable(false);
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
							delete.setDisable(false);
							edit.setDisable(false);
						} else {
							delete.setDisable(true);
							edit.setDisable(true);
						}

						edit.setOnAction(new EventHandler<ActionEvent>() {
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

						delete.setOnAction(new EventHandler<ActionEvent>() {
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

						readMoreButton.setDisable(false);
						readMoreButton.setOnAction(new EventHandler<ActionEvent>() {
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
		this.delete.setDisable(true);
		this.edit.setDisable(true);
		this.readMoreButton.setDisable(true);
	}
	
	@FXML
	void initialize() {
		assert headlineList != null : "fx:id=\"headlineList\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert categoryFilter != null : "fx:id=\"categoryFilter\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert articleImage != null : "fx:id=\"articleImage\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert articleAbstract != null : "fx:id=\"articleAbstract\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert readMoreButton != null : "fx:id=\"readMoreButton\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert edit != null : "fx:id=\"edit\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert delete != null : "fx:id=\"delete\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'NewsReader.fxml'.";
		assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'NewsReader.fxml'.";
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


}
