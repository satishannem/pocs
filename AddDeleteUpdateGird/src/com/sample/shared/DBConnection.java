package com.sample.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static DBConnection instance;
	Connection con = null;

	private DBConnection() {

	}
	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
		}
		return instance;
	}
	public Connection getConnection() {
		try {
			Class.forName("org.firebirdsql.jdbc.FBDriver").newInstance();
			con = DriverManager.getConnection(
					"jdbc:firebirdsql:localhost/3050:F:/DataBase/person.gdb",
					"SYSDBA", "masterkey");
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return con;
	}

}
