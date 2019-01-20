package inc.collars.lensperson;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeFragment extends Fragment implements View.OnClickListener {

    EditText change;
    Button update;
    MyInterface myInterface;
    String command;

    public ChangeFragment() {
        // Required empty public constructor
    }

// a simple fragment that upon a click will show the option to either change email or change password decided by the user's click on SettingsActivity
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_change, container, false);
        //referencing elements
        change=view.findViewById(R.id.change);
        update=view.findViewById(R.id.update);
        //getting arguments from the settings activity and setting hint and set the button to on click listener
        command=getArguments().getString("change");
        change.setHint(command);
        update.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myInterface=(MyInterface)context;
    }


    public interface MyInterface{
        void buttonClicked(String text,String command);
    }


//upon click it will pass the values to the function that will be either Enter new email or Enter new password that the main activity will handle
    @Override
    public void onClick(View view) {
        String newval=change.getText().toString();
        myInterface.buttonClicked(newval,command);
    }
}
