package com.dpi.catatku;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static android.Manifest.*;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    public  static  final int REQUEST_CODE_STORAGE =100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Aplikasi Catatan Harian");
        setContentView(R.layout.activity_main);
        // memanggil widget dari id
        listView=(ListView)findViewById(R.id.listview);
        //panggilan balik untuk dipanggil saat item di AdapterView ini telah diklik
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,InsertAndViewActivity.class);
                Map<String,Object> data =(Map<String,Object>) parent.getAdapter().getItem(position);
                intent.putExtra("filname",data.get("name").toString());
                Toast.makeText(MainActivity.this,"You Clicked"+data.get("name"),Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> data = (Map<String, Object>)parent.getAdapter().getItem(position);
                return true;
            }
        });


    }


    @Override
    protected  void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >=23){
            if (periksaIzinPenyimpanan()){
                mengambilListFilePadaFolder();
            }
        } else {
            mengambilListFilePadaFolder();
        }
    }
    public boolean periksaIzinPenyimpanan(){
        if(Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                return false;
            }
        } else {
            return true;
        }
    }
    void mengambilListFilePadaFolder(){
        String path = Environment.getExternalStorageDirectory().toString()+"/kominfo.proyek1";
        File directory = new File(path);
        if(directory.exists()){
            File[] files = directory.listFiles();
            String[] filenames = new String[files.length];
            String[] dateCreated = new String[files.length];
            String[] contents = new String[files.length];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm:ss");
            ArrayList<Map<String, Object>> itemDataList = new ArrayList<>();
            for(int i=0;i<files.length;i++){
                filenames[i] = files[i].getName();
                // contents[i] = files[i].getC
                Date lastModDate = new Date(files[i].lastModified());
                dateCreated[i] = simpleDateFormat.format(lastModDate);
                Map<String, Object> listItemMap = new HashMap<>();
                listItemMap.put("name", filenames[i]);
                listItemMap.put("date", dateCreated[i]);
                itemDataList.add(listItemMap);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(this, itemDataList, android.R.layout.simple_list_item_2,
                    new String[]{"name","date"}, new int[]{android.R.id.text1, android.R.id.text2});
            listView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.optionmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.action_tambah:
                Intent intent = new Intent(this, InsertAndViewActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_CODE_STORAGE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mengambilListFilePadaFolder();
                }
                break;
        }
    }

    void tampilkanDialogKonfirmasiHapusCatatan(final String filename){
        new AlertDialog.Builder(this).setTitle("Hapus Catatan ini")
                .setMessage("Apakah anda yakin ingin menghapus catatan"+filename+"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton){
                        hapusFile(filename);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    void hapusFile(String filename){
        String path = Environment.getExternalStorageDirectory().toString()+"/catatku";
        File file = new File(path, filename);
        if(file.exists()){
            file.delete();
        }
        mengambilListFilePadaFolder();
    }

}