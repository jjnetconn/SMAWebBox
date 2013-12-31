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
			System.out.println(new File (".").getCanonicalPath() + "\\db");
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
			PreparedStatement statement = Conn.prepareStatement(schema);
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String table = "CREATE TABLE SMADATA.OVERVIEW (ID INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),GRIPWR INTEGER,GRIEGYTDY FLOAT,GRIEGYTOT INTEGER,OPSTT VARCHAR(128),MSG VARCHAR(128))";
		
		try {
			PreparedStatement statement = Conn.prepareStatement(table);
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void WriteData(ArrayList<String[]> results){
		String insert = "INSERT INTO SMADATA.OVERVIEW (GRIPWR,GRIEGYTDY,GRIEGYTOT,OPSTT,MSG)"
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
	}
	
	public void SelectData(){
		String sql = "SELECT * FROM SMADATA.OVERVIEW";
		
		try {
			PreparedStatement statement = Conn.prepareStatement(sql);
			
			ResultSet result = statement.executeQuery();
			while(result.next()){
				System.out.println(result.getObject("GriPwr").toString());
				System.out.println(result.getObject("GriEgyTdy").toString());
				System.out.println(result.getObject("GriEgyTot").toString());
				System.out.println(result.getObject("OpStt").toString());
				System.out.println(result.getObject("Msg").toString());
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
