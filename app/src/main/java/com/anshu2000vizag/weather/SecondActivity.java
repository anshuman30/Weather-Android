package com.anshu2000vizag.weather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class SecondActivity extends AppCompatActivity {


    String result;
    JSONObject jsonObject;
    TextView temp;
    TextView minTemp;
    TextView maxTemp;
    TextView main;
    TextView desc;
    TextView city;

    public void getImage(){



        ImageView imageView = (ImageView) findViewById(R.id.weatherIcon);
        ImageDownloader task = new ImageDownloader();
        Bitmap img=null;
        try {
            String iconId=jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
            Log.i("icon",jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon"));
            img=task.execute("https://api.openweathermap.org/img/w/"+iconId+".png").get();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(img);

    }

    public void getTemp(){

        try {
            int t=jsonObject.getJSONObject("main").getInt("temp");
            int mint=jsonObject.getJSONObject("main").getInt("temp_min");
            int maxt=jsonObject.getJSONObject("main").getInt("temp_max");
            String main1=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            String description=jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            String cityname=jsonObject.getString("name");
            t-=273.00;
            mint-=273.00;
            maxt-=273.00;
            Log.i("temp",Integer.toString(t));
            temp.setText(Integer.toString(t)+" C");
            minTemp.setText("Min temp: "+Integer.toString(mint)+" C");
            maxTemp.setText("Max temp: "+Integer.toString(maxt)+" C");
            main.setText(main1);
            desc.setText(description);
            city.setText(cityname);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public class ImageDownloader extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream in = connection.getInputStream();
                Bitmap res = BitmapFactory.decodeStream(in);
                return res;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        result=intent.getStringExtra("Data");
        temp=(TextView) findViewById(R.id.temp);
        minTemp=(TextView) findViewById(R.id.minTemp);
        maxTemp=(TextView) findViewById(R.id.maxTemp);
        main=(TextView) findViewById(R.id.main);
        desc=(TextView) findViewById(R.id.desc);
        city=(TextView) findViewById(R.id.cityName);

        try {
            jsonObject = new JSONObject(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(jsonObject.getInt("cod")==404){
                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);
            }
            else{
                getImage();
                getTemp();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}
