package com.example.aula7app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onExercicio1Click(View view) {
        startActivity(new Intent(MainActivity.this, Exercicio1Activity.class));
    }

    public void onExercicio2Click(View view) {
        startActivity(new Intent(MainActivity.this, Exercicio2Activity.class));
    }
}