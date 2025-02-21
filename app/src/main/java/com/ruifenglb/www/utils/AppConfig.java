package com.ruifenglb.www.utils;

import android.content.Context;
import android.content.res.AssetManager;
import androidx.core.app.MethodTools2;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AppConfig {

    public  static String getId(Context context){
       String str = MethodTools2.method02(readStringFromAssets(context));
        try {
            JSONObject parse = new JSONObject(str);
           return   parse.optString("MediaId","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String readStringFromAssets(Context context) {
        //通过设备管理对象 获取Asset的资源路径
        AssetManager assetManager = context.getAssets();

        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        StringBuffer sb =  new StringBuffer();
        try{
            inputStream = assetManager.open("appconfig");
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);

            sb.append(br.readLine());
            String line = null;
            while((line = br.readLine()) != null){
                sb.append(line);
            }

            br.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
