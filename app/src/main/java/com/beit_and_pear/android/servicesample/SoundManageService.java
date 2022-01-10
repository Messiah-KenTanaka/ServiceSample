package com.beit_and_pear.android.servicesample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

public class SoundManageService extends Service {

    // メディアプレーヤーフィールド
    private MediaPlayer _player;
    // 通知チャネルID文字列定数
    private static final String CHANNEL_ID = "soundmanagerservice_notification_channel";

    @Override
    public void onCreate() {
        // フィールドのメディアプレーヤーオブジェクトを生成
        _player = new MediaPlayer();
        // 通知チャネル名をstring.xmlから取得
        String name = getString(R.string.notification_channel_name);
        // 通知チャネルの重要度を標準に設定
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        // 通知チャネルを生成
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        // NotificationManagerオブジェクトを取得
        NotificationManager manager = getSystemService(NotificationManager.class);
        // 通知チャネルを設定
        manager.createNotificationChannel(channel);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
            // Notificationを作成するBuilderクラス生成
            NotificationCompat.Builder builder = new NotificationCompat.Builder(SoundManageService.this, CHANNEL_ID);
            // 通知エリアに表示されるアイコンを設定
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);
            // 通知ドロワーでの表示タイトルを設定
            builder.setContentTitle(getString(R.string.msg_notification_title_start));
            // 通知ドロワーでの表示メッセージを設定
            builder.setContentText(getString(R.string.msg_notification_text_start));
            // 起動先Activityクラスを指定したIntentオブジェクトを生成
            Intent intent = new Intent(SoundManageService.this, MainActivity.class);
            // 起動先アクティビティに引き継ぎデータを格納
            intent.putExtra("fromNotification", true);
            // pendingIntentオブジェクトを取得
            PendingIntent stopServiceIntent = PendingIntent.getActivity(SoundManageService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            // PendingIntentオブジェクトをビルダーに設定
            builder.setContentIntent(stopServiceIntent);
            // タップされた通知メッセージを自動的に削除するように設定
            builder.setAutoCancel(true);
            // BuilderからNotificationオブジェクトを生成
            Notification notification = builder.build();
            // Notificationオブジェクトを元にサービスをフォアグラウンド
            startForeground(200, notification);
        }
    }

    // メディア再生が終了した時のリスナクラス
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // Notificationを生成するBuilderクラス生成
            NotificationCompat.Builder builder = new NotificationCompat.Builder(SoundManageService.this, CHANNEL_ID);
            // 通知エリアに表示されるアイコンを設定
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);
            // 通知ドロワーでの表示タイトルを設定
            builder.setContentTitle(getString(R.string.msg_notification_title_finish));
            // 通知ドロワーでの表示メッセージを設定
            builder.setContentText(getString(R.string.msg_notification_text_finish));
            // BuilderからNotificationオブジェクトを生成
            Notification notification = builder.build();
            // NotificationManagerCompatオブジェクトを取得
            NotificationManagerCompat manager = NotificationManagerCompat.from(SoundManageService.this);
            // 通知
            manager.notify(100, notification);

            // 自分自身を終了
            stopSelf();
        }
    }
}