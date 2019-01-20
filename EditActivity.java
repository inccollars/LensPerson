package inc.collars.lensperson;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import inc.collars.lensperson.Classes.PhotographersDetails;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    //defining elements
    ImageView logoImg,sample1,sample2,sample3;
    TextView name,num,location,weblinks,packages;
    Button back,create;
    DatabaseReference tempReference;
    ProgressBar progress_bar;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
//referencing elements
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
        back = findViewById(R.id.back);
        create=findViewById(R.id.create);
//setting button on click listeners
        back.setOnClickListener(this);
        create.setOnClickListener(this);
//getting intent from the main activity
        Intent intent=getIntent();
        String userId=intent.getStringExtra("userid");
        //taking database reference
        tempReference = FirebaseDatabase.getInstance().getReference("PhotographersDetails").child(userId);
//getting all the values of that user from the database
        tempReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PhotographersDetails obj = dataSnapshot.getValue(PhotographersDetails.class);
                if (obj != null) {
                   //setting these values in their respective fields
                    name.setText(obj.getName());
                    num.setText(obj.getNum());
                    location.setText(obj.getLocation());
                    weblinks.setText(obj.getWeblinks());
                    packages.setText(obj.getPackages());
                    Picasso.get().load(obj.getLogoUrl()).into(logoImg);
                    Picasso.get().load(obj.getSample1Url()).into(sample1);
                    Picasso.get().load(obj.getSample2Url()).into(sample2);
                    Picasso.get().load(obj.getSample3Url()).into(sample3);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onClick(View view) {
     if (view.getId()==R.id.back){
         finish();
     }
     else if(view.getId()==R.id.create){
         //showing the alert to create new portfolio
         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
         alertDialogBuilder.setTitle("Confirm to Create");
         alertDialogBuilder.setMessage("Are you sure to create a new Portfolio? Your previous Portfolio shall be deleted.");
         alertDialogBuilder.setCancelable(false);
         alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 //if yes selected remove the object of that user from the database and taking to createportfolio activity again
                 tempReference.removeValue();
                 Intent intent=new Intent(EditActivity.this,CreatePortfolio.class);
                 startActivity(intent);
                 finish();
             }
         }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.cancel();
             }
         });
         AlertDialog alertDialog = alertDialogBuilder.create();
         alertDialog.show();
     }
    }
}
