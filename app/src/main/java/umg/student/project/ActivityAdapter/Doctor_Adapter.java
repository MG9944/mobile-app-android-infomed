package umg.student.project.ActivityAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import umg.student.project.ModelClasses.Doctor;
import umg.student.project.ModelClasses.Illness;
import umg.student.project.R;

public class Doctor_Adapter extends RecyclerView.Adapter<Doctor_Adapter.MyViewHolder> {

    private final Context context;
    private List<Doctor> doctorList;

    public Doctor_Adapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFiltredList(List<Doctor> filtredList)
    {
        this.doctorList = filtredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView listrow_dot;
        public TextView listrow_specialization;
        public TextView listrow_name;
        public TextView listrow_firstname;

        public MyViewHolder(View view) {
            super(view);
            listrow_dot = view.findViewById(R.id.idrow_doctor_id_dot);
            listrow_specialization = view.findViewById(R.id.idrow_doctor_specialization);
            listrow_name = view.findViewById(R.id.idrow_doctor_name);
            listrow_firstname = view.findViewById(R.id.idrow_doctor_firstname);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_doctor, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.listrow_dot.setText(Integer.toString(doctor.getId()));
        holder.listrow_name.setText("Lastname: " + doctor.getNameDoctor());
        holder.listrow_firstname.setText("Firstname: " + doctor.getFirstname());
        holder.listrow_specialization.setText("Specialisation: " + doctor.getSpecialisation());
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }
}

