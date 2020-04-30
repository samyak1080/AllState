package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.notez.com.myapplication.R;
import com.notez.com.myapplication.RaiseClaim;

import java.io.File;
import java.util.List;

public class IncidentImageProofAdapter extends RecyclerView.Adapter<IncidentImageProofAdapter.RecyclerViewHolder> {


    private List<File> imageProofList;
    File imageProof;
    public IncidentImageProofAdapter(Context context , List<File> imageProofList)
    {
        this.imageProofList = imageProofList;
    }



    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recy_incident_image_proof, parent, false);
        // SearchChemistAdapter.ViewHolder viewHolder = new SearchChemistAdapter.ViewHolder(v);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        imageProof = imageProofList.get(position);
        String filePath = imageProof.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        holder.incident_image_proof.setImageBitmap(bitmap);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageProofList.remove(position);

                notifyItemRemoved(position);
            }
        });


        }

    @Override
    public int getItemCount() {
        return imageProofList.size();
    }
    @Override
    public int getItemViewType(int Position) {
        return Position;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public ImageView incident_image_proof,remove;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
           incident_image_proof = (ImageView)itemView.findViewById(R.id.incident_image_proof);
           remove= (ImageView)itemView.findViewById(R.id.remove);

        }
    }


}


