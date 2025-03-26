package com.example.apphuongdancongthuc;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class GridImageAdapter extends BaseAdapter {
    private final Context context;
    private final List<Uri> imageUris;
    private final LayoutInflater inflater;

    public GridImageAdapter(Context context, List<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageUris.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUris.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_message_2, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imgMessage2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.imageView.setImageURI(imageUris.get(position));
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
