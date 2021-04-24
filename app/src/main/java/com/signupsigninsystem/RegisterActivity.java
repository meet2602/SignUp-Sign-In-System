package com.signupsigninsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.signupsigninsystem.Adapter.CountriesAdapter;
import com.signupsigninsystem.Model.CountryModel;
import com.signupsigninsystem.Utils.ListOfCountry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {

    private EditText edUserName, edEmailId, edPhoneNum, edPassword, edConPassword;
    private TextView txtDiaCode;
    private RadioGroup radioGender;
    private LinearLayout countryLL;
    private Button signUpBtn;
    private String genderStr = null;
    public static String codeStr = "IN";
    public static String countryName = "India";
    public static String dialCode = "+91";
    private ImageView countryImg;
    private Dialog countryDialog;
    private RecyclerView countryRV;
    private TextInputLayout txtPasswordL, txtConPasswordL;
    private boolean isValidPassword = false;
    private boolean isValidConPassword = false;
    private ProgressBar progressBar;
    private ArrayList<CountryModel> countryModelModelModelList;
    private CountriesAdapter countriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUserName = findViewById(R.id.edUserName);
        edEmailId = findViewById(R.id.edEmailId);
        edPhoneNum = findViewById(R.id.edMobileNumber);
        edPassword = findViewById(R.id.edPassword);
        edConPassword = findViewById(R.id.edConPassword);
        radioGender = findViewById(R.id.radioGroup);
        countryLL = findViewById(R.id.countryLayout);
        txtDiaCode = findViewById(R.id.txtDialCode);
        countryImg = findViewById(R.id.countryImg);
        txtPasswordL = findViewById(R.id.txtPasswordL);
        txtConPasswordL = findViewById(R.id.txtConPasswordL);
        signUpBtn = findViewById(R.id.signUpBtn);
        progressBar = findViewById(R.id.progress_bar);

        radioGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioMale) {
                genderStr = "Male";
            } else if (checkedId == R.id.radioFemale) {
                genderStr = "Female";
            }
        });
        countryLL.setOnClickListener(v -> {
            countryDialog = new Dialog(this);
            countryDialog.setContentView(R.layout.country_layout);
            countryDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            countryDialog.setCancelable(true);
            countryRV = countryDialog.findViewById(R.id.recycler_view);

            ImageView closeImg = countryDialog.findViewById(R.id.close_img);
            EditText edSearch = countryDialog.findViewById(R.id.ed_search);
            edSearch.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            countryModelModelModelList = new ArrayList<>(Arrays.asList(ListOfCountry.COUNTRIES));

            countriesAdapter = new CountriesAdapter(RegisterActivity.this, countryModelModelModelList, countryDialog, countryImg, txtDiaCode);
            countryRV.setAdapter(countriesAdapter);

            edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    filter(editable.toString());
                }
            });
            closeImg.setOnClickListener(v1 -> countryDialog.dismiss());
            countryDialog.show();

        });
        signUpBtn.setOnClickListener(v -> validation());

        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditTextPassword(edPassword, txtPasswordL, "password");
            }
        });
        edConPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEditTextPassword(edConPassword, txtConPasswordL, "conPassword");
            }
        });

    }

    private void filter(String text) {
        ArrayList<CountryModel> filteredList = new ArrayList<>();
        for (CountryModel item : countryModelModelModelList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        countriesAdapter.filterList(filteredList);
        countriesAdapter.notifyDataSetChanged();
    }

    private void validation() {
        if (edUserName.getText().toString().trim().isEmpty()) {
            edUserName.setError("Required");
        } else if (edEmailId.getText().toString().trim().isEmpty()) {
            edEmailId.setError("Required");
        } else if (!validEmail(edEmailId.getText().toString().trim())) {
            edEmailId.setError("Enter valid e-mail!");
        } else if (edPhoneNum.getText().toString().trim().isEmpty()) {
            edPhoneNum.setError("Required");
        } else if (!validPhoneNo(edPhoneNum.getText().toString().trim())) {
            edPhoneNum.setError("Enter valid phone no.!");
        } else if (genderStr == null) {
            Toast.makeText(this, "Please enter gender!", Toast.LENGTH_SHORT).show();
        } else {
            validateEditTextPassword(edPassword, txtPasswordL, "password");
            if (isValidPassword) {
                validateEditTextPassword(edConPassword, txtConPasswordL, "conPassword");
                if (isValidConPassword) {
                    if (!edPassword.getText().toString().trim().equals(edConPassword.getText().toString().trim())) {
                        txtConPasswordL.setError("Password don't match!");
                    } else {
                        txtConPasswordL.setError(null);
                        Register();
                    }
                }
            }
        }
    }

    private void Register() {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, Object> registerHM = new HashMap<>();
        String emailId = edEmailId.getText().toString();
        String password = edPassword.getText().toString();
        registerHM.put("username", edUserName.getText().toString());
        registerHM.put("email_id", emailId);
        registerHM.put("gender", genderStr);
        registerHM.put("country_code", codeStr);
        registerHM.put("country_name", countryName);
        registerHM.put("phone_no", dialCode + edPhoneNum.getText().toString());
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        registerHM.put("id", id);
                        FirebaseFirestore.getInstance().collection("Users").document(id).set(registerHM).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(mainIntent);
                                finish();
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task1.getException()).toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void validateEditTextPassword(EditText editText, TextInputLayout textInputLayout, String state) {
        boolean truthState = false;
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError("Required");
        } else if (editText.getText().toString().length() < 8) {
            textInputLayout.setError("Password must be 8 character!");
        } else {
            textInputLayout.setError(null);
            truthState = true;
        }
        if (truthState) {
            if (state.equals("password")) {
                isValidPassword = true;
            } else if (state.equals("conPassword")) {
                isValidConPassword = true;
            }
        } else {
            if (state.equals("password")) {
                isValidPassword = false;
            } else if (state.equals("conPassword")) {
                isValidConPassword = false;
            }
        }
    }

    private boolean validEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validPhoneNo(String phoneNo) {
        String regex = "[7-9][0-9]{9}";
        return phoneNo.matches(regex);
    }
}