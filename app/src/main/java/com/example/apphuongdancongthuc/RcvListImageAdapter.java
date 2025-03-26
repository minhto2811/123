package com.example.apphuongdancongthuc;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RcvListImageAdapter extends RecyclerView.Adapter<RcvListImageAdapter.ImageHolder> {

    private Context mContext;
    private ArrayList<Uri> uris;

    public RcvListImageAdapter(Context mContext, ArrayList<Uri> uris) {
        this.mContext = mContext;
        this.uris = uris;
    }

    @NonNull
    @Override
    public RcvListImageAdapter.ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.image_message, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvListImageAdapter.ImageHolder holder, int position) {
        Uri uri = uris.get(position);
        holder.imgMessage.setImageURI(uri);
        holder.imbRemove.setOnClickListener(v->{
            uris.remove(position);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getItemCount() {
        return uris == null ? 0 : uris.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        private ImageView imgMessage;
        private ImageButton imbRemove;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imgMessage = itemView.findViewById(R.id.imgMessage);
            imbRemove = itemView.findViewById(R.id.imbRemove);
        }
    }
}
