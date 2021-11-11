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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author AngelLucas
 *
 */
public class ArticleDetailsController {
	//TODO add attributes and methods as needed
	    private User usr;
	    private Article article;
	    private Scene mainScene;
	    
	    @FXML
		private Label userName;
	    @FXML
	    private Label title;
	    @FXML
	    private Label subtitle;
	    @FXML
	    private Label category;
	    @FXML
	    private ImageView image;
	    @FXML
	    private TextArea body;
	    @FXML
	    private TextArea aAbstract;

	    @FXML
		void initialize() {
			assert title != null : "fx:id=\"title\" was not injected";
			assert subtitle != null : "fx:id=\"subtitle\" was not injected";
			assert category != null : "fx:id=\"category\" was not injected";
			assert image != null : "fx:id=\"image\" was not injected";
			assert aAbstract != null : "fx:id=\"aAbstract\" was not injected.";
			assert body != null : "fx:id=\"body\" was not injected.";
		}
	    
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
			this.title.setText(article.getTitle());
			this.subtitle.setText(article.getSubtitle());
			this.category.setText(article.getCategory());
			if (article.getImageData() != null) {
				this.image.setImage(article.getImageData());
			}
			String bodyText = article.getBodyText();
			String noHTMLString = bodyText.replaceAll("\\<.*?\\>", "");
			bodyText = noHTMLString.replaceAll("\\n", "");
			this.aAbstract.setText(article.getAbstractText());
            this.body.setText(article.getBodyText());
            aAbstract.setEditable(false);
            body.setEditable(false);
			}
		
		@FXML
		void back(ActionEvent e) {
			Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			primaryStage.setScene(mainScene);
		}
		
		@FXML
		void switchContent() {
			if (body.isVisible()) {
		    	aAbstract.setVisible(true);
		    	body.setVisible(false);
		    	return;
			}
			if (aAbstract.isVisible()){
			   	body.setVisible(true);
			   	aAbstract.setVisible(false);
		        return;
			}
		}
}