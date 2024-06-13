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

import umg.student.project.ModelClasses.Appointment;
import umg.student.project.R;

public class

Appointment_Adapter extends RecyclerView.Adapter<Appointment_Adapter.MyViewHolder> {
    private final Context context;
    private final List<Appointment> consultationList;

    public Appointment_Adapter(Context context, List<Appointment> consultationList) {
        this.context = context;
        this.consultationList = consultationList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView listrow_dataconsult;
        public TextView listrow_diagnostic;

        // TextView to display attributes of other classes
        public TextView listrow_display_patient;
        public TextView listrow_display_doctor;


        public MyViewHolder(View view) {
            super(view);

            listrow_dataconsult = view.findViewById(R.id.idrow_consultation_dataconsultation);
            listrow_diagnostic = view.findViewById(R.id.idrow_consultation_diagnostic);

            listrow_display_patient = view.findViewById(R.id.idrow_consultation_display_patient);
            listrow_display_doctor = view.findViewById(R.id.idrow_consultation_display_doctor);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_appointment, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Appointment consultation = consultationList.get(position);

        // Display data (id, consultation date, diagnosis, medication dose)
        // entered for each consultation in the Consultation table (consultationList)

        holder.listrow_dataconsult.setText(consultation.getDataconsultation());
        holder.listrow_diagnostic.setText("Diagnostic " + consultation.getDiagnosis());
        holder.listrow_display_patient.setText("Patient: " + consultation.getNamePatient());
        holder.listrow_display_doctor.setText("Doctor: Dr. " + consultation.getNameDoctor());
    }

    @Override
    public int getItemCount() {
        return consultationList.size();
    }
}

