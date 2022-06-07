package skripsi.com;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import skripsi.com.Fragmant_Keruh.Model_minggu;

import static android.content.Context.MODE_PRIVATE;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/**
 * A simple {@link Fragment} subclass.
 */
public class Homee extends Fragment  {
    ImageView photo;
    SwitchCompat switchCompatlevel, switchCompatkeruh;
    TextView username, text_level, text_keruh, text_daily, text_weekly, text_mountly, text_data, textView;
    CircularProgressBar circularProgressBar, circularProgressBar_keruh;
    LinearLayout level,keruh;
    GraphView grafik_level;
    Button insert;


    private PieChart ChartLevel, ChartKeruh;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef, reference2, reference3;
    DatabaseReference myRef_turbidity, referencesend, tampil_ref, tampil_lev, hari, minggu, bulan, harilevel;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String name_key = "";
    GraphView grafik;
    LineChart chart;
    //piechartlevel
    int jml_level=16;
    int jml_keruh=30;
    int persen =100;
    LineGraphSeries series;
    SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy kk:mm");
    SimpleDateFormat sdf1= new SimpleDateFormat("kk:mm");
    public Homee() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homee, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view,savedInstanceState);

        getUsernameLocal();

        photo = view.findViewById(R.id.profile_pic);
        username = view.findViewById(R.id.home_user);
        switchCompatlevel = view.findViewById(R.id.switcLevel);
        switchCompatkeruh = view.findViewById(R.id.switcKeruh);
        textView = view.findViewById(R.id.level_jam);
        //grafik_level=view.findViewById(R.id.grafiklevel);
        level=view.findViewById(R.id.level);
        keruh=view.findViewById(R.id.keruh);

        circularProgressBar = view.findViewById(R.id.progresbar_level);
        circularProgressBar_keruh=view.findViewById(R.id.progresbar_keruh);

        text_keruh = view.findViewById(R.id.text_keruh);
        text_level = view.findViewById(R.id.text_level);
        text_daily =view.findViewById(R.id.HARI);
        text_weekly=view.findViewById(R.id.minggu);
        text_mountly=view.findViewById(R.id.BULAN);
        text_data=view.findViewById(R.id.level_hari);
        //grafik = view.findViewById(R.id.grafiklevel);
       // chart = view.findViewById(R.id.chartkuras);

       // xValue= view.findViewById(R.id.xValue);

        firebaseDatabase = FirebaseDatabase.getInstance();
         hari = firebaseDatabase.getReference("grafik").child("DataKeruh");
        minggu = firebaseDatabase.getReference("DataMingguan");
        bulan = firebaseDatabase.getReference("DataPerbulan");
        referencesend = firebaseDatabase.getReference("grafik").child("Datalevel");
       harilevel = firebaseDatabase.getReference("DataHarianLevel");

        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(name_key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username.setText(dataSnapshot.child("name").getValue().toString());
                Picasso.with(getActivity()).load(dataSnapshot.child("url_photo_profile").getValue().toString()).centerCrop().fit().into(photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        hari.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                   int count = (int) dataSnapshot.getChildrenCount();
                   String angka = Integer.toString(count);
                    text_daily.setText(angka);
                }else {
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        minggu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int count = (int) dataSnapshot.getChildrenCount();
                    String angka = Integer.toString(count);
                    text_weekly.setText(angka);
                }else {
                    return;
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        bulan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int count = (int) dataSnapshot.getChildrenCount();
                    String angka = Integer.toString(count);
                    text_mountly.setText(angka);
                }else {
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        referencesend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int count = (int) dataSnapshot.getChildrenCount();
                    String angka = Integer.toString(count);
                    text_data.setText(angka);
                }else {
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        harilevel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int count = (int) dataSnapshot.getChildrenCount();
                    String angka = Integer.toString(count);
                    textView.setText(angka);
                }else {
                    return;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//
//
//        username.setText(currentUser.getDisplayName());
//        Glide.with(this).load(currentUser.getPhotoUrl()).into(photo);

        myRef = FirebaseDatabase.getInstance().getReference().child("Data");
        myRef_turbidity = FirebaseDatabase.getInstance().getReference().child("Data");

        circular_level();
        circular_keruh();
//        piechart_level();
//        piechart_keruh();

//desain level circualbar
        circularProgressBar.setProgressWithAnimation(65f, Long.valueOf(1000));
        circularProgressBar.setProgressMax(100);
        circularProgressBar.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);
        circularProgressBar.setProgressBarWidth(10f); // in DP
        circularProgressBar.setBackgroundProgressBarWidth(5f); // in DP
        circularProgressBar.setRoundBorder(true);
        circularProgressBar.setStartAngle(0f);
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);

        circularProgressBar_keruh.setProgressWithAnimation(65f, Long.valueOf(1000));
        circularProgressBar_keruh.setProgressMax(100);
        circularProgressBar_keruh.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);
        circularProgressBar_keruh.setProgressBarWidth(10f); // in DP
        circularProgressBar_keruh.setBackgroundProgressBarWidth(5f); // in DP
        circularProgressBar_keruh.setRoundBorder(true);
        circularProgressBar_keruh.setStartAngle(0f);
        circularProgressBar_keruh.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);


        //grafikkeruh
        series = new LineGraphSeries();
        series.setDrawBackground(true);
        series.setAnimated(true);
        series.setDrawDataPoints(true);
        series.setTitle("People");
//        grafik_level.addSeries(series);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef_turbidity = firebaseDatabase.getReference("grafik").child("DataKeruh");
        tampil_ref =firebaseDatabase.getReference("Tampil");
        tampil_lev= firebaseDatabase.getReference("Tampil");
        //setListeners();
//        grafik_level.getGridLabelRenderer().setNumHorizontalLabels(9);
//        grafik_level.getGridLabelRenderer().setNumVerticalLabels(9);
//        grafik_level.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
//            @Override
//            public String formatLabel(double value, boolean isValueX) {
//                if (isValueX){
//                    return sdf.format(new Date((long) value));
//                }else {
//                    return super.formatLabel(value, isValueX);
//                }
//
//            }
//        });

//        // register tap on series callback
//        series.setOnDataPointTapListener(new OnDataPointTapListener() {
//            @Override
//            public void onTap(Series series, DataPointInterface dataPoint) {
//                Toast.makeText(grafik_level.getContext(), "On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
//            }
//        });

        switchCompatlevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchCompatlevel.isChecked()){
                firebaseDatabase = FirebaseDatabase.getInstance();
                reference2 = firebaseDatabase.getReference().child("Control").child("ControlLevel");
                reference2.setValue("ON");
                }else{
                    reference2.setValue("OFF");
                }
            }
        });

        switchCompatkeruh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchCompatkeruh.isChecked()){
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference3 = firebaseDatabase.getReference().child("Control").child("ControlKeruh");
                    reference3.setValue("ON");
                }else{
                    reference3.setValue("OFF");
                }
            }
        });

    }
//
//    private void setListeners() {
//
//        insert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String id = myRef_turbidity.push().getKey();
//                long x= new Date().getTime();
//                int y = Integer.parseInt(yValue.getText().toString());
//                PointsValue pointsValue = new PointsValue(x,y);
//                myRef_turbidity.child(id).setValue(pointsValue);
//
//                long epoch = System.currentTimeMillis()/1000;
//                String convert = sdf.format(new java.util.Date(epoch*1000));
//                String Value = Integer.toString(y);
//                String no = tampil_ref.push().getKey();
//                PointsValue2 pointsValue2 = new PointsValue2(convert,Value);
//                tampil_ref.child("DataKeruh1").child(no).setValue(pointsValue2);
//                tampil_lev.child("DataLevel").child(no).setValue(pointsValue2);
//
//
//
//            }
//        });
//    }

    //level
//    @Override
//    public void onStart() {
//        super.onStart();
//        myRef_turbidity.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                com.jjoe64.graphview.series.DataPoint[] dp =
//                        new com.jjoe64.graphview.series.DataPoint[(int) dataSnapshot.getChildrenCount()];
//                int index=0;
//                int sum =0;
//                for (DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
//                    PointsValue pointsValue = myDataSnapshot.getValue(PointsValue.class);
//                    dp[index]= new com.jjoe64.graphview.series.DataPoint(pointsValue.getxValue(), pointsValue.getyValue());
//                    index ++;
//
//                    Map<String, Object> map = (Map<String, Object>) myDataSnapshot.getValue();
//                    Object jumlah = map.get("yValue");
//                    int Value = Integer.parseInt(String.valueOf(jumlah));
//                    sum+=Value;
//
//                    //Toast.makeText(getContext(), Integer.toString(sum) +"sum data",Toast.LENGTH_LONG ).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


    private void getUsernameLocal() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences (USERNAME_KEY, MODE_PRIVATE);
        name_key = sharedPreferences.getString(username_key, "");

    }


    private void circular_level(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot value: dataSnapshot.getChildren()){
                        DataPoint dataPoint = value.getValue(DataPoint.class);

                        int dataLevel= dataPoint.getLevel_rlt();
                        int jml_persenL = dataLevel*persen;
                        int hasil = jml_persenL/jml_level;
                        String tampilL=Integer.toString(hasil);

                        circularProgressBar.setProgress(hasil);
                        text_level.setText(tampilL+"%");
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void circular_keruh(){
        myRef_turbidity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot value: dataSnapshot.getChildren()){
                        DataPoint dataPoint = value.getValue(DataPoint.class);

                        int datakeruh= dataPoint.getKeruh_rlt();
                        int jml_persen = datakeruh*persen;
                        int hasilK = jml_persen/jml_keruh;
                        String tampilK= Integer.toString(hasilK);
                        circularProgressBar_keruh.setProgress(hasilK);
                        text_keruh.setText(tampilK+"%");

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    private void piechart_keruh() {
//        myRef_turbidity.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                yData = new ArrayList<>();
//                labels = new ArrayList<String>();
//
//                if (dataSnapshot.hasChildren()) {
//                    for (DataSnapshot Data : dataSnapshot.getChildren()) {
//                        DataPoint dataPoint = Data.getValue(DataPoint.class);
//                        yData.add(new PieEntry(dataPoint.getxValue(), "nilai X"));
//
//                    }
//
//                    PieDataSet dataSet = new PieDataSet(yData, "Category");
//                    dataSet.setSliceSpace(3f);
//                    dataSet.setSelectionShift(5f);
//
//                    dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//                    PieData data = new PieData(dataSet);
//                    data.setValueTextSize(10f);
//                    data.setValueTextColor(Color.BLACK);
//
//                    ChartKeruh.setUsePercentValues(true);
//                    ChartKeruh.getDescription().setEnabled(false);
//                    ChartKeruh.setExtraOffsets(5, 5, 5, 5);
//                    ChartKeruh.setDragDecelerationFrictionCoef(5f);
//                    ChartKeruh.setDrawHoleEnabled(true);
//                    ChartKeruh.setHoleColor(Color.WHITE);
//                    ChartKeruh.setTransparentCircleRadius(61f);
//
//                    ChartKeruh.setData(data);
//                    ChartKeruh.animate();
//                    ChartKeruh.getDescription().setEnabled(false);
//                    ChartKeruh.setCenterText("Keruh Air");
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

//    private void piechart_level() {
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                lData = new ArrayList<>();
//                Llabels = new ArrayList<String>();
//
//                if (dataSnapshot.hasChildren()) {
//                    for (DataSnapshot Data : dataSnapshot.getChildren()) {
//                        DataPoint dataPoint = Data.getValue(DataPoint.class);
//                        lData.add(new PieEntry(dataPoint.getxValue(), "nilai"));
//
//                    }
//
//                    PieDataSet dataSet = new PieDataSet(lData, "Category");
//                    dataSet.setSliceSpace(3f);
//                    dataSet.setSelectionShift(5f);
//
//                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//                    PieData data = new PieData(dataSet);
//                    data.setValueTextSize(10f);
//                    data.setValueTextColor(Color.BLACK);
//
//                    ChartLevel.setUsePercentValues(true);
//                    ChartLevel.getDescription().setEnabled(false);
//                    ChartLevel.setExtraOffsets(5, 5, 5, 5);
//                    ChartLevel.setDragDecelerationFrictionCoef(5f);
//                    ChartLevel.setDrawHoleEnabled(true);
//                    ChartLevel.setHoleColor(Color.WHITE);
//                    ChartLevel.setTransparentCircleRadius(61f);
//
//                    ChartLevel.setData(data);
//                    ChartLevel.animate();
//                    ChartLevel.getDescription().setEnabled(false);
//                    ChartLevel.setCenterText("Level Air");
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }


}
