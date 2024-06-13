package umg.student.project.Utils;



import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umg.student.project.ModelClasses.Appointment;
import umg.student.project.ModelClasses.Doctor;
import umg.student.project.ModelClasses.Illness;
import umg.student.project.ModelClasses.MedicalCenter;
import umg.student.project.ModelClasses.Specialisation;


public class RequestService {
    public static String USER_TOKEN = "";
    public static final String URL = "http://168.138.89.150:8801";
    protected static final String LOGIN = URL + "/api/login_check";
    protected static final String REGISTER_DOCTOR = URL + "/api/register/doctor";
    protected static final String FORGOT_PASSWORD_CHECK_EMAIL = URL + "/api/check/email";
    protected static final String VERFICATION_CODE = URL + "/api/check/code";
    protected static final String SET_NEW_PASSWORD = URL + "/api/new/password";
    public static final String GET_USER_INFO = URL + "/api/user/info";
    protected static final String CHANGE_DOCTOR_PASSWORD = URL + "/api/change/password";
    protected static final String EDIT_DOCTOR = URL + "/api/patient/{userId}/edit";
    public static final String GET_DOCTORS = URL + "/api/doctors";
    protected static final String CREATE_NEW_DOCTOR = URL+"/api/doctor/create";
    public static final String GET_MEDICAL_CENTER = URL + "/api/medical/center";
    public static final String GET_SPECIALISATIONS = URL + "/api/specialisation";
    public static final String GET_MEDICAMENTE = URL + "/api/medicamente";
    protected static final String CREATE_MEDICAMENTE = URL + "/api/admin/create/medicamente";
    public static final String GET_ILLNESS = URL + "/api/illness";
    protected static final String CREATE_ILLNESS = URL + "/api/admin/create/illness";
    public static final String GET_DOCTOR_APPOINTMENT = URL + "/api/doctor/appointments";
    protected static final String CREATE_APPOINTMENT = URL + "/api/create/appointment";
    public static final String GET_PATIENTS = URL + "/api/patients";
    protected static final String CREATE_PATIENT = URL + "/api/create/patient";
    public static final String GET_PATIENTS_IN_PATIENT_CARD = URL + "/api/patients/patient-card";




    public static boolean loginActivity(Context context, String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", email);
            postData.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN, postData,
                response -> {
                    try {
                        USER_TOKEN = response.getString("token");
                        Log.d("Token", USER_TOKEN);
                        Toast.makeText(context, "Login successful", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show());

        requestQueue.add(jsonObjectRequest);
        return true;
    }


    public static void registerActivity(Context context, String firstName, String lastName, String email, String password, Integer medicalCenter, Integer specialisation, String phoneNumber) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("firstName", firstName);
            postData.put("lastName", lastName);
            postData.put("email", email);
            postData.put("password", password);
            postData.put("medicalCenter", medicalCenter);
            postData.put("specialisation", specialisation);
            postData.put("phoneNumber", phoneNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_DOCTOR, postData,
                response -> Toast.makeText(context, "Register successful" + response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(context, "Register failed", Toast.LENGTH_LONG).show());

        requestQueue.add(jsonObjectRequest);
    }


    public static void checkEmailExist(Context context, String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FORGOT_PASSWORD_CHECK_EMAIL, postData,
                response -> Toast.makeText(context, "Your email is valid, sending verification code", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(context, "Your email is invalid" + error, Toast.LENGTH_LONG).show());

        requestQueue.add(jsonObjectRequest);
    }

    public static void checkCode(Context context, String email, String otp) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("otp", otp);
            postData.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, VERFICATION_CODE, postData,
                response -> Toast.makeText(context, "", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(context, "", Toast.LENGTH_LONG).show());

        requestQueue.add(jsonObjectRequest);
    }

    public static void setNewPassword(Context context, String email, String newPassword, String repeatedNewPassword) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("new_password", newPassword);
            postData.put("repeated_new_password", repeatedNewPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SET_NEW_PASSWORD, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "Your password is not changed" + error, Toast.LENGTH_LONG).show());

        requestQueue.add(jsonObjectRequest);
    }

    public static void changeDoctorPassword(Context context, String oldPassword, String newPassword, String repeatedNewPassword) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("old_password", oldPassword);
            postData.put("new_password", newPassword);
            postData.put("repeated_new_password", repeatedNewPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CHANGE_DOCTOR_PASSWORD, postData, response -> {
            try {
                Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(context, "Doctor data is not changed", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };
        requestQueue.add(request);
    }

    public static void createDoctor(Context context, String firstName, String lastName, String email, String phoneNumber, Integer specialisation) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("firstName", firstName);
            postData.put("lastName", lastName);
            postData.put("email", email);
            postData.put("specialisation", specialisation);
            postData.put("phoneNumber", phoneNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CREATE_NEW_DOCTOR, postData,
                response -> Toast.makeText(context, "Register successful" + response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(context, "Register failed", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void editDoctor(Context context, String email, Integer specialisation, ArrayList<String> roles) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("specialisation", specialisation);
            postData.put("roles", roles);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EDIT_DOCTOR, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "Doctor data is not changed", Toast.LENGTH_LONG).show());

        requestQueue.add(jsonObjectRequest);
    }

    public static void changeStatusDoctor(Context context, Integer doctorId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String EDIT_DOCTOR_STATUS = URL + "api/admin/users/"+doctorId+"/change-status";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EDIT_DOCTOR_STATUS, null,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "" + error, Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static List<Specialisation> getSpecialisations(Context context, List<Specialisation> specialisationList) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_SPECIALISATIONS, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject specialisationObject = response.getJSONObject(i);
                    Specialisation specialisation = new Specialisation();
                    specialisation.setIdSpecialisation(specialisationObject.getInt("id"));
                    specialisation.setSpecialisationName(specialisationObject.getString("name").toString());

                    specialisationList.add(specialisation);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            Log.d("Tag", "Response error" + error.getMessage());
        });
        requestQueue.add(jsonArrayRequest);
        return specialisationList;
    }

    public static void getMedicalCenter(Context context, List<MedicalCenter> medicalCentersList) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_MEDICAL_CENTER, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject medicalCenterObject = response.getJSONObject(i);
                    MedicalCenter medicalCenter = new MedicalCenter();
                    medicalCenter.setIdMedicalCenter(medicalCenterObject.getInt("id"));
                    medicalCenter.setName(medicalCenterObject.getString("name").toString());
                    medicalCenter.setAddress(medicalCenterObject.getString("address").toString());
                    medicalCenter.setNip(medicalCenterObject.getString("nip").toString());

                    medicalCentersList.add(medicalCenter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            Log.d("Tag", "Response error" + error.getMessage());
        });
        requestQueue.add(jsonArrayRequest);
    }


    public static void createMedicamente(Context context, String name, String category) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("name", name);
            postData.put("category", category);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CREATE_MEDICAMENTE, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "There was a problem when creating new medicamente" + error, Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void editMedicamente(Context context, String name, String category, Integer medicamenteId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String EDIT_MEDICAMENTE = URL + "/api/admin/medicamente/"+medicamenteId+"/edit";
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", name);
            postData.put("category", category);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, EDIT_MEDICAMENTE, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "There was a problem with editing medicamente", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static List<Illness> getIllness(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final List<Illness> illnessList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_ILLNESS, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject illnessObject = response.getJSONObject(i);
                    Illness illness = new Illness();
                    illness.setId(illnessObject.getInt("id"));
                    illness.setIllnessName(illnessObject.getString("name").toString());
                    illness.setCategory(illnessObject.getString("category").toString());
                    illness.setMedicamente(illnessObject.getString("medicamente").toString());

                    illnessList.add(illness);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            Log.d("Tag", "Response error" + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
        return illnessList;
    }

    public static void createIllness(Context context, String name, String category, Integer medicamente) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("name", name);
            postData.put("category", category);
            postData.put("medicamente", medicamente);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CREATE_ILLNESS, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "There was a problem with creating illness", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void editIllness(Context context, String name, String category, Integer medicamente, Integer illnessId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String EDIT_ILLNESS = URL + "/api/admin/illness/"+illnessId+"/edit";
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", name);
            postData.put("category", category);
            postData.put("medicamente", medicamente);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EDIT_ILLNESS, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "There was a problem with editing illness", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static List<Appointment> getAppointment(Context context) {
        List<Appointment> appointmentList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_DOCTOR_APPOINTMENT, null, (Response.Listener<JSONArray>) response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject appointmentObject = response.getJSONObject(i);
                    Appointment appointment = new Appointment();
                    appointment.setDataconsultation(appointmentObject.getString("appointmentDate").toString());
                    appointment.setNamePatient(appointmentObject.getString("patientFullName").toString());
                    appointment.setDiagnosis(appointmentObject.getString("diagnose").toString());

                    appointmentList.add(appointment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            Log.d("Tag", "Response error" + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
        return appointmentList;
    }

    public static void createAppointment(Context context, String appointmentDate, Integer patient, String diagnosis) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("patient", patient);
            postData.put("appointmentDate", appointmentDate);
            postData.put("diagnose", diagnosis);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CREATE_APPOINTMENT, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "There was a problem with editing appointment", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void editAppointment(Context context, String appointmentDate, Integer appointmentId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String EDIT_APPOINTMENT = URL + "/api/appointment/"+appointmentId+"/edit";
        JSONObject postData = new JSONObject();
        try {
            postData.put("appointmentDate", appointmentDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EDIT_APPOINTMENT, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "There was a problem with editing appointment", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void cancelAppointment(Context context, Integer appointmentId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String CANCEL_APPOINTMENT = URL + "/api/appointment/"+appointmentId+"/cancel";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CANCEL_APPOINTMENT, null,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "There was a problem with editing appointment" , Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void continueInsertDataAppointment(Context context, Float temperature, Integer bloodPressure, Integer sugarLevel, String description, Integer appointmentId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String CONTINUE_INSERT_DATA_TO_APPOINTMENT = URL + "/api/appointment/"+appointmentId+"/continue";
        JSONObject postData = new JSONObject();
        try {
            postData.put("temperature", temperature);
            postData.put("bloodPressure", bloodPressure);
            postData.put("sugarLevel", sugarLevel);
            postData.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, CONTINUE_INSERT_DATA_TO_APPOINTMENT, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "" + error, Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void createPatient(Context context, String pesel, String lastname, String firstname, String address, String phoneNumber) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("pesel", pesel);
            postData.put("firstname", firstname);
            postData.put("lastname", lastname);
            postData.put("address", address);
            postData.put("phoneNumber", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, CREATE_PATIENT, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "There was a problem with creating patient ", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void editPatient(Context context, String pesel, String lastname, String firstname, String address, String phoneNumber, Integer patientId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String EDIT_PATIENT = URL + "/api/patient/"+patientId+"/edit";
        JSONObject postData = new JSONObject();
        try {
            postData.put("pesel", pesel);
            postData.put("firstname", firstname);
            postData.put("lastname", lastname);
            postData.put("address", address);
            postData.put("phoneNUmber", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, EDIT_PATIENT, postData,
                response -> {
                    try {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "There was a problem with editing patient", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void generatePatientCard(Context context, Integer patientId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String GENERATE_PATIENT_CARD = URL + "/api/patients/patient-card/"+patientId+"/generate";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GENERATE_PATIENT_CARD, null,
                response -> {
                    Toast.makeText(context, "Patient card generate successfully", Toast.LENGTH_LONG).show();
                },
                error -> Toast.makeText(context, "There was a problem with generate patient card", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public static void generatePatientPrescription(Context context, Integer appointmentId) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String GENERATE_PATIENT_PRESCRIPTION = URL + "/api/appointment/generate/"+appointmentId+"/prescription";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GENERATE_PATIENT_PRESCRIPTION, null,
                response -> {
                    Toast.makeText(context, "Patient prescription generate successfully", Toast.LENGTH_LONG).show();
                },
                error -> Toast.makeText(context, "There was a problem with generate patient prescription", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + USER_TOKEN);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

}