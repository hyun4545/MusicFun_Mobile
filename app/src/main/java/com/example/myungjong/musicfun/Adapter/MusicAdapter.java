package com.example.myungjong.musicfun.Adapter;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myungjong.musicfun.Activity.Main2Activity;
import com.example.myungjong.musicfun.Model.MusicListInfo;
import com.example.myungjong.musicfun.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by myungjong on 2016/6/22.
 */
public class MusicAdapter extends BaseAdapter implements Filterable {
    int columnIndex;
    ViewHolder viewHolder;
    Boolean isChecked=false;
   Main2Activity ctx;
    public int select_count=0;
    MusicListInfo mlinfo;
    ArrayList<String> select_arr;
    public ArrayList<String> arr_list=new ArrayList<String>();
    private ArrayList<MusicListInfo> musicListInfoArrayList;
    ArrayList<MusicListInfo> OriginArrayList;
    public MusicAdapter(ArrayList<MusicListInfo> musicListInfoArrayList, Main2Activity context){
      this.musicListInfoArrayList=musicListInfoArrayList;
        this.OriginArrayList=musicListInfoArrayList;

      this.ctx=context;
        //select_arr=select_arr();
    }
    @Override
    public int getCount() {
        return musicListInfoArrayList.size();
    }

    public ArrayList<String> getArrList(){
        return arr_list;
    }
    @Override
    public Object getItem(int position) {
        return musicListInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(musicListInfoArrayList.get(position)._id);
    }

    public void  allClick(Boolean isChecked){
        this.isChecked=isChecked;
    }
    protected ArrayList<String> select_arr(){
        ArrayList<String> arr=new ArrayList<String>();
        for (MusicListInfo info:musicListInfoArrayList
             ) {
            arr.add(info.file_path);
        }
        return  arr;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mlinfo=musicListInfoArrayList.get(position);


            //viewHolder=new ViewHolder();
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView=inflater.inflate(R.layout.listitem,parent,false);
        TextView tv= (TextView) convertView.findViewById(R.id.music_title);
        final ImageButton imgbtn= (ImageButton) convertView.findViewById(R.id.imageButton);
tv.setText(musicListInfoArrayList.get(position).title);

            //convertView.setTag(viewHolder);
            if(musicListInfoArrayList.get(position).isSelected){
                Drawable d= ContextCompat.getDrawable(convertView.getContext(), R.drawable.ic_check_circle_black_24px);
                imgbtn.setImageDrawable(d);

            }else {
                Drawable d= ContextCompat.getDrawable(convertView.getContext(), R.drawable.ic_radio_button_unchecked_black_24px);
                imgbtn.setImageDrawable(d);

            }
           imgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w("position",Integer.toString(position));
                    if(musicListInfoArrayList.get(position).isSelected){
                        Drawable d= ContextCompat.getDrawable(v.getContext(), R.drawable.ic_radio_button_unchecked_black_24px);
                       imgbtn.setImageDrawable(d);
                        musicListInfoArrayList.get(position).isSelected=false;
                        arr_list.remove(musicListInfoArrayList.get(position)._id);
                        // finalViewHolder.imgbtn.setTag(false);
                        //select_arr.remove(mlinfo._id);

                        ctx.setToolBarTitle("selected "+--select_count);
                    }else {
                        Drawable d= ContextCompat.getDrawable(v.getContext(), R.drawable.ic_check_circle_black_24px);
                       imgbtn.setImageDrawable(d);
                        musicListInfoArrayList.get(position).isSelected=true;
                        arr_list.add(musicListInfoArrayList.get(position)._id);
                        //finalViewHolder.imgbtn.setTag(true);
                        //select_arr.add(mlinfo._id);
                        Main2Activity ff= (Main2Activity)ctx;
                        ctx.setToolBarTitle("selected "+(++select_count));
                    }
                }

            });



        //final View finalConvertView1 = convertView;

        //viewHolder.tv.setText(mlinfo.title);


        return convertView;
    }

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
int countSelect(){
    int count=0;
    for (MusicListInfo info:musicListInfoArrayList
         ) {
        if(info.isSelected==true)count++;
    }return count;}

    @Override
    public Filter getFilter() {
        final Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<MusicListInfo> FilteredArrList = new ArrayList<MusicListInfo>();

                    constraint = constraint.toString().toLowerCase();
                Log.w("count22",constraint.toString());
                    for (int i = 0; i < OriginArrayList.size(); i++) {
                        MusicListInfo data = OriginArrayList.get(i);
                        if (data.title.toLowerCase().contains(constraint.toString())) {

                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                Log.w("count",Integer.toString(results.count));
                    results.values = FilteredArrList;

                return results;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                musicListInfoArrayList = (ArrayList<MusicListInfo>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
                    ctx.setSearch_text(musicListInfoArrayList.size());

            }
        };
            return filter;
    }

    class ViewHolder{
        TextView tv;
        ImageButton imgbtn;
    }


}
