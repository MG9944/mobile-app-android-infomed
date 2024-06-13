package umg.student.project.ModelClasses;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Medicamente {
    private int id;
    private String name;
    private String category;

    // Class methods
    public Medicamente() {
    }

    public Medicamente(int id,String name,  String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {return category;}

    public void setCategory(String category) {
        this.category = category;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
