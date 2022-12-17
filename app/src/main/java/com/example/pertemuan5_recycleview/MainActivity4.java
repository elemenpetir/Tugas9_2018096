package com.example.pertemuan5_recycleview;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.example.pertemuan5_recycleview.databinding.ActivityMain4Binding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
public class MainActivity4 extends AppCompatActivity implements View.OnClickListener{
    //declaration variable
    private ActivityMain4Binding binding;
    String index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup view binding
        binding = ActivityMain4Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fetchButton.setOnClickListener(this);
    }
    //onclik button fetch
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.fetch_button){
            index = binding.inputId.getText().toString();
            try {
                getData();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    //get data using api link
    public void getData() throws MalformedURLException {
        Uri uri = Uri.parse("https://www.7timer.info/bin/astro.php?lon=113.2&lat=23.1&ac=0&unit=metric&output=json&tzshift=0").buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }
    class DOTask extends AsyncTask<URL, Void, String>{
        //connection request
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls [0];
            String data = null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            }catch (IOException e){
                e.printStackTrace();
            }
            return data;
        }
        @Override
        protected void onPostExecute(String s){
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //get data json
        public void parseJson(String venues) throws JSONException{
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(venues);
            }catch (JSONException e){
                e.printStackTrace();
            }
//            JSONObject innerObj = jsonObject.getJSONObject("venues");
            JSONArray cityArray = jsonObject.getJSONArray("dataseries");
            for (int i =0; i <cityArray.length(); i++){
                JSONObject obj = cityArray.getJSONObject(i);
                String Sobj = obj.get("seeing").toString();
                if (Sobj.equals(index)){
                    String id = obj.get("seeing").toString();
                    binding.resultId.setText(id);
                    String name = obj.get("prec_type").toString();
                    binding.resultName.setText(name);
                    break;
                }
                else{
                    binding.resultName.setText("Not Found");
                }
            }
        }
    }
}
