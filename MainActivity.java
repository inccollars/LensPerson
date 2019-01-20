package inc.collars.lensperson;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import inc.collars.lensperson.Classes.PhotographersDetails;


public class MainActivity extends AppCompatActivity {
//defining objects
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    ArrayList<PhotographersDetails> photographersDetailsArrayList;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    EditText search_bar;
    int viewcount = 0;
    int s_type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//referncing objects and taking the value from shared preference to set the layout (linear or grid)
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        s_type=pref.getInt("layout_type",1);
        //referencing elements
        search_bar = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recyclerview);
        if(s_type==1)
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        else if(s_type==0)
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        photographersDetailsArrayList = new ArrayList<PhotographersDetails>();

        //referencing Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("PhotographersDetails");
        //referencing Firebase Authentication
        auth=FirebaseAuth.getInstance();


        //setting recyclerview
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //get all the childs from the database
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
    }
//function to take all the values from database and setting them on recyclerview
    public void getAllData(DataSnapshot dataSnapshot) {
        PhotographersDetails obj = dataSnapshot.getValue(PhotographersDetails.class);
        photographersDetailsArrayList.add(obj);
        myAdapter = new MyAdapter(photographersDetailsArrayList, MainActivity.this);
        recyclerView.setAdapter(myAdapter);
    }
//function to handle on clicks sends the data to DetailActivity and opens DetailActivity
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
//create the options menu for members
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formembers, menu);
        return true;
    }
//handes on click to different functions and activities such as showportfolio,searchbar,changeview,settings
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //this will open EditActivity
                if(item.getItemId()==R.id.showportfolio) {
                    Intent intent2 = new Intent(this, EditActivity.class);
                    intent2.putExtra("userid",auth.getCurrentUser().getUid());
                    startActivity(intent2);
                }
                //this will make search bar visible and perform search inside the recycler views adapter
                else if (item.getItemId()==R.id.search) {
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
                            filter(editable.toString());
                        }
                    });
                }
                else if(item.getItemId()==R.id.changeview) {
                    //this will change the view and will store it in shared preference to set the layout the next time app opens
                    if (viewcount == 0) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        myAdapter = new MyAdapter(photographersDetailsArrayList, this);
                        recyclerView.setAdapter(myAdapter);
                        editor.putInt("layout_type",viewcount);
                        editor.commit();
                        viewcount = 1;

                    } else if (viewcount == 1) {
                        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                        myAdapter = new MyAdapter(photographersDetailsArrayList, this);
                        recyclerView.setAdapter(myAdapter);
                        editor.putInt("layout_type",viewcount);
                        editor.commit();
                        viewcount = 0;
                    }
                }
                //opens settings activity
                else if(item.getItemId()== R.id.settings){
                    Intent intent3 = new Intent(this,SettingsActivity.class);
                    intent3.putExtra("userid",auth.getCurrentUser().getUid());
                    startActivity(intent3);
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
