package umg.student.project.ModelClasses;

import androidx.annotation.NonNull;

public class Specialisation {
    private int idSpecialisation;
    private String name;
    // Class Methods
    public Specialisation() {
    }

    public Specialisation(int idSpecialisation, String name) {
        this.idSpecialisation = idSpecialisation;
        this.name = name;
    }


    public int getIdSpecialisation() {
        return idSpecialisation;
    }

    public void setIdSpecialisation(int idSpecialisation) {
        this.idSpecialisation = idSpecialisation;
    }

    public String getSpecialisationName() {
        return name;
    }

    public void setSpecialisationName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return getSpecialisationName();
    }
}
