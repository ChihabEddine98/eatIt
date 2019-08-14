package com.chihab_eddine98.eatit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chihab_eddine98.eatit.interfaces.ItemClickListener;
import com.chihab_eddine98.eatit.model.Food;
import com.chihab_eddine98.eatit.viewHolder.FoodVH;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recycler_food;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Food, FoodVH> adapter;

    FirebaseDatabase bdd;
    DatabaseReference table_food;

    String categoryId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        recycler_food=findViewById(R.id.recycler_food);
        recycler_food.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);

        recycler_food.setLayoutManager(layoutManager);


        bdd=FirebaseDatabase.getInstance();
        table_food=bdd.getReference("Food");


        if(getIntent()!=null)
        {
            categoryId=getIntent().getStringExtra("categoryId");

        }
        if(!categoryId.isEmpty() && categoryId!=null)
        {
            loadListFood(categoryId);
        }





    }

    private void loadListFood(String categoryId) {

        adapter=new FirebaseRecyclerAdapter<Food, FoodVH>(Food.class,R.layout.food_item,FoodVH.class,
                table_food.orderByChild("categoryId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodVH foodVH, Food food, int i) {

                foodVH.food_nom.setText(food.getNom());
                Picasso.with(getBaseContext()).load(food.getImgUrl())
                        .into(foodVH.food_img);


                final Food local=food;

                foodVH.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent foodDetailActivity=new Intent(FoodList.this,FoodDetail.class);
                        foodDetailActivity.putExtra("foodId",adapter.getRef(position).getKey());

                        startActivity(foodDetailActivity);
                    }
                });



            }
        };


        recycler_food.setAdapter(adapter);



    }
}
