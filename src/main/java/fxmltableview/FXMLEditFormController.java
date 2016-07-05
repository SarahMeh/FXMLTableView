package fxmltableview;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FXMLEditFormController {
	Stage tableViewStage;
	String origFirstName;
	@FXML
	private TextField firstNameEdit;
	@FXML
	private TextField lastNameEdit;
	@FXML
	private TextField emailEdit;
	@FXML
	private Button saveEdit;
	@FXML
	private Button cancelEdit;

	@FXML
	protected void editPerson(ActionEvent event) throws Exception {
		if (event.getSource() == cancelEdit) {
			Stage stage = (Stage) cancelEdit.getScene().getWindow();
			stage.close();
		}
		if (event.getSource() == saveEdit) {
			String firstName = firstNameEdit.getText(), lastName = lastNameEdit.getText(), email = emailEdit.getText();
			FXMLTableViewModel model = new FXMLTableViewModel();
			model.updateValues(origFirstName, firstName, lastName, email);

			Stage stage = (Stage) cancelEdit.getScene().getWindow();
			stage.close();

			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml_tableview.fxml"));
			Pane myPane = loader.load();
			FXMLTableViewController controller = loader.<FXMLTableViewController>getController();
			controller.initData();
			controller.connectToJDBC();

			Scene myScene = new Scene(myPane);
			tableViewStage.setScene(myScene);
			tableViewStage.show();
		}
	}

	public void initEditForm(String firstName, String lastName, String email, Stage stage) throws Exception {
		this.tableViewStage = stage;
		this.origFirstName = firstName;
		firstNameEdit.setText(firstName);
		lastNameEdit.setText(lastName);
		emailEdit.setText(email);
	}
}
