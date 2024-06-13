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

import umg.student.project.ModelClasses.Medicamente;
import umg.student.project.ModelClasses.Patient;
import umg.student.project.R;

public class Patient_Adapter extends RecyclerView.Adapter<Patient_Adapter.MyViewHolder> {
    private final Context context;
    private  List<Patient> patientList;

    public Patient_Adapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    public void filterList(ArrayList<Patient> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        patientList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView listrow_dot;
        public TextView listrow_pesel;
        public TextView listrow_name;
        public TextView listrow_firstname;
        public TextView listrow_address;
        public TextView listrow_phoneNumber;

        public MyViewHolder(View view) {
            super(view);
            listrow_dot = view.findViewById(R.id.idrow_patient_id_dot);
            listrow_pesel = view.findViewById(R.id.idrow_patient_pesel);
            listrow_name = view.findViewById(R.id.idrow_patient_firstname);
            listrow_firstname = view.findViewById(R.id.idrow_patient_lastname);
            listrow_address = view.findViewById(R.id.idrow_patient_address);
            listrow_phoneNumber = view.findViewById(R.id.idrow_patient_phonenumber);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_patient, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Patient patient = patientList.get(position);

        holder.listrow_dot.setText(Integer.toString(patient.getId()));
        holder.listrow_pesel.setText("Pesel: " + patient.getPesel());
        holder.listrow_name.setText("Lastname: " + patient.getNamePatient());
        holder.listrow_firstname.setText("Firstname: " +patient.getFirstnamePatient());
        holder.listrow_address.setText("Address: " + patient.getAddress());
        holder.listrow_phoneNumber.setText("Phone Number: " + patient.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }
}

