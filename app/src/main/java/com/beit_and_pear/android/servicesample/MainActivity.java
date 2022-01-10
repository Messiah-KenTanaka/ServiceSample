package com.beit_and_pear.android.servicesample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 再生ボタンタップ時の処理
    public void onPlayButtonClick(View view) {
        // インテントオブジェクトを生成
        Intent intent = new Intent(MainActivity.this, SoundManageService.class);
        // サービスを起動
        startService(intent);
        // 再生ボタンをタップ不可に、停止ボタンをタップ可に変更
        Button btPlay = findViewById(R.id.btPlay);
        Button btStop = findViewById(R.id.btStop);
        btPlay.setEnabled(false);
        btStop.setEnabled(true);
    }

    // 停止ボタンタップ時の処理
    public void onStopButtonClick(View view) {
        // インテントオブジェクトを生成
        Intent intent = new Intent(MainActivity.this, SoundManageService.class);
        // サービスを停止
        stopService(intent);
        // 再生ボタンをタップ可に、停止ボタンをタップ不可に変更
        Button btPlay = findViewById(R.id.btPlay);
        Button btStop = findViewById(R.id.btStop);
        btPlay.setEnabled(true);
        btStop.setEnabled(false);
    }
}