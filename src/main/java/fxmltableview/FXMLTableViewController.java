package fxmltableview;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class FXMLTableViewController {
	Connection conn = null;
	private final ObservableList<Person> data = FXCollections.observableArrayList(
	// new Person("Jacob", "Smith", "jacob.smith@example.com"),
	// new Person("Isabella", "Johnson", "isabella.johnson@example.com"),
	// new Person("Ethan", "Williams", "ethan.williams@example.com"),
	// new Person("Emma", "Jones", "emma.jones@example.com"),
	// new Person("Michael", "Brown", "michael.brown@example.com")
	);
	@FXML
	private TableView<Person> table;
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField emailField;

	public Connection connectToJDBC() {
		String sDriverName = "org.sqlite.JDBC";

		try {
			Class.forName(sDriverName);

			String url = "jdbc:sqlite:addressbook.db";
			conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS addressbook (\n" + "	id integer PRIMARY KEY,\n"
					+ "	firstname text,\n" + "	lastname text,\n" + "	email text" + ");";

			stmt.execute(sql);

			ResultSet rs = stmt.executeQuery("SELECT * FROM addressbook");
			while (rs.next()) {
				data.add(new Person(rs.getString("firstname"), rs.getString("lastname"), rs.getString("email")));
			}
			table.setItems(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void initData() {
		table.setEditable(true);
		// table.setItems(data);
		table.setRowFactory(new Callback<TableView<Person>, TableRow<Person>>() {
			public TableRow<Person> call(TableView<Person> tableView) {
				final TableRow<Person> row = new TableRow<Person>();
				final ContextMenu contextMenu = new ContextMenu();
				final MenuItem removeMenuItem = new MenuItem("Remove");
				removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						table.getItems().remove(row.getItem());
						Statement stmt;
						try {
							stmt = conn.createStatement();
							stmt.executeUpdate("DELETE FROM addressbook WHERE firstname = \'"
									+ row.getItem().getFirstName() + "\'");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				contextMenu.getItems().add(removeMenuItem);
				// Set context menu on row, but use a binding to make it only
				// show for non-empty rows:
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
			Connection conn = this.conn;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM addressbook");
			String maxId = rs.getString("MAX(id)");
			int id = 1;
			if (maxId != null)
				id = (Integer.parseInt(maxId) + 1);
			stmt.executeUpdate("INSERT INTO addressbook VALUES (" + id + ",\'" + firstNameField.getText() + "\',\'"
					+ lastNameField.getText() + "\',\'" + emailField.getText() + "\')");
		} catch (Exception e) {
			e.printStackTrace();
		}

		firstNameField.setText("");
		lastNameField.setText("");
		emailField.setText("");
	}
}