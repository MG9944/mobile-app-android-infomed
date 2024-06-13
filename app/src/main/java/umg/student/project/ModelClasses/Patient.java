package umg.student.project.ModelClasses;

import androidx.annotation.NonNull;

public class Patient {
    private int id;
    private String pesel, name, firstname, address, phonenumber;

    // Class methods
    public Patient() {
    }

    public Patient(int id, String pesel, String name, String firstname, String address, String phonenumber) {
        this.id = id;
        this.pesel = pesel;
        this.name = name;
        this.firstname = firstname;
        this.address = address;
        this.phonenumber = phonenumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getNamePatient() {
        return name;
    }

    public void setNamePatient(String name) {
        this.name = name;
    }

    public String getFirstnamePatient() {
        return firstname;
    }

    public void setFirstnamePatient(String firstname) {
        this.firstname = firstname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public void setPhoneNumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getFullName(){return firstname+" "+name;};

    @Override
    public String toString() {
        return getFullName();
    }
}

