import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ReadingApi {

	public static final String url = "jdbc:sqlserver://localhost:1433;databaseName=ApiDB;encrypt=true;trustServerCertificate=true";
	public static final String user = "sa";
	public static final String pass = "root";
	public static Connection con;

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

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://universities.hipolabs.com/search?country=United+States")).build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		String uglyJsonString = response.body();
//		System.out.println(uglyJsonString);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uglyJsonString);
		String prettyJsonString = gson.toJson(je);
		// System.out.println(prettyJsonString);

		ColumnNamesClass[] names = gson.fromJson(prettyJsonString, ColumnNamesClass[].class);
		for (ColumnNamesClass r : names) {
			String webPage = r.getWeb_pages()[0];
			String country = r.getCountry();
			String name = r.getName();
			String state = r.getState_province();
			String code = r.getAlpha_two_code();
			String domains = r.getDomins()[0];
//			
//			System.out.println("\n");
//			System.out.println(webPage + country + name + state + code + domains + "/n");
			String sqlQueryToInsert = " INSERT INTO ApiResults (web_pages,state_province,alpha_two_code , name,country ,domins)"
					+ "VALUES('" + webPage + "','" + country + "', '" + name + "', '" + state + "','" + code + "', '"
					+ domains + "')";

			System.out.println(sqlQueryToInsert);

			try {
				Statement st = con.createStatement();
				int executing = st.executeUpdate(sqlQueryToInsert);
				if (executing >= 0) {
					System.out.println("Created Successfully : " + sqlQueryToInsert);
				} else {
					System.out.println("Creation Is Failed");
				}

//                closingConnection();
			} catch (Exception ex) {

				System.err.println(ex);
			}

		}

//		List<String> resultsList = names.web_pages;
//		names.

	}
}
