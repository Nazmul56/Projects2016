package com.droidking.nazmul.librarymanagementsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerCatalogDataHolderAdapter extends RecyclerView.Adapter<RecyclerCatalogDataHolderAdapter.MyViewHolder> {

    private List<RecyclerCatalogDataHolder> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre,avaiable;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            avaiable =(TextView) view.findViewById(R.id.available);

        }
    }


    public RecyclerCatalogDataHolderAdapter(List<RecyclerCatalogDataHolder> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catalog_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecyclerCatalogDataHolder recyclerDataHolder = moviesList.get(position);
        holder.title.setText(recyclerDataHolder.getTitle());
        holder.genre.setText(recyclerDataHolder.getAuthors());
        holder.year.setText(recyclerDataHolder.getTotalBook());
        holder.avaiable.setText(recyclerDataHolder.getAvailable());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
