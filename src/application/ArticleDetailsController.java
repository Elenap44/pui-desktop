/**
 * 
 */
package application;

import application.news.Article;
import application.news.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * @author AngelLucas
 *
 */
public class ArticleDetailsController {

	private User usr;
	private Article article;
	private Scene mainScene;

	@FXML
	private Text articleTitle;

	@FXML
	private Text articleSubtitle;

	@FXML
	private Text articleCategory;

	@FXML
	private ImageView articleImage;

	@FXML
	private Text articleContent;

	/**
	 * @param usr the usr to set
	 */
	void setUsr(User usr) {
		this.usr = usr;
		if (usr == null) {
			return; // Not logged user
		}
		// TODO Update UI information
	}

	/**
	 * @param article the article to set
	 */
	void setArticle(Article article) {
		this.article = article;
		this.setData();
	}

	void setMainScene(Scene scene) {
		this.mainScene = scene;
	}

	private void setData() {
		this.articleTitle.setText(article.getTitle());
		this.articleSubtitle.setText(article.getSubtitle());
		this.articleCategory.setText(article.getCategory());
		this.articleImage.setImage(article.getImageData());
				
		String textBody = article.getBodyText();
		String noHTMLString = textBody.replaceAll("\\<.*?\\>", "");
		textBody = noHTMLString.replaceAll("\\n", "");	
		this.articleContent.setText(textBody);

	}

	@FXML
	void initialize() {
		assert articleTitle != null : "fx:id=\"articleTitle\" was not injected: check your FXML file 'ArticleDetails.fxml'.";
		assert articleSubtitle != null : "fx:id=\"articleSubtitle\" was not injected: check your FXML file 'ArticleDetails.fxml'.";
		assert articleCategory != null : "fx:id=\"articleCategory\" was not injected: check your FXML file 'ArticleDetails.fxml'.";
		assert articleImage != null : "fx:id=\"articleImage\" was not injected: check your FXML file 'ArticleDetails.fxml'.";
		assert articleContent != null : "fx:id=\"articleContent\" was not injected: check your FXML file 'ArticleDetails.fxml'.";
	}

	@FXML
	void backAction(ActionEvent e) {
		Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		primaryStage.setScene(mainScene);
	}

	@FXML
	void switchContentAction(ActionEvent e) {
		if (this.articleContent.getText().equals(this.article.getAbstractText()))
			this.articleContent.setText(article.getBodyText());
		else
			this.articleContent.setText(article.getAbstractText());
	}
}
