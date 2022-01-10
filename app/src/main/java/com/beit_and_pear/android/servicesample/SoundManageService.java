package com.beit_and_pear.android.servicesample;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class SoundManageService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // メディアプレーヤーフィールド
    private MediaPlayer _player;

    @Override
    public void onCreate() {
        // フィールドのメディアプレーヤーオブジェクトを生成
        _player = new MediaPlayer();
    }

    // バックグラウンドで行う処理
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 音声ファイルのURI文字列を作成
        String mediaFileUriStr = "android.resource://" + getPackageName() + "/" + R.raw.sandy_beach;
        // 音声ファイルのURI文字列を元にURIオブジェクトを生成
        Uri mediaFileUri = Uri.parse(mediaFileUriStr);
        try {
            // メディアプレーヤーに音声ファイルを指定
            _player.setDataSource(SoundManageService.this, mediaFileUri);
            // 非同期でのメディア再生準備が完了した際のリスナを設定。
            _player.setOnPreparedListener(new PlayerPreparedListener());
            // メディア再生が終了した際のリスナを設定
            _player.setOnCompletionListener(new PlayerCompletionListener());
            // 非同期でメディア再生を準備
            _player.prepareAsync();
        } catch (IOException ex) {
            Log.e("ServiceSample", "メディアプレーヤー準備時の例外発生", ex);

        }
        // 定数を返す
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // プレーヤーが再生中なら
        if (_player.isPlaying()) {
            // プレーヤーを停止
            _player.stop();
        }
        // プレーヤーを解放
        _player.release();
        // プレーヤー用フィールドをnullに
        _player = null;
    }

    // メディア再生準備が完了した時のリスナクラス
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            // メディアを再生
            mp.start();
        }
    }

    // メディア再生が終了した時のリスナクラス
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // 自分自身を終了
            stopSelf();
        }
    }
}