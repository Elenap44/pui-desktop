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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Ã�ngelLucas
 *
 */
public class ArticleDetailsController {
	//TODO add attributes and methods as needed
	    private User usr;
	    private Article article;
	    private Scene mainScene;
	    
	    @FXML
	    private Text aTitle;
	    @FXML
	    private Text aSubtitle;
	    @FXML
	    private Text aCategory;
	    @FXML
	    private ImageView aImage;
	    @FXML
	    private Text aContent;

	    

		/**
		 * @param usr the usr to set
		 */
		void setUsr(User usr) {
			this.usr = usr;
			if (usr == null) {
				return; //Not logged user
			}
			//TODO Update UI information
		}

		/**
		 * @param article the article to set
		 */
		void setMainScene(Scene scene1) {
			this.mainScene = scene1;
		}
		void setArticle(Article article) {
			this.article = article;
			this.setData();
			//TODO complete this method
		}
		private void setData() {
			this.aTitle.setText(article.getTitle());
			this.aSubtitle.setText(article.getSubtitle());
			this.aCategory.setText(article.getCategory());
			this.aImage.setImage(article.getImageData());
			String bodyText = article.getBodyText();
			String noHTMLString = bodyText.replaceAll("\\<.*?\\>", "");
			bodyText = noHTMLString.replaceAll("\\n", "");
			this.aContent.setText(bodyText);
			}
		@FXML
		void initialize() {
			assert aTitle != null : "fx:id=\"articleTitle\" was not injected";
			assert aSubtitle != null : "fx:id=\"articleSubtitle\" was not injected";
			assert aCategory != null : "fx:id=\"articleCategory\" was not injected";
			assert aImage != null : "fx:id=\"articleImage\" was not injected";
			assert aContent != null : "fx:id=\"articleContent\" was not injected.";
		}
		@FXML
		void backAction(ActionEvent e) {
			Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			primaryStage.setScene(mainScene);
		}
		@FXML
		void switchContentAction(ActionEvent e) {
			if (this.aContent.getText().equals(this.article.getAbstractText()))
				this.aContent.setText(article.getBodyText());
			else
				this.aContent.setText(article.getAbstractText());
		}
}