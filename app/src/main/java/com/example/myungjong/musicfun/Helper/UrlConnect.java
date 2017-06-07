package com.example.myungjong.musicfun.Helper;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.myungjong.musicfun.Model.MusicList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by myungjong on 2016/8/27.
 */
public class UrlConnect<T> {
    HttpURLConnection conn = null;
    int serverResponseCode = 0;
    DataOutputStream dos = null;
    final String lineEnd = "\r\n";
    final String twoHyphens = "--";
    final String boundary = "*****";
    public static final String FORM_DATA="multipart/form-data";
    public static final String JSON_DATA="application/json";
    String contentType,uri,method,token;
    byte[] buffer;

    ResponseInfo responseInfo;
    OutputStreamWriter wr;
    StringBuilder result = new StringBuilder();
    public UrlConnect(String uri,String method,@Nullable String contentType,@Nullable String token){

        this.uri=uri;
        this.method=method;
        this.contentType=contentType;
        this.token=token;
        if(method=="POST"){
            connectPOST();
        }else if(method=="GET"){
            connectGET();
        }else{

        }

    }



    private void connectPOST(){
        URL url = null;
        try {
            url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod(method);
           // conn.setChunkedStreamingMode(0);
            conn.setRequestProperty("Connection", "Keep-Alive");
            if(contentType==FORM_DATA){
                conn.setRequestProperty("Content-Type", contentType+";boundary=" + boundary);
            }else {
                conn.setRequestProperty("Content-Type", contentType);
            }
            if(token!=null){
                conn.setRequestProperty("access_token",token);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dos = new DataOutputStream(conn.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void connectGET(){
        URL url = null;
        try {
            url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            if(token!=null){
                conn.setRequestProperty("access_token",token);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void setJsonData(JSONObject jsonParam){
        try {
            dos.writeBytes(jsonParam.toString());
Log.w("json",jsonParam.toString());
                    } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFormData (String key,String value){
        try {
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\""+key+"\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(value);
            dos.writeBytes(lineEnd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void FileUpload(FileInputStream fileInputStream,String filename){
        try {
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file"+"\";filename=\""
                    + filename + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            buffer = new byte[1024*10];
            int c=0;
            while((c=fileInputStream.read(buffer))!=-1){
                dos.write(buffer,0,c);
            }
            dos.writeBytes(lineEnd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public ResponseInfo endToConnect(){
        try {
            if(contentType==this.FORM_DATA){
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            }

           serverResponseCode = conn.getResponseCode();
            InputStream in = new BufferedInputStream(conn.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            String serverResponseMessage = result.toString();
            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);
            responseInfo=new ResponseInfo ();
            responseInfo.message=serverResponseMessage;
            responseInfo.code=serverResponseCode;
            if(dos!=null){
                dos.flush();
                dos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
            return responseInfo;
    }
    public String endToConnect4(){
        String serverResponseMessage="";
        try {
            if(contentType==this.FORM_DATA){
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            }


            InputStream in = new BufferedInputStream(conn.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            serverResponseMessage = result.toString();
            Log.e("serverResponseMessage",serverResponseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponseMessage;
    }
    public ArrayList<T> endToConnect1(Class<T> tClass){
        ArrayList<T> jsonArr=null;

        try {
            if(contentType==this.FORM_DATA){
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            }

            serverResponseCode = conn.getResponseCode();
            InputStream in = new BufferedInputStream(conn.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            String serverResponseMessage = result.toString();
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<T>>() {}.getType();
            Log.e("listType ",new ListOfSomething<T>(tClass).toString());
            jsonArr = gson.fromJson(serverResponseMessage, new ListOfSomething<T>(tClass));
            if(dos!=null){
                dos.flush();
                dos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArr;
    }
    public int endToConnect2(){
        try {
            if(contentType==this.FORM_DATA){
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            }

            serverResponseCode = conn.getResponseCode();

            String serverResponseMessage = conn.getResponseMessage();
            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);
            if(dos!=null){
                dos.flush();
                dos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
return serverResponseCode;
    }
   public class ResponseInfo{
       public String message;
       public int code;
   }
}
class ListOfSomething<X> implements ParameterizedType {

    private Class<?> wrapped;

    public ListOfSomething(Class<X> wrapped) {
        this.wrapped = wrapped;
    }

    public Type[] getActualTypeArguments() {
        return new Type[] {wrapped};
    }

    public Type getRawType() {
        return List.class;
    }

    public Type getOwnerType() {
        return null;
    }

}
