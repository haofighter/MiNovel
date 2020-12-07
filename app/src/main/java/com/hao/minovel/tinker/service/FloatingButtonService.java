package com.hao.minovel.tinker.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.hao.minovel.utils.SystemConfigUtil;
import com.hao.sharelib.FileUtils;
import com.hao.suspensionwindow.R;

import java.io.File;
import java.util.Objects;


public class FloatingButtonService extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private MediaRecorder mediaRecorder;
    private File mediaFile;
    private VirtualDisplay virtualDisplayMediaRecorder;
    public static MediaProjection mediaProjection;
    View layout;

    @Override
    public void onCreate() {
        super.onCreate();
        layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.show_top_window, null);

        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = (int) (200 * getResources().getDisplayMetrics().density);
        layoutParams.height = (int) (360 * getResources().getDisplayMetrics().density);
        layoutParams.x = 0;
        layoutParams.y = 0;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(100, SystemConfigUtil.getInstance().creatNotification(this));
        int mResultCode = intent.getIntExtra("code", -1);
        Intent mResultData = intent.getParcelableExtra("data");
        MediaProjectionManager mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        mediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, Objects.requireNonNull(mResultData));
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }


    private void showFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                windowManager.addView(layout, layoutParams);
                layout.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startRecording();
                    }
                });

                layout.findViewById(R.id.big).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutParams.width = layoutParams.width + (int) (100 * getResources().getDisplayMetrics().density);
                        layoutParams.height = layoutParams.height + (int) (180 * getResources().getDisplayMetrics().density);
                        windowManager.updateViewLayout(layout, layoutParams);
                    }
                });

                layout.findViewById(R.id.small).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutParams.width = layoutParams.width - (int) (100 * getResources().getDisplayMetrics().density);
                        layoutParams.height = layoutParams.height - (int) (180 * getResources().getDisplayMetrics().density);
                        windowManager.updateViewLayout(layout, layoutParams);
                    }
                });
                layout.setOnTouchListener(new FloatingOnTouchListener());

            }
        }
    }


    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            Log.i("触摸事件", "eventx=" + event.getX() + "      eventy=" + event.getY());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    String filePath;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createMediaRecorder() {
        filePath = "MediaRecorder_" + System.currentTimeMillis() + ".mp4";
        int width = 720;
        int height = 1080;
        int densityDpi = 1;

        // 创建保存路径
        final File dirFile = FileUtils.getCacheMovieDir(this);
        boolean mkdirs = dirFile.mkdirs();
        // 创建保存文件
        mediaFile = new File(dirFile, filePath);

        // 调用顺序不能乱
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(mediaFile.getAbsolutePath());
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setVideoSize(width, height);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoEncodingBitRate(5 * width * height);

        mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
//                if (mediaRecorderCallback != null) {
//                    mediaRecorderCallback.onFail();
//                }
            }
        });

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (virtualDisplayMediaRecorder == null) {
            virtualDisplayMediaRecorder = mediaProjection.createVirtualDisplay("MediaRecorder",
                    width, height, densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mediaRecorder.getSurface(), null, null);
        } else {
            virtualDisplayMediaRecorder.setSurface(mediaRecorder.getSurface());
        }


    }


    /**
     * 开始 媒体录制
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startRecording() {
        createMediaRecorder();
        mediaRecorder.start();

    }

    /**
     * 停止 媒体录制
     */
    public void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        Log.i("保存路径", mediaFile.getAbsolutePath());
        mediaRecorder = null;
        mediaFile = null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        super.onDestroy();
    }


}
