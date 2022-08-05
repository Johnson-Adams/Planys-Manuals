package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awesome.adams.planysmanuals.DosNDonts;
import com.awesome.adams.planysmanuals.R;

import java.util.ArrayList;
import java.util.List;

public class MainListRecyclerAdapter extends RecyclerView.Adapter<MainListRecyclerAdapter.ViewHolder> {

    private Context context;
    private String[] list;

    public MainListRecyclerAdapter(Context context, String[] list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MainListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rview = LayoutInflater.from(context).inflate(R.layout.mainlistcard,parent,false);
        return new ViewHolder(rview,context);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListRecyclerAdapter.ViewHolder holder, int position) {
        final String main = list[position];
        Log.d("main list name", main);
        Log.d("mlrecadpt","1");
        holder.lists.setText(main);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("main list touch", String.valueOf(1));
                if (main == "Do's and Dont's") {
                    Intent intent = new Intent(context, DosNDonts.class);
                    context.startActivity(intent);
                }
                else if (main == "Process") {
                    Intent intent = new Intent(context, DosNDonts.class);
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView lists;
        private RelativeLayout rl;
        public ViewHolder(View itemView,Context context) {
            super(itemView);
            //lists = (TextView) itemView.findViewById(R.id.mainlist_listname);
        }
    }
}
