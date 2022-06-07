package skripsi.com.Fragment_level;

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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import skripsi.com.Fragmant_Keruh.AdapterKeruh_2;
import skripsi.com.Fragmant_Keruh.MingguanFragment;
import skripsi.com.Fragmant_Keruh.Model_minggu;
import skripsi.com.Fragmant_Keruh.PointsValue3;
import skripsi.com.R;

public class HarianLevel extends Fragment {
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
    public HarianLevel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_harian_level, container, false);
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_plus = view.findViewById(R.id.btn_plusL);
        bargraph = view.findViewById(R.id.chart_level);
        recyclerView = view.findViewById(R.id.list_level);
        itemList = new ArrayList<PointsValue3>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef_turbidity = firebaseDatabase.getReference("Tampil").child("DataLevel2");
        referencecache = firebaseDatabase.getReference("DataHarianLevel");


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
                            double nilai = sum/count;
                            if (count >= 1){
                                Toast.makeText(getContext(),"Nilai rata-rata level = "+ nilai, Toast.LENGTH_LONG).show();
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
//
//    public class MyXaxis extends ValueFormatter implements IAxisValueFormatter {
//        private String[]mValues;
//        public MyXaxis(String[]xValue){
//            this.mValues=xValue;
//        }
//
//        @Override
//        public String getFormattedValue(float value, AxisBase axis) {
//            return mValues[(int)value];
//        }
//    }


    private void Tampil() {
        myRef = firebaseDatabase.getReference("DataHarianLevel");
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
                    //xAxis.setValueFormatter(new HarianLevel.MyXaxis(hari));
                    xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                    bargraph.setDrawBarShadow(false);
                    bargraph.setDrawGridBackground(false);
                    bargraph.setData(data);
                    bargraph.animate();
                    bargraph.animateY(1500);

                    XAxis xl = bargraph.getXAxis();
                    xl.setAvoidFirstLastClipping(true);
                    xl.setAxisMinimum(0f);

                    YAxis leftAxis = bargraph.getAxisLeft();
                    leftAxis.setInverted(true);
                    leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                    YAxis rightAxis = bargraph.getAxisRight();
                    rightAxis.setEnabled(false);

                    // add data

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



}