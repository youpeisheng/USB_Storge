package com.example.usb_storge;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.usb_storge.utils.FileIOUtils;
import com.example.usb_storge.utils.FileUtils;
import com.example.usb_storge.utils.SPUtils;
import com.tencent.mmkv.MMKV;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.BatchUpdateException;
import java.util.Map;
import java.util.Set;
//使用  SharedPreferences 系统原生的类来操作 键值对

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int datadefault=999999;
    private static final String name_package="com.example.usb_storge";
    private static final String name_data="dug_data";
    public static final String DIR_UDISK = File.separator + "storage" + File.separator + "udisk" + File.separator;
    public static final String DIR_UDISK_AIEXPRESS = DIR_UDISK + "AiExpress" + File.separator;
    public static final String path_data_Machine="/data/data/"+name_package+"/shared_prefs/"+name_data+".xml";
    public static final String name_1="搅拌臂-垂直-清洗位";
    public static final String name_2="搅拌臂-水平-清洗位";
    public static final String name_3="搅拌臂-垂直-反应杯位";
    public static final String name_4="搅拌臂-水平-反应杯位";
    public static final String name_5="穿刺臂-垂直-清洗位";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册按钮
        Button btn_write_data = (Button) findViewById(R.id.write_data);
        btn_write_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //将数据写入
                if(FileUtils.isFileExists(path_data_Machine)){
                    FileUtils.delete(path_data_Machine);
                    if(!FileUtils.isFileExists(path_data_Machine))
                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(MainActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                }
                SharedPreferences.Editor editor=getSharedPreferences(name_data,Context.MODE_PRIVATE+Context.MODE_MULTI_PROCESS).edit();
                editor.putInt(name_1,1);
                editor.putInt(name_2,2);
                editor.putInt(name_3,3);
                editor.putInt(name_4,4);
                editor.putInt(name_5,5);
                editor.apply();
            }
        });
        Button btn_read_data= (Button) findViewById(R.id.read_data);
        final TextView textView_read=(TextView) findViewById(R.id.textView_data);
        btn_read_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //将数据读取出来
            textView_read.setText("");
            if(FileUtils.isFileExists(path_data_Machine)){
                Toast.makeText(MainActivity.this,"存在"+path_data_Machine,Toast.LENGTH_SHORT).show();
                //使用系统原生方式读取 并且每次读取前都要重新加载一次文件
                SharedPreferences pref=getSharedPreferences(name_data,MODE_PRIVATE+ Context.MODE_MULTI_PROCESS);
                int namebuff_1=pref.getInt(name_1,datadefault);
                int namebuff_2=pref.getInt(name_2,datadefault);
                int namebuff_3=pref.getInt(name_3,datadefault);
                int namebuff_4=pref.getInt(name_4,datadefault);
                int namebuff_5=pref.getInt(name_5,datadefault);

                textView_read.append(Integer.toString(namebuff_1)+"\r\n");
                textView_read.append(Integer.toString(namebuff_2)+"\r\n");
                textView_read.append(Integer.toString(namebuff_3)+"\r\n");
                textView_read.append(Integer.toString(namebuff_4)+"\r\n");
                textView_read.append(Integer.toString(namebuff_5)+"\r\n");
                String xmlBuffer=FileIOUtils.readFile2String(path_data_Machine);
                textView_read.append(xmlBuffer);
            }else {
                Toast.makeText(MainActivity.this,"不存在"+path_data_Machine,Toast.LENGTH_SHORT).show();
            };
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
                if(FileUtils.copyFile(DIR_UDISK_AIEXPRESS+name_data+"2"+".xml",path_data_Machine)){
                    Toast.makeText(MainActivity.this,"复制成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"复制失败",Toast.LENGTH_SHORT).show();
                }

            }
        });
        Button btn_deleteData_Machine=(Button) findViewById(R.id.deleteData_Machine);
        btn_deleteData_Machine.setOnClickListener(new View.OnClickListener() { //删除 仪器数据
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
        Button btn_readUSB_data=(Button) findViewById(R.id.readUSB_data);
        btn_readUSB_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_read.setText("");
                if(FileUtils.isFileExists(DIR_UDISK_AIEXPRESS+"dug_data2.xml")){
                    Toast.makeText(MainActivity.this,"存在"+DIR_UDISK_AIEXPRESS+"dug_data2.xml",Toast.LENGTH_SHORT).show();
                    //使用系统原生方式读取 并且每次读取前都要重新加载一次文件
                    SharedPreferences pref=getSharedPreferences("dug_data2",MODE_PRIVATE+ Context.MODE_MULTI_PROCESS);
                    int namebuff_1=pref.getInt(name_1,datadefault);
                    int namebuff_2=pref.getInt(name_2,datadefault);
                    int namebuff_3=pref.getInt(name_3,datadefault);
                    int namebuff_4=pref.getInt(name_4,datadefault);
                    int namebuff_5=pref.getInt(name_5,datadefault);

                    textView_read.append(Integer.toString(namebuff_1)+"\r\n");
                    textView_read.append(Integer.toString(namebuff_2)+"\r\n");
                    textView_read.append(Integer.toString(namebuff_3)+"\r\n");
                    textView_read.append(Integer.toString(namebuff_4)+"\r\n");
                    textView_read.append(Integer.toString(namebuff_5)+"\r\n");
                    String xmlBuffer=FileIOUtils.readFile2String(DIR_UDISK_AIEXPRESS+"dug_data2.xml");
                    textView_read.append(xmlBuffer);
                }else {
                    Toast.makeText(MainActivity.this,"不存在"+DIR_UDISK_AIEXPRESS+"dug_data2.xml",Toast.LENGTH_SHORT).show();
                };
            }
        });
    }
}



//使用  MMKV——基于 mmap 的高性能通用 key-value 组件 腾讯开源
//public class MainActivity extends AppCompatActivity {
//    private static final String TAG = "MainActivity";
//    private static final int datadefault=999999;
//    private static final String name_package="com.example.usb_storge";
//    private static final String name_data="USB_Data.crc";
//    public static final String DIR_UDISK = File.separator + "storage" + File.separator + "udisk" + File.separator;
//    public static final String DIR_UDISK_AIEXPRESS = DIR_UDISK + "AiExpress" + File.separator;
//    public static final String path_data_Machine="/data/data/com.example.usb_storge/files/mmkv/"+name_data;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //注册按钮
//        Button btn_write_data = (Button) findViewById(R.id.write_data);
//        btn_write_data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { //将数据写入
//                String rootDir = MMKV.initialize(MainActivity.this);
////                MMKV kv = MMKV.defaultMMKV();
//                MMKV kv = MMKV.mmkvWithID(name_data);
//                Log.d(TAG,rootDir);
//                //写入
//                kv.encode("name_1", 1);
//                kv.encode("name_2", 2);
//                kv.encode("name_3", 3);
//                kv.encode("name_4", 4);
//                kv.encode("name_5", 5);
//                byte[] bytes = {'m', 'm', 'k', 'v'};
//                kv.encode("bytes", bytes);
//
//                //读取出
//                Log.d(TAG,Integer.toString(kv.decodeInt("name_1")));
//                Log.d(TAG,Integer.toString(kv.decodeInt("name_2")));
//                Log.d(TAG,Integer.toString(kv.decodeInt("name_3")));
//                Log.d(TAG,Integer.toString(kv.decodeInt("name_4")));
//                Log.d(TAG,Integer.toString(kv.decodeInt("name_5")));
//                Log.d(TAG,new String(kv.decodeBytes("bytes")));
//                kv.close();
////                kv.putInt("name_1",1);
////                kv.putInt("name_2",2);
////                kv.putInt("name_3",3);
////                kv.putInt("name_4",4);
////                kv.putInt("name_5",5);
//
//            }
//        });
//        Button btn_read_data= (Button) findViewById(R.id.read_data);
//        final TextView textView_read=(TextView) findViewById(R.id.textView_data);
//        btn_read_data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { //将数据读取出来
//                textView_read.setText("");
//
//                if(FileUtils.isFileExists(path_data_Machine)){
//                    Toast.makeText(MainActivity.this,"存在"+path_data_Machine,Toast.LENGTH_SHORT).show();
//                    //使用系统原生方式读取
//
////                    int name_1=pref.getInt("name_1",datadefault);
////                    int name_2=pref.getInt("name_2",datadefault);
////                    int name_3=pref.getInt("name_3",datadefault);
////                    int name_4=pref.getInt("name_4",datadefault);
////                    int name_5=pref.getInt("name_5",datadefault);
////
////                    textView_read.append(Integer.toString(name_1)+"\r\n");
////                    textView_read.append(Integer.toString(name_2)+"\r\n");
////                    textView_read.append(Integer.toString(name_3)+"\r\n");
////                    textView_read.append(Integer.toString(name_4)+"\r\n");
////                    textView_read.append(Integer.toString(name_5)+"\r\n");
//                    String xmlBuffer=FileIOUtils.readFile2String(path_data_Machine);
//                    textView_read.append(xmlBuffer);
//                }else {
//                    Toast.makeText(MainActivity.this,"不存在"+path_data_Machine,Toast.LENGTH_SHORT).show();
//                };
//            }
//        });
//        Button btn_copydata_toUSB=(Button) findViewById(R.id.copydata_toUSB);
//        btn_copydata_toUSB.setOnClickListener(new View.OnClickListener() { //拷贝数据到U盘
//            @Override
//            public void onClick(View v) {
//                if(FileUtils.isFileExists(DIR_UDISK_AIEXPRESS+name_data)){
//                    FileUtils.delete(DIR_UDISK_AIEXPRESS+name_data);
//                    if(!FileUtils.isFileExists(DIR_UDISK_AIEXPRESS+name_data))
//                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
//                }
//                if(FileUtils.copyFile(path_data_Machine,DIR_UDISK_AIEXPRESS+name_data)){
//                    Toast.makeText(MainActivity.this,"复制成功",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(MainActivity.this,"复制失败",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        Button btn_copydata_toMachine=(Button) findViewById(R.id.copydata_toMachine);
//        btn_copydata_toMachine.setOnClickListener(new View.OnClickListener() { //将USB 数据拷贝到仪器内部
//            @Override
//            public void onClick(View v) {
//                if(FileUtils.copyFile(DIR_UDISK_AIEXPRESS+name_data+".xml",path_data_Machine)){
//                    Toast.makeText(MainActivity.this,"复制成功",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(MainActivity.this,"复制失败",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        Button btn_deleteData_Machine=(Button) findViewById(R.id.deleteData_Machine);
//        btn_deleteData_Machine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(FileUtils.isFileExists(path_data_Machine)){
//                    FileUtils.delete(path_data_Machine);
//                    if(!FileUtils.isFileExists(path_data_Machine))
//                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
//                    else Toast.makeText(MainActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        SharedPreferences pref=getSharedPreferences(name_data,MODE_PRIVATE+ Context.MODE_MULTI_PROCESS);
//    }
//}

