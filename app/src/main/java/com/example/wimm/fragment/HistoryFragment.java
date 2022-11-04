package com.example.wimm.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.List;


public class HistoryFragment extends Fragment {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private UserList userList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText editText;
    private Button buttonHistory;
    private String user;
    private static final String TAG = HistoryFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        editText = view.findViewById(R.id.editDateHistory);
        buttonHistory = view.findViewById(R.id.buttonHistory);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgay();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = firebaseUser.getEmail();

        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = databaseReference.child("users").orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                user = data.getKey();
                            }
                            Query query1 = databaseReference.child("users").child(user).orderByChild("date").equalTo(editText.getText().toString());
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String listName = "";
                                    if (snapshot.exists()) {
                                        for (DataSnapshot data : snapshot.getChildren()) {
                                            listName = data.getKey();
                                        }
                                        databaseReference.child("users").child(user).child(listName).child("list").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                userList = snapshot.getValue(UserList.class);
                                                List<ItemHistorySpending> image_details = getListData(userList.getEat(), userList.getShopping(), userList.getMove(), userList.getHealth(), userList.getEntertainment(), userList.getOther());
                                                final ListView listView = view.findViewById(R.id.listView);
                                                listView.setAdapter(new ListHistoryAdapter(getActivity(), image_details));

                                                // When the user clicks on the ListItem
                                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                    @Override
                                                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                                        Object o = listView.getItemAtPosition(position);
                                                        ItemHistorySpending item = (ItemHistorySpending) o;
                                                        Toast.makeText(getActivity(), "Selected :" + " " + item, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }

                                        });
                                    } else {
                                        ListView listView = view.findViewById(R.id.listView);
                                        listView.setAdapter(null);
                                        Toast.makeText(getActivity(), "Không có dữ liệu cho ngày này!", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    private List<ItemHistorySpending> getListData(double eatMoney, double shoppingMoney, double moveMoney, double healthMoney, double entertainmentMoney, double otherMoney) {
        List<ItemHistorySpending> list = new ArrayList<ItemHistorySpending>();
        ItemHistorySpending eat = new ItemHistorySpending("eat", "Ăn uống", eatMoney);
        ItemHistorySpending shopping = new ItemHistorySpending("shopping", "Mua sắm", shoppingMoney);
        ItemHistorySpending move = new ItemHistorySpending("move", "Đi lại", moveMoney);
        ItemHistorySpending health = new ItemHistorySpending("health", "Sức khoẻ", healthMoney);
        ItemHistorySpending entertainment = new ItemHistorySpending("entertainment", "Giải trí", entertainmentMoney);
        ItemHistorySpending other = new ItemHistorySpending("other", "Khác", otherMoney);

        list.add(eat);
        list.add(shopping);
        list.add(move);
        list.add(health);
        list.add(entertainment);
        list.add(other);

        return list;

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
                editText.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }

}
