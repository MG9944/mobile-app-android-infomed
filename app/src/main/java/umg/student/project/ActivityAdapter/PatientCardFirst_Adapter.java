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

import umg.student.project.ModelClasses.Patient;
import umg.student.project.R;

public class PatientCardFirst_Adapter extends RecyclerView.Adapter<PatientCardFirst_Adapter.MyViewHolder> {
    private final Context context;
    private List<Patient> patientList;

    public PatientCardFirst_Adapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    public void filterList(ArrayList<Patient> filterlist) {
        patientList = filterlist;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView listrow_pesel;
        public TextView listrow_name;
        public TextView listrow_firstname;
        public TextView listrow_address;


        public MyViewHolder(View view) {
            super(view);
            listrow_pesel = view.findViewById(R.id.idrow_patient_card_pesel);
            listrow_name = view.findViewById(R.id.idrow_patient_card_lastname);
            listrow_firstname = view.findViewById(R.id.idrow_patient_card_firstname);
        }
    }

    @NonNull
    @Override
    public PatientCardFirst_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_patient_card, parent, false);
        return new PatientCardFirst_Adapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PatientCardFirst_Adapter.MyViewHolder holder, int position) {
        Patient patient = patientList.get(position);
        holder.listrow_pesel.setText("Pesel: " + patient.getPesel());
        holder.listrow_name.setText("Lastname: " + patient.getNamePatient());
        holder.listrow_firstname.setText("Firstname: " +patient.getFirstnamePatient());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }
}
