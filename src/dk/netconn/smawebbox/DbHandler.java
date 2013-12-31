package dk.netconn.smawebbox;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import org.apache.commons.io.*;

public class DbHandler {
	static final String DB_URL = "jdbc:derby:db;create=false";
	private Connection Conn = null;
	
	
	public DbHandler(){
		CleanOldDb();
		Conn = Connect();
		InitializeDb();
		//SelectData();
		//Dispose();
	}
	
	private Connection Connect(){
		
		//Properties props = null;
		Connection conn;
		try {
				//Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			conn = DriverManager.getConnection("jdbc:derby:db;create=true");
				return conn;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
	
	private void CleanOldDb(){
		try {
			File f = new File( new File (".").getCanonicalPath() + "\\db");
			//System.out.println(new File (".").getCanonicalPath() + "\\db");
			if(f.exists()){
				FileUtils.deleteDirectory(new File( new File (".").getCanonicalPath() + "\\db"));

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void InitializeDb(){
		String schema = "CREATE SCHEMA SMADATA";
		
		try {
			PreparedStatement schemaStatement = Conn.prepareStatement(schema);
			schemaStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String data = "CREATE TABLE SMADATA.DATA (ID INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),GRIPWR INTEGER,GRIEGYTDY FLOAT,GRIEGYTOT INTEGER,OPSTT VARCHAR(128),MSG VARCHAR(128))";
		
		try {
			PreparedStatement dataStatement = Conn.prepareStatement(data);
			dataStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String unit = "CREATE TABLE SMADATA.UNITS (ID INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),GRIPWR VARCHAR(10),GRIEGYTDY VARCHAR(10),GRIEGYTOT VARCHAR(10),OPSTT VARCHAR(128),MSG VARCHAR(128))";
		
		try {
			PreparedStatement unitStatement = Conn.prepareStatement(unit);
			unitStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void WriteData(ArrayList<String[]> results){
		String insert = "INSERT INTO SMADATA.DATA (GRIPWR,GRIEGYTDY,GRIEGYTOT,OPSTT,MSG)"
				+ "Values(" 
				+ Integer.parseInt(results.get(0)[0]) + "," 
				+ Float.parseFloat(results.get(1)[0]) + ","
				+ Integer.parseInt(results.get(2)[0]) + ","
				+ "'" + results.get(3)[0] + "',"
				+ "'" + results.get(4)[0] + "')";
		try {
			PreparedStatement statement = Conn.prepareStatement(insert);
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		insert = "INSERT INTO SMADATA.UNITS (GRIPWR,GRIEGYTDY,GRIEGYTOT,OPSTT,MSG)"
				+ "Values(" 
				+ "'" + results.get(0)[1] + "'," 
				+ "'" + results.get(1)[1] + "',"
				+ "'" + results.get(2)[1] + "',"
				+ "'" + results.get(3)[1] + "',"
				+ "'" + results.get(4)[1] + "')";
		try {
			PreparedStatement statement = Conn.prepareStatement(insert);
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void SelectData(){
		String data = "SELECT * FROM SMADATA.DATA WHERE ID = (SELECT MAX(ID) FROM SMADATA.DATA)";
		String unit = "SELECT * FROM SMADATA.UNITS WHERE ID = (SELECT MAX(ID) FROM SMADATA.UNITS)";
		
		ResultSet rsData;
		ResultSet rsUnit;
		
		try {
			PreparedStatement dataStatement = Conn.prepareStatement(data);
			PreparedStatement unitStatement = Conn.prepareStatement(unit);
			
			rsData = dataStatement.executeQuery();
			rsUnit = unitStatement.executeQuery();
			
			while(rsData.next() && rsUnit.next()){
				System.out.print(rsData.getObject("GriPwr").toString() + " " + rsUnit.getObject("GriPwr") + " ");
				System.out.print(rsData.getObject("GriEgyTdy").toString() + " " + rsUnit.getObject("GriEgyTdy") + " ");
				System.out.print(rsData.getObject("GriEgyTot").toString() + " " + rsUnit.getObject("GriEgyTot") + " ");
				System.out.print(rsData.getObject("OpStt").toString() + " " + rsUnit.getObject("OpStt") + " ");
				System.out.print(rsData.getObject("Msg").toString() + " " + rsUnit.getObject("Msg") + "\n");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Dispose(){
		String table = "DROP TABLE OVERVIEW";
		
		try {
			PreparedStatement statement = Conn.prepareStatement(table);
			statement.executeQuery(table);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String schema = "DROP SCHEMA SMADATA RESTRICT";
		
		try {
			PreparedStatement statement = Conn.prepareStatement(schema);
			statement.executeQuery(schema);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CleanOldDb();
	}
}
