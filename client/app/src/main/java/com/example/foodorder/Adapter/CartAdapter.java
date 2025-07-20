package com.example.foodorder.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.CartItem;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;

    public CartAdapter(Cart cart) {
        this.cartItems = cart.getCartItems();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemQty;
        ImageView itemImage;

        public CartViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cartItemImage);
            itemName = itemView.findViewById(R.id.cartItemName);
            itemPrice = itemView.findViewById(R.id.cartItemPrice);
            itemQty = itemView.findViewById(R.id.cartItemQty);
        }
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
                .placeholder(R.drawable.gradient_avatar_background)
                .into(holder.itemImage);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(String.format(Locale.US, "$%.2f", item.getPrice()));
        holder.itemQty.setText(String.format(Locale.US, "Số lượng: %d", item.getQuantity()));

        //TODO: Create logic redirect to the food detail when click on item in cart
//        holder.itemView.setOnClickListener(v -> {
//            Bundle extras = new Bundle();
//            extras.putInt("id", item.id);
//            Routing.redirect(holder.itemView.getContext(), FoodActivity.class, extras);
//        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
