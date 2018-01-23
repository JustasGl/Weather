package com.example.android.orelis;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.factor;
import static android.R.attr.min;
import static com.example.android.orelis.R.id.laikas1;
import static com.example.android.orelis.R.id.laikas6;
import static com.example.android.orelis.R.id.laipsniai;

public class MainActivity extends AppCompatActivity {
    String[] laikas = new String[8];
    String[] laipsniai = new String[8];
    String[] url = new String[8];
    String imgurl;
    ProgressBar progress;
    TextView empty;
    int sk=0;
    String URLS = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=83256ca341d7423797b165145180301&q=Mažeikiai&format=json&num_of_days=1";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<hourly> list = new ArrayList<hourly>();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        empty = (TextView)findViewById(R.id.empty);
        progress = (ProgressBar)findViewById(R.id.progress);
        ConnectivityManager cm =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected())
        {
            Oroasynctask task = new Oroasynctask();
            task.execute();
            new valandineasyncTask().execute();

        }
        else
        {
            empty.setVisibility(View.VISIBLE);
            empty.setText("Nėra interneto");
            progress.setVisibility(View.GONE);
        }
    }
    public void valandinis ()
    {
        new DownloadImageTask((ImageView)findViewById(R.id.pic)).execute(imgurl);

        TextView lapsniai0=(TextView)findViewById(R.id.laipsniai0);
        TextView laikas0 = (TextView)findViewById(R.id.laikas0);
        new DownloadImageTask((ImageView) findViewById(R.id.pic0)).execute(url[0]);
        laikas0.setText(+Integer.valueOf(laikas[0])/100+":00");
        lapsniai0.setText(laipsniai[0]+"° C");


        TextView lapsniai1=(TextView)findViewById(R.id.laipsniai1);
        TextView laikas1 = (TextView)findViewById(R.id.laikas1);

        new DownloadImageTask((ImageView) findViewById(R.id.pic1)).execute(url[1]);
        laikas1.setText(+Integer.valueOf(laikas[1])/100+":00");
        lapsniai1.setText(laipsniai[1]+"° C");

        TextView lapsniai2=(TextView)findViewById(R.id.laipsniai2);
        TextView laikas2 = (TextView)findViewById(R.id.laikas2);

        new DownloadImageTask((ImageView) findViewById(R.id.pic2)).execute(url[2]);
        laikas2.setText(+Integer.valueOf(laikas[2])/100+":00");
        lapsniai2.setText(laipsniai[2]+"° C");

        TextView lapsniai3=(TextView)findViewById(R.id.laipsniai3);
        TextView laikas3 = (TextView)findViewById(R.id.laikas3);
        new DownloadImageTask((ImageView) findViewById(R.id.pic3)).execute(url[3]);
        ImageView img3 = (ImageView)findViewById(R.id.pic3);
        laikas3.setText(+Integer.valueOf(laikas[3])/100+":00");
        lapsniai3.setText(laipsniai[3]+"° C");

        TextView lapsniai4=(TextView)findViewById(R.id.laipsniai4);
        TextView laikas4 = (TextView)findViewById(R.id.laikas4);
        ImageView img4 = (ImageView)findViewById(R.id.pic4);
        new DownloadImageTask((ImageView) findViewById(R.id.pic4)).execute(url[4]);
        laikas4.setText(+Integer.valueOf(laikas[4])/100+":00");
        lapsniai4.setText(laipsniai[4]+"° C");

        TextView lapsniai5=(TextView)findViewById(R.id.laipsniai5);
        TextView laikas5 = (TextView)findViewById(R.id.laikas5);
        ImageView img5 = (ImageView)findViewById(R.id.pic5);
        new DownloadImageTask((ImageView) findViewById(R.id.pic5)).execute(url[5]);
        laikas5.setText(+Integer.valueOf(laikas[5])/100+":00");
        lapsniai5.setText(laipsniai[5]+"° C");

        TextView lapsniai6=(TextView)findViewById(R.id.laipsniai6);
        TextView laikas6 = (TextView)findViewById(R.id.laikas6);
        ImageView img6 = (ImageView)findViewById(R.id.pic6);
        new DownloadImageTask((ImageView) findViewById(R.id.pic6)).execute(url[6]);
        laikas6.setText(+Integer.valueOf(laikas[6])/100+":00");
        lapsniai6.setText(laipsniai[6]+"° C");

        TextView lapsniai7=(TextView)findViewById(R.id.laipsniai7);
        TextView laikas7 = (TextView)findViewById(R.id.laikas7);
        ImageView img7 = (ImageView)findViewById(R.id.pic7);
        new DownloadImageTask((ImageView) findViewById(R.id.pic7)).execute(url[7]);
        laikas7.setText(+Integer.valueOf(laikas[7])/100+":00");
        lapsniai7.setText(laipsniai[7]+"° C");

        Log.i("Imigo urls yra :  ", imgurl);
    }
    public void updateUi(Oras mzk) {
        String minutes = "";
        String laikas = mzk.mlaikas;
        int tikrasislaikas = 0;
        String[] parts;
        String pirmdalis = "";
        String miesta = mzk.mmiestas;
        TextView txt = (TextView) findViewById(R.id.laikas);
        TextView oras = (TextView) findViewById(R.id.laipsniai);
        TextView miest = (TextView) findViewById(R.id.miestas);
        if (Integer.valueOf(mzk.mlaipsniai) > 0)
            oras.setText("+" + mzk.mlaipsniai);
        else
            oras.setText(mzk.mlaipsniai);
        if (laikas.contains("AM")) {
            laikas = laikas.replace("AM", "");
            laikas = laikas.replace(" ", "");
            minutes = laikas.substring(3, 5);
            laikas = laikas.substring(0, 2);
            tikrasislaikas = Integer.valueOf(laikas) + 2;
            Log.e("log", laikas);
            if (tikrasislaikas > 12)
                tikrasislaikas -= 12;
            if (tikrasislaikas > 24)
                tikrasislaikas -= 24;

        } else {
            laikas.replace("PM", "");
            laikas = laikas.replace(" ", "");
            minutes = laikas.substring(3, 5);
            laikas = laikas.substring(0, 2);
            tikrasislaikas = Integer.valueOf(laikas) + 2;
        }
        txt.setText(tikrasislaikas + ":" + minutes);
        if (miesta.contains(",")) {
            parts = miesta.split(",");
            pirmdalis = parts[0];

        }
        miest.setText(pirmdalis);
    }

    private class Oroasynctask extends AsyncTask<URL, Void, Oras> {

        @Override
        protected Oras doInBackground(URL... urls) {
            URL url = sukurtiUrl(URLS);
            String jsonResponse = "";
            try {
                jsonResponse = sukurtiHHTPprasyma(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }
            Oras mzk = extractFeatureFromJson(jsonResponse);
            return mzk;
        }

        @Override
        protected void onPostExecute(Oras mzk) {
            if (mzk == null) {
                Log.e(LOG_TAG, "MZK == null");
                return;
            }
            updateUi(mzk);
        }

        private URL sukurtiUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String sukurtiHHTPprasyma(URL url) throws IOException {
            String jsonResponse = "";
            if (url == null)
                return jsonResponse;
            HttpURLConnection urlConnection = null;
            inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);

                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200)
                    inputStream = urlConnection.getInputStream();
                else
                    Log.e("KODAS: ", String.valueOf(urlConnection.getResponseCode()));
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                Log.e("IOException: ", String.valueOf(e));
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private Oras extractFeatureFromJson(String earthquakeJSON) {
            if (TextUtils.isEmpty(earthquakeJSON))
                return null;
            try {
                JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
                JSONObject dataobject = baseJsonResponse.getJSONObject("data");
                JSONArray myrequest = dataobject.getJSONArray("request");
                JSONObject miestoobjektas = myrequest.getJSONObject(0);
                String miestas = miestoobjektas.getString("query");
                JSONArray curentCondition = dataobject.getJSONArray("current_condition");

                // If there are results in the features array
                if (curentCondition.length() > 0) {
                    JSONObject firstFeature = curentCondition.getJSONObject(0);
                    JSONArray curentConditioncondition = firstFeature.getJSONArray("weatherIconUrl");
                    JSONObject urlobject = curentConditioncondition.getJSONObject(0);
                    String urls = urlobject.getString("value");
                    // Extract out the title, time, and tsunami values
                    String temp = firstFeature.getString("temp_C");
                    String time = firstFeature.getString("observation_time");
                    // Extract out the first feature (which is an earthquake)
                    imgurl = urls;
                    if (imgurl.contains("mist.png")) {
                        imgurl = "https://static.mopik.net/img/1080x1920/mist_12896_mopik.jpg";
                    } else if (imgurl.contains("cloudy_with_heavy_snow_night"))
                        imgurl = "http://wallpaperswide.com/download/snow_24-wallpaper-1920x1080.jpg";
                    else if (imgurl.contains("black_low_cloud"))
                        imgurl = "https://i.imgur.com/XnJh1Fh.jpg";
                    else if (imgurl.contains("light_rain_showers_night"))
                        imgurl = "https://www.desktopbackground.org/download/1080x1920/2010/04/10/76_happy-rainy-day-wallpapers-toptenpack-com_3984x2489_h.jpg";
                    else if (imgurl.contains("cloudy_with_light_rain"))
                        imgurl = "http://getwallpapers.com/wallpaper/full/a/f/a/16933.jpg";
                    else
                        imgurl = "https://i.pinimg.com/originals/b6/1a/4a/b61a4a09110a213fa022508fa5fbbbbe.jpg";
                    // Create a new {@link Event} object
                    return new Oras(temp, time, miestas);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            if(sk==6) {
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);
            }
            sk++;

        }
    }

    private class valandineasyncTask extends AsyncTask<URL, Void, List<hourly>> {

        @Override
        protected List<hourly> doInBackground(URL... urls) {
            URL url = sukurtiUrl(URLS);
            String jsonResponse = "";
            try {
                jsonResponse = sukurtiHHTPprasyma(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }
            List<hourly> valandinis = extractFeatureFromJson(jsonResponse);
            return valandinis;
        }

        @Override
        protected void onPostExecute(List<hourly> str) {

            valandinis();
        }

        private URL sukurtiUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String sukurtiHHTPprasyma(URL url) throws IOException {
            String jsonResponse = "";
            if (url == null)
                return jsonResponse;
            HttpURLConnection urlConnection = null;
            inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);

                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200)
                    inputStream = urlConnection.getInputStream();
                else
                    Log.e("KODAS: ", String.valueOf(urlConnection.getResponseCode()));
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                Log.e("IOException: ", String.valueOf(e));
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private List<hourly> extractFeatureFromJson(String earthquakeJSON) {
            if (TextUtils.isEmpty(earthquakeJSON))
                return null;
            try {
                JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
                JSONObject dataobject = baseJsonResponse.getJSONObject("data");
                JSONArray weatherarray = dataobject.getJSONArray("weather");

                // If there are results in the features array
                for (int i = 0; i < weatherarray.length(); i++) {
                    JSONObject diena = weatherarray.getJSONObject(i);
                    JSONArray hourlyarray = diena.getJSONArray("hourly");
                    for (int j = 0; j < hourlyarray.length(); j++) {
                        JSONObject valanda = hourlyarray.getJSONObject(j);
                        JSONArray weathericon = valanda.getJSONArray("weatherIconUrl");
                        JSONObject nuliniswethericon = weathericon.getJSONObject(0);

                        laikas[j] = valanda.getString("time");
                        laipsniai[j] = valanda.getString("tempC");
                        url[j] = nuliniswethericon.getString("value");

                    }
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }
    }
}
