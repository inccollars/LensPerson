package inc.collars.lensperson;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import inc.collars.lensperson.Classes.PhotographersDetails;

public class CreatePortfolio extends AppCompatActivity implements View.OnClickListener {

    //creating elements
    ImageView logoImg,sample1,sample2,sample3;
    EditText name,num,location,weblinks,packages;
    Button upLogo,upPics,save;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseAuth auth;
    ProgressBar progress_bar;
    Uri LogoUri,sample1Uri,sample2Uri,sample3Uri;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_portfolio);

        //referencing elements
        auth = FirebaseAuth.getInstance();
        logoImg = findViewById(R.id.logoImg);
        sample1 = findViewById(R.id.sample1);
        sample2 = findViewById(R.id.sample2);
        sample3 = findViewById(R.id.sample3);
        progress_bar = findViewById(R.id.progress_bar);
        name = findViewById(R.id.name);
        num = findViewById(R.id.num);
        location = findViewById(R.id.location);
        weblinks = findViewById(R.id.weblinks);
        packages = findViewById(R.id.packages);
        upLogo = findViewById(R.id.upLogo);
        save = findViewById(R.id.save);
//setting on click listeners
        upLogo.setOnClickListener(this);
        save.setOnClickListener(this);
        sample1.setOnClickListener(this);
        sample2.setOnClickListener(this);
        sample3.setOnClickListener(this);

        //taking database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("PhotographersDetails");
        //taking storage reference
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");
    }
    //function to select the logo image and have request code1
    public void openLogoChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    //function to select the sample1 image and have request code2
    public void openSample1Chooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,2);
    }

    //function to select the sample2 image and have request code3
    public void openSample2Chooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,3);
    }

    //function to select the sample3 image and have request code4
    public void openSample3Chooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,4);
    }

    //on the data recieved from image chooser functions, setting the image with picasso on imageviews.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            LogoUri=data.getData();
            Picasso.get().load(LogoUri).into(logoImg);
        }
        else if(requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            sample1Uri=data.getData();
            Picasso.get().load(sample1Uri).into(sample1);
        }
        else if(requestCode==3 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            sample2Uri=data.getData();
            Picasso.get().load(sample2Uri).into(sample2);
        }
        else if(requestCode==4 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            sample3Uri=data.getData();
            Picasso.get().load(sample3Uri).into(sample3);
        }
        else{
            Toast.makeText(getApplicationContext(),"Error Loading Images",Toast.LENGTH_SHORT).show();
        }
    }

    //function to get the image's extension
    public String getImageExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    //function that will uploasd the logo url inside objects collection in firebase database, and upload the image inside firebase storage with user's uid.
    public void uploadLogo(){
        if(logoImg!=null){
            //referencing the firebase storage with user's uid.
            final StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid()).child(("Logo")+"."+getImageExtension(LogoUri));
            //uploading the file on firebase storage
            fileReference.putFile(LogoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //geting the download url from firebase storage and putting it inside user's uid in firebase database
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("logourl", "onSuccess: uri= "+ uri.toString());
                            databaseReference.child(auth.getCurrentUser().getUid()).child("logoUrl").setValue(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
    }
    //function that will uploasd the sample1 url inside objects collection in firebase database, and upload the image inside firebase storage with user's uid.
    public void uploadSample1(){
        if(sample1!=null){
            //referencing the firebase storage with user's uid.
            final StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid()).child(("Sample1")+"."+getImageExtension(LogoUri));
            //uploading the file on firebase storage
            fileReference.putFile(sample1Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //geting the download url from firebase storage and putting it inside user's uid in firebase database
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("samplel", "onSuccess: uri= "+ uri.toString());
                            databaseReference.child(auth.getCurrentUser().getUid()).child("sample1Url").setValue(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
    }
    //function that will uploasd the sample2 url inside objects collection in firebase database, and upload the image inside firebase storage with user's uid.
    public void uploadSample2(){
        if(sample2!=null){
            //referencing the firebase storage with user's uid.
            final StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid()).child(("Sample2")+"."+getImageExtension(LogoUri));
            //uploading the file on firebase storage
            fileReference.putFile(sample2Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //geting the download url from firebase storage and putting it inside user's uid in firebase database
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("sample2", "onSuccess: uri= "+ uri.toString());
                            databaseReference.child(auth.getCurrentUser().getUid()).child("sample2Url").setValue(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
    }
    //function that will uploasd the sample3 url inside objects collection in firebase database, and upload the image inside firebase storage with user's uid.
    public void uploadSample3(){
        if(sample3!=null){
            //referencing the firebase storage with user's uid.
            final StorageReference fileReference = storageReference.child(auth.getCurrentUser().getUid()).child(("sample3Url")+"."+getImageExtension(LogoUri));
            //uploading the file on firebase storage
            fileReference.putFile(sample3Uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //geting the download url from firebase storage and putting it inside user's uid in firebase database
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("sample3", "onSuccess: uri= "+ uri.toString());
                            databaseReference.child(auth.getCurrentUser().getUid()).child("sample3Url").setValue(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
    }
//clicks to execute the uploading functions
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.upLogo){
            openLogoChooser();
        }
        else if(view.getId()==R.id.sample1){
            openSample1Chooser();
        }
        else if(view.getId()==R.id.sample2){
            openSample2Chooser();
        }
        else if(view.getId()==R.id.sample3){
            openSample3Chooser();
        }
        else if (view.getId()==R.id.save){
            //taking all the inputs from the fields
            String iname=name.getText().toString();
            String inum=num.getText().toString();
            String iloc=location.getText().toString();
            //checking if no field is empty
            if(iname.length()!=0 && inum.length()!=0 && iloc.length()!=0 && logoImg!=null && sample1!=null &&sample2!=null && sample3!=null) {
                uploadLogo();
                uploadSample1();
                uploadSample2();
                uploadSample3();
                //making the object and posting that object inside the user's uid.
                PhotographersDetails obj = new PhotographersDetails(name.getText().toString(), num.getText().toString(), location.getText().toString()
                        , weblinks.getText().toString(), packages.getText().toString());
                userId = auth.getCurrentUser().getUid();
                databaseReference.child(userId).setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Portfolio Created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreatePortfolio.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Creating Portfolio", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(),"Please fill in the required fields to continue!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
