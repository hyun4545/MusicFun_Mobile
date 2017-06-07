package com.example.myungjong.musicfun.Fragment;

import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.myungjong.musicfun.Activity.MyApplication;
import com.example.myungjong.musicfun.Model.MusicListInfo;
import com.example.myungjong.musicfun.Adapter.UploadAdapter;
import com.example.myungjong.musicfun.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class UploadFragment extends Fragment {
    Button btn;
    String upLoadServerUri/*="http://webapplication5020160711045409.azurewebsites.net/api/Account/upload3";*/;
    ListView listView;
    ArrayList<String> files_ids=null;
    ArrayList<String> music_titles2=null;
    ArrayList<String> file_names=new ArrayList<>();
    ArrayList<MusicListInfo>  music_infos=new ArrayList<>();
    ArrayList<File> files=new ArrayList<>();
    Bundle bundle;
    static UploadAdapter adapter=null;
    ArrayList<String> file_paths = new ArrayList<String>();
   // static ArrayList<String> music_titles = new ArrayList<String>();
    private static Handler handler;
    public  Boolean ArgOnNot=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upload, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView2);
        String[] ll={"ff","ferf","sgrerg"};
        Log.w("ArgOnNot",Boolean.toString(ArgOnNot));
        upLoadServerUri= MyApplication.uri+"upload3";
        /*if (ArgOnNot){
            files_ids=((MainActivity)getActivity()).getFiles_ids();
        }else {
            files_ids=null;
        }*/

       /* bundle = this.getArguments();
        // Log.w("files_id2",bundle.getString("files_id"));
        if(bundle!=null) {
            //Log.w("files_id",bundle.getString("files_id"));
            files_ids = bundle.getStringArrayList("files_id");

        }else {
            files_ids=null;
        }*/

      /* listView.setAdapter(new ArrayAdapter<String>(getActivity(),
              android.R.layout.simple_list_item_1,ll));*/
      //  btn= (Button) rootView.findViewById(R.id.update_btn);
       // TextView textView = (TextView) rootView.findViewById(R.id.tv_update_fragment);
       // textView.setText("UpdateFragment");
        Log.w("UploadFragment","onCreateView");
        String[] ll2={"cc","ccc","ccccc"};
        //  btn= (Button) rootView.findViewById(R.id.update_btn);
        // TextView textView = (TextView) rootView.findViewById(R.id.tv_update_fragment);
        // textView.setText("UpdateFragment");

if(listView.getAdapter()==null){
    listView.setAdapter(adapter);
}
        Log.w("files_id",Boolean.toString(files_ids==null));

       // listView= (ListView) rootView.findViewById(android.R.id.list);

        return rootView;
    }

    public void setUploadAdapter(ArrayList<String> music_titles){
        if(music_titles!=null) {

            //getFileName(files);

            if(adapter!=null){
                adapter.addItem(music_titles);
            }else{
                adapter = new UploadAdapter(getActivity(), music_titles);
                if(listView!=null){
                    listView.setAdapter(adapter);
                }

                Log.w("adapter",Boolean.toString(adapter==null));
                // Toast.makeText(getActivity(), music_titles.toString(), Toast.LENGTH_LONG).show();

            }
            if(handler==null){
                handler=new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch(msg.what){
                            case 1:
                                adapter.changeStatus(adapter.UPLOADED);
                                break;
                            case 2:
                                adapter.changeStatus((int)msg.obj);
                                break;

                            case 3:
                                adapter.removeItem(0);
                                break;

                        }
                    }
                };
            }

           // listView.setAdapter(adapter);
        }
    }
public Handler getHandler(){
    return  handler;
}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
       Log.w("UploadFragment","onCreat");

        super.onCreate(savedInstanceState);

    }
public void setMyArg(ArrayList<String> file_ids){


}
    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onDetach() {

        super.onDetach();
        Log.w("UploadFragment","onDetach");
    }

    @Override
    public void onAttach(Context context) {
       // files_ids=((MainActivity)getActivity()).getIntentData();

        Log.w("UploadFragment","onAttach");
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




            /*new Thread(new Runnable() {
                public void run() {

                    int file_count=0;
                    for (File file:files){
                        //adapter.changeStatus(adapter.UPLOADING_STATUS);
                        Message msg=Message.obtain();
                        Message msg1=Message.obtain();
                        Message msg2=Message.obtain();
                        msg1.what=2;
                        msg1.obj=adapter.UPLOADING_STATUS;
                        handler.sendMessage(msg1);
                        Log.w("ooooo","esgrseg");
                        int response_code=uploadFile(file);
                        if(response_code!=200){
                            msg.what=2;
                            msg.obj=adapter.ERROR_STATUS;
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
            }).start();
            // }*/


    }
    /*private void getFileName(ArrayList<File> files){
        //ArrayList<String> file_names;
        for (File file:files
                ) {
            file_names.add(file.getName());
        }

    }*/

    private ArrayList<File> UrisToFiles(ArrayList<String> sourceFileUri){
        ArrayList<File> files=new ArrayList<File>();
        //File file;
        Log.w("fffff22",sourceFileUri.toString());
        File file;
        for (String s:sourceFileUri
                ) {
            file=new File(s);
            Log.w("fffff",file.getName());
            files.add(file);
        }
        return files;
    }

    private int uploadFile( File file) {

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
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                Log.w("yy","001");
                dos = new DataOutputStream(conn.getOutputStream());
                // out_stream =conn.getOutputStream();
                //-----start

                Log.w("file",files.get(0).getName());
                int i=2;
                Log.w("yy","0052");
                //File file=files.get(i);




                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file"+i+"\";filename=\""
                        + file_name + "\"" + lineEnd);

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
                String serverResponseMessage = conn.getResponseMessage();


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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.w("UploadFragment","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
