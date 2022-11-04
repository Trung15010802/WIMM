package com.example.wimm.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wimm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SpendingMoneyFragment extends Fragment {
    private Spinner spinnerDanhsach;
    private EditText eddate;
    EditText moneyEditText;
    private Button imageButton;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static final String TAG = SpendingMoneyFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spending_money, container, false);
        spinnerDanhsach = view.findViewById(R.id.Sipnner_chieutieu);
        eddate = view.findViewById(R.id.edit_Date);
        imageButton = view.findViewById(R.id.saveImgButton);
        moneyEditText = view.findViewById(R.id.edit_inpMoney);

        eddate.setText(simpleDateFormat.format(new Date()));

        eddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgay();
            }
        });
        ArrayList<String> arrayChiTieu = new ArrayList<String>();
        arrayChiTieu.add("Ăn uống");
        arrayChiTieu.add("Mua sắm");
        arrayChiTieu.add("Đi lại");
        arrayChiTieu.add("Sức khoẻ");
        arrayChiTieu.add("Giải trí");
        arrayChiTieu.add("Khác");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrayChiTieu);
        spinnerDanhsach.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = firebaseUser.getEmail();


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(moneyEditText.getText())) {
                    moneyEditText.requestFocus();
                    moneyEditText.setError("Vui lòng nhập số tiền đã chi!");
                } else {
                    Query query = databaseReference.child("users").orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        String user;

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    user = data.getKey();

                                }
                            }

                            Query query1 = databaseReference.child("users").child(user).orderByChild("date").equalTo(eddate.getText().toString());
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                String listName;

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot data : snapshot.getChildren()) {
                                            listName = data.getKey();
                                            Map<String, Object> objectMap = new HashMap<>();

                                            switch (spinnerDanhsach.getSelectedItemPosition()) {
                                                case 0:
                                                    objectMap.put("eat", Double.parseDouble(moneyEditText.getText().toString()));
                                                    break;
                                                case 1:
                                                    objectMap.put("shopping", Double.parseDouble(moneyEditText.getText().toString()));
                                                    break;
                                                case 2:
                                                    objectMap.put("move", Double.parseDouble(moneyEditText.getText().toString()));
                                                    break;
                                                case 3:
                                                    objectMap.put("health", Double.parseDouble(moneyEditText.getText().toString()));
                                                    break;
                                                case 4:
                                                    objectMap.put("entertainment", Double.parseDouble(moneyEditText.getText().toString()));
                                                    break;
                                                case 5:
                                                    objectMap.put("other", Double.parseDouble(moneyEditText.getText().toString()));
                                                    break;
                                            }
                                            databaseReference.child("users").child(user).child(listName).child("list").updateChildren(objectMap);

                                        }
                                    } else {
                                        UserList list = new UserList(Double.parseDouble(moneyEditText.getText().toString()), spinnerDanhsach.getSelectedItemPosition());
                                        DataUser dataUser = new DataUser(eddate.getText().toString(), list);
                                        databaseReference.child("users").child(user).push().setValue(dataUser);
                                        Log.e(TAG, list.toString());
                                        Log.e(TAG, dataUser.toString());
                                    }
                                    Toast.makeText(getActivity(), "Thêm thành công!",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


        return view;
    }

    private void ChonNgay() {
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                //i:năm - i1:thang - i2: ngày
                calendar.set(i, i1, i2);
                eddate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
}
