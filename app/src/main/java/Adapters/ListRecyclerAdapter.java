package Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesome.adams.planysmanuals.R;

import java.util.ArrayList;
import java.util.List;

import Model.ListObject;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<String> list;

    public ListRecyclerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listcard,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecyclerAdapter.ViewHolder holder, int position) {
        String listname = list.get(position);
        holder.name.setText(listname);
        SharedPreferences sharedPreferences = context.getSharedPreferences("count_list",Context.MODE_PRIVATE);
        SharedPreferences.Editor sav = sharedPreferences.edit();
        sav.putInt("noofart",getItemCount() + 4);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,num;
        public ViewHolder(View itemView, Context context) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.list_listname);
            num = (TextView) itemView.findViewById(R.id.numberlists);
        }
    }
}
