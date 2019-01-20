package inc.collars.lensperson;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import inc.collars.lensperson.AccountActivity.LoginActivity;
import inc.collars.lensperson.Classes.PhotographersDetails;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener,ChangeFragment.MyInterface {
//defining objects
    DatabaseReference databaseReference;
    ImageView img;
    TextView title;
    Button changepassword,changeemail,deleteaccount,logout;
    FirebaseAuth auth;
    String newval;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//referncing them
        img=findViewById(R.id.img);
        title=findViewById(R.id.title);
        changepassword=findViewById(R.id.changepassword);
        changeemail=findViewById(R.id.changeemail);
        deleteaccount=findViewById(R.id.deleteaccount);
        logout=findViewById(R.id.logout);
        frameLayout=findViewById(R.id.framelayout);
//taking the value from main activity
        Intent intent=getIntent();
        String userid=intent.getStringExtra("userid");
//setting firebase authentication instance
        auth=FirebaseAuth.getInstance();
        //setting firebase database
        databaseReference=FirebaseDatabase.getInstance().getReference("PhotographersDetails").child(userid);
        //take value of title and logo url from the uid from main activity and set it on the respective fields
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PhotographersDetails obj=dataSnapshot.getValue(PhotographersDetails.class);
                if(obj!=null) {
                    Picasso.get().load(obj.getLogoUrl()).into(img);
                    title.setText(obj.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//setting the buttons on clicks
        changepassword.setOnClickListener(this);
        changeemail.setOnClickListener(this);
        deleteaccount.setOnClickListener(this);
        logout.setOnClickListener(this);
    }
//create the option menu for settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forsettings,menu);
        return true;
    }
//will hanndle the clicks on the option menu i.e only feedback to the developers
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //email to the developers
        if(item.getItemId()==R.id.feedback){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, "inccollars@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK:LensPerson");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        return true;
    }
// function that will perform either update email or update password depending upon the valu passed by the ChangeFragmnet
    public void buttonClicked(String text,String command) {
        newval=text;
        if(command=="Enter New Email"){
            //update email of the respective user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updateEmail(newval).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        Toast.makeText(getApplicationContext(),"Email Updated!",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(),"Failed to update Email!",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(command=="Enter New Password"){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            //update password of the respective user
            user.updatePassword(newval).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        Toast.makeText(getApplicationContext(), "Password is updated!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Failed to updated password!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.changeemail){
            //will open a fragment for change email and pass the value to change fragment
            Bundle b = new Bundle();
            String change="Enter New Email";
            b.putString("change",change);
            ChangeFragment changeFragment=new ChangeFragment();
            changeFragment.setArguments(b);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.framelayout,changeFragment);
            fragmentTransaction.commit();
            /**/
        }
        else if(view.getId()==R.id.changepassword){
            //will open a fragment for change password and pass the value to change fragment
            Bundle b = new Bundle();
            String change="Enter New Password";
            b.putString("change",change);
            ChangeFragment changeFragment=new ChangeFragment();
            changeFragment.setArguments(b);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.framelayout,changeFragment);
            fragmentTransaction.commit();
            /**/
        }
        else if(view.getId()==R.id.deleteaccount){
            //give an alert before deleting account
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Confirm to Delete");
            alertDialogBuilder.setMessage("Are you sure to Delete Account? This can't be undo.");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //if yes , get the current user from firebase authentication and delete it from there aswell as remove that object from fireabse database
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            databaseReference.removeValue();
                                            Toast.makeText(getApplicationContext(), "Your profile is deleted!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
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
        else if(view.getId()==R.id.logout){
            //create alert for logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("LOGOUT");
            alertDialogBuilder.setMessage("Are you sure to Logout?");
            alertDialogBuilder.setCancelable(false);
            //if yes get the current user and signout and take the user to the login activity
            alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    auth.signOut();
                    startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
