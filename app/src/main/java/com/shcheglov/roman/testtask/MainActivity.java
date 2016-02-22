package com.shcheglov.roman.testtask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button url_button;
    EditText url_edit;
    TextView url_text;

    //Диалоговое окно, появляющееся во время загрузки.
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url_button = (Button)findViewById(R.id.url_button);
        url_button.setOnClickListener(this);

        url_edit = (EditText)findViewById(R.id.url_edit);

        url_text = (TextView)findViewById(R.id.url_text);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait");
        dialog.setMessage("Downloading web-page");
        dialog.setCancelable(false);
    }

    // Класс, реализующий загрузку интернет страницы
    class DownloadCodeTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                //Таймаут подключения
                connection.setConnectTimeout(15000);
                //Таймаут получения данных
                connection.setReadTimeout(10000);
                connection.setRequestMethod("GET");
                connection.connect();

                InputStreamReader sreader = new InputStreamReader(connection.getInputStream());

                StringBuilder sbilder = new StringBuilder();
                
                char[] buffer = new char[255];
                int count = 0;
                while (sreader.read(buffer, 0, 255) > 0) {
                    sbilder.append(buffer);
                }
                sreader.close();
                connection.disconnect();
                return sbilder.toString();
            }
            catch (Exception e){
                e.printStackTrace();
                return e.getMessage();
            }
        }
        @Override
        protected void onPreExecute() {
            super.onProgressUpdate();
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            url_text.setText(s);
            dialog.dismiss();
        }

    }

    @Override
    public void onClick(View v){
        new DownloadCodeTask().execute(url_edit.getText().toString());
    }
}