package ma.dradeb.hiddenfounders.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import ma.dradeb.hiddenfounders.Adapters.PhotosGridAdapter;
import ma.dradeb.hiddenfounders.CustomViews.RobotoTextViewRegular;
import ma.dradeb.hiddenfounders.Fragments.PhotosFragment;
import ma.dradeb.hiddenfounders.Modals.Photo;
import ma.dradeb.hiddenfounders.R;
import ma.dradeb.hiddenfounders.Utils.Utils;

public class PhotosActivity extends AppCompatActivity {

//    ViewPager photosvp ;

    ProgressBar progressBar ;
    GridView photosGrid ;
    ProgressDialog progressDialog;
    ImageButton doneButton;
    boolean downloadState ;


    static final int progress_type = 0 ;

    String albumId ;
    String albumPhotosCount ;

    List<Photo> photosList = new ArrayList<>();
    List<Photo> allPhotosList = new ArrayList<>();

    List<Fragment> fragments = new ArrayList<>();
    PhotosGridAdapter adapter;
    Button prev , next ;
    RobotoTextViewRegular counter ;
    String prevCursor , nextCursor ;
    int imagePerPage = 21 ;
    //control pages to add
    boolean addPageToList = true ;
    int lastPage = 0 ;
    int currentPage = 0 ;
    int selectedCounter = 0 ;

    //image for test
    String urlTest ="https://scontent.xx.fbcdn.net/v/t31.0-0/q83/p600x600/19453139_1392603997461353_7769550551506732427_o.jpg?oh=be857fa78e33276fd69c669fb9b72c3d&oe=59CDD4CA";

    //
    int checkedFiles = 0 ;
    int checkedCounter = 0  ;

    //Url list
    List<Photo> lstUrl = new ArrayList<>() ;

    //downloaded files
    int downloaded = 0 ;


    //firebase storage
    private StorageReference mStorageRef;

    int countViewed = imagePerPage;
    int photosNumber = 0 ;

    //firebase authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        //Authentication
        mAuth = FirebaseAuth.getInstance();
        downloadState = Utils.getDownloadAction(getApplicationContext());

        //view init
        photosGrid = (GridView)findViewById(R.id.photos_container);
        prev = (Button)findViewById(R.id.prevbutton);
        next = (Button)findViewById(R.id.nextbutton);
        counter = (RobotoTextViewRegular)findViewById(R.id.photo_counter);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        doneButton = (ImageButton)findViewById(R.id.done);

        //init firebase storage
        mStorageRef = FirebaseStorage.getInstance().getReference();


        //Sign in to firebase
        signIn("youness.dradeb@gmail.com","Dradeb96");
//        signInAnymously();
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearDownloadRepo();
                ArrayList<String> lst = new ArrayList<String>();
                for(int i = 0 ; i<allPhotosList.size();i++)
                {
                    if(allPhotosList.get(i).isChecked())
                    {
                        checkedFiles += 1 ;
                        lstUrl.add(allPhotosList.get(i));

                    }

                }
                new DownloadTask().execute();




            }
        });

        //
        photosGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(allPhotosList.get(i+imagePerPage*currentPage).isChecked())
                {
                    allPhotosList.get(i+imagePerPage*currentPage).setChecked(false);
                    view.findViewById(R.id.checkit).setVisibility(View.INVISIBLE);
                    selectedCounter--;
                    if(selectedCounter==0)
                    {
                        doneButton.setVisibility(View.INVISIBLE);
                    }
                }else{
                    allPhotosList.get(i+imagePerPage*currentPage).setChecked(true);
                    view.findViewById(R.id.checkit).setVisibility(View.VISIBLE);
                    selectedCounter++;

                    doneButton.setVisibility(View.VISIBLE);
                }
            }
        });

       //getting the album id
        if(getIntent().hasExtra("id"))
        {
            albumId = getIntent().getStringExtra("id");
        }
        if(getIntent().hasExtra("count"))
        {
            albumPhotosCount = getIntent().getStringExtra("count");

            photosNumber = Integer.parseInt(albumPhotosCount);

            if(imagePerPage>photosNumber) {
                imagePerPage = photosNumber;
                countViewed = photosNumber;
            }
            if(Integer.parseInt(albumPhotosCount)<=imagePerPage){
                counter.setText(albumPhotosCount+"/"+albumPhotosCount);
                next.setVisibility(View.INVISIBLE);
                prev.setVisibility(View.INVISIBLE);
            }
            else
                counter.setText(countViewed+"/"+albumPhotosCount);

        }

        loadImages("/photos?fields=images{source}&limit="+imagePerPage);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Loading
                progressBar.setVisibility(View.VISIBLE);
                prev.setVisibility(View.INVISIBLE);
                next.setVisibility(View.INVISIBLE);



                // max page and current one
                currentPage += 1 ;
                if(currentPage >lastPage && !addPageToList)
                {
                    lastPage += 1 ;
                    addPageToList = true;
                }else{

                    addPageToList = false;
                }

                //imagePerPage means imagePerPage image per page
                if(countViewed<photosNumber) {
                    String query = "/photos?fields=images{source}&limit="+imagePerPage+"+&after=" + prevCursor;
                    countViewed += imagePerPage ;
                    if(countViewed>=photosNumber)
                    {
                        countViewed = photosNumber ;
                        next.setVisibility(View.INVISIBLE);

                    }

                    if(countViewed>imagePerPage)
                        prev.setVisibility(View.VISIBLE);

                    counter.setText(countViewed + "/" + albumPhotosCount);

                    loadImages(query);


                }
            }
        });



        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Loading
                progressBar.setVisibility(View.VISIBLE);
                prev.setVisibility(View.INVISIBLE);
                next.setVisibility(View.INVISIBLE);

                addPageToList = false ;
                currentPage -=1 ;

                //retrieve the next 21 photo
                String query = "/photos?fields=images{source}&limit="+imagePerPage+"&before=" + nextCursor;

                    if(countViewed == photosNumber){
                        countViewed -= photosNumber%imagePerPage ;
                        next.setVisibility(View.VISIBLE);
                    }else{
                        countViewed -=imagePerPage;
                    }

                if(countViewed<=imagePerPage){
                    countViewed = imagePerPage ;
                    prev.setVisibility(View.INVISIBLE);
                }

                counter.setText(countViewed + "/" + albumPhotosCount);
                loadImages(query);


            }
        });


            //----------------------------------------  in case i needed a view pager [dumb solution]--------------------------------------------------//
//        //init views here
//        photosvp = (ViewPager)findViewById(R.id.imagespager);
//
//
//
//        //getting the album id
//        if(getIntent().hasExtra("id"))
//        {
//            albumId = getIntent().getStringExtra("id");
//        }
//
//        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/"+albumId+"/photos?fields=images{source}", null, HttpMethod.GET, new GraphRequest.Callback() {
//            @Override
//            public void onCompleted(GraphResponse response) {
//
//                try {
//                    JSONObject obj = response.getJSONObject();
//                    JSONArray photos = obj.getJSONArray("data");
//                    System.out.println(" DATA SIZE  "+photos.length());
//
//                    for(int i=0 ; i<photos.length();i++)
//                    {
//                        final Photo p = new Photo();
//                        JSONArray images = photos.getJSONObject(i).getJSONArray("images");
//
//                        p.setId(photos.getJSONObject(i).getString("id"));
////                        p.setThumb(images.getJSONObject(4).getString("source"));
//                        photosList.add(p);
//
//                    }
//                    PhotosFragment ph = new PhotosFragment(photosList,getApplicationContext());
//                    fragments.add(ph);
//
//
//                    //setting created fragments
//                    PhotosFragmentsAdapter adapter = new PhotosFragmentsAdapter(getSupportFragmentManager(),fragments);
//                    photosvp.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//        request.executeAsync();
//

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case progress_type:
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Downloading image");
                progressDialog.setIndeterminate(false);
                progressDialog.setMax(100);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                return progressDialog;

        default:
            return null;
        }

    }

    void loadImages(String query)
    {

        photosList.clear();
        //so that we delete old data
        adapter = new PhotosGridAdapter(getApplicationContext(),new ArrayList<Photo>());
        photosGrid.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        //let's not keep calling the ws each time
        if(countViewed<=allPhotosList.size())
        {
            if(countViewed>imagePerPage)
                prev.setVisibility(View.VISIBLE);
            if (countViewed<photosNumber)
                next.setVisibility(View.VISIBLE);

            if(countViewed == photosNumber)
            {
                adapter = new PhotosGridAdapter(getApplicationContext(),allPhotosList.subList(countViewed-photosNumber%imagePerPage,countViewed));

            }else {
                adapter = new PhotosGridAdapter(getApplicationContext(), allPhotosList.subList(countViewed - imagePerPage, countViewed));
            }

            photosGrid.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else {

            //let's fill the images list
            //only 20 image
            GraphRequest photosRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + albumId + "" + query, null, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    //Loading
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject obj = response.getJSONObject();

                    try {

                        String before = obj.getJSONObject("paging").getJSONObject("cursors").has("before") ? obj.getJSONObject("paging").getJSONObject("cursors").getString("before") : "";
                        String after = obj.getJSONObject("paging").getJSONObject("cursors").has("after") ? obj.getJSONObject("paging").getJSONObject("cursors").getString("after") : "";

                        if (!before.equalsIgnoreCase("")) {
                            nextCursor = before;
                        }
                        if (!before.equalsIgnoreCase("")) {
                            prevCursor = after;
                        }


                        //first data
                        JSONArray data = obj.getJSONArray("data");
                        //then images
                        for (int i = 0; i < data.length(); i++) {
                            Photo p = new Photo();
                            p.setId(data.getJSONObject(i).getString("id"));

                            JSONArray images = data.getJSONObject(i).getJSONArray("images");

                            //first image as original
                            p.setSource(images.getJSONObject(0).getString("source"));
                            //last image as a thumb
                            p.setThumb(images.getJSONObject(images.length() - 1).getString("source"));

                            photosList.add(p);

                            Log.i("INSERTING  "," NUMBER "+i);
                        }

                        //see if the page photos should be added to the list
                        if (addPageToList) {
                            allPhotosList.addAll(photosList);
                            addPageToList = false;
                            Log.i("Page  ","Adding Page ");

                        }
                        //managin next and prev buttons
                        if (currentPage > 0)
                            prev.setVisibility(View.VISIBLE);
                        if (countViewed < photosNumber)
                            next.setVisibility(View.VISIBLE);
                        if(countViewed == photosNumber) {
                            if(photosNumber<=imagePerPage)
                                    adapter = new PhotosGridAdapter(getApplicationContext(), allPhotosList.subList(0,photosNumber));
                                else
                                    adapter = new PhotosGridAdapter(getApplicationContext(), allPhotosList.subList(countViewed - photosNumber % imagePerPage, countViewed));

                            Log.i("Photo  ","Equals ");
                        }else {
                            if(imagePerPage > photosNumber) {
                                imagePerPage = photosNumber;
                                countViewed = photosNumber ;
                            }

                            adapter = new PhotosGridAdapter(getApplicationContext(), allPhotosList.subList(countViewed - imagePerPage, countViewed));

                        }
                        photosGrid.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                    } catch (JSONException e ) {
                        e.printStackTrace();
                    }catch (NullPointerException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(PhotosActivity.this," Check your Connection and Try again",Toast.LENGTH_LONG).show();
                        finish();
                    }

                }
            });
            photosRequest.executeAsync();
        }
    }

    class DownloadTask extends AsyncTask<String,String,String>{

        String name ,mUrl;

        public DownloadTask(){}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_type);
            name = lstUrl.get(downloaded).getId();
            mUrl = lstUrl.get(downloaded).getSource();
            downloaded +=  1 ;
            progressDialog.setMessage("Downloading "+downloaded+"/"+lstUrl.size());

        }

        @Override
        protected String doInBackground(String... strings) {

            int count ;
            try {

                File downloadLocation = new File(Environment.getExternalStorageDirectory()+"/exportapp");

                if(!downloadLocation.exists())
                    downloadLocation.mkdir();


                URL url = new URL(mUrl);
                URLConnection connection = url.openConnection();
                connection.connect();

                //photo size
                int fileLenght = connection.getContentLength();

                //read file
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);

                //write file
                OutputStream outputStream = new FileOutputStream(downloadLocation+"/"+name+".jpg");
                byte data[] = new byte[1024];
                long total = 0 ;

                while ((count = inputStream.read(data)) != -1)
                {
                    total += count ;
                    publishProgress(""+(int)((total*100)/fileLenght));

                    //write data
                    outputStream.write(data,0,count);

                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();





            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            checkedCounter += 1  ;
            if(downloaded<lstUrl.size())
            {
                new DownloadTask().execute();

            }else{
                progressDialog.setProgress(0);
                dismissDialog(progress_type);
            }
            if(checkedCounter == checkedFiles )
            {

                //uncheeck aaall the photos and update the UI
                for(Photo p:allPhotosList)
                {
                    p.setChecked(false);
                }
                adapter.notifyDataSetChanged();


                String path = Environment.getExternalStorageDirectory().toString()+"/exportapp";
                File dir = new File(path);

                File[] files = dir.listFiles();
//                for(File f:files){
                //cuz i need to know the last one
                for(int i = 0 ; i<files.length;i++)
                  {
                    File f = files[i];
                    boolean last = i==files.length-1?true:false;
                    uploadFile(String.valueOf(f),f.getName(),last);

                }


            }
                super.onPostExecute(s);
        }
    }

    void clearDownloadRepo()
    {
        String path = Environment.getExternalStorageDirectory().toString()+"/exportapp";
        File dir = new File(path);

        File[] files = dir.listFiles();
//                for(File f:files){
        //cuz i need to know the last one
        for(int i = 0 ; i<files.length;i++)
        {
            File f = files[i];
            f.delete();
        }

    }
    //return size of directory
    // so i can get a realtime progress
    long getFilesSize()
    {

        long totalSize = 0 ;
        String path = Environment.getExternalStorageDirectory().toString()+"/exportapp";
        File dir = new File(path);

        File[] files = dir.listFiles();
        for(File f:files){

            totalSize += f.length();

        }
        return totalSize ;
    }

    void uploadFile(String path, String fileName, final boolean last)
    {
        final boolean[] added = {false};
        final long filesLenght = getFilesSize();
        final int[] progress = {0};
        progressDialog.setMessage("Uploading Files To server");
        showDialog(progress_type);
        final File f = new File(path);
        final Uri file = Uri.fromFile(f);
        final StorageReference riversRef = mStorageRef.child("images/"+fileName);

        final UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int x = (int)(((taskSnapshot.getBytesTransferred() * 10) / f.length())*10);
                System.out.println(" X : "+x);
                if(x>0 && x<100)
                {
                    try{
                    progressDialog.setProgress(progressDialog.getProgress()+((x)/checkedFiles));
                    added[0] = true;}
                    catch (ArithmeticException e) {

                    }
                }


                }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        uploadTask.cancel();
                        progressDialog.dismiss();
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(!added[0]) {
                        try{
                    progressDialog.setProgress(progressDialog.getProgress() + ((100) / checkedFiles));
                    }
                    catch (ArithmeticException e) {

                    }
                 }
                if(last) {
                    //re initialize daata
                    progressDialog.dismiss();
                    if(downloadState)
                    {
                        f.delete();
                    }
                    initData();

                }
            }
        });
    }

    void initData()
    {
        checkedFiles = 0 ;
        checkedCounter = 0;
        lstUrl.clear();
        downloaded = 0 ;
        progressDialog.setProgress(0);
    }

    void signIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Success", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if(!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                System.out.println(""+e.getMessage());
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                System.out.println(""+e.getMessage());
                            } catch(FirebaseAuthUserCollisionException e) {
                                System.out.println(""+e.getMessage());
                            } catch(Exception e) {
                                Log.e("Failure ", e.getMessage());
                            }
                        }
                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errorMessage = e.getMessage();
                System.out.println(" Error message : "+errorMessage);
                // test the errorCode and errorMessage, and handle accordingly

            }
        });
    }

    void verifyHowMuchChecked()
    {

    }

    void signInAnymously()
    {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Failure", "signInAnonymously:failure", task.getException());


                        }

                        // ...
                    }
                });
    }

}