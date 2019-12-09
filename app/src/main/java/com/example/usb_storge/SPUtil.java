//package com.example.usb_storge;
//import android.app.Activity;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.content.SharedPreferences;
//import com.imageviewpager.language.MyApplication;
//import java.io.File;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Map;
//public class SPUtil {
//    /** debug 环境下允许修改 sp文件的路径 */
//    public static final boolean isDebug = true;
//    /** 修改以后的sp文件的路径 MyApplication.getContext().getExternalFilesDir(null).getAbsolutePath()=/sdcard/Android/%package_name%/file */
//    public static final String FILE_PATH = MyApplication.getContext().getExternalFilesDir(null).getAbsolutePath();
//
//    /**
//     143      * @param context
//     144      * @param fileName
//     145      * @return isDebug = 返回修改路径(路径不存在会自动创建)以后的 SharedPreferences :%FILE_PATH%/%fileName%.xml<br/>
//     146      * !isDebug = 返回默认路径下的 SharedPreferences : /data/data/%package_name%/shared_prefs/%fileName%.xml
//     147      */
//   private static SharedPreferences getSharedPreferences(Context context, String fileName) {
//       if (isDebug) {
//               try {
//               // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
//               Field field = ContextWrapper.class.getDeclaredField("mBase");
//               field.setAccessible(true);
//               // 获取mBase变量
//               Object obj = field.get(context);
//               // 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
//               field = obj.getClass().getDeclaredField("mPreferencesDir");
//               field.setAccessible(true);
//               // 创建自定义路径
//               File file = new File(FILE_PATH);
//               // 修改mPreferencesDir变量的值
//               field.set(obj, file);
//               // 返回修改路径以后的 SharedPreferences :%FILE_PATH%/%fileName%.xml
//               return context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
//           } catch (NoSuchFieldException e) {
//               e.printStackTrace();
//           } catch (IllegalArgumentException e) {
//               e.printStackTrace();
//           } catch (IllegalAccessException e) {
//               e.printStackTrace();
//           }
//   }
//       // 返回默认路径下的 SharedPreferences : /data/data/%package_name%/shared_prefs/%fileName%.xml
//       return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
//   }
//
//}
