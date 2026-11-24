package com.shitzuproxy.app;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
public class FloatingService extends Service {
    private WindowManager windowManager;
    private View bubbleView;
    private View panelView;
    private WindowManager.LayoutParams bubbleParams;
    private WindowManager.LayoutParams panelParams;
    private boolean panelVisible;
    private boolean bubbleMoved;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundServiceWithNotification();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        createBubble();
        createPanel();
        panelVisible = false;
        panelView.setVisibility(View.GONE);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bubbleView != null) {
            windowManager.removeView(bubbleView);
        }
        if (panelView != null) {
            windowManager.removeView(panelView);
        }
    }
    private void startForegroundServiceWithNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("shitzu", "Shitzu Proxy", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this, "shitzu").setContentTitle("Shitzu Proxy").setContentText("Painel ativo").setSmallIcon(R.mipmap.ic_launcher).build();
            startForeground(1, notification);
        } else {
            Notification notification = new Notification.Builder(this).setContentTitle("Shitzu Proxy").setContentText("Painel ativo").setSmallIcon(R.mipmap.ic_launcher).build();
            startForeground(1, notification);
        }
    }
    private void createBubble() {
        bubbleParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        bubbleParams.gravity = Gravity.TOP | Gravity.START;
        bubbleParams.x = 100;
        bubbleParams.y = 300;
        FrameLayout container = new FrameLayout(this);
        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.logo_circle);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        int size = UiHelper.dp(this, 56);
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(size, size);
        image.setLayoutParams(imageParams);
        image.setBackground(UiHelper.roundedStroke(UiHelper.DARK, UiHelper.RED, 28, this));
        image.setClipToOutline(true);
        container.addView(image);
        bubbleView = container;
        windowManager.addView(bubbleView, bubbleParams);
        enableBubbleDrag(bubbleView, bubbleParams);
        bubbleView.setOnClickListener(v -> {
            if (!bubbleMoved) {
                togglePanel();
            }
            bubbleMoved = false;
        });
    }
    private void createPanel() {
        panelParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        panelParams.gravity = Gravity.TOP | Gravity.START;
        panelParams.x = 40;
        panelParams.y = 200;
        PanelBuilder builder = new PanelBuilder(this);
        panelView = builder.build(this::hidePanel);
        windowManager.addView(panelView, panelParams);
        View header = builder.getHeader();
        if (header != null) {
            enableHeaderDrag(header, panelParams, panelView);
        }
    }
    private void togglePanel() {
        panelVisible = !panelVisible;
        panelView.setVisibility(panelVisible ? View.VISIBLE : View.GONE);
    }
    private void hidePanel() {
        panelVisible = false;
        panelView.setVisibility(View.GONE);
    }
    private void enableBubbleDrag(View view, WindowManager.LayoutParams params) {
        final float[] startTouch = new float[2];
        final int[] startPos = new int[2];
        view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startTouch[0] = event.getRawX();
                startTouch[1] = event.getRawY();
                startPos[0] = params.x;
                startPos[1] = params.y;
                bubbleMoved = false;
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float dx = event.getRawX() - startTouch[0];
                float dy = event.getRawY() - startTouch[1];
                if (Math.abs(dx) > 8 || Math.abs(dy) > 8) {
                    bubbleMoved = true;
                }
                params.x = startPos[0] + (int) dx;
                params.y = startPos[1] + (int) dy;
                windowManager.updateViewLayout(view, params);
                return true;
            }
            return false;
        });
    }
    private void enableHeaderDrag(View header, WindowManager.LayoutParams params, View panel) {
        final float[] startTouch = new float[2];
        final int[] startPos = new int[2];
        header.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startTouch[0] = event.getRawX();
                startTouch[1] = event.getRawY();
                startPos[0] = params.x;
                startPos[1] = params.y;
                return false;
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                params.x = startPos[0] + (int) (event.getRawX() - startTouch[0]);
                params.y = startPos[1] + (int) (event.getRawY() - startTouch[1]);
                windowManager.updateViewLayout(panel, params);
                return true;
            }
            return false;
        });
    }
}
