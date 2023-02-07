package com.hypersoft.flyinghagiwagi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.EventListener;

public class GameOverActivity extends AppCompatActivity {
    private Button resetButton;
    private TextView displayScore;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        resetButton = (Button) findViewById(R.id.play_again_button);
        displayScore = (TextView) findViewById(R.id.displayScore);

        Intent intent = getIntent();
        score = intent.getIntExtra("score",0);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(GameOverActivity.this,MainActivity.class);
                startActivity(mainIntent);
            }
        });
        displayScore.setText("Score: "+score);
    }
}