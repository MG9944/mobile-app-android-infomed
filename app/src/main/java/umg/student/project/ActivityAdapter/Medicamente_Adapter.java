package umg.student.project.ActivityAdapter;
import android.annotation.SuppressLint;
import android.content.Context;

import android.text.Html;
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


public class Medicamente_Adapter extends RecyclerView.Adapter<Medicamente_Adapter.MyViewHolder> {
    private final Context context;
    private List<Medicamente> medicamenteList;

    public Medicamente_Adapter(Context context, List<Medicamente> medicamenteList) {
        this.context = context;
        this.medicamenteList = medicamenteList;
    }

    public void filterList(ArrayList<Medicamente> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        medicamenteList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView listrow_dot;
        public TextView listrow_name;
        public TextView listrow_category;

        public MyViewHolder(View view) {
            super(view);
            listrow_dot = view.findViewById(R.id.idrow_medicament_id_dot);
            listrow_name = view.findViewById(R.id.idrow_medicament_name);
            listrow_category = view.findViewById(R.id.idrow_medicament_category);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_medicamente, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Medicamente medicament = medicamenteList.get(position);

        // Displaying literally a dot from HTML character code
        holder.listrow_dot.setText(Integer.toString(medicament.getId()));
        holder.listrow_name.setText(medicament.getName());
        holder.listrow_category.setText(medicament.getCategory());
    }

    @Override
    public int getItemCount() {
        return medicamenteList.size();
    }
}
