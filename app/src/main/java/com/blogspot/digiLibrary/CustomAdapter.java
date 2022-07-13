package com.blogspot.digiLibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.digiLibrary.Utils.Utils;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList book_Id, book_Title, book_page, book_author;
    private Animation animation;
    private ArrayList<byte[]> imageBye;

    CustomAdapter(Activity a, Context context, ArrayList book_Id, ArrayList book_Title, ArrayList book_page, ArrayList book_author, ArrayList<byte[]>image) {
        this.book_author = book_author;
        this.context = context;
        this.book_Id = book_Id;
        this.book_page = book_page;
        this.book_Title = book_Title;
        this.imageBye = image;
        this.activity = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.book_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bookID.setText(String.valueOf(book_Id.get(position)));
        holder.bookAuthorTextview.setText(String.valueOf(book_author.get(position)));
        holder.bookTitleTextView.setText(String.valueOf(book_Title.get(position)));
        holder.bookPageTextView.setText(String.valueOf(book_page.get(position)));

        try{

        if (imageBye.get(position) !=null){
            holder.bImage.post(new Runnable() {
                @Override
                public void run() {
                    holder.bImage.setImageBitmap(Utils.getImage(imageBye.get(position)));
                }
            });
        }}catch (Exception e){
            e.printStackTrace();
        }
        Log.d("System", "onCreate:authorString onBindViewHolder " + String.valueOf(book_author.get(position)) + " titleString " + String.valueOf(book_Title.get(position)) + " pageString " + String.valueOf(book_page.get(position)) + " idString " + String.valueOf(book_Id.get(position)));

        holder.book_row.setOnClickListener(View -> {
            Log.d("System", "onCreate:authorString onBindViewHolder clicke" + String.valueOf(book_author.get(position)) + " titleString " + String.valueOf(book_Title.get(position)) + " pageString " + String.valueOf(book_page.get(position)) + " idString " + String.valueOf(book_Id.get(position)));

            Intent in = new Intent(context, AddBookActivity.class);
            in.putExtra("type", "update");
            in.putExtra("Author", String.valueOf(book_author.get(position)));
            in.putExtra("title", String.valueOf(book_Title.get(position)));
            in.putExtra("page", String.valueOf(book_page.get(position)));
            in.putExtra("id", String.valueOf(book_Id.get(position)));


            activity.startActivityForResult(in, 2);

        });

    }

    @Override
    public int getItemCount() {
        return book_Id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bookID, bookAuthorTextview, bookTitleTextView, bookPageTextView;
        LinearLayout book_row;
        ImageView bImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookAuthorTextview = itemView.findViewById(R.id.bookAuthor);
            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookPageTextView = itemView.findViewById(R.id.bookPage);
            bookID = itemView.findViewById(R.id.book_id);
            book_row = itemView.findViewById(R.id.book_row);
            bImage = itemView.findViewById(R.id.bookImage);
            animation = AnimationUtils.loadAnimation(context,
                    R.anim.top_to_pop);
            book_row.setAnimation(animation);

        }
    }
}
