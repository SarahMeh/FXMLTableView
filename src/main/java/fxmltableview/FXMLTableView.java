package fxmltableview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FXMLTableView extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("FXML TableView Example");

		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_tableview.fxml"));
		Pane myPane = loader.load();
		FXMLTableViewController controller = loader.<FXMLTableViewController>getController();
		controller.initData();
		controller.connectToJDBC();

		Scene myScene = new Scene(myPane);
		primaryStage.setScene(myScene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}