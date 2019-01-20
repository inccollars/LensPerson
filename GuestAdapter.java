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

//adapter similar to MyAdapter with only the difference that the item clicks on GuestActivity are handled by this adapter
public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.MyViewholder> {
    ArrayList<PhotographersDetails> list;
    WeakReference<Context> mContextWeakReference;
    public GuestAdapter(ArrayList<PhotographersDetails>list,Context context){
        this.list=list;
        this.mContextWeakReference= new WeakReference<Context>(context);
    }

    @NonNull
    @Override
    public GuestAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = mContextWeakReference.get();
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.singleitem,parent,false);
        return new GuestAdapter.MyViewholder(view,context);
    }


    @Override
    public void onBindViewHolder(@NonNull GuestAdapter.MyViewholder holder, int position) {
        Context context = mContextWeakReference.get();

        if (context == null) {
            return;
        }
        PhotographersDetails obj=list.get(position);
        holder.name.setText(obj.getName());
        Picasso.get().load(obj.getLogoUrl()).fit().centerInside().into(holder.image);
        //Imageview here holder.lname.setText(obj.getLastname());
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
                    ((GuestActivity)context).userItemClick(getAdapterPosition());
                }
            });
        }
    }

    public void filterList(ArrayList<PhotographersDetails> filteredList){
        list=filteredList;
        notifyDataSetChanged();
    }
}
