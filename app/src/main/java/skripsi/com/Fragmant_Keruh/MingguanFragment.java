package skripsi.com.Fragmant_Keruh;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import skripsi.com.AdapterKeruh_1;
import skripsi.com.PointsValue;
import skripsi.com.PointsValue2;
import skripsi.com.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * A simple {@link Fragment} subclass.
 */
public class MingguanFragment extends Fragment {
    ImageButton btn_plus;
    BarChart bargraph;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef, myRef_turbidity, referencecache, reference;
//    BarDataSet barDataSet = new BarDataSet(null,null);
    ArrayList<IBarDataSet> ibardataset = new ArrayList<>();
    BarData barData;
     int count;
     int sum=0;
     int z=0;
    SimpleDateFormat sdf= new SimpleDateFormat("dd");
    RecyclerView recyclerView;
    ArrayList<PointsValue3> itemList;
    AdapterKeruh_2 adapter;
    public MingguanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mingguan, container, false);
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_plus = view.findViewById(R.id.btn_plus);
        bargraph = view.findViewById(R.id.chart2);
        recyclerView = view.findViewById(R.id.list_m);
        itemList = new ArrayList<PointsValue3>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef_turbidity = firebaseDatabase.getReference("Tampil").child("DataKeruh2");
        referencecache = firebaseDatabase.getReference("DataMingguan");


        myRef_turbidity.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    PointsValue3 p = dataSnapshot1.getValue(PointsValue3.class);
                    itemList.add(p);
                }
                adapter = new AdapterKeruh_2(getActivity(), itemList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referencecache.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot mydata : dataSnapshot.getChildren()) {
                            //count child
                            Map<String, Object> map = (Map<String, Object>) mydata.getValue();
                            Object jumlah = map.get("data");
                            int Value = Integer.parseInt(String.valueOf(jumlah));
                            sum+=Value;
                        }

                        if (dataSnapshot.exists()){
                            count = (int) dataSnapshot.getChildrenCount();
                            if (count >= 1){
                               savedata();
                                //hapus
                               referencecache.removeValue();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        Tampil();
    }

    public class MyXaxis extends ValueFormatter implements IAxisValueFormatter{
        private String[]mValues;
        public MyXaxis(String[]xValue){
            this.mValues=xValue;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];
        }
    }


    private void Tampil() {
        myRef = firebaseDatabase.getReference("DataMingguan");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<BarEntry> dataValue= new ArrayList<BarEntry>();
                 ArrayList<BarEntry>  lData = new ArrayList<>();
                  ArrayList Llabels = new ArrayList<String>();

                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot mydata : dataSnapshot.getChildren()){
                        Model_minggu model_minggu = mydata.getValue(Model_minggu.class);
                        lData.add(new BarEntry(model_minggu.getDay(),model_minggu.getData()));

                    }

                    BarDataSet dataSet = new BarDataSet(lData, "Day");
                      BarData data = new BarData(dataSet);
                      data.setBarWidth(0.9f);
                      data.setValueTextSize(10f);
                      data.setValueTextColor(Color.BLACK);
                      dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                      dataSet.setValueTextColor(Color.BLACK);
                      dataSet.setValueTextSize(12);
                      String[] hari=new String[]{"Day"};
                      XAxis xAxis =bargraph.getXAxis();
                      xAxis.setValueFormatter(new MyXaxis(hari));
                      xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                     bargraph.setDrawBarShadow(false);
                     bargraph.setDrawGridBackground(false);
                     bargraph.setData(data);
                     bargraph.animate();
                     bargraph.animateY(1500);

                }else {
                    bargraph.clear();
                    bargraph.invalidate();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    private void savedata() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference referencesend = firebaseDatabase.getReference("DataPerbulan");
        int y = sum/count;
        //rata-rata
//        z+=1;
//        int [] jumlah= {z};
//        int nilai = jumlah[0];

        long epoch = System.currentTimeMillis()/1000;
        String convert = sdf.format(new java.util.Date(epoch*1000));
        int tgl = Integer.parseInt(convert);

        String id = referencesend.push().getKey();
        Model_minggu model_minggu = new Model_minggu(tgl,y);
        referencesend.child(id).setValue(model_minggu);


        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Tampil").child("DataKeruh3");
        String a=Integer.toString(y);
        PointsValue3 pointsValue3 = new PointsValue3(a,convert);
        reference.child(id).setValue(pointsValue3);

        referencecache.removeValue();
    }




}
