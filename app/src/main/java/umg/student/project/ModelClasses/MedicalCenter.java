package umg.student.project.ModelClasses;

import androidx.annotation.NonNull;

public class MedicalCenter {

    int idMedicalCenter;
    private String name, address, nip;
    // Class Methods
    public MedicalCenter() {
    }


    public MedicalCenter(int idMedicalCenter, String name, String address, String nip) {
        this.idMedicalCenter = idMedicalCenter;
        this.name = name;
        this.address = address;
        this.nip = nip;
    }

    public int getIdMedicalCenter() {
        return idMedicalCenter;
    }

    public void setIdMedicalCenter(int idMedicalCenter) {
        this.idMedicalCenter = idMedicalCenter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
