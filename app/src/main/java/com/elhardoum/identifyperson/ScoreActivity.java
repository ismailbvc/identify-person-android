package com.elhardoum.identifyperson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Your Scores");

        TextView text = findViewById(R.id.score_message);
        AppCompatButton restart = findViewById(R.id.button_restart);

        text.setText(getIntent().getExtras().get("score_message").toString());

        restart.setOnClickListener(ref -> {
             Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
             intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
             ScoreActivity.this.startActivity(intent);
             finish();
        });
    }
}