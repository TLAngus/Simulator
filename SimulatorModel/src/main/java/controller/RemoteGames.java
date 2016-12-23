/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * This class is used to fetch games from a remote server.
 * @author gillesbraun
 */
public class RemoteGames {

    private static final String server = "http://lux-gaming.lu/~gillo/simulator/index.php";

    /**
     * Saves a Game on the remote server and gives it an ID automatically
     * @param sim
     * @throws Exception 
     */
    public static void saveGametoRemote(Simulator sim) throws Exception {
        String json = sim.getJson();
        executePost(server, "game=" + URLEncoder.encode(json, "UTF-8"));
    }

    /**
     * Returns a list with all the games from the server in JSON
     * @return
     * @throws Exception 
     */
    public static HashMap<Integer, String> getGames() throws Exception {
        String get = executeGet(server);

        HashMap<Integer, String> gamesList = new HashMap<>();

        String[] rows = get.split("\\$");

        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            String[] cols = row.split(";");

            if (cols.length == 2) {
                int id = Integer.valueOf(cols[0]);
                String json = cols[1];
                gamesList.put(id, json);
            }
        }
        return gamesList;
    }

    private static String executePost(String targetURL, String urlParameters) throws Exception {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response  
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            rd.close();
            return response.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String executeGet(String targetURL) throws Exception {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);

            //Get Response  
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            rd.close();
            return response.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
