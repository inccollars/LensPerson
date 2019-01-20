package inc.collars.lensperson;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {

    TextView num2,loc2,web2,pack2;

    public BlankFragment() {
        // Required empty public constructor
    }

//a simple blank fragment for detailactivity that upon a click will show the details of a user given by detail activity.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflating the layout
        View view=inflater.inflate(R.layout.detailsfragment, container, false);
//referencing elements
        num2=view.findViewById(R.id.num2);
        loc2=view.findViewById(R.id.loc2);
        web2=view.findViewById(R.id.web2);
        pack2=view.findViewById(R.id.pack2);
//setting the texts
        num2.setText(getArguments().getString("phone"));
        loc2.setText(getArguments().getString("location"));
        web2.setText(getArguments().getString("weblinks"));
        pack2.setText(getArguments().getString("packages"));
        return view;
    }

}
