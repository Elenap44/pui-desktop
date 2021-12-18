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
import javafx.event.Event;
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
import javafx.scene.control.MenuButton;
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
 * Elena Maria Perez Perez
 * Remedios Pastor Molines
 * Abel Horvath
 */
public class NewsReaderController {

	@FXML
	private Label newsUser;
	
	private Scene scene;
	
	@FXML
    private ListView<Article> headlineList;
	
	
	@FXML
	private ImageView articleImage;
	
	@FXML
	private WebView articleAbstract;
	
	@FXML
	private ComboBox<Categories> categoryFilter;
	
	@FXML
	private Button login;

	@FXML
	private Button newA;
	
	@FXML
	private Button loadArticle;
	
	@FXML
	private Button edit;
	
	@FXML
	private Button delete;

	@FXML
	private Button exit;

	@FXML
	private Button readMoreButton;
	
    private FilteredList<Article> filteredArticles;
	private NewsReaderModel newsReaderModel = new NewsReaderModel();
	private User usr;
	
	Article Article;
	
	private ConnectionManager connectionManager;

	//TODO add attributes and methods as needed


	public NewsReaderController() {
		//TODO
		//Uncomment next sentence to use data from server instead dummy data
		newsReaderModel.setDummyData(false);
		//Get text Label
	}
	
	@FXML
	void initialize() {
		System.out.print("initialize function");

		headlineList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Article>() {
			 @Override
				public void changed(ObservableValue<? extends Article> observable, Article oldValue, Article newValue) {
					if (newValue != null){
						Article = newValue;
						articleAbstract.getEngine().loadContent(Article.getAbstractText());;
						articleImage.setImage(Article.getImageData());
						articleAbstract.setDisable(false);
					}
					else { //Nothing selected

					}
				}
	 });
		
	}


	 @FXML
	    void updateCategory(ActionEvent event) {
	    	String filterText = this.categoryFilter.getValue().toString();

	    	if(filterText == "All") {
		    	filteredArticles.setPredicate(article -> true);
	    	}else {
		    	filteredArticles.setPredicate(article -> article.getCategory().equals(filterText));
	    	}
	    	
	    	this.headlineList.setItems(filteredArticles);
	    }
	

	private void getData() {
		 newsReaderModel.retrieveData();
		 categoryFilter.getItems().addAll(newsReaderModel.getCategories());
		 categoryFilter.getSelectionModel().selectFirst();
		 
    	filteredArticles = new FilteredList<>(newsReaderModel.getArticles(), article -> true);
    	
    	this.headlineList.setItems(filteredArticles);
    	
		headlineList.getSelectionModel().selectFirst();
    	
    	if(this.usr == null) {
    		this.delete.setDisable(true);
    		this.edit.setDisable(true);
    	}else {
    		this.delete.setDisable(false);
    		this.edit.setDisable(false);
    	}
	}

	/**
	 * @return the usr
	 */
	User getUsr() {
		return usr;
	}

	void setConnectionManager (ConnectionManager connection){
		this.connectionManager = connection;
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
		this.newsUser.setText(this.usr.getLogin());
	}
	
	public void CNew(ActionEvent e) {
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
	
	public void CEdit(Event e) {
		NewScene(AppScenes.EDITOR, this.Article, e);
	}
	
	public void CObserve(Event e) {
		NewScene(AppScenes.NEWS_DETAILS, this.Article, e);
	}
	
	public void CLogin(Event e) {
		NewScene(AppScenes.LOGIN, null,e );
	}
	
	public void CDelete() {
		newsReaderModel.deleteArticle(this.Article);
		getData();
		
	}
	
	
	@FXML
	public void LoadArticleFromFile(Event e) {
		FileChooser chooser =  new FileChooser();
		chooser.setTitle("");
		
		chooser.getExtensionFilters().addAll(

		new FileChooser.ExtensionFilter("All news", "*.news"));
		File file = chooser.showOpenDialog(new Stage());
		if (file == null) {
			return;
		}
		
		try {
			Article article = JsonArticle.jsonToArticle(JsonArticle.readFile(file.getAbsolutePath()));
			NewScene(AppScenes.EDITOR, article, e);
		} catch (ErrorMalFormedArticle ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}
	

	@FXML
	void CExit(ActionEvent e) {
		System.exit(0);
	}
	

	
	public void NewScene(AppScenes scene, Article article, Event event) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader (getClass().getResource(
			scene.getFxmlFile()));
			Pane root = loader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle(scene.toString());
            stage.setScene(new Scene(root));
            
            if(scene == AppScenes.LOGIN) {
            	
            	LoginController controller = loader.<LoginController>getController();
    			controller.setConnectionManager(this.connectionManager);
    			
    			stage.showAndWait();
    			
            	User loggedInUsr = controller.getLoggedUsr();
            	
            	if (loggedInUsr != null) {
            		setUsr(loggedInUsr);
            	}
            	return;
            	
			 } else if (scene == AppScenes.EDITOR) {
	            	ArticleEditController controller = loader.<ArticleEditController>getController();
	            	
	            	if (article != null) {
	            		controller.setArticle(article);
	            	
					}
	            	controller.setConnectionMannager(this.connectionManager);
	            	controller.setUsr(usr);
	            	stage.showAndWait();
	            	
	            	getData();
	            	return;
	            	
				}
            else if(scene == AppScenes.NEWS_DETAILS) {
				 ArticleDetailsController controller = loader.<ArticleDetailsController>getController();
	            	
	             if (article != null) {
	            	controller.setArticle(article);
				 }
	            	
	             controller.setUsr(usr);
	             stage.show();
	             return;
            }else {
            	 stage.show();
            	 NewsReaderController controller = loader.<NewsReaderController>getController();
            	
            }

		} catch(IOException e){
			e.printStackTrace();
		}
		
	}


}