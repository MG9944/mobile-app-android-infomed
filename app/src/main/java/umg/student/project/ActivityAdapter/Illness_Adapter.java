package umg.student.project.ActivityAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import umg.student.project.ModelClasses.Illness;
import umg.student.project.ModelClasses.Medicamente;
import umg.student.project.R;

public class Illness_Adapter extends RecyclerView.Adapter<Illness_Adapter.MyViewHolder> {
    private List<Illness> IllnessList;

    public Illness_Adapter(Context context, List<Illness> IllnessList) {
        this.IllnessList = IllnessList;
    }

    public void filterList(ArrayList<Illness> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        IllnessList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView listrow_id;
        public TextView listrow_name;
        public TextView listrow_category;
        public TextView listrow_medicamente;

        public MyViewHolder(View view) {
            super(view);
            listrow_id = view.findViewById(R.id.idrow_illness_id_dot);
            listrow_name = view.findViewById(R.id.idrow_illness_name);
            listrow_category = view.findViewById(R.id.idrow_illness_category);
            listrow_medicamente = view.findViewById(R.id.idrow_illness_medicamente);
        }
    }

    @NonNull
    @Override
    public Illness_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_illness, parent, false);
        return new Illness_Adapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Illness_Adapter.MyViewHolder holder, int position) {
        Illness illness = IllnessList.get(position);

        holder.listrow_id.setText(Integer.toString(illness.getId()));
        holder.listrow_name.setText(illness.getIllnessName());
        holder.listrow_category.setText("Category: " + illness.getCategory());
        holder.listrow_medicamente.setText("Medicamente: " + illness.getMedicamente());
    }

    @Override
    public int getItemCount() {
        return IllnessList.size();
    }
}
