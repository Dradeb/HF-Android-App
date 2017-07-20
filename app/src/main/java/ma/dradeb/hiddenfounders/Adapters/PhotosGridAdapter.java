package ma.dradeb.hiddenfounders.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
        CardView itemContainer ;

        View v = LayoutInflater.from(context).inflate(R.layout.photo_item,null,false);
        itemContainer= (CardView)v.findViewById(R.id.item_container);

        //check if checked ... rhyme B|
        if(Photos.get(i).isChecked())
        {
            v.findViewById(R.id.checkit).setVisibility(View.VISIBLE);
        }

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics m = new DisplayMetrics();
        display.getMetrics(m);
        System.out.println(" DISPLAAAY "+m.widthPixels);

        itemContainer.getLayoutParams().width = m.widthPixels/3;
        itemContainer.getLayoutParams().height = m.widthPixels/3;


        cover = (ImageView)v.findViewById(R.id.photo_cover);
        Picasso.with(context).load(Photos.get(i).getThumb()).into(cover);

        return v;
    }
}
