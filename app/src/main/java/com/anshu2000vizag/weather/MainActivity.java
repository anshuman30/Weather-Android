package com.anshu2000vizag.weather;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText ed;
    TextView tv;
    String res="";


    public void toSecondActivity(){

        Intent intent = new Intent(getApplicationContext(),SecondActivity.class);

        intent.putExtra("Data",res);
        startActivity(intent);

    }


    public class DownloadTask extends AsyncTask< String,Void,String > {

        @Override
        protected String doInBackground(String... urls){
            Log.i("URL: ",urls[0]);
            res="";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char cur=(char) data;
                    res+=cur;
                    data=reader.read();
                }
                return res;
            }
            catch(Exception e){
                //Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);
                //e.printStackTrace();
                Log.i("Error","not found");
                return null;

            }
            //Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

        }
        protected void onPostExecute(String resu){
            super.onPostExecute(resu);

            //tv.setText(res);
            if(!res.equals("")) {
                Log.i("content: ",resu);
                toSecondActivity();
            }
            else{
                Toast.makeText(getApplicationContext(), "Could not find city "+ed.getText(), Toast.LENGTH_LONG).show();
            }


        }
    }


    public void getWeather(View view){


        String city = ed.getText().toString();

        try {
            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=8fd2cd1d223e6fd4e966797e23638e25");
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed = (EditText) findViewById(R.id.editText);
        tv = (TextView) findViewById(R.id.textView3);


    }
}
