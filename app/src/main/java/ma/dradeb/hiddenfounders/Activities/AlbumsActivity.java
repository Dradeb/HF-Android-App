package ma.dradeb.hiddenfounders.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ma.dradeb.hiddenfounders.Adapters.AlbumsGridAdapter;
import ma.dradeb.hiddenfounders.Manifest;
import ma.dradeb.hiddenfounders.Modals.Album;
import ma.dradeb.hiddenfounders.R;
import ma.dradeb.hiddenfounders.Utils.Utils;

public class AlbumsActivity extends AppCompatActivity {

    JSONArray albums;
    ProgressDialog progress;
    List<Album> albumsList = new ArrayList<>();
    AlbumsGridAdapter adapter ;
    GridView albumsGrid ;
    Toolbar toolbar ;
    Utils utils ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_albums,null);
        setContentView(v);
        utils = new Utils();
        //init views
        albumsGrid = (GridView)findViewById(R.id.albumsgrid);

        //grid click event
        albumsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                int result = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
                int resultII = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if(result ==  PackageManager.PERMISSION_GRANTED&& resultII == PackageManager.PERMISSION_GRANTED)
                {
                    Intent photosIntent = new Intent(AlbumsActivity.this,PhotosActivity.class);
                    photosIntent.putExtra("id",albumsList.get(i).getId());
                    photosIntent.putExtra("count",albumsList.get(i).getCount());
                    if(Integer.parseInt(albumsList.get(i).getCount())==0) {
                        Toast.makeText(getApplicationContext(), "This Album is Empty", Toast.LENGTH_LONG).show();
                    }else {
                        startActivity(photosIntent);
                    }
                }else{
                    requestPermissions();

                }

            }
        });





        //Setting up permissions
        LoginManager.getInstance().logInWithReadPermissions(AlbumsActivity.this, Arrays.asList("user_photos"));
        // A request to get Albums Data
        GraphRequest requestAlbums = new GraphRequest(AccessToken.getCurrentAccessToken(),"/me/albums?fields=count,id,created_time,name,picture",null, HttpMethod.GET, new GraphRequest.Callback(){
            @Override
            public void onCompleted(GraphResponse response) {

                try {
                    //getting only albums data
                    albums = response.getJSONObject().getJSONArray("data");
                    //Let's Load albums Covers
                    for(int i = 0 ; i <albums.length();i++) {
                        final Album album = new Album();

                        final String id = albums.getJSONObject(i).getString("id");
                        final String name = albums.getJSONObject(i).getString("name");
                        final String date = utils.fbDateToSimpleFormat(albums.getJSONObject(i).getString("created_time"));
                        final String count = albums.getJSONObject(i).getString("count");
                        final String imgUrl = albums.getJSONObject(i).getJSONObject("picture").getJSONObject("data").getString("url");

                        album.setId(id);
                        album.setName(name);
                        album.setCreationDate(date);
                        album.setCount(count);
                        album.setCoverUrl(imgUrl);



                        albumsList.add(album);
                    }
                    System.out.println("Size"+albumsList.size());
                    adapter = new AlbumsGridAdapter(getApplicationContext(),albumsList);
                    albumsGrid.setAdapter(adapter);
                    adapter.notifyDataSetChanged();





                } catch (JSONException e) {
                    Toast.makeText(AlbumsActivity.this,"Error Loading Data",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                    Toast.makeText(AlbumsActivity.this,getString(R.string.error_connection),Toast.LENGTH_LONG).show();
                    finish();

                }

            }
        });
        requestAlbums.executeAsync();


    }

    void requestPermissions()
    {
        ActivityCompat.requestPermissions(AlbumsActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},9);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                }else{
                    Toast.makeText(getApplicationContext()," You should allow the app to access your external storage",Toast.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
