package com.example.xtreme.newmenuorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OpenOrders extends AppCompatActivity {

    private RecyclerView mFoodList;
    private DatabaseReference mDatabse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_orders);

        //Setting up the Recycler view
        mFoodList = (RecyclerView) findViewById(R.id.orderLayout);
        mFoodList.setHasFixedSize(true);
        mFoodList.setLayoutManager(new LinearLayoutManager(this));
        mDatabse = FirebaseDatabase.getInstance().getReference().child("Orders");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Order,OrderViewHolder> FRBA = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(
                Order.class,
                R.layout.singleorderlayout,
                OrderViewHolder.class,
                mDatabse
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Order model, int position) {
                viewHolder.setUserName(model.getUsername());
                viewHolder.setItemName(model.getItemname());
            }
        };
        mFoodList.setAdapter(FRBA);
    }
    public  static class OrderViewHolder extends  RecyclerView.ViewHolder{
        View orderView;
        public OrderViewHolder(View itemView) {
            super(itemView);
            orderView = itemView;
        }

        //set the values for the username
        public void setUserName(String username){
            TextView username_content =(TextView) orderView.findViewById(R.id.orderUserName);
            username_content.setText(username);
        }
        public void setItemName(String itemname){
            TextView itemname_content =(TextView) orderView.findViewById(R.id.orderItemName);
            itemname_content.setText(itemname);
        }
    }
}
