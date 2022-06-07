package skripsi.com.Fragmant_Keruh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import skripsi.com.PointsValue2;
import skripsi.com.R;

public class AdapterKeruh_2 extends RecyclerView.Adapter<AdapterKeruh_2.MyViewHolder> {

    Context context;
    ArrayList<PointsValue3> listDoas;

    public AdapterKeruh_2(Context c, ArrayList<PointsValue3> p){
        context = c;
        listDoas= p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_keruh2, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
//        myViewHolder.xbg_program.setImageURI(listTickets.get(i).getBg_program());

        myViewHolder.tanggal_.setText(listDoas.get(i).getDay());
        myViewHolder.Value.setText(listDoas.get(i).getData());
//        myViewHolder.text_doa.setText(listDoas.get(i).getText_doa());
//        myViewHolder.latin.setText(listDoas.get(i).getLatin_doa());
//        myViewHolder.terjemah.setText(listDoas.get(i).getTerjemah_doa());



//        final String getKode_paket = listDoas.get(i).getKode_doa();
//
//        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent gotoprogramdetail = new Intent(context, MyDoa.class);
//                gotoprogramdetail.putExtra("kode_doa", getKode_paket);
//                context.startActivity(gotoprogramdetail);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return listDoas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tanggal_, Value, latin, terjemah;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tanggal_ = itemView.findViewById(R.id.tanggal2);
            Value = itemView.findViewById(R.id.valueKeruh2);
//            latin=itemView.findViewById(R.id.latin);
//            terjemah=itemView.findViewById(R.id.terjemah);
        }
    }
}
