package com.example.myungjong.musicfun.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.myungjong.musicfun.Activity.MainActivity;
import com.example.myungjong.musicfun.Activity.MyApplication;
import com.example.myungjong.musicfun.Helper.DialogHelper;
import com.example.myungjong.musicfun.Helper.MusicFun;
import com.example.myungjong.musicfun.Helper.MusicInfo;
import com.example.myungjong.musicfun.Model.MusicList;
import com.example.myungjong.musicfun.Model.MusicListInfo;
import com.example.myungjong.musicfun.R;
import com.example.myungjong.musicfun.Fragment.*;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by myungjong on 2016/9/1.
 */
public class ListAdapter extends BaseAdapter implements Filterable {
    List<MusicList> musicLists;
    LayoutInflater inflater;
    MusicFragment songsFragment;
    MainActivity mainActivity;
    static int selected_index;
    Context ctx;
    //MusicFun musicFun;
    static int  selected=-1;
    public static boolean isSelected;
    MyApplication application;
    int current_position;
    View lastView;
    int lastViewId;
    Boolean isSearch=false;
    int _position;
    public ListAdapter(Context ctx, List<MusicList> musicLists,MusicFragment songsFragment){
        this.ctx=ctx;
        this.songsFragment=songsFragment;
        inflater= LayoutInflater.from(ctx);
        this.musicLists=musicLists;

        mainActivity= (MainActivity) songsFragment.getActivity();
        application= (MyApplication) ((MainActivity)ctx).getApplication();
        //musicFun=application.getMusicService().musicFun;
        //lastView=application.getLastView();
       // Log.e("lastView",Boolean.toString(lastView==null));
    }
    @Override
    public int getCount() {
        return musicLists.size();
    }

    @Override
    public Object getItem(int position) {
        return musicLists.get(position);
    }


    public void setIsSearch(Boolean isSearch){
        this.isSearch=isSearch;
    }
    @Override
    public long getItemId(int position) {
        return musicLists.get(position).getId();
    }

    /*private void setSelected(int position){
        musicFun.setPosition(position);
        if(!musicFun.isSelect()){
            musicFun.setPlay(true);
        }else {
            musicFun.setPlay(false);
        }
       musicFun.setSelect(true);
        notifyDataSetChanged();
    }*/
    @Override
    public Filter getFilter() {
        final Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                Log.w("constraint22",constraint.toString());
                Log.w("constraint",Boolean.toString(constraint.toString().isEmpty()));// Holds the results of a filtering operation in values
                ArrayList<MusicList> FilteredArrList = new ArrayList<MusicList>();
                if(constraint.toString().isEmpty()){
                    results.values=application.getOriginal_musicLists();
                    results.count =application.getOriginal_musicLists().size();
                    return results;
                }
                constraint = constraint.toString().toLowerCase();
                Log.w("count22",constraint.toString());
                for (int i = 0; i < getMusicInfo().getMusicLists().size(); i++) {
                    MusicList data = getMusicInfo().getMusicLists().get(i);
                    if (data.getSong_title().toLowerCase().contains(constraint.toString())) {
                        //data.setOrigin_index(i);
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
                isSearch=true;
                musicLists = (ArrayList<MusicList>) results.values;

                // has the filtered values
                //getMusicInfo().setMusicLists(musicLists);
                notifyDataSetChanged();  // notifies the data with new filtered values
                songsFragment.setSearch_text(musicLists.size());

            }


        };
        return filter;
    }
    private MusicInfo getMusicInfo(){
        return MusicInfo.getInstance();
    }
    @Override
    public void notifyDataSetChanged() {
        Log.e("iiisSearch",Boolean.toString(isSearch));
        if(isSearch){
            musicLists=getMusicInfo().getMusicLists();
        }
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        this._position=position;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.listitem_main,null);
            viewHolder=new ViewHolder();
            viewHolder.title= (TextView) convertView.findViewById(R.id.music_title);
            viewHolder.author= (TextView) convertView.findViewById(R.id.music_author);
            viewHolder.menu= (Button) convertView.findViewById(R.id.list_menu);
            viewHolder.linearLayout= (LinearLayout) convertView.findViewById(R.id.list_body);
            convertView.setTag(viewHolder);
        }else {

            viewHolder= (ViewHolder) convertView.getTag();
        }
        final MusicList musicList=musicLists.get(position);
        Log.e("musicLists count",Integer.toString(musicLists.size()));
        viewHolder.title.setText(musicList.getSong_title());
        viewHolder.author.setText(musicList.getAuthor());
        Log.e("musicList.getId()",Integer.toString(musicList.getId()));
       // Log.e("getMusicInfo().getId()",Integer.toString(getMusicInfo().getId()));
        if((getMusicInfo().getId()!=0)&&(musicList.getId()==getMusicInfo().getId())){
            Log.e("BB1",Integer.toString(position));
            viewHolder.linearLayout.setBackground(ctx.getDrawable(R.drawable.bottom_layout_gradient11));
           /* getMusicInfo().setPosition(position);
            if(lastViewId!=musicList.getId()) {
                if (lastView != null) {
                    lastView.setBackgroundColor(Color.TRANSPARENT);
                }
                lastView = viewHolder.linearLayout;
            }else {
                if (isSearch){
                    if (lastView != null) {
                        lastView.setBackgroundColor(Color.TRANSPARENT);
                    }
                    lastView = viewHolder.linearLayout;
                }
            }




             lastViewId=musicList.getId();

*/

        }else {
            viewHolder.linearLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        viewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // parent.showContextMenuForChild(v);
                songsFragment.setEditDialog(musicList);
                //application.windoww=mainActivity.getWindow().getDecorView().getBackground();
                //DialogHelper dialogHelper=new DialogHelper(mainActivity,DialogHelper.EDIT_DIALOG);
                //dialogHelper.bulid();
            }
        });

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(isSearch){
                    application.setMusic_id(musicList.getId());
                    for (int i=0;i<application.getMusicLists().size();i++)
                          {
                        if(application.getMusicLists().get(i).getId()==musicList.getId()){
                            _position=i;
                        }
                    }

                }*/




                getMusicInfo().setMusicInfo(musicLists,position);
                Log.e("wefwef",Integer.toString(musicLists.size()));
                notifyDataSetChanged();
                application.getMusicService().musicFun.playMusic();

                Log.e("list position",Integer.toString(position));

                Log.e("app position",Integer.toString(getMusicInfo().getPosition()));
                //setSelected(position);

            }
        });
        return convertView;
    }
    static class ViewHolder{
       TextView title,author;
        Button menu;
        LinearLayout linearLayout;

    }
}
