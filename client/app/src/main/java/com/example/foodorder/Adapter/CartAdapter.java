package com.example.foodorder.Adapter;

import static android.view.View.GONE;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public static final boolean IS_CART_ACTIVITY = true;
    public static final boolean IS_CHECKOUT_ACTIVITY = false;
    public static final OnCartItemChangeListener NO_LISTENER = null;
    private final List<CartItem> cartItems;
    private OnCartItemChangeListener listener;

    private boolean editable;

    public CartAdapter(Cart cart, OnCartItemChangeListener listener, boolean flag) {
        this.cartItems = cart.getCartItems();
        this.listener = listener;
        this.editable = flag;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemQty;
        ImageView itemImage;
        Button btnIncrease, btnDecrease;

        public CartViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cartItemImage);
            itemName = itemView.findViewById(R.id.cartItemName);
            itemPrice = itemView.findViewById(R.id.cartItemPrice);
            itemQty = itemView.findViewById(R.id.cartItemQty);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
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
        holder.itemPrice.setText(String.format(Locale.US, "%.2fđ", item.getPrice()));
        holder.itemQty.setText(String.format(Locale.US, "Số lượng: %d", item.getQuantity()));

        if (editable) {
            holder.btnIncrease.setOnClickListener(v -> {
                listener.onIncrease(position);
            });

            holder.btnDecrease.setOnClickListener(v -> {
                listener.onDecrease(position);
            });
        } else {
            holder.btnIncrease.setVisibility(GONE);
            holder.btnDecrease.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public interface OnCartItemChangeListener {
        void onIncrease(int position);

        void onDecrease(int position);
    }
}
