package com.example.management.minigame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.management.MainActivity;
import com.example.management.R;

public class MiniGameHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide(); //타이틀 바 숨기기.. setContentView()앞에 써야함..
        setContentView(R.layout.activity_mini_game_home);

        Button button=(Button)findViewById(R.id.button);
        Button button2=(Button)findViewById(R.id.button2);
        Button button3= (Button)findViewById(R.id.button3);
        TextView textView = (TextView)findViewById(R.id.title_name);

        Intent it = getIntent();
        final String userId = it.getStringExtra("NAME");
        textView.setText(userId + " 님의");

        button.setOnClickListener(new View.OnClickListener() {  //게임시작하는 버튼
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MiniGameHome.this, MiniGameMain.class);
                intent.putExtra("NAME",userId);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {//랭킹화면 보여주는것
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MiniGameHome.this, MiniGameRank.class);
                startActivity(intent2);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() { //제작자를 보여주는 버튼
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MiniGameHome.this, MiniGameMade.class);
                startActivity(intent3);
            }
        });
    }
}
