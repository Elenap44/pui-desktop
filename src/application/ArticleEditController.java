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
 * Elena Maria Perez Perez
 * Remedios Pastor Molines
 * Abel Horvath
 */
public class ArticleEditController {
    private ConnectionManager connection;
	private ArticleEditModel editingArticle;
	private User usr;
	private Scene mainScene;
	private NewsReaderController mainController;
	//TODO add attributes and methods as needed

	@FXML
	private ImageView aImage;
	@FXML
	private TextField title;
	@FXML
	private TextField subtitle;
	@FXML
	private Text aCategory;
	@FXML
	private TextArea aAbstract;
	@FXML
	private TextArea body;
	@FXML
	private HTMLEditor abstractHTML;
	@FXML
	private HTMLEditor bodyHTML;
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
					this.aImage.setImage(image);
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
		String titleText = this.title.getText(); // TODO Get article title
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
		Article article = new Article();
		
		if (this.aAbstract.isVisible())
			textAbstract = this.aAbstract.getText();
		else
			textAbstract = this.abstractHTML.getHtmlText();

		if (this.body.isVisible()) {
			textBody = this.body.getText();
		}
		else {
			textBody = this.bodyHTML.getHtmlText();
		}
		this.editingArticle.titleProperty().set(titleText);
		this.editingArticle.subtitleProperty().set(this.subtitle.getText());
		this.editingArticle.abstractTextProperty().set(textAbstract);
		this.editingArticle.bodyTextProperty().set(textBody);
		this.editingArticle.setCategory(category);
		Image imagedata =  aImage.getImage();
        if (imagedata != null) {
        	article.setImageData(imagedata);
		}
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
		//this.backButton.setDisable(false);
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
			this.aImage.setImage(article.getImageData());
			this.title.setText(article.getTitle());
			this.subtitle.setText(article.getSubtitle());
			this.aCategory.setText(article.getCategory());
			this.aAbstract.setText(article.getAbstractText());
			this.body.setText(article.getBodyText());
			this.abstractHTML.setHtmlText(article.getAbstractText());
			this.bodyHTML.setHtmlText(article.getBodyText());
		}
		this.setCategories();
	}
	
	/**
	 * Save an article to a file in a json format
	 * Article must have a title
	 */
	private void write() {
		//TODO Consolidate all changes
		String textTitle= this.title.getText();
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
				textAbstract= this.abstractHTML.getHtmlText();

			if (this.body.isVisible())
				textBody = this.body.getText();
			else
				textBody = this.bodyHTML.getHtmlText();

			this.editingArticle.titleProperty().set(textTitle);
			this.editingArticle.subtitleProperty().set(this.subtitle.getText());
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
		assert title != null : "fx:id=\"title\" was not injected";
		assert subtitle != null : "fx:id=\"subtitle\" was not injected";
		assert aCategory != null : "fx:id=\"aCategory\" was not injected";
		assert aImage != null : "fx:id=\"aImage\" was not injected";
		assert aAbstract != null : "fx:id=\"aAbstract\" was not injected";
		assert body != null : "fx:id=\"body\" was not injected";
		assert abstractHTML != null : "fx:id=\"abstractHTML\" was not injected";
		assert bodyHTML != null : "fx:id=\"bodyHTML\" was not injected";
		assert categoryMenu != null : "fx:id=\"categoryMenu\" was not injected";
	}
	
	@FXML
	public void clickback(ActionEvent e) {
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.close();
		}

	@FXML
	void sendBack(ActionEvent e) {
		if (this.send()) {

	        Stage stage1 = (Stage) ((Node) e.getSource()).getScene().getWindow();
	    	stage1.close();
		}
		return;
	}

	@FXML
	void saveFile(ActionEvent e) {
		System.out.println("saving...");
		write();
		System.out.println("article draft saved!");
	}

	@FXML
	void switchType(ActionEvent e) {
		if (this.aAbstract.isVisible()) {
			this.aAbstract.setVisible(false);
			this.abstractHTML.setVisible(true);
			this.saveAbstract(false);
		} else if (this.abstractHTML.isVisible()) {
			this.aAbstract.setVisible(true);
			this.abstractHTML.setVisible(false);
			this.saveAbstract(true);
		} else if (this.body.isVisible()) {
			this.body.setVisible(false);
			this.bodyHTML.setVisible(true);
			this.saveBody(false);
		} else {
			this.body.setVisible(true);
			this.bodyHTML.setVisible(false);
			this.saveBody(true);
		}
	}

	@FXML
	void switchContent(ActionEvent e) {
		if (this.aAbstract.isVisible()) {
			this.body.setVisible(true);
			this.aAbstract.setVisible(false);
			this.saveAbstract(false);
		} else if (this.body.isVisible()) {
			this.aAbstract.setVisible(true);
			this.body.setVisible(false);
			this.saveBody(false);
		} else if (this.abstractHTML.isVisible()) {
			this.bodyHTML.setVisible(true);
			this.abstractHTML.setVisible(false);
			this.saveAbstract(true);
		} else {
			this.abstractHTML.setVisible(true);
			this.bodyHTML.setVisible(false);
			this.saveBody(true);
		}
	}

	private void saveAbstract(boolean checkHTML) {
		if (checkHTML)
			this.aAbstract.setText(this.abstractHTML.getHtmlText());
		else
			this.abstractHTML.setHtmlText(this.aAbstract.getText());
	}

	private void saveBody(boolean checkHTML) {
		if (checkHTML)
			this.body.setText(this.bodyHTML.getHtmlText());
		else
			this.bodyHTML.setHtmlText(this.body.getText());
	}
	
}