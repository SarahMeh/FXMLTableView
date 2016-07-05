package fxmltableview;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FXMLTableViewController {

	final ObservableList<Person> data = FXCollections.observableArrayList();
	FXMLTableViewModel model;
	@FXML
	public TableView<Person> table;
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField emailField;

	public void connectToJDBC() {
		try {
			model.createTable();
			model.selectValues();
			table.setItems(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initData() throws Exception {
		model = new FXMLTableViewModel(this);
		table.setEditable(true);
		table.setRowFactory(new Callback<TableView<Person>, TableRow<Person>>() {
			public TableRow<Person> call(TableView<Person> tableView) {
				final TableRow<Person> row = new TableRow<Person>();
				final ContextMenu contextMenu = new ContextMenu();
				final MenuItem removeMenuItem = new MenuItem("Delete");
				removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						table.getItems().remove(row.getItem());
						try {
							model.deleteValues(row.getItem().getFirstName());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				final MenuItem editMenuItem = new MenuItem("Edit");
				editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						try {
							Stage primaryStage = (Stage) firstNameField.getScene().getWindow();
							FXMLLoader loader = new FXMLLoader(
									getClass().getClassLoader().getResource("edit_form.fxml"));
							Pane myPane = loader.load();
							FXMLEditFormController ctrl = loader.<FXMLEditFormController>getController();
							ctrl.initEditForm(row.getItem().getFirstName(), row.getItem().getLastName(),
									row.getItem().getEmail(), primaryStage);

							Scene myScene = new Scene(myPane);
							Stage stage = new Stage();
							stage.setTitle("FXML Edit Form");
							stage.setScene(myScene);
							stage.show();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				contextMenu.getItems().add(editMenuItem);
				contextMenu.getItems().add(removeMenuItem);
				row.contextMenuProperty()
						.bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
				return row;
			}
		});
	}

	@FXML
	protected void addPerson(ActionEvent event) {
		ObservableList<Person> data = table.getItems();
		data.add(new Person(firstNameField.getText(), lastNameField.getText(), emailField.getText()));

		try {
			model.insertValues(firstNameField.getText(), lastNameField.getText(), emailField.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}

		firstNameField.setText("");
		lastNameField.setText("");
		emailField.setText("");
	}
}
