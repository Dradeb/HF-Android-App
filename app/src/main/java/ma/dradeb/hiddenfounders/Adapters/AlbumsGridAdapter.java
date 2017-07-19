package ma.dradeb.hiddenfounders.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ma.dradeb.hiddenfounders.CustomViews.RobotoTextViewBold;
import ma.dradeb.hiddenfounders.CustomViews.RobotoTextViewThin;
import ma.dradeb.hiddenfounders.Modals.Album;
import ma.dradeb.hiddenfounders.R;

/**
 * Created by Youness on 09/07/2017.
 */

public class AlbumsGridAdapter extends BaseAdapter {


    List<Album> albums = new ArrayList<>();
    Context context ;


    public AlbumsGridAdapter(Context context , List<Album> albums) {

        this.context = context ;
        this.albums = albums ;
    }


    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int i) {
        return null ;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        RobotoTextViewBold title  ;
        RobotoTextViewThin date ;
        ImageView cover;


        View v = LayoutInflater.from(context).inflate(R.layout.album_item,null,false);

        title = (RobotoTextViewBold) v.findViewById(R.id.album_title);
        date = (RobotoTextViewThin)v.findViewById(R.id.album_date) ;
        cover = (ImageView)v.findViewById(R.id.album_cover);


        title.setText(albums.get(i).getName());
        date.setText(albums.get(i).getCreationDate());
        System.out.println("Image url "+albums.get(i).getCoverUrl());
        Picasso.with(context).load(albums.get(i).getCoverUrl()).into(cover);

        return v;
    }




}
