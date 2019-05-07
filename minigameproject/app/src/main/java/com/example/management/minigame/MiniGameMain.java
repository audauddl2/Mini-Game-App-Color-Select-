package com.example.management.minigame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.management.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MiniGameMain extends AppCompatActivity {

    TimerThread mTimer; //타이머
    SendMessageHandler mHandler = new SendMessageHandler(); //메세지를 주는 핸들러

    TableLayout tLayout; //네모모양때문에 쓰는 tablelayout
    TableRow tableRow[];
    TextView textView[];    //10x10

    int start_rows = 2;
    int tv_count = start_rows * start_rows;
    int rand_num;
    int end_level=1;
    String encrypt_end_level;

    int color_low_alpha = 50;        //알파값을 수정하기 위한 변수
    int color_high_alpha = 155;
    int color_index = 0;
    ArrayList<ArrayList> color;
    ArrayList<Integer> color_red;
    ArrayList<Integer> color_green;
    ArrayList<Integer> color_blue;
    ArrayList<Integer> color_pink;
    ArrayList<Integer> color_gray;
    ArrayList<Integer> color_lt_gray;
    ArrayList<Integer> color_lt_green;
    ArrayList<Integer> color_lt_blue;

    ArrayList<Integer> rand_index;
    TextView tv_timer;
    TextView tv_level;

    int time = 14;
    int mlis = 9;
    int level = 1;
    int rounds = 1;

    Button btn_start;

    final int SET_TIMER = 0 ;
    final int GAME_OVER = 1;


    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().hide(); //타이틀 바 숨기기.. setContentView()앞에 써야함..
        setContentView(R.layout.activity_mini_game_main);

        tLayout = (TableLayout) findViewById(R.id.colorPad);
        tv_timer = (TextView) findViewById(R.id.timer);
        tv_level = (TextView) findViewById(R.id.level);
        btn_start = (Button) findViewById(R.id.gamestart);

        Intent it = getIntent();
        userId = it.getStringExtra("NAME");

        tv_timer.setText(15 + "." + 0);
        tv_level.setText("" + level);
    }

    public void game_start(View v){
        tLayout.setVisibility(View.VISIBLE); //색깔테이블을 보이게 한다.
        btn_start.setVisibility(View.INVISIBLE);// 스타트 버튼은 안보이게 설정

        mTimer = new TimerThread();
        mTimer.TimerThread();
        mTimer.TimerStart();//flag는 true
        mTimer.start();

        init_color(); //색깔을 보여주도록 하는 것!!!

        set_layout(start_rows); //처음은 2X2로
        set_rand_index(); //색깔이 다른 한 블록을 랜덤으로 지정하는 함수
    }

    public void game_over(){
        tLayout.setVisibility(View.INVISIBLE);
        btn_start.setVisibility(View.VISIBLE);

        mTimer.TimerStop();
        show_alert(); //점수를 보여줌
        //변수 초기화
        start_rows = 2;
        tv_count = start_rows * start_rows; //전체 테이블 갯수,..
        rounds = 1;
        level = 1;
        time = 14;
        mlis = 9;
        color_index = 0;
        color_low_alpha = 50;
        color_high_alpha = 155;

        tv_level.setText("" + level);
        tv_timer.setText("" + 15 + "." + 0);
    }

    public void set_rand_index(){ //랜덤으로
        rand_index = new ArrayList<>();
        for(int index = 0; index < tv_count; index++){ //
            rand_index.add(index);
        }
        Collections.shuffle(rand_index); //List의 값들이 무작위로 리턴해 줍니다.

        rand_num = rand_index.get(0);
        //진한색 index 첫번째로 가져온다!
        textView[rand_num].setBackgroundColor((Integer)color.get(color_index).get(1));
    }

    public void level_up(int row_count, int chk_index){
        if(chk_index == rand_num){
            time = 14;
            mlis = 9;
            color_index = color_index + 1;
            level = level + 1;

            //alpha값이 최대값을 넘지 못하도록 설정
            if(!(color_low_alpha >= 134))
                color_low_alpha = color_low_alpha + 2;
            else   color_low_alpha = 134;
            if(!(color_high_alpha >= 241))
                color_high_alpha = color_high_alpha + 2;
            else   color_high_alpha = 241;

            if(color_index > color.size()-1)
                color_index = 0;

            tv_timer.setText("" + 15 + "." + 0);
            tv_level.setText("" + level);

            init_color();
            //제대로 선택되면 색상이 점점 어두워지도록(?) alpha 값을 증가시킨다

            //현재 등록된 색상이 다 선택될때까지
            if(( rounds % color.size() )==0) {
                row_count= row_count + 1;
                tv_count = row_count * row_count;
                set_layout(row_count);
                set_rand_index();
                rounds = 1;
            }else{
                rounds++;
                tv_count = row_count * row_count;
                set_layout(row_count);
                set_rand_index();
            }
        }else{
            if(time > 0) {
                mTimer.TimerStop();
                time = time - 3;
                mTimer.TimerStart();
                if(time <= 0){
                    time = 0;
                    mHandler.sendEmptyMessage(GAME_OVER);
                }
                tv_timer.setText("" + time + "." + mlis);
            }
        }
    }
    public void set_layout(final int row_count){
        final TableLayout tLayout = (TableLayout) findViewById(R.id.colorPad);
        tableRow = new TableRow[row_count];
        textView = new TextView[tv_count];

        tLayout.removeAllViews(); //

        int tv_index = 0;

        for(int row_index = 0 ; row_index < row_count; row_index++){
            tableRow[row_index] = new TableRow(this);
            //tableRow weight setting
            TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1
            );
            tableRow[row_index].setLayoutParams(rowParams);

            for(; tv_index < row_count*(row_index+1); tv_index++){
                final int chk_index = tv_index;

                textView[tv_index] = new TextView(this);
                //연한색 index 0번
                textView[tv_index].setBackgroundColor((Integer)color.get(color_index).get(0));
                TableRow.LayoutParams tvParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1
                );
                tvParams.setMargins(5,5,5,5);
                textView[tv_index].setLayoutParams(tvParams);
                textView[tv_index].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        level_up(row_count,chk_index);
                    }
                });

                tableRow[row_index].addView(textView[tv_index]);
            }
            tLayout.addView(tableRow[row_index]);
        }
    }

    class SendMessageHandler extends Handler {
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case SET_TIMER:
                    tv_timer.setText("" + time + "." + mlis);
                    break;
                case GAME_OVER:
                    game_over();
                    break;
            }
        }
    }

    class TimerThread extends Thread implements Runnable {
        boolean flag = false;
        public void TimerThread(){
            time = 14;
        }
        public void TimerStart(){ //타이머가 스타트가되면 flag는 true로 바꾸고..!
            this.flag = true;
        }
        public void TimerStop(){
            this.flag = false;
        }

        public void run() {
            super.run();
            while (flag){  //true 이면 무한루프
                try {
                    mlis --;
                    mHandler.sendEmptyMessage(SET_TIMER);
                    sleep(100);//0.1초
                    if(mlis == 0 ) {
                        time--; //
                        if (time < 0) { //시간이 0이 아니면
                            mlis = 9;
                            mHandler.sendEmptyMessage(GAME_OVER);
                            break;
                        } else { //시간이 0이상인 경우
                            mlis = 9;
                            mHandler.sendEmptyMessage(SET_TIMER);
                        }
                    }
                } catch (InterruptedException e) {}
            }
        }
    }
    public void init_color(){
        color = new ArrayList<>();

        color_red = new ArrayList<>();
        color_red.add(Color.argb(255, 255, 0, 0));
        color_red.add(Color.argb(color_high_alpha, 255, 0, 0));

        color_green = new ArrayList<>();
        color_green.add(Color.argb(255, 0, 255, 0));
        color_green.add(Color.argb(color_high_alpha, 0, 255, 0));

        color_blue = new ArrayList<>();
        color_blue.add(Color.argb(255, 0, 0, 255));
        color_blue.add(Color.argb(color_high_alpha, 0, 0, 255));

        color_pink = new ArrayList<>();
        color_pink.add(Color.argb(255, 255, 0, 255));
        color_pink.add(Color.argb(color_high_alpha, 255, 0, 255));

        color_gray = new ArrayList<>();
        color_gray.add(Color.argb(150, 0, 0, 0));
        color_gray.add(Color.argb(color_low_alpha, 0, 0, 0));

        color_lt_gray = new ArrayList<>();
        color_lt_gray.add(Color.argb(255, 200, 200, 200));
        color_lt_gray.add(Color.argb(color_high_alpha, 200, 200, 200));

        color_lt_blue = new ArrayList<>();
        color_lt_blue.add(Color.argb(150, 0, 0, 200));
        color_lt_blue.add(Color.argb(color_low_alpha, 0, 0, 200));

        color_lt_green = new ArrayList<>();
        color_lt_green.add(Color.argb(150, 0, 200, 0));
        color_lt_green.add(Color.argb(color_low_alpha, 0, 200, 0));

        color.add(color_red);
        color.add(color_green);
        color.add(color_lt_green);
        color.add(color_blue);
        color.add(color_lt_blue);
        color.add(color_pink);
        color.add(color_gray);
        color.add(color_lt_gray);

        Collections.shuffle(color);
    }

    public void show_alert(){// 게임이 끝났을때 알려주는 창

        if(level > 1) end_level = level - 1;

        if(end_level>=1 & end_level<=7){
            Toast.makeText(MiniGameMain.this, "초보 감각", Toast.LENGTH_LONG).show();
        }
        if(end_level>=8 & end_level<=15){
            Toast.makeText(MiniGameMain.this, "일반인 감각", Toast.LENGTH_LONG).show();
        }
        if(end_level>=16 & end_level<=24){
            Toast.makeText(MiniGameMain.this, "뛰어난 감각", Toast.LENGTH_LONG).show();
        }
        if(end_level>=25 & end_level<=31){
            Toast.makeText(MiniGameMain.this, "미대생 감각", Toast.LENGTH_LONG).show();
        }
        if(end_level>=32 & end_level<=40){
            Toast.makeText(MiniGameMain.this, "피카소 감각", Toast.LENGTH_LONG).show();
        }
        if(end_level>=41){
            Toast.makeText(MiniGameMain.this, "끝판왕 등극", Toast.LENGTH_LONG).show();
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        try {
            String originalText = String.valueOf(end_level);
            String key = "key";
            encrypt_end_level = Encrypt( originalText, key);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        alert.setTitle("축하합니다\n랭킹에 등록하시겠습니까?").
                setMessage("LEVEL : "+ end_level ).
                setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        insertToDatabase(userId, encrypt_end_level);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alert.show();
    }

    private void insertToDatabase (String Id, String rank){
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MiniGameMain.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String Id = (String) params[0];
                    String rank = (String) params[1];

                    String link = "http://kangsy763.cafe24.com/RankingUpdate.php";
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("rank", "UTF-8") + "=" + URLEncoder.encode(rank, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(Id, String.valueOf(rank));
    }

    public static String Decrypt(String text, String key) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= key.getBytes("UTF-8");
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
//               BASE64Decoder decoder = new BASE64Decoder();
//               Base64.decode(input, flags)
//               byte [] results = cipher.doFinal(decoder.decodeBuffer(text));
        // BASE64Decoder decoder = new BASE64Decoder();
        // Base64.decode(input, flags)
        byte [] results = cipher.doFinal(Base64.decode(text, 0));
        return new String(results,"UTF-8");
    }

    public static String Encrypt(String text, String key) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] b= key.getBytes("UTF-8");
        int len= b.length;
        if (len > keyBytes.length) len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
//               BASE64Encoder encoder = new BASE64Encoder();
//               return encoder.encode(results);
        return Base64.encodeToString(results, 0);
    }
}
