package com.example.aula7app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Exercicio2Activity extends AppCompatActivity {

    EditText etTemperatura, etUmidade, etPontoOrvalho, etPressao;
    ListView lvLista;

    String[] index = {"temperature", "humidity", "dewpoint", "pressure", "speed", "direction"};
    int[] campos = {R.id.tvTemperatura, R.id.tvUmidade, R.id.tvPontoOrvalho, R.id.tvPressao, R.id.tvVelocidade, R.id.tvDirecao};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicio2);

        etTemperatura = findViewById(R.id.etTemperatura);
        etUmidade = findViewById(R.id.etUmidade);
        etPontoOrvalho = findViewById(R.id.etPontoOrvalho);
        etPressao = findViewById(R.id.etPressao);

        lvLista = findViewById(R.id.lvLista);

        new HttpAsyncTask().execute();
    }

    class HttpAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://ghelfer.net/la/weather.json");
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

                    JSONArray array = obj.getJSONArray("weather");

                    ArrayList<HashMap<String, Object>> dados = new ArrayList<>();

                    double mediaTemperatura = 0;
                    double mediaUmidade = 0;
                    double mediaPontoOrvalho = 0;
                    double mediaPressao = 0;

                    int x;
                   for (x=0; x < array.length(); x++){
                       JSONObject item = array.getJSONObject(x);

                        double temperatura = item.getDouble(index[0]);
                        double umidade = item.getDouble(index[1]);
                        double pontoOrvalho = item.getDouble(index[2]);
                        double pressao = item.getDouble(index[3]);

                        mediaTemperatura += temperatura;
                        mediaUmidade += umidade;
                        mediaPontoOrvalho += pontoOrvalho;
                        mediaPressao += pressao;

                       HashMap<String, Object> new_item = new HashMap<>();

                       new_item.put(index[0], temperatura);
                       new_item.put(index[1], umidade);
                       new_item.put(index[2], pontoOrvalho);
                       new_item.put(index[3], pressao);
                       new_item.put(index[4], item.getDouble(index[4]));
                       new_item.put(index[5], item.getString(index[5]));

                       dados.add(new_item);
                   }

                   mediaTemperatura /= x;
                   mediaUmidade /= x;
                   mediaPontoOrvalho /= x;
                   mediaPressao /= x;

                    SimpleAdapter adapter = new SimpleAdapter(Exercicio2Activity.this, dados, R.layout.item_row, index, campos);

                    etTemperatura.setText(String.valueOf(mediaTemperatura));
                    etUmidade.setText(String.valueOf(mediaUmidade));
                    etPontoOrvalho.setText(String.valueOf(mediaPontoOrvalho));
                    etPressao.setText(String.valueOf(mediaPressao));

                    lvLista.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Exercicio2Activity.this);
            dialog.show();
        }
    }
}