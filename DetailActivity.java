package inc.collars.lensperson;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    //defining objects
    ImageView logo,sample1,sample2,sample3;
    TextView title;
    Button backbutton,detailsbutton;
    String name,phone,location,weblinks,packages,logourl,sample1url,sample2url,sample3url;
    FullScreenDialogFragment fullScreenDialogFragment;
    FrameLayout framelayout;
    //for facebook share button
    ShareButton shareButton;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    ShareLinkContent linkContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //referencing the elements
        logo=findViewById(R.id.logo);
        sample1=findViewById(R.id.sample1);
        sample2=findViewById(R.id.sample2);
        sample3=findViewById(R.id.sample3);
        title=findViewById(R.id.title);
        framelayout=findViewById(R.id.framelayout);
        backbutton=findViewById(R.id.backbutton);
        detailsbutton=findViewById(R.id.detailsbutton);
        shareButton=findViewById(R.id.sharebutton);

        //for facebook link share content
        linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                .build();
//setting all the buttons on click listeners
        shareButton.setShareContent(linkContent);
        backbutton.setOnClickListener(this);
        detailsbutton.setOnClickListener(this);
//getting all the values from the main activity upon clik and setting them on the respective fields
        Intent intent=getIntent();
        title.setText(intent.getStringExtra("title"));
        phone=intent.getStringExtra("phone");
        location=intent.getStringExtra("location");
        weblinks=intent.getStringExtra("weblinks");
        packages=intent.getStringExtra("packages");
        logourl=intent.getStringExtra("logourl");
        sample1url=intent.getStringExtra("sample1url");
        sample2url=intent.getStringExtra("sample2url");
        sample3url=intent.getStringExtra("sample3url");
        Picasso.get().load(logourl).into(logo);
        Picasso.get().load(sample1url).into(sample1);
        Picasso.get().load(sample2url).into(sample2);
        Picasso.get().load(sample3url).into(sample3);
//defining the fragment to open fullscreen dialog upon longclicks
        fullScreenDialogFragment = new FullScreenDialogFragment();
//putting all the images on longclicks
        logo.setOnLongClickListener(this);
        sample1.setOnLongClickListener(this);
        sample2.setOnLongClickListener(this);
        sample3.setOnLongClickListener(this);
        shareButton.setOnClickListener(this);
//for facebook: call manager and share dialog.
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    //making the option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.fordetails,menu);
        return true;
    }
//opening the respective common intents on clicks on option items such as call , sms , map
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.sms){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:"+phone));
            intent.putExtra("sms_body", "Hi! I looked your portfolio on LensPerson.");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else if(item.getItemId()==R.id.call){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        else if(item.getItemId()==R.id.map){
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(location));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.backbutton){
            onBackPressed();
        }
        else if(view.getId()==R.id.detailsbutton){
            //clicking detail button will open the simle blank fragment and the values are passed to set there
            Bundle bundle = new Bundle();
            bundle.putString("phone",phone);
            bundle.putString("location",location);
            bundle.putString("weblinks",weblinks);
            bundle.putString("packages",packages);
            BlankFragment blankFragment= (BlankFragment) new BlankFragment();
            blankFragment.setArguments(bundle);
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, blankFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
//opening the full screen images with zoom functionality upon long clicks on images
    @Override
    public boolean onLongClick(View view) {
        if(view.getId()==R.id.logo){
            Bundle b = new Bundle();
            b.putString("url",logourl);
            fullScreenDialogFragment.setArguments(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fullScreenDialogFragment.show(ft,FullScreenDialogFragment.TAG);
            return true;
        }
        else if (view.getId()==R.id.sample1){
            Bundle b = new Bundle();
            b.putString("url",sample1url);
            fullScreenDialogFragment.setArguments(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fullScreenDialogFragment.show(ft,FullScreenDialogFragment.TAG);
            return true;
        }
        else if(view.getId()==R.id.sample2){
            Bundle b = new Bundle();
            b.putString("url",sample2url);
            fullScreenDialogFragment.setArguments(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fullScreenDialogFragment.show(ft,FullScreenDialogFragment.TAG);
            return true;
        }
        else if(view.getId()==R.id.sample3){
            Bundle b = new Bundle();
            b.putString("url",sample3url);
            fullScreenDialogFragment.setArguments(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fullScreenDialogFragment.show(ft,FullScreenDialogFragment.TAG);
            return true;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.commit();
        fullScreenDialogFragment.dismiss();
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
//callback manager for facebook share button
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
