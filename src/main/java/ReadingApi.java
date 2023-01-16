import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
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

	String webPage;
	String country;
	String name;
	String code;
	String domains;

	public static void main(String[] args) throws IOException, InterruptedException {

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://universities.hipolabs.com/search?country=United+States")).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		String uglyJsonString = response.body();
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
			String sqlQueryToInsert = insertStatement(webPage, country, name, state, code, domains);
			String sqlQueryToRead = readStatement(webPage, country, name, state, code, domains);
			String sqlQueryToUpdate = updateStatement(webPage, country, name, state, code, domains);
			String sqlQueryToDelete = deleteStatement(webPage, country, name, state, code, domains);
			executingOfQurey(sqlQueryToInsert);
			executingOfQurey(sqlQueryToRead);
			executingOfQurey(sqlQueryToUpdate);
			executingOfQurey(sqlQueryToDelete);

		}

	}

	public static void executingOfQurey(String sql) {

		try {
			Statement st = con.createStatement();
			int executing = st.executeUpdate(sql);
			if (executing >= 0) {
				System.out.println("Created Successfully : " + sql);
			} else {
				System.out.println("Creation Is Failed");
			}
		} catch (Exception ex) {

			System.err.println(ex);
		}

	}

	public static String insertStatement(String webPage, String country, String name, String state, String code,
			String domains) {
		String sqlQueryToInsert = " INSERT INTO ApiResults (web_pages,state_province,alpha_two_code , name,country ,domins)"
				+ "VALUES('" + webPage + "','" + country + "', '" + name + "', '" + state + "','" + code + "', '"
				+ domains + "')";
		return sqlQueryToInsert;
	}

	public static String readStatement(String webPage, String country, String name, String state, String code,
			String domains) {
		String sqlQueryToRead = "SELECT * FROM ApiResults";
		return sqlQueryToRead;
	}

	public static String updateStatement(String webPage, String country, String name, String state, String code,
			String domains) {
		String sqlQueryToUpdate = " Update  ApiResults SET (web_pages,state_province,alpha_two_code , name,country ,domins)"
				+ "VALUES('" + webPage + "','" + country + "', '" + name + "', '" + state + "','" + code + "', '"
				+ domains + "')";
		return sqlQueryToUpdate;
	}

	public static String deleteStatement(String webPage, String country, String name, String state, String code,
			String domains) {

		System.out.println("Please Enter The Id To Be Deleted ");
		int idToBeDeleted = sc.nextInt();

		String sqlQueryToDelete = "DELETE FROM ApiResults WHERE id =" + idToBeDeleted;
		return sqlQueryToDelete;
	}
}
