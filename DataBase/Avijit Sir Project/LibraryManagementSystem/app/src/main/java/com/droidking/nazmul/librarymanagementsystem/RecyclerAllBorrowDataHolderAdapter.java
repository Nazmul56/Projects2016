package com.droidking.nazmul.librarymanagementsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerAllBorrowDataHolderAdapter extends RecyclerView.Adapter<RecyclerAllBorrowDataHolderAdapter.MyViewHolder> {

    private List<RecyclerAllBorrowDataHolder> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre,mem_name,book_title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);

            mem_name = (TextView) view.findViewById(R.id.borrow_mem);
            book_title =(TextView) view.findViewById(R.id.borrow_book_title);
        }
    }


    public RecyclerAllBorrowDataHolderAdapter(List<RecyclerAllBorrowDataHolder> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_borrowed_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecyclerAllBorrowDataHolder movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
        holder.mem_name.setText(movie.getMemName());
        holder.book_title.setText(movie.getBookTitle());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
