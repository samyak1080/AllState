package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.notez.com.myapplication.R;

import java.util.List;

import Models.DriverModel;

public class DriverDetailsAdapter extends RecyclerView.Adapter<DriverDetailsAdapter.RecyclerViewHolder> {

    Context mContext;
    private List<DriverModel> driver_model_list;
    DriverModel generatemodel;
    public DriverDetailsAdapter(Context context , List<DriverModel> driver_model_list)
    {
        this.mContext=context;
        this.driver_model_list=driver_model_list;
    }



    @NonNull
    @Override
    public DriverDetailsAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recy_driver_details, parent, false);
        return new DriverDetailsAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int i) {

        holder.heading.setText("   Driver "+(i+1)+" Details :");
        holder.driver_name.setText(driver_model_list.get(i).getName());

        holder.driver_id.setText(driver_model_list.get(i).getId());
        holder.dl_expiry_date.setText(driver_model_list.get(i).getDl_expiry_date());
        holder.dl_issue_date.setText(driver_model_list.get(i).getDl_issue_date());
        holder.dl_number.setText(driver_model_list.get(i).getDl_number());
        holder.driver_address.setText(  driver_model_list.get(i).getAddress_line_1()+", "+
                                        driver_model_list.get(i).getAddress_line_2()+", "+
                                        driver_model_list.get(i).getCity()+", " +
                                        driver_model_list.get(i).getState()+", "+
                                        driver_model_list.get(i).getCountry()+", ");
        holder.driver_dob.setText(driver_model_list.get(i).getDob());
        holder.collapsable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.is_collapsed=!holder.is_collapsed;

                if(holder.is_collapsed)
                {
                   holder.collapsable.setImageDrawable(mContext.getDrawable(R.drawable.ic_expand_less_24dp));
                   holder.collapsable_view.setVisibility(View.VISIBLE);
                }
                else
                {

                    holder.collapsable.setImageDrawable(mContext.getDrawable(R.drawable.ic_expand_more_24dp));
                    holder.collapsable_view.setVisibility(View.GONE);

                    }
            }
        });
    }


    @Override
    public int getItemCount() {
        return driver_model_list.size();
    }
    @Override
    public int getItemViewType(int Position) {
        return Position;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView driver_name,driver_id,dl_number,dl_issue_date,dl_expiry_date,driver_dob,driver_address,heading;
        public ImageView collapsable;
        public LinearLayout collapsable_view;
        public boolean is_collapsed=false;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            driver_name = (TextView)itemView.findViewById(R.id.driver_name);

            driver_id = (TextView)itemView.findViewById(R.id.driver_id);

            dl_number = (TextView)itemView.findViewById(R.id.dl_number);

            dl_issue_date = (TextView)itemView.findViewById(R.id.dl_issue_date);

            dl_expiry_date = (TextView)itemView.findViewById(R.id.dl_expiry_date);

            driver_dob = (TextView)itemView.findViewById(R.id.dob);

            driver_address = (TextView)itemView.findViewById(R.id.address);



            heading = (TextView)itemView.findViewById(R.id.heading);
            collapsable = (ImageView)itemView.findViewById(R.id.collapsable);
            collapsable_view = (LinearLayout)itemView.findViewById(R.id.collapsable_view);

        }
    }

}
