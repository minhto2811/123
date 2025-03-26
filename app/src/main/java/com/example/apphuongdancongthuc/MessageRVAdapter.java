package com.example.apphuongdancongthuc;


import static android.view.View.GONE;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class MessageRVAdapter extends RecyclerView.Adapter {

    private ArrayList<MessageModel> messageModalArrayList;
    private Context context;

    public MessageRVAdapter(ArrayList<MessageModel> messageModalArrayList, Context context) {
        this.messageModalArrayList = messageModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg, parent, false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg, parent, false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel model = messageModalArrayList.get(position);
        if (model.isUser()) {
            ((UserViewHolder) holder).userTV.setText(model.getMessage());
            ((UserViewHolder) holder).gridView.setVisibility(GONE);
            if (model.getImages() == null) return;
            List<Uri> uris = UriConverter.toUriList(model.getImages());
            if (uris.isEmpty()) return;
            ((UserViewHolder) holder).gridView.setVisibility(View.VISIBLE);
            GridImageAdapter adapter = new GridImageAdapter(context, uris);
            ((UserViewHolder) holder).gridView.setAdapter(adapter);
            int colum = uris.size() < 4 ? uris.size() : 3;
            ((UserViewHolder) holder).gridView.setNumColumns(colum);
            adapter.notifyDataSetChanged();
            ViewGroup.LayoutParams params = ((UserViewHolder) holder).gridView.getLayoutParams();
            int columnWidthDp = 100;
            float scale = context.getResources().getDisplayMetrics().density;
            int columnWidthPx = (int) (columnWidthDp * scale + 0.5f);
            params.width = colum * columnWidthPx;
            ((UserViewHolder) holder).gridView.setLayoutParams(params);
            ((UserViewHolder) holder).gridView.requestLayout();
        } else {
            if (model.getMessage() != null) {
                ((BotViewHolder) holder).botTV.setText(model.getMessage());
            } else {
                ((BotViewHolder) holder).botTV.setText("Đang suy nghĩ...");
            }

        }
    }

    @Override
    public int getItemCount() {
        return messageModalArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModalArrayList.get(position).isUser()) {
            return 0;
        }
        return 1;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView userTV;
        private GridView gridView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userTV = itemView.findViewById(R.id.idTVUser);
            gridView = itemView.findViewById(R.id.gridView);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botTV;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botTV = itemView.findViewById(R.id.idTVBot);
        }
    }
}