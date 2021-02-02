package com.mygdx.game;

import java.sql.*;


//DATABASE NOT LINKED WITH JAR FILE

public class SQLiteConnect {
	private static String url = "jdbc:sqlite:Progress.db";
	private static Connection conn = null;
	
	public static void main(String[] args) {
		connect();
		createTable();
		defaultInfo();
	}
	
	public static Connection connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());		
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} 
		return conn;
	}
	
	public static void createTable() {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS Progress (\n"
					+ "		Volume INTEGER,\n"
					+ "		Turn INTEGER,\n"
					+ "		Playing INTEGER,\n"
					+ "		Tutorial INTEGER, \n"
					+ " 	PositionX INTEGER,\n"
					+ "		PositionY INTEGER,\n"
					+ " 	Points1 INTEGER,\n"
					+ "		Points2 INTEGER"
					+ ");";
			System.out.println("Conection to SQLite has been established.");
			Statement statement = connect().createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		System.out.println("Table created");
	}


	public static void defaultInfo() {
		String sql = "INSERT INTO Progress(Volume,Turn,Playing,Tutorial,PositionX,PositionY,Points1,Points2) Values(50,0,0,0,27,40,0,0)";
		try {
			conn = DriverManager.getConnection(url);
			PreparedStatement input = connect().prepareStatement(sql);
			input.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateAll(int volume, int turn, int playing, int tutorial, int positionX, int positionY, int points1, int points2) {
		String sql = "UPDATE Progress SET"
				+ "	Volume = ? ,"
				+ "	Turn = ? ,"
				+ " Playing = ? ,"
				+ " Tutorial = ? ,"
				+ " PositionX = ? ,"
				+ " PositionY = ? ,"
				+ " Points1 = ? ,"
				+ "	Points2 = ?";
		try {
			conn = DriverManager.getConnection(url);
			PreparedStatement input = conn.prepareStatement(sql);
			input.setInt(1, volume);
			input.setInt(2, turn);
			input.setInt(3, playing);
			input.setInt(4, tutorial);
			input.setInt(5, positionX);
			input.setInt(6, positionY);
			input.setInt(7, points1);
			input.setInt(8, points2);
			input.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateVolume(int volume) {
		String sql = "UPDATE Progress SET Volume = " + volume;
		try {
			PreparedStatement input = connect().prepareStatement(sql);
			input.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public int volume() {
		String sql = "SELECT Volume FROM Progress";
		int volumeData = 50;
		try {
			conn = DriverManager.getConnection(url);
			Statement stmt = connect().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			volumeData = rs.getInt("volume");
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return volumeData;
	}
	
	public int turn() {
		String sql = "SELECT Turn FROM Progress";
		int turnData = 50;
		try {
			Statement stmt = connect().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			turnData = rs.getInt("Turn");
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return turnData;
	}
	
	public int playing() {
		String sql = "SELECT Playing FROM Progress";
		int turnData = 50;
		try {
			Statement stmt = connect().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			turnData = rs.getInt("Playing");
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return turnData;
	}
	
	public int tutorial() {
		String sql = "SELECT Tutorial FROM Progress";
		int turnData = 50;
		try {
			Statement stmt = connect().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			turnData = rs.getInt("Tutorial");
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return turnData;
	}
	
	public int positionX() {
		String sql = "SELECT PositionX FROM Progress";
		int xData = 50;
		try {
			Statement stmt = connect().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			xData = rs.getInt("PositionX");
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return xData;
	}
	
	public int positionY() {
		String sql = "SELECT PositionY FROM Progress";
		int yData = 50;
		try {
			Statement stmt = connect().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			yData = rs.getInt("PositionY");
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return yData;
	}
	
	public int points1() {
		String sql = "SELECT Points1 FROM Progress";
		int point1Data = 50;
		try {
			Statement stmt = connect().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			point1Data = rs.getInt("Points1");
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return point1Data;	}
	
	public int points2() {
		String sql = "SELECT Points2 FROM Progress";
		int point2Data = 50;
		try {
			Statement stmt = connect().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			point2Data = rs.getInt("Points2");
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return point2Data;
	}
}
