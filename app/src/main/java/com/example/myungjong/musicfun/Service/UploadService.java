package com.example.myungjong.musicfun.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.myungjong.musicfun.Adapter.UploadAdapter;
import com.example.myungjong.musicfun.Helper.MyBinder;
import com.example.myungjong.musicfun.Helper.UrlConnect;
import com.example.myungjong.musicfun.Model.UploadInfo;
import com.example.myungjong.musicfun.Model.UploadInfos;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by myungjong on 2016/8/10.
 */
public class UploadService extends Service {
    private final IBinder binder=new ServiceBinder();
    private Context context;
    final String upLoadServerUri=" http://webapplication5020170423112144.azurewebsites.net/api/Account/upload3";
    //final String upLoadServerUri="http://webapplication5020160711045409.azurewebsites.net/api/Account/upload3";



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }



    public class ServiceBinder extends MyBinder<UploadService> {

        @Override
        public UploadService getService() {
            return UploadService.this;
        }
    }
    public void startUpload(ArrayList<UploadInfo> uploadInfos, Handler handler,String token){
        new UploadThread(uploadInfos,handler,token).start();
    }
    private class UploadThread extends Thread {
        ArrayList<UploadInfo> uploadInfos;
        String token;
        //ArrayList<File> files;
        Handler handler;
        public UploadThread(ArrayList<UploadInfo> uploadInfos, Handler handler,String token){
            this.uploadInfos=uploadInfos;
            this.handler=handler;
            this.token=token;
        }

        public void run() {

            int file_count=0;
            for (UploadInfo uploadInfo:uploadInfos){
                //adapter.changeStatus(adapter.UPLOADING_STATUS);

                Message msg=Message.obtain();
                Message msg1=Message.obtain();
                Message msg2=Message.obtain();
                msg1.what=2;
                msg1.obj=UploadAdapter.UPLOADING_STATUS;
                handler.sendMessage(msg1);
                Log.w("ooooo",uploadInfos.get(0).music_title);
                int response_code=uploadFile2(uploadInfo,token);
                if(response_code!=200){
                    msg.what=2;
                    msg.obj=UploadAdapter.ERROR_STATUS;
                    handler.sendMessage(msg);
                }else {
                    msg.what=1;
                    //file_count++;
                    msg.obj=file_count;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    msg2.what=3;
                    handler.sendMessage(msg2);
                }

            }
    }
    }

    // }
    private int uploadFile2( UploadInfo uploadInfo,String token) {
        File file=uploadInfo.file;
        String music_title= null;
        String music_author=null;
        String music_duration=null;
        try {
            music_title = URLEncoder.encode(uploadInfo.music_title,"UTF-8").replace("+", "%20");
            music_author=URLEncoder.encode(uploadInfo.music_author,"UTF-8").replace("+", "%20");
            music_duration=uploadInfo.music_duration;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        FileInputStream fileInputStream = null;
        UrlConnect urlConnect=new UrlConnect(upLoadServerUri,"POST",UrlConnect.FORM_DATA,token);
        if(!music_author.contentEquals("%3Cunknown%3E")){
            urlConnect.setFormData("author",music_author);
        }
        Log.w("author",music_author);
        Log.w("song_name",music_title);
        Log.w("song_duration",music_duration);
        urlConnect.setFormData("song_name",music_title);
        urlConnect.setFormData("song_duration",music_duration);
        try {
            fileInputStream=new FileInputStream(file);
            String file_name= URLEncoder.encode(file.getName(),"UTF-8").replace("+", "%20");
            Log.w("file_name",file_name);
            urlConnect.FileUpload(fileInputStream,file_name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return urlConnect.endToConnect2();
    }

    private int uploadFile( UploadInfo uploadInfo) {
        File file=uploadInfo.file;
        String music_title=uploadInfo.music_title;
        String music_author=uploadInfo.music_author;
        int serverResponseCode = 0;
        //String fileName =sourceFileUri.getPathSegments().get(0);
        Log.w("yy","00");
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 650;
        OutputStream out_stream;
        //ArrayList<File> files=UrisToFiles(sourceFileUri);
        // File sourceFile = new File(music_dir);
        Log.w("yy","004");

        try {

            // open a URL connection to the Servlet

            // BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            if (!file.isFile()) {
                Log.w("yy","0051");
                return 0;

            } else {

                URL url = new URL(upLoadServerUri);
                FileInputStream fileInputStream=new FileInputStream(file);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                String file_name= URLEncoder.encode(file.getName(),"UTF-8").replace("+", "%20");
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setChunkedStreamingMode(0);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


                Log.w("yy","001");
                dos = new DataOutputStream(conn.getOutputStream());
                // out_stream =conn.getOutputStream();
                //-----start

               // Log.w("file",files.get(0).getName());
                int i=2;
                Log.w("yy","0052");
                //File file=files.get(i);

                Log.w("yy2",Boolean.toString(music_author.contentEquals("<unknown>")));
if(!music_author.contentEquals("<unknown>")){
    dos.writeBytes(twoHyphens + boundary + lineEnd);
    dos.writeBytes("Content-Disposition: form-data; name=\"author\"" + lineEnd);
    dos.writeBytes(lineEnd);
    dos.writeBytes(music_author);
    Log.w("author",music_author);
    dos.writeBytes(lineEnd);
}


                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"song_name\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(music_title);
                Log.w("title",music_title);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file"+i+"\";filename=\""
                        + file_name + "\"" + lineEnd);
               Log.w("filename",file_name);
                dos.writeBytes(lineEnd);

                Log.w("yy","005");
                // bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[1024*10];

                // read file and write it into form...
                //bytesRead = fileInputStream.read(buffer);
                // Log.w("loop_count2",Double.toString(bytesAvailable));
                int c=0;
                //double process,v=0.0;
                while((c=fileInputStream.read(buffer))!=-1){
                    dos.write(buffer,0,c);
                }
                // Log.w("loop_count",Integer.toString(v));
                dos.writeBytes(lineEnd);
                //

                // fileInputStream.close();
                //dos.close();


               /* while((count = bufferedInputStream.read(buffer)) != -1)
                {
                    dos.write(buffer, 0 , count);

                }*/

                //    while (bytesRead > 0) {

                //dos.write(buffer, 0, bufferSize);
                   /* bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);*/

                // }
//-----final
                // send multipart form data necesssary after file data...

                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage().toString();


                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                // Toast.makeText(getActivity(),"HTTP Response is : "+serverResponseMessage+": "+Integer.toString(serverResponseCode) ,Toast.LENGTH_LONG).show();
                //close the streams //
                fileInputStream.close();
                dos.flush();
                //dos.flush();
                dos.close();
                // conn.disconnect(); }
            }
        } catch (MalformedURLException ex) {

            // dialog.dismiss();
            ex.printStackTrace();

                /*runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(MainActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });*/

            // Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            //dialog.dismiss();
            e.printStackTrace();

                /* runOnUiThread(new Runnable() {
                   public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(MainActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);
            }
            dialog.dismiss();*/

        }         // End else block
        return serverResponseCode;
    }

}
