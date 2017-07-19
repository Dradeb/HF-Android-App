package ma.dradeb.hiddenfounders.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import ma.dradeb.hiddenfounders.Adapters.PhotosGridAdapter;
import ma.dradeb.hiddenfounders.Modals.Photo;
import ma.dradeb.hiddenfounders.R;

/**
 * Created by Youness on 10/07/2017.
 */

public class PhotosFragment extends Fragment {

    List<Photo> photos = new ArrayList<>();
    GridView photosgrid ;
    PhotosGridAdapter adapter;
    Context context ;

    public PhotosFragment(List<Photo> photos,Context context) {
        this.photos = photos;
        this.context = context ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.photos_fragment,null,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        photosgrid = (GridView)view.findViewById(R.id.photosgrid);
        adapter = new PhotosGridAdapter(context,photos);
        photosgrid.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        super.onViewCreated(view, savedInstanceState);
    }
}
