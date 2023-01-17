import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ReadingApi {

	public static final String url = "jdbc:sqlserver://localhost:1433;databaseName=ApiDB;encrypt=true;trustServerCertificate=true";
	public static final String user = "sa";
	public static final String pass = "root";
	public static Connection con;
	static Scanner sc = new Scanner(System.in);
	static String newWebPage = "'http://www.Rawd.ac.uk/'";
	static String newCountry = "'OMAN'";
	static String newName = "'RAWDHA'";
	static String newCode = "'1001'";
	static String newState = "'SEEB'";
	static String newDomains = "'Something'";
	static String region = "' Rawdha'";
	static String common = "'Updated'";

	static {
		try {
			con = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void closingConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Select an option");
		System.out.println("1 - Insert Into Table");
		System.out.println("2 - Update A Row");
		System.out.println("3 - Delet A Row");
		System.out.println("4 - Read Table");
		String option = sc.next();
		int op = Integer.parseInt(option);
		switch (op) {
		case 1:
//			insertStatement();
			insertIntoDummyDataTable();
			break;
		case 2:
			updateDummyDataTable();
//			updateStatement();
			break;
		case 3:
			deleteDummyDataTable();
//			deleteStatement();
			break;
		case 4:
			readDummyDataTable();
//			readStatement();

		}

	}

	public static void executingOfReadDummyData(String sql) {
		Statement statement;
		try {
			statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			System.out.println(resultSet);
			while (resultSet.next()) {
				String cioc = resultSet.getString("cioc");
				String region = resultSet.getString("region");
				String startOfWeek = resultSet.getString("startOfWeek");
				String common = resultSet.getString("common");
				String f = resultSet.getString("f");
				String symbol = resultSet.getString("symbol");
				String googleMaps = resultSet.getString("googleMaps");
				int unMember = resultSet.getInt("unMember");
				int landlocked = resultSet.getInt("landlocked");
				Float area = resultSet.getFloat("area");
				System.out.println(cioc + " " + region + " " + startOfWeek + " " + common + " " + f + " " + symbol + " "
						+ googleMaps + " " + unMember + " " + landlocked + " " + area);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void insertIntoDummyDataTable() {
		String uglyJsonString = url("https://restcountries.com/v3.1/all");

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uglyJsonString);
		String prettyJsonString = gson.toJson(je);
		Data[] records = gson.fromJson(prettyJsonString, Data[].class);
		for (Data r : records) {
			String cioc = r.getCioc();
			String region = r.getRegion();
			String startOfWeek = r.getStartOfWeek();
			String common = r.getName().getCommon();
			String f = r.getLanguages().getEng();
			String symbol = r.getCcn3();
			String googleMaps = r.getMaps().getGoogleMaps();
			int unMember = r.getUnMember() == true ? 1 : 0;
			int landlocked = r.getLandlocked() == true ? 1 : 0;
			Float area = r.getArea();
			String sqlQueryToInsert = " insert into DummyDataFromAp (" + "cioc," + "region, " + "startOfWeek,"
					+ "common," + "f ," + "symbol," + "googleMaps ," + "unMember  ," + "landlocked,"
					+ "area ) Values ('" + cioc + "','" + region + "','" + startOfWeek + "','" + common + "','" + f
					+ "','" + symbol + "','" + googleMaps + "','" + unMember + "','" + landlocked + "'," + area + ")";
			executingOfQurey(sqlQueryToInsert);

		}
	}

	public static void readDummyDataTable() {
		System.out.println("ID :");
		Integer userInput = sc.nextInt();
		String sqlQueryToRead = "SELECT * FROM DummyDataFromAp WHERE id = " + userInput;
		executingOfReadDummyData(sqlQueryToRead);
	}

	public static void updateDummyDataTable() {

		System.out.println("ID :");
		Integer userInput = sc.nextInt();
		String sqlQueryToUpdate = " Update DummyDataFromAp SET region= " + region + ",common= " + common + " Where id ="
				+ userInput;
		executingOfQurey(sqlQueryToUpdate);
	}

	public static void deleteDummyDataTable() {

		System.out.println("Please Enter The Id To Be Deleted ");
		int idToBeDeleted = sc.nextInt();
		String sqlQueryToDelete = "DELETE FROM DummyDataFromAp WHERE id = " + idToBeDeleted;
		executingOfQurey(sqlQueryToDelete);

	}

	public static void executingOfQurey(String sql) {

		try {
			Statement st = con.createStatement();
			int executing = st.executeUpdate(sql);
			System.out.println(sql);
			if (executing > 0) {
				System.out.println("Successful : " + sql);
			} else {
				System.out.println("Failed");
			}
		} catch (Exception ex) {

			System.err.println(ex);
		}

	}

	public static void executingOfRead(String sql) {

		try {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			System.out.println(resultSet);

			while (resultSet.next()) {
				Integer id = resultSet.getInt("id");
				String web_pages = resultSet.getString("web_pages");
				String state_province = resultSet.getString("state_province");
				String alpha_two_code = resultSet.getString("alpha_two_code");
				String name = resultSet.getString("name");
				String country = resultSet.getString("country");
				String domins = resultSet.getString("domins");

				System.out.println(id + " " + web_pages + " " + state_province + " " + alpha_two_code + " " + name + " "
						+ country + " " + domins);
			}

		} catch (Exception ex) {

			System.err.println(ex);
		}
	}

	public static void readStatement() {
		System.out.println("ID :");
		Integer userInput = sc.nextInt();
		String sqlQueryToRead = "SELECT * FROM ApiResults WHERE id = " + userInput;
		executingOfRead(sqlQueryToRead);
	}

	public static void updateStatement() {

		System.out.println("ID :");
		Integer userInput = sc.nextInt();
		String sqlQueryToUpdate = " Update  ApiResults SET web_pages= " + newWebPage + ",state_province= " + newState
				+ ",alpha_two_code= " + newCode + " , name= " + newName + ",country= " + newCountry + ",domins= "
				+ newDomains + " Where id =" + userInput;
		executingOfQurey(sqlQueryToUpdate);
	}

	public static void deleteStatement() {

		System.out.println("Please Enter The Id To Be Deleted ");
		int idToBeDeleted = sc.nextInt();
		String sqlQueryToDelete = "DELETE FROM ApiResults WHERE id = " + idToBeDeleted;
		executingOfQurey(sqlQueryToDelete);

	}

	public static void insertStatement() {
		String uglyJsonString = url("http://universities.hipolabs.com/search?country=United+States");

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uglyJsonString);
		String prettyJsonString = gson.toJson(je);
		ColumnNamesClass[] names = gson.fromJson(prettyJsonString, ColumnNamesClass[].class);
		for (ColumnNamesClass r : names) {
			String webPage = r.getWeb_pages()[0];
			String country = r.getCountry();
			String name = r.getName();
			String state = r.getState_province();
			String code = r.getAlpha_two_code();
			String domains = r.getDomins()[0];
			String sqlQueryToInsert = " INSERT INTO ApiResults (web_pages,state_province,alpha_two_code , name,country ,domins)"
					+ "VALUES('" + webPage + "','" + country + "', '" + name + "', '" + state + "','" + code + "', '"
					+ domains + "')";
			executingOfQurey(sqlQueryToInsert);

		}
	}

	public static String url(String url) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
		HttpResponse<String> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (Exception e) {

			e.printStackTrace();
		}

		return response.body();
	}

}
