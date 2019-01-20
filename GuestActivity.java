package inc.collars.lensperson;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import inc.collars.lensperson.AccountActivity.LoginActivity;
import inc.collars.lensperson.AccountActivity.SignupActivity;
import inc.collars.lensperson.Classes.PhotographersDetails;


public class GuestActivity extends AppCompatActivity {
//defining object
    DatabaseReference databaseReference;
    ArrayList<PhotographersDetails> photographersDetailsArrayList;
    RecyclerView recyclerview;
    GuestAdapter myAdapter, myAdapter2;
    EditText search_bar;
    int viewcount = 0;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        //referencing elements
        search_bar = findViewById(R.id.search_bar);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        photographersDetailsArrayList = new ArrayList<PhotographersDetails>();

        //referencing Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("PhotographersDetails");


        //setting recyclerview by getting all the data from the database reference
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getAllData(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getAllData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//fetting the firebase authentication instance
        auth=FirebaseAuth.getInstance();
    }
//function to get all the data from the database and setting the recyclerview
    public void getAllData(DataSnapshot dataSnapshot) {
        PhotographersDetails obj = dataSnapshot.getValue(PhotographersDetails.class);
        photographersDetailsArrayList.add(obj);
        myAdapter = new GuestAdapter(photographersDetailsArrayList, this);
        recyclerview.setAdapter(myAdapter);
    }
//on item click send all the data to DetailActivity and open DetailActivity
    public void userItemClick(int pos) {
        PhotographersDetails obj = photographersDetailsArrayList.get(pos);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("title", obj.getName());
        intent.putExtra("phone", obj.getNum());
        intent.putExtra("location", obj.getLocation());
        intent.putExtra("weblinks", obj.getWeblinks());
        intent.putExtra("packages", obj.getPackages());
        intent.putExtra("logourl", obj.getLogoUrl());
        intent.putExtra("sample1url", obj.getSample1Url());
        intent.putExtra("sample2url", obj.getSample2Url());
        intent.putExtra("sample3url", obj.getSample3Url());
        startActivity(intent);
    }
//create options menu for guests
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.forguests, menu);
            return true;
    }
//opening activities and search bar and changeview
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            if(item.getItemId()==R.id.signup) {
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
            }
            else if(item.getItemId()==R.id.search) {
                //setting the searchbar visible
                search_bar.setVisibility(View.VISIBLE); //add query to search
                search_bar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        //calling tje function to search inside the recyclerview adapter
                        filter(editable.toString());
                    }
                });
            }
            else if(item.getItemId()==R.id.changeview) {
                //click to change the layout from linear to grid and vice versa
                if (viewcount == 0) {
                    recyclerview.setLayoutManager(new LinearLayoutManager(this));
                    myAdapter2 = new GuestAdapter(photographersDetailsArrayList, this);
                    recyclerview.setAdapter(myAdapter2);
                    viewcount = 1;
                } else if (viewcount == 1) {
                    recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
                    myAdapter = new GuestAdapter(photographersDetailsArrayList, this);
                    recyclerview.setAdapter(myAdapter);
                    viewcount = 0;
                }
            }
            return true;
    }
//function to search
    private void filter(String text){
        ArrayList<PhotographersDetails> filteredList= new ArrayList<>();
        for(PhotographersDetails item: photographersDetailsArrayList){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        myAdapter.filterList(filteredList);
    }
}
