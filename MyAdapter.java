package inc.collars.lensperson;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import inc.collars.lensperson.Classes.PhotographersDetails;
//simple adapter for recyclerview
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewholder> {
    //takes in a list and context
    ArrayList<PhotographersDetails>list;
    WeakReference<Context> mContextWeakReference;
    public MyAdapter(ArrayList<PhotographersDetails>list,Context context){
        this.list=list;
        this.mContextWeakReference= new WeakReference<Context>(context);
    }

    @NonNull
    @Override
    public MyAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout and return the view
        Context context = mContextWeakReference.get();
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.singleitem,parent,false);
        return new MyViewholder(view,context);
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewholder holder, int position) {
        Context context = mContextWeakReference.get();

        if (context == null) {
            return;
        }
        //setting the title and image for every object in the recycler view
        PhotographersDetails obj=list.get(position);
        holder.name.setText(obj.getName());
        Picasso.get().load(obj.getLogoUrl()).fit().centerInside().into(holder.image);
    }

    public void removeItem(int position){
        list.remove(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearData(){
        list.clear();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        //defining objects and referencing them and setting onclick listener
        TextView name;
        ImageView image;
        LinearLayout parent;
        public MyViewholder(View itemView,final Context context){
            super(itemView);
            name=itemView.findViewById(R.id.name);
            image=itemView.findViewById(R.id.image);
            parent = itemView.findViewById(R.id.parent);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity)context).userItemClick(getAdapterPosition());
                }
            });
        }
    }
//for search function
    public void filterList(ArrayList<PhotographersDetails> filteredList){
        list=filteredList;
        notifyDataSetChanged();
    }
}

