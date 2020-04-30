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

import com.notez.com.myapplication.FragmentUserProfile;
import com.notez.com.myapplication.R;

import java.util.List;

import Models.DependentModel;

public class DependentDetailAdapter extends RecyclerView.Adapter<DependentDetailAdapter.RecyclerViewHolder> {

        Context mContext;
private List<DependentModel> dependent_model_list;
        DependentModel generatemodel;
public DependentDetailAdapter(Context context , List<DependentModel> dependent_model_list)
        {
        this.mContext=context;
        this.dependent_model_list =dependent_model_list;
        }



@NonNull
@Override
public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recy_dependent_detail, parent, false);
        return new RecyclerViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int i) {

        holder.relationship.setText("   "+dependent_model_list.get(i).getRelationship());
        if (dependent_model_list.get(i).getRelationship().equals("Daughter"))
            holder.relationship.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.ic_daughter),null,null,null);
        else if (dependent_model_list.get(i).getRelationship().equals("Son"))
            holder.relationship.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.ic_son),null,null,null);
        else if (dependent_model_list.get(i).getRelationship().equals("Spouse"))
            holder.relationship.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.ic_bride),null,null,null);
        else
            holder.relationship.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.ic_friend),null,null,null);

    holder.dependent_name.setText(dependent_model_list.get(i).getName());

        holder.dependent_id.setText(dependent_model_list.get(i).getDependentID());

        holder.dependent_dob.setText(dependent_model_list.get(i).getDob());

    holder.collapsable.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            holder.is_collapsed=!holder.is_collapsed;

            if(holder.is_collapsed)
            {
                holder.collapsable.setImageDrawable(mContext.getDrawable(R.drawable.ic_expand_less_24dp));
                holder.collapsable_view.setVisibility(View.VISIBLE);
                FragmentUserProfile.mRecyclerView.setHasFixedSize(false);

            }
            else
            {
                notifyDataSetChanged();
                holder.collapsable.setImageDrawable(mContext.getDrawable(R.drawable.ic_expand_more_24dp));
                holder.collapsable_view.setVisibility(View.GONE);
                FragmentUserProfile.mRecyclerView.setHasFixedSize(false);
            }


        }
    });

        }


@Override
public int getItemCount() {
        return dependent_model_list.size();
        }
@Override
public int getItemViewType(int Position) {
        return Position;
        }

public class RecyclerViewHolder extends RecyclerView.ViewHolder{

    public TextView dependent_name,dependent_id, dependent_dob, relationship,policy_id;
    public ImageView collapsable;
    public LinearLayout collapsable_view;
    public boolean is_collapsed=false;


    public RecyclerViewHolder(View itemView) {
        super(itemView);
        dependent_name = (TextView)itemView.findViewById(R.id.dependent_name);

        dependent_id = (TextView)itemView.findViewById(R.id.dependent_id);





        dependent_dob = (TextView)itemView.findViewById(R.id.dob);


        collapsable = (ImageView)itemView.findViewById(R.id.collapsable);
        collapsable_view = (LinearLayout)itemView.findViewById(R.id.collapsable_view);


        relationship = (TextView)itemView.findViewById(R.id.relationship);
        policy_id = (TextView)itemView.findViewById(R.id.policy_id);

    }
}

}

