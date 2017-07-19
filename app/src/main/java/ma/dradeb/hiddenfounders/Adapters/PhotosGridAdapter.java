package ma.dradeb.hiddenfounders.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ma.dradeb.hiddenfounders.CustomViews.RobotoTextViewBold;
import ma.dradeb.hiddenfounders.CustomViews.RobotoTextViewThin;
import ma.dradeb.hiddenfounders.Modals.Photo;
import ma.dradeb.hiddenfounders.R;

/**
 * Created by Youness on 09/07/2017.
 */

public class PhotosGridAdapter extends BaseAdapter {


    List<Photo> Photos = new ArrayList<>();
    Context context ;

    public PhotosGridAdapter(Context context , List<Photo> Photos) {
        this.context   = context ;
        this.Photos = Photos ;
    }


    @Override
    public int getCount() {
        return Photos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ImageView cover;

        View v = LayoutInflater.from(context).inflate(R.layout.photo_item,null,false);

        //check if checked ... rhyme B|
        if(Photos.get(i).isChecked())
        {
            v.findViewById(R.id.checkit).setVisibility(View.VISIBLE);
        }

        cover = (ImageView)v.findViewById(R.id.photo_cover);
        Picasso.with(context).load(Photos.get(i).getThumb()).into(cover);

        return v;
    }
}
