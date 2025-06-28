package com.example.papanibadah;
import android.view.KeyEvent;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    private int okPressCount = 0;
    private long lastOkPressTime = 0;
    private int clickCount = 0;
    private long lastClickTime = 0;

    TextView tvJam, tvJudul, tvIsi;
    DBHelper dbHelper;
    Handler handler = new Handler();
    List<String[]> infoList = new ArrayList<>();
    int index = 0;

    Runnable updateInfoRunnable = new Runnable() {
        @Override
        public void run() {
            if (!infoList.isEmpty()) {
                String[] info = infoList.get(index);
                tvJudul.setText(info[0]);
                tvIsi.setText(info[1]);

                index = (index + 1) % infoList.size();
            }
            handler.postDelayed(this, 10000); // update setiap 10 detik
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        infoList.clear(); // hapus data lama
        loadInfo();       // muat data terbaru dari database
        index = 0;        // reset ke info pertama
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvJam = findViewById(R.id.tvJam);
        tvJam.setOnClickListener(view -> {
            long now = System.currentTimeMillis();

            if (now - lastClickTime < 1000) {
                clickCount++;
                if (clickCount >= 5) {
                    clickCount = 0;
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            } else {
                clickCount = 1;
            }

            lastClickTime = now;
        });

        tvJudul = findViewById(R.id.tvJudul);
        tvIsi = findViewById(R.id.tvIsi);

        dbHelper = new DBHelper(this);
        loadInfo();
        startClock();
        handler.post(updateInfoRunnable);
    }

    private void loadInfo() {
        Cursor c = dbHelper.getAllInfo();
        while (c.moveToNext()) {
            String judul = c.getString(c.getColumnIndexOrThrow("judul"));
            String isi = c.getString(c.getColumnIndexOrThrow("isi"));
            infoList.add(new String[]{judul, isi});
        }
    }

    private void startClock() {
        new Thread(() -> {
            while (true) {
                runOnUiThread(() -> {
                    String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    tvJam.setText(time);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            long now = System.currentTimeMillis();

            if (now - lastOkPressTime < 1000) {
                okPressCount++;
                if (okPressCount >= 3) {
                    okPressCount = 0;
                    // Buka AdminActivity
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            } else {
                okPressCount = 1;
            }

            lastOkPressTime = now;
            return true; // konsumsi tombol
        }

        return super.onKeyDown(keyCode, event);
    }
}



