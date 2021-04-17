package com.example.aula7app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;

public class Exercicio1Activity extends AppCompatActivity {

    EditText etCEP, etLogradouro, etComplemento, etBairro, etLocalidade, etUF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicio1);

        etCEP = findViewById(R.id.etCEP);
        etLogradouro = findViewById(R.id.etLogradouro);
        etComplemento = findViewById(R.id.etComplemento);
        etBairro = findViewById(R.id.etBairro);
        etLocalidade = findViewById(R.id.etLocalidade);
        etUF = findViewById(R.id.etUF);

    }


    public void onBuscarClick(View view) {
        String cep = String.valueOf(etCEP.getText());

        etLogradouro.setText("");
        etComplemento.setText("");
        etBairro.setText("");
        etLocalidade.setText("");
        etUF.setText("");


        new HttpAsyncTask().execute(cep);
    }

    class HttpAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://viacep.com.br/ws/"+strings[0]+"/json/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int status = urlConnection.getResponseCode();

                if (status == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder builder = new StringBuilder();

                    String inputString;
                    while ((inputString = bufferedReader.readLine()) != null) {
                        builder.append(inputString);
                    }

                    urlConnection.disconnect();
                    return builder.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s != null) {
                try {
                    JSONObject obj = new JSONObject(s);

                    etLogradouro.setText(obj.getString("logradouro"));
                    etComplemento.setText(obj.getString("complemento"));
                    etBairro.setText(obj.getString("bairro"));
                    etLocalidade.setText(obj.getString("localidade"));
                    etUF.setText(obj.getString("uf"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Exercicio1Activity.this);
            dialog.show();
        }
    }

}