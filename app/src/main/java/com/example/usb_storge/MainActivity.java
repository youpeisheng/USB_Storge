package com.example.usb_storge;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.usb_storge.utils.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int datadefault=999999;
    private static final String name_package="com.example.usb_storge";
    private static final String name_data="dug_data";
    public static final String DIR_UDISK = File.separator + "storage" + File.separator + "udisk" + File.separator;
    public static final String DIR_UDISK_AIEXPRESS = DIR_UDISK + "AiExpress" + File.separator;
    public static final String path_data_Machine="/data/data/"+name_package+"/shared_prefs/"+name_data+".xml";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册按钮
        Button btn_write_data = (Button) findViewById(R.id.write_data);
        btn_write_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //将数据写入
                SharedPreferences.Editor editor=getSharedPreferences(name_data,MODE_PRIVATE).edit();
                editor.putInt("name_1",1);
                editor.putInt("name_2",2);
                editor.putInt("name_3",3);
                editor.putInt("name_4",4);
                editor.putInt("name_5",4);
                editor.apply();
            }
        });
        Button btn_read_data= (Button) findViewById(R.id.read_data);
        final TextView textView_read=(TextView) findViewById(R.id.textView_data);
        btn_read_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //将数据读取出来
            File path1=getCacheDir();

            if(FileUtils.isFileExists(path_data_Machine)){
                Toast.makeText(MainActivity.this,"存在"+path_data_Machine,Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this,"不存在"+path_data_Machine,Toast.LENGTH_SHORT).show();
            };
            SharedPreferences pref=getSharedPreferences(name_data,MODE_PRIVATE);
                int name_1=pref.getInt("name_1",datadefault);
                int name_2=pref.getInt("name_2",datadefault);
                int name_3=pref.getInt("name_3",datadefault);
                int name_4=pref.getInt("name_4",datadefault);
                int name_5=pref.getInt("name_5",datadefault);
                textView_read.setText("");
                textView_read.append(Integer.toString(name_1)+"\r\n");
                textView_read.append(Integer.toString(name_2)+"\r\n");
                textView_read.append(Integer.toString(name_3)+"\r\n");
                textView_read.append(Integer.toString(name_4)+"\r\n");
                textView_read.append(Integer.toString(name_5)+"\r\n");
            }
        });
        Button btn_copydata_toUSB=(Button) findViewById(R.id.copydata_toUSB);
        btn_copydata_toUSB.setOnClickListener(new View.OnClickListener() { //拷贝数据到U盘
            @Override
            public void onClick(View v) {
//                if(FileUtils.isDir(DIR_UDISK_AIEXPRESS)){
//                    Toast.makeText(MainActivity.this,"存在"+DIR_UDISK_AIEXPRESS,Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this,"不存在"+DIR_UDISK_AIEXPRESS,Toast.LENGTH_SHORT).show();
//                };
                if(FileUtils.isFileExists(DIR_UDISK_AIEXPRESS+name_data+".xml")){
                    FileUtils.delete(DIR_UDISK_AIEXPRESS+name_data+".xml");
                    if(!FileUtils.isFileExists(DIR_UDISK_AIEXPRESS+name_data+".xml"))
                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                }
                if(FileUtils.copyFile(path_data_Machine,DIR_UDISK_AIEXPRESS+name_data+".xml")){
                    Toast.makeText(MainActivity.this,"复制成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"复制失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btn_copydata_toMachine=(Button) findViewById(R.id.copydata_toMachine);
        btn_copydata_toMachine.setOnClickListener(new View.OnClickListener() { //将USB 数据拷贝到仪器内部
            @Override
            public void onClick(View v) {
                if(FileUtils.copyFile(DIR_UDISK_AIEXPRESS+name_data+".xml",path_data_Machine)){
                    Toast.makeText(MainActivity.this,"复制成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"复制失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btn_deleteData_Machine=(Button) findViewById(R.id.deleteData_Machine);
        btn_deleteData_Machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FileUtils.isFileExists(path_data_Machine)){
                    FileUtils.delete(path_data_Machine);
                    if(!FileUtils.isFileExists(path_data_Machine))
                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(MainActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
