package com.example.wimm.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wimm.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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

public class ChartFragment extends Fragment {
    private BarChart barChart;
    private PieChart pieChart;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String user;
    private Button buttonChart;
    private EditText editTextChart;
    private TextView totalTextView;
    private static final String TAG = ChartFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        buttonChart = view.findViewById(R.id.buttonChart);
        editTextChart = view.findViewById(R.id.editTextChart);
        totalTextView = view.findViewById(R.id.totalTextView);

        editTextChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonNgay();
            }
        });

//        Get data form realtime database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = firebaseUser.getEmail();

        buttonChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = databaseReference.child("users").orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                user = data.getKey();
                            }
                            Query query1 = databaseReference.child("users").child(user).orderByChild("date").equalTo(editTextChart.getText().toString());
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                String listName = "";

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot data : snapshot.getChildren()) {
                                            listName = data.getKey();
                                        }
                                        databaseReference.child("users").child(user).child(listName).child("list").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                UserList userList = snapshot.getValue(UserList.class);
                                                loadPieData(userList.getEat(), userList.getShopping(), userList.getMove(), userList.getHealth(), userList.getEntertainment(), userList.getOther());
                                                setUpPieChart();

                                                loadBarData(userList.getEat(), userList.getShopping(), userList.getMove(), userList.getHealth(), userList.getEntertainment(), userList.getOther());
                                                setUpBarData();
                                                Double totalMN = userList.getEat() + userList.getShopping() + userList.getMove() + userList.getHealth() + userList.getEntertainment() + userList.getOther();
                                                totalTextView.setText("Tổng số liền đã tiêu hôm nay: " + totalMN + " $");

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }

                                        });
                                    } else
                                        Toast.makeText(getActivity(), "Không có dữ liệu cho ngày này!", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });


        return view;
    }

    private void setUpBarData() {
        // Set animation
        barChart.animateY(3000);
        //Set description text and color
        barChart.getDescription().setText("Thống kê chi tiêu 7 ngày");
        barChart.getDescription().setTextSize(12);
        barChart.getDescription().setTextColor(Color.BLUE);
        barChart.getDescription().setPosition(900, 1090);
    }

    private void loadBarData(double eatMN, double shoppingMN, double moveMN, double healthMN, double entertainmentMN, double otherMN) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, (float) eatMN));
        barEntries.add(new BarEntry(2, (float) shoppingMN));
        barEntries.add(new BarEntry(3, (float) moveMN));
        barEntries.add(new BarEntry(4, (float) healthMN));
        barEntries.add(new BarEntry(5, (float) entertainmentMN));
        barEntries.add(new BarEntry(6, (float) otherMN));

        List<String> listType = new ArrayList();
        listType.add(null);
        listType.add("Ăn uống");
        listType.add("Mua sắm");
        listType.add("Đi lại");
        listType.add("Sức khoẻ");
        listType.add("Giải trí");
        listType.add("Khác");

        //Initialize bar data set
        BarDataSet barDataSet = new BarDataSet(barEntries, "Số tiền chi tiêu");

        //Set colors
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //Hide draw value
        barDataSet.setDrawValues(true);
        //Set bar data
        barDataSet.setValueTextSize(12);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(listType));

        barChart.setData(new BarData(barDataSet));

    }

    private void loadPieData(double eatMN, double shoppingMN, double moveMN, double healthMN, double entertainmentMN, double otherMN) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        if (eatMN > 0)
            pieEntries.add(new PieEntry((float) eatMN, "Ăn uống"));
        if (shoppingMN > 0)
            pieEntries.add(new PieEntry((float) shoppingMN, "Mua sắm"));
        if (moveMN > 0)
            pieEntries.add(new PieEntry((float) moveMN, "Đi lại"));
        if (healthMN > 0)
            pieEntries.add(new PieEntry((float) healthMN, "Sức Khoẻ"));
        if (entertainmentMN > 0)
            pieEntries.add(new PieEntry((float) entertainmentMN, "Giải Trí"));
        if (otherMN > 0)
            pieEntries.add(new PieEntry((float) otherMN, "Khác"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS)
            colors.add(color);
        for (int color : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(color);
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Loại chi tiêu");
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(12);
        pieData.setValueTextColor(Color.BLACK);
        pieChart.setData(pieData);
    }

    private void setUpPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Các khoản chi tiêu");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateXY(3000, 3000);


        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setDrawInside(false);
        legend.setEnabled(true);

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
                editTextChart.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
}


