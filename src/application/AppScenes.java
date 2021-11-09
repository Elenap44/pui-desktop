/**
 * 
 */
package application;

/**
 * Contain all app scenes
<<<<<<< HEAD
 * @author ÁngelLucas
=======
 * @author AngelLucas
>>>>>>> 6497dc4f2eb468ea989664c129d4e5a43853acb4
 *
 */
public enum AppScenes {
 LOGIN("Login.fxml"), READER("NewsReader.fxml"), 
 NEWS_DETAILS ("ArticleDetails.fxml"),
 EDITOR("ArticleEdit.fxml"), ADMIN("AdminNews.fxml")
		 ,IMAGE_PICKER("ImagePicker.fxml")
		 /*,IMAGE_PICKER("ImagePickerMaterailDesign.fxml")*/; 
 private String fxmlFile;
 
 private AppScenes (String file){
	 this.fxmlFile = file;
 }
 
 public String getFxmlFile()
 {
	 return this.fxmlFile;
 }
 
}
