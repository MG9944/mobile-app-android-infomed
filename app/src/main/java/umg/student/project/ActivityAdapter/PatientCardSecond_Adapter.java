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

public class PatientCardSecond_Adapter extends RecyclerView.Adapter<PatientCardSecond_Adapter.MyViewHolder> {
    private final Context context;
    private final List<Appointment> consultationList;

    public PatientCardSecond_Adapter(Context context, List<Appointment> consultationList) {
        this.context = context;
        this.consultationList = consultationList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView listrow_dataconsult;
        public TextView listrow_diagnostic;
        public TextView listrow_temperature;
        public TextView listrow_bloodpressure;
        public TextView listrow_sugarlevel;
        public TextView listrow_description;



        public MyViewHolder(View view) {
            super(view);
            listrow_dataconsult = view.findViewById(R.id.idrow_patient_card_extended_dataconsultation);
            listrow_diagnostic = view.findViewById(R.id.idrow_patient_card_extended_diagnostic);
            listrow_temperature = view.findViewById(R.id.idrow_patient_card_extended_temperature);
            listrow_bloodpressure = view.findViewById(R.id.idrow_patient_card_extended_bloodpresure);
            listrow_sugarlevel = view.findViewById(R.id.idrow_patient_card_extended_sugarlevel);
            listrow_description = view.findViewById(R.id.idrow_patient_card_extended_description);

        }
    }

    @NonNull
    @Override
    public PatientCardSecond_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_patient_card_extended, parent, false);
        return new PatientCardSecond_Adapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PatientCardSecond_Adapter.MyViewHolder holder, int position) {
        Appointment consultation = consultationList.get(position);
        holder.listrow_dataconsult.setText(consultation.getDataconsultation());
        holder.listrow_diagnostic.setText("Diagnostic: " + consultation.getDiagnosis());
        holder.listrow_temperature.setText("Temperature: " + consultation.getTemperature());
        holder.listrow_bloodpressure.setText("Blood Pressure: " + consultation.getBloodPressure());
        holder.listrow_sugarlevel.setText("Sugar Level: " + consultation.getSugarLevel());
        holder.listrow_description.setText("Description: " + consultation.getDescription());
    }

    @Override
    public int getItemCount() {
        return consultationList.size();
    }

}
