package com.example.management.minigame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.management.R;

public class MiniGameMade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide(); //타이틀 바 숨기기.. setContentView()앞에 써야함..
        setContentView(R.layout.activity_mini_game_made);
    }
}
