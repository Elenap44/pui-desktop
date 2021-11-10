/**
 * 
 */
package application;

import java.io.FileWriter;
import java.io.IOException;

import javax.json.JsonObject;


import application.news.Article;
import application.news.Categories;
import application.news.User;
import application.utils.JsonArticle;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import serverConection.ConnectionManager;
import serverConection.exceptions.ServerCommunicationError;

/**
 * @author AngelLucas
 *
 */
public class ArticleEditController {
    private ConnectionManager connection;
	private ArticleEditModel editingArticle;
	private User usr;
	private Scene mainScene;
	private NewsReaderController mainController;
	//TODO add attributes and methods as needed
	@FXML
	private Text pageTitle;
	@FXML
	private ImageView aImage;
	@FXML
	private TextField aTitle;
	@FXML
	private TextField aSubtitle;
	@FXML
	private Text aCategory;
	@FXML
	private TextArea aAbstract;
	@FXML
	private TextArea aBody;
	@FXML
	private HTMLEditor aAbstractHTML;
	@FXML
	private HTMLEditor aBodyHTML;
	@FXML
	private Button backButton;
	@FXML
	private MenuButton categoryMenu;
	

	void setMainController(NewsReaderController i) {
		this.mainController = i;
	}

	void setMainScene(Scene scene1) {
		this.mainScene = scene1;
	}

	void setCategories() {
		for (Categories category:Categories.values()) {
			final MenuItem menuItem=new MenuItem(category.toString());
			menuItem.setId(category.toString());
			menuItem.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e) {
					aCategory.setText(menuItem.getId());
				}
			});
			this.categoryMenu.getItems().add(menuItem);
		}
	}



	@FXML
	void onImageClicked(MouseEvent event) {
		if (event.getClickCount() >= 2) {
			Scene parentScene = ((Node) event.getSource()).getScene();
			FXMLLoader loader = null;
			try {
				loader = new FXMLLoader(getClass().getResource(AppScenes.IMAGE_PICKER.getFxmlFile()));
				Pane root = loader.load();
				// Scene scene = new Scene(root, 570, 420);
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Window parentStage = parentScene.getWindow();
				Stage stage = new Stage();
				stage.initOwner(parentStage);
				stage.setScene(scene);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.initModality(Modality.WINDOW_MODAL);
				stage.showAndWait();
				ImagePickerController controller = loader.<ImagePickerController>getController();
				Image image = controller.getImage();
				if (image != null) {
					editingArticle.setImage(image);
					//TODO Update image on UI
					aImage.setImage(image);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Send and article to server,
	 * Title and category must be defined and category must be different to ALL
	 * @return true if the article has been saved
	 */
	private boolean send() {
		String titleText = this.aTitle.getText(); // TODO Get article title
		Categories category = Categories.valueOf(this.aCategory.getText().toUpperCase()); //TODO Get article category
		if (titleText == null || category == null || 
				titleText.equals("") || category == Categories.ALL) {
			Alert alert = new Alert(AlertType.ERROR, "Imposible send the article!! Title and categoy are mandatory", ButtonType.OK);
			alert.showAndWait();
			return false;
		}
//TODO prepare and send using connection.saveArticle( ...)
		String textAbstract;
		String textBody;
		
		if (this.aAbstract.isVisible())
			textAbstract = this.aAbstract.getText();
		else
			textAbstract = this.aAbstractHTML.getHtmlText();

		if (this.aBody.isVisible()) {
			textBody = this.aBody.getText();
		}
		else {
			textBody = this.aBodyHTML.getHtmlText();
		}
		this.editingArticle.titleProperty().set(titleText);
		this.editingArticle.subtitleProperty().set(this.aSubtitle.getText());
		this.editingArticle.abstractTextProperty().set(textAbstract);
		this.editingArticle.bodyTextProperty().set(textBody);
		this.editingArticle.setCategory(category);
		try {
			this.editingArticle.commit();
			connection.saveArticle(getArticle());
		} catch (ServerCommunicationError e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * This method is used to set the connection manager which is
	 * needed to save a news 
	 * @param connection connection manager
	 */
	void setConnectionMannager(ConnectionManager connection) {
		this.connection = connection;
		//TODO enable send and back button
		this.backButton.setDisable(false);
	}

	/**
	 * 
	 * @param usr the usr to set
	 */
	void setUsr(User usr) {
		this.usr = usr;
		//TODO Update UI and controls 
		
	}

	Article getArticle() {
		Article result = null;
		if (this.editingArticle != null) {
			result = this.editingArticle.getArticleOriginal();
		}
		return result;
	}

	/**
	 * PRE: User must be set
	 * 
	 * @param article
	 *            the article to set
	 */
	void setArticle(Article article) {
		this.editingArticle = (article != null) ? new ArticleEditModel(article) : new ArticleEditModel(usr);
		//TODO update UI
		if(article!=null) {
			this.pageTitle.setText("Edit Article");
			this.aImage.setImage(article.getImageData());
			this.aTitle.setText(article.getTitle());
			this.aSubtitle.setText(article.getSubtitle());
			this.aCategory.setText(article.getCategory());
			this.aAbstract.setText(article.getAbstractText());
			this.aBody.setText(article.getBodyText());
			this.aAbstractHTML.setHtmlText(article.getAbstractText());
			this.aBodyHTML.setHtmlText(article.getBodyText());
		}
		this.setCategories();
	}
	
	/**
	 * Save an article to a file in a json format
	 * Article must have a title
	 */
	private void write() {
		//TODO Consolidate all changes
		String textTitle= this.aTitle.getText();
		if (textTitle == null || textTitle.equals("")) {
			Alert alert = new Alert(AlertType.INFORMATION, "Enter a title to save the article.");
			alert.showAndWait();
		} else {
			Categories category = Categories.valueOf(this.aCategory.getText().toUpperCase());
			String textAbstract;
			String textBody;

			if (this.aAbstract.isVisible())
				textAbstract = this.aAbstract.getText();
			else
				textAbstract= this.aAbstractHTML.getHtmlText();

			if (this.aBody.isVisible())
				textBody = this.aBody.getText();
			else
				textBody = this.aBodyHTML.getHtmlText();

			this.editingArticle.titleProperty().set(textTitle);
			this.editingArticle.subtitleProperty().set(this.aSubtitle.getText());
			this.editingArticle.abstractTextProperty().set(textAbstract);
			this.editingArticle.bodyTextProperty().set(textBody);
			this.editingArticle.setCategory(category);
			this.editingArticle.commit();
			
			//Removes special characters not allowed for filenames
		String name = this.getArticle().getTitle().replaceAll("\\||/|\\\\|:|\\?","");
		String fileName ="saveNews//"+name+".news";
		JsonObject data = JsonArticle.articleToJson(this.getArticle());
		  try (FileWriter file = new FileWriter(fileName)) {
	            file.write(data.toString());
	            file.flush();
				Alert alert = new Alert(AlertType.INFORMATION, "The article has been saved.");
				alert.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	@FXML
	void initialize() {
		assert aTitle != null : "fx:id=\"articleTitle\" was not injected";
		assert aSubtitle != null : "fx:id=\"articleSubtitle\" was not injected";
		assert aCategory != null : "fx:id=\"articleCategory\" was not injected";
		assert pageTitle != null : "fx:id=\"pageTitle\" was not injected";
		assert aImage != null : "fx:id=\"articleImage\" was not injected";
		assert aAbstract != null : "fx:id=\"articleAbstractText\" was not injected";
		assert aBody != null : "fx:id=\"articleBodyText\" was not injected";
		assert aAbstractHTML != null : "fx:id=\"articleAbstractHTML\" was not injected";
		assert aBodyHTML != null : "fx:id=\"articleBodyHTML\" was not injected";
		assert categoryMenu != null : "fx:id=\"categoryMenu\" was not injected";
	}
	@FXML
	void backAction(ActionEvent e) {
		this.editingArticle.discardChanges();
		Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		primaryStage.setScene(mainScene);
	}

	@FXML
	void sendBackAction(ActionEvent e) {
		if (this.send()) {
			Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			this.mainController.updateScene();
			primaryStage.setScene(mainScene);
		}
		return;
	}

	@FXML
	void saveFileAction(ActionEvent e) {
		System.out.println("saving...");
		write();
		System.out.println("article draft saved!");
	}

	@FXML
	void switchTypeAction(ActionEvent e) {
		if (this.aAbstract.isVisible()) {
			this.aAbstract.setVisible(false);
			this.aAbstractHTML.setVisible(true);
			this.saveAbstract(false);
		} else if (this.aAbstractHTML.isVisible()) {
			this.aAbstract.setVisible(true);
			this.aAbstractHTML.setVisible(false);
			this.saveAbstract(true);
		} else if (this.aBody.isVisible()) {
			this.aBody.setVisible(false);
			this.aBodyHTML.setVisible(true);
			this.saveBody(false);
		} else {
			this.aBody.setVisible(true);
			this.aBodyHTML.setVisible(false);
			this.saveBody(true);
		}
	}

	@FXML
	void switchContentAction(ActionEvent e) {
		if (this.aAbstract.isVisible()) {
			this.aBody.setVisible(true);
			this.aAbstract.setVisible(false);
			this.saveAbstract(false);
		} else if (this.aBody.isVisible()) {
			this.aAbstract.setVisible(true);
			this.aBody.setVisible(false);
			this.saveBody(false);
		} else if (this.aAbstractHTML.isVisible()) {
			this.aBodyHTML.setVisible(true);
			this.aAbstractHTML.setVisible(false);
			this.saveAbstract(true);
		} else {
			this.aAbstractHTML.setVisible(true);
			this.aBodyHTML.setVisible(false);
			this.saveBody(true);
		}
	}

	private void saveAbstract(boolean checkHTML) {
		if (checkHTML)
			this.aAbstract.setText(this.aAbstractHTML.getHtmlText());
		else
			this.aAbstractHTML.setHtmlText(this.aAbstract.getText());
	}

	private void saveBody(boolean checkHTML) {
		if (checkHTML)
			this.aBody.setText(this.aBodyHTML.getHtmlText());
		else
			this.aBodyHTML.setHtmlText(this.aBody.getText());
	}
	
	
	
	
}