package model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * A class for communicating with the TraktTV API according to its specification.
 */
public class TraktTVAPI {
    final static String API_CLIENT_ID = "19b5dc7fbf80c295778c75c5c35bd33ee06b343acfc9ab3693f629913f2f5c5a";
    final static String CONTENT_TYPE = "application/json";
    final static String API_URL = "https://api-v2launch.trakt.tv/search";
    final static String TRAKT_API_VER = "2";

    private APICommunication APIComm = new APICommunication();

    public TraktTVAPI() {APIComm = new APICommunication();}

    public ArrayList<TVShow> getTVShows(String query){

        String result = null;
        ArrayList<TVShow> tvShows = null;
        try {
            result = APIComm.execute("show", query).get();

            tvShows = parseJSON(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tvShows;
    }

    /**
     * Parse the JSON recieved from the POST-request from TraktTV API
     */
    private ArrayList<TVShow> parseJSON(String jsonString) throws JSONException {
        ArrayList<TVShow> tvShows = new ArrayList<>();
        TVShow tvShow;
        JSONArray jsonArray = new JSONArray(jsonString);
        JSONObject jsonObj;

        for (int i=0; i < jsonArray.length(); i++) {
            jsonObj = jsonArray.getJSONObject(i).getJSONObject("show");

            // The constructor in this TVShow; Default values on a TV-shows, assuming that the objects are n/a
            tvShow = new TVShow();

            // If object not available, ignore setting a value (default: n/a)
            try  {
                tvShow.setID(jsonObj.getJSONObject("ids").getInt("trakt"));
            } catch (Exception e) { e.printStackTrace();}
            try  {
                tvShow.setTitle(jsonObj.get("title").toString());
            } catch (Exception e) { } // Title missing, Just continue
            try  {
                tvShow.setOverview(jsonObj.get("overview").toString());
            } catch (Exception e) { } // Overview missing, Just continue
            try  {
                if(jsonObj.get("year").toString() != "null") {tvShow.setYear(jsonObj.get("year").toString());}
            } catch (Exception e) { } // Year missing, Just continue
            try  {
                tvShow.setImgThumb(jsonObj.getJSONObject("images").getJSONObject("poster").getString("thumb"));
            } catch (Exception e) { } // Thumb image, Just continue

            tvShows.add(tvShow);
        }

        return tvShows;
    }

    class APICommunication extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            try {

                URI searchTerm = new URI(params[1].replace(" ", "%20"));
                URL url = new URL(API_URL+"?type="+params[0]+"&query="+searchTerm+"");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // Set request method
                conn.setRequestMethod("GET");

                // Set headers required by API
                conn.setRequestProperty("Content-type", CONTENT_TYPE);
                conn.setRequestProperty("trakt-api-key", API_CLIENT_ID);
                conn.setRequestProperty("trakt-api-version", TRAKT_API_VER);

                int responseCode = conn.getResponseCode();
                if(responseCode != 200) {
                    // Error connecting to server...
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();

            } catch (Exception e) {
                Log.d("ShowTracker", "Failed to fetch data from TraktTV API:" + Log.getStackTraceString(e));
            }
            return "Error fetching data from TraktTV API";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
        }

    }
}