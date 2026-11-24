package com.shitzuproxy.app;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    private TextView statusText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusText = findViewById(R.id.statusText);
        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);
        startButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                statusText.setText("Status: conceda a permissao e volte");
            } else {
                startPanel();
            }
        });
        stopButton.setOnClickListener(v -> {
            stopService(new Intent(this, FloatingService.class));
            statusText.setText("Status: painel parado");
        });
        updateStatus();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }
    private void updateStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            statusText.setText("Status: permissao necessaria");
        } else {
            statusText.setText("Status: pronto para ativar");
        }
    }
    private void startPanel() {
        Intent intent = new Intent(this, FloatingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        statusText.setText("Status: painel ativo");
        moveTaskToBack(true);
    }
}
