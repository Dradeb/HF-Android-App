package ma.dradeb.hiddenfounders.Activities;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookBroadcastReceiver;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import ma.dradeb.hiddenfounders.Modals.Album;
import ma.dradeb.hiddenfounders.R;
import ma.dradeb.hiddenfounders.Utils.Utils;

public class MainActivity extends AppCompatActivity {


    //variables init
    Button proceed ;
    LoginButton loginButton ;
    CallbackManager clManager;
    List<Album> listAlbums = new ArrayList<>();
    GridView gridView ;
    boolean logged ;
    SwitchCompat downloadAction ;
    //End variables init


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        proceed = (Button)findViewById(R.id.proceed);
        logged = AccessToken.getCurrentAccessToken()  != null?true:false;
        downloadAction = (SwitchCompat) findViewById(R.id.delete_photos);


        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    proceed.setVisibility(View.INVISIBLE);

                }else{
                    proceed.setVisibility(View.VISIBLE);

                }
            }
        };

        if(logged && Utils.isOnline(getApplicationContext()))
        {
            proceed.setVisibility(View.VISIBLE);
        }

        if(Utils.getDownloadAction(getApplicationContext()))
        {
            downloadAction.setChecked(true);

        }else{
            downloadAction.setChecked(false);

        }

        downloadAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SwitchDownloadState();
                System.out.println(""+Utils.getDownloadAction(getApplicationContext()));
            }
        });

//        //redirect directly if already Logged
//        if(logged && Utils.isOnline(getApplicationContext()))
//        {
//            Intent albumsIntent = new Intent(MainActivity.this,AlbumsActivity.class);
//            startActivity(albumsIntent);
//        }
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isOnline(getApplicationContext())){
                Intent albumsIntent = new Intent(MainActivity.this,AlbumsActivity.class);
                startActivity(albumsIntent);
                }else{
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_connection),Toast.LENGTH_LONG).show();
                }
            }
        });



        //i*n case i want to retrieve the App hash key
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "ma.dradeb.hiddenfounders",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        //end hash things

        ///init views here///
        loginButton = (LoginButton) findViewById(R.id.login);
        gridView  =(GridView) findViewById(R.id.albumsgrid);

        ///end init views///


        //configuring facebook callback manager
        clManager = CallbackManager.Factory.create();


        loginButton.registerCallback(clManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i("STATUS"," Connected ");
                //switching to albums activity
                Intent albumsIntent = new Intent(MainActivity.this,AlbumsActivity.class);
                startActivity(albumsIntent);
                if(logged)
                {
                    proceed.setVisibility(View.INVISIBLE);
                }else{
                    proceed.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancel() {
                System.out.println(" Canceled ");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println(" Error "+error.toString());

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        clManager.onActivityResult(requestCode,resultCode,data);

    }

    void SwitchDownloadState()
    {
        if(Utils.getDownloadAction(getApplicationContext()))
        {
            Utils.saveDownloadAction(false,getApplicationContext());

        }else{
            Utils.saveDownloadAction(true,getApplicationContext());
        }

    }

}
