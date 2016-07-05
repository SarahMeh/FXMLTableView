package fxmltableview;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class FXMLTableViewModel {
	Connection conn = null;
	FXMLTableViewController controller;

	public FXMLTableViewModel(FXMLTableViewController controller) throws Exception {
		this.controller = controller;
		String sDriverName = "org.sqlite.JDBC";
		Class.forName(sDriverName);

		String url = "jdbc:sqlite:addressbook.db";
		conn = DriverManager.getConnection(url);
	}

	public FXMLTableViewModel() throws Exception {
		String sDriverName = "org.sqlite.JDBC";
		Class.forName(sDriverName);

		String url = "jdbc:sqlite:addressbook.db";
		conn = DriverManager.getConnection(url);
	}

	public void createTable() throws Exception {
		Statement stmt = conn.createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS addressbook (\n" + "	id integer PRIMARY KEY,\n"
				+ "	firstname text,\n" + "	lastname text,\n" + "	email text" + ");";
		stmt.execute(sql);
	}

	public void selectValues() throws Exception {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM addressbook");
		while (rs.next()) {
			controller.data.add(new Person(rs.getString("firstname"), rs.getString("lastname"), rs.getString("email")));
		}
	}

	public void insertValues(String firstName, String lastName, String email) throws Exception {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM addressbook");
		String maxId = rs.getString("MAX(id)");
		int id = 1;
		if (maxId != null)
			id = (Integer.parseInt(maxId) + 1);
		stmt.executeUpdate("INSERT INTO addressbook VALUES (" + id + ",\'" + firstName + "\',\'" + lastName + "\',\'"
				+ email + "\')");
	}

	public void deleteValues(String firstName) throws Exception {
		Statement stmt;
		stmt = conn.createStatement();
		stmt.executeUpdate("DELETE FROM addressbook WHERE firstname = \'" + firstName + "\'");
	}

	public void updateValues(String origFirstName, String firstName, String lastName, String email) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE addressbook SET firstName = \'" + firstName + "\', lastName = \'" + lastName
					+ "\'," + "email = \'" + email + "\' WHERE firstName = \'" + origFirstName + "\'");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
