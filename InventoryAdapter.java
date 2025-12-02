package com.example.fitnessprojectneal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    private final List<CosmeticItem> inventory;
    private final Player player;

    public InventoryAdapter(List<CosmeticItem> inventory, Player player) {
        this.inventory = inventory;
        this.player = player;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory_cosmetic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CosmeticItem item = inventory.get(position);

        holder.tvName.setText(item.id);

        // Optional: If items have images
        // holder.ivIcon.setImageResource(item.getIconRes());

        holder.itemView.setOnClickListener(v -> {
            player.equippedCosmetics.put(item.category, item.id);
            notifyDataSetChanged();  // highlight selected item
        });
    }

    @Override
    public int getItemCount() {
        return inventory.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivCosmeticIcon);
            tvName = itemView.findViewById(R.id.tvCosmeticName);
        }
    }
}
