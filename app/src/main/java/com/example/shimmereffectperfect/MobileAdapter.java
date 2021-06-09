package com.example.shimmereffectperfect;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import java.util.ArrayList;

public class MobileAdapter extends RecyclerView.Adapter<MobileAdapter.MyViewHolder> {
    Context context;
    ArrayList<MobileModel> mobiles;

    public MobileAdapter(Context context, ArrayList<MobileModel> mobiles) {
        this.context = context;
        this.mobiles = mobiles;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public MobileAdapter.MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list, parent, false); //if true the app will crash
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MobileAdapter.MyViewHolder holder, int position) {
        // Initialize shimmer
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#AEADAD"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#E7E7E7"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();

        // Initialize shimmer drawable
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        // Set shimmer
        shimmerDrawable.setShimmer(shimmer);

        Glide.with(context).load(mobiles.get(position).getImage())
                .placeholder(shimmerDrawable)
                .into(holder.imageView);
        holder.modelName.setText(mobiles.get(position).getModel());
        holder.price.setText("Price: $" + mobiles.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return mobiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView modelName;
        TextView price;
        ImageView imageView;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            modelName = itemView.findViewById(R.id.tvModel);
            price = itemView.findViewById(R.id.tvPrice);
            imageView = itemView.findViewById(R.id.ivImage);
        }
    }
}