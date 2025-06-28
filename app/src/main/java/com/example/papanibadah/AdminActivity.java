package com.example.papanibadah;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdminActivity extends AppCompatActivity {

    EditText etJudul, etIsi;
    Button btnSimpan, btnUpdate;
    ListView listInfo;
    DBHelper dbHelper;
    ArrayAdapter<String> adapter;
    List<String[]> dataList = new ArrayList<>();
    int selectedId = -1; // id yang dipilih

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        etJudul = findViewById(R.id.etJudul);
        etIsi = findViewById(R.id.etIsi);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnUpdate = findViewById(R.id.btnUpdate);
        Button btnHapus = findViewById(R.id.btnHapus);
        listInfo = findViewById(R.id.listInfo);
        dbHelper = new DBHelper(this);

        loadData();
        btnHapus.setOnClickListener(v -> {
            if (selectedId == -1) {
                Toast.makeText(this, "Pilih data dari daftar untuk dihapus", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.deleteInfo(selectedId);
            Toast.makeText(this, "Data dihapus", Toast.LENGTH_SHORT).show();
            etJudul.setText(""); etIsi.setText(""); selectedId = -1;
            loadData();
        });
        btnSimpan.setOnClickListener(v -> {
            String judul = etJudul.getText().toString().trim();
            String isi = etIsi.getText().toString().trim();
            String waktu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            if (judul.isEmpty() || isi.isEmpty()) {
                Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.insertInfo(judul, isi, waktu);
            Toast.makeText(this, "Data disimpan", Toast.LENGTH_SHORT).show();
            etJudul.setText(""); etIsi.setText(""); selectedId = -1;
            loadData();
        });

        btnUpdate.setOnClickListener(v -> {
            if (selectedId == -1) {
                Toast.makeText(this, "Pilih data dari daftar untuk update", Toast.LENGTH_SHORT).show();
                return;
            }

            String judul = etJudul.getText().toString().trim();
            String isi = etIsi.getText().toString().trim();

            if (judul.isEmpty() || isi.isEmpty()) {
                Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.updateInfo(selectedId, judul, isi);
            Toast.makeText(this, "Data diupdate", Toast.LENGTH_SHORT).show();
            etJudul.setText(""); etIsi.setText(""); selectedId = -1;
            loadData();
        });

        listInfo.setOnItemClickListener((parent, view, position, id) -> {
            String[] data = dataList.get(position);
            selectedId = Integer.parseInt(data[0]);
            etJudul.setText(data[1]);
            etIsi.setText(data[2]);
        });
    }

    private void loadData() {
        Cursor c = dbHelper.getAllInfo();
        dataList.clear();
        List<String> titles = new ArrayList<>();

        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndexOrThrow("id"));
            String judul = c.getString(c.getColumnIndexOrThrow("judul"));
            String isi = c.getString(c.getColumnIndexOrThrow("isi"));
            dataList.add(new String[]{id, judul, isi});
            titles.add("#" + id + " - " + judul);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        listInfo.setAdapter(adapter);
    }
}
