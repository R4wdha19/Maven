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

	static String newWebPage = "'http://www.Rawd.ac.uk/'";
	static String newCountry = "'OMAN'";
	static String newName = "'RAWDHA'";
	static String newCode = "'1001'";
	static String newState = "'SEEB'";
	static String newDomains = "'Something'";

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
			insertStatement();
			break;
		case 2:
			updateStatement();
			break;
		case 3:
			deleteStatement();
			break;
		case 4:
			readStatement();

		}

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
