package skripsi.com.Fragment_level;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.tabs.TabLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import skripsi.com.AdapterKeruh_1;
import skripsi.com.Fragmant_Keruh.BulananFragment;
import skripsi.com.Fragmant_Keruh.HarianFragment;
import skripsi.com.Fragmant_Keruh.MingguanFragment;
import skripsi.com.Fragmant_Keruh.Model_minggu;
import skripsi.com.Fragmant_Keruh.PointsValue3;
import skripsi.com.PointsValue;
import skripsi.com.PointsValue2;
import skripsi.com.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataKeruh_ extends Fragment {

    ImageButton btn_plus;
    LineChart grafik_level;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    DatabaseReference myRef_turbidity, reference2, referencesend, reference;
    int count;
    Integer z=0;
    int sum=0 ;
    LineGraphSeries series;
    SimpleDateFormat sdf= new SimpleDateFormat("dd");
    SimpleDateFormat sdf_l= new SimpleDateFormat("yyyy-MM-dd");
    ArrayList<ILineDataSet> ibardataset = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<ContractorView> itemList;
    AdepterLevel adapter;

    public DataKeruh_() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_keruh_, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        grafik_level=view.findViewById(R.id.chartLev);
        btn_plus = view.findViewById(R.id.btn_tambah);
        //recyclerView

        recyclerView = view.findViewById(R.id.list_level);

        itemList = new ArrayList<ContractorView>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initData();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef_turbidity = firebaseDatabase.getReference("Tampil").child("DataLevel");
        myRef = firebaseDatabase.getReference("grafik").child("Datalevel");
        myRef_turbidity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ContractorView p = dataSnapshot1.getValue(ContractorView.class);
                    itemList.add(p);
                }
                adapter = new AdepterLevel(getActivity(), itemList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //reyclerview
        //setListeners();

        // register tap on series callback
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot mydata : dataSnapshot.getChildren()) {
                            //count child
                            Map<String, Object> map = (Map<String, Object>) mydata.getValue();
                            Object jumlah = map.get("yValue");
                            int Value = Integer.parseInt(String.valueOf(jumlah));
                            sum+=Value;
                        }

                        if (dataSnapshot.exists()){
                            count = (int) dataSnapshot.getChildrenCount();
                            if (count >=24 ){
                                int rata_rata = sum/count;
                                Toast.makeText(getContext(), "Nilai rata-rata level =" + rata_rata,Toast.LENGTH_LONG ).show();
                                simpandata();
                                myRef.removeValue();

                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Entry> dataValue= new ArrayList<Entry>();
                ArrayList<Entry>  lData = new ArrayList<>();
                ArrayList Llabels = new ArrayList<String>();

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot mydata : dataSnapshot.getChildren()) {
                        ContractorLev contractorLev = mydata.getValue(ContractorLev.class);
                        lData.add(new Entry(contractorLev.getxValue(), contractorLev.getyValue()));
                        //count child

                    }

                    LineDataSet dataSet = new LineDataSet(lData, "Day");
                    dataSet.setDrawFilled(true);
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                    dataSet.setFillDrawable( drawable);
                    LineData data = new LineData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.BLACK);
                    dataSet.setColors(R.color.colorPrimary);
                    dataSet.setValueTextColor(Color.BLACK);
                    dataSet.setValueTextSize(12);
                    //String[] hari=new String[]{"Day"};


                    grafik_level.setDrawGridBackground(false);
                    grafik_level.setBackgroundColor(Color.WHITE);

                    // no description text
                    grafik_level.getDescription().setEnabled(false);

                    // enable touch gestures
                    grafik_level.setTouchEnabled(true);

                    // enable scaling and dragging
                    grafik_level.setDragEnabled(true);
                    grafik_level.setScaleEnabled(true);
                    grafik_level.setPinchZoom(true);
                    grafik_level.setData(data);
                    grafik_level.animate();
                    grafik_level.animateY(1500);
//
//                    XAxis xl = grafik_level.getXAxis();
//                    xl.setAvoidFirstLastClipping(true);
//                    xl.setAxisMinimum(0f);
//
//                    YAxis leftAxis = grafik_level.getAxisLeft();
//                    leftAxis.setInverted(true);
//                    leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//                    YAxis rightAxis = grafik_level.getAxisRight();
//                    rightAxis.setEnabled(false);

                    XAxis xAxis;

                    {   // // X-Axis Style // //
                        xAxis = grafik_level.getXAxis();

                        // vertical grid lines
                        xAxis.enableGridDashedLine(10f, 10f, 0f);
                    }

                    YAxis yAxis;
                    {   // // Y-Axis Style // //
                        yAxis = grafik_level.getAxisLeft();

                        // disable dual axis (only use LEFT axis)
                        grafik_level.getAxisRight().setEnabled(false);

                        // horizontal grid lines
                        yAxis.enableGridDashedLine(10f, 10f, 0f);

                        // axis range
                        yAxis.setAxisMaximum(30f);
                        yAxis.setAxisMinimum(0f);
                    }

                    Legend l = grafik_level.getLegend();

                    // draw legend entries as lines
                    l.setForm(Legend.LegendForm.LINE);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void simpandata() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        referencesend = firebaseDatabase.getReference("DataHarianLevel");
        int y = sum/count;
        long epoch = System.currentTimeMillis()/1000;
        String convert = sdf.format(new java.util.Date(epoch*1000));
        String convert2 = sdf_l.format(new java.util.Date(epoch*1000));
        int tgl = Integer.parseInt(convert);

        String id = referencesend.push().getKey();
        Model_minggu model_minggu = new Model_minggu(tgl,y);
        referencesend.child(id).setValue(model_minggu);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Tampil").child("DataLevel2");

        String a=Integer.toString(y);
        PointsValue3 pointsValue3 = new PointsValue3(a,convert2);
        reference.child(id).setValue(pointsValue3);

        //
        Toast.makeText(getContext(),"Jumlah rata-rata : "+ y, Toast.LENGTH_LONG).show();
    }



}
