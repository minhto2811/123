package com.example.apphuongdancongthuc;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
        recyclerView = findViewById(R.id.recyclerView);
        List<Product> productList = new ArrayList<>();

        // Thêm sản phẩm vào danh sách
        productList.add(new Product("Product 1", 29.99, R.drawable.monca));
        productList.add(new Product("Product 2", 49.99, R.drawable.moncuon));
        productList.add(new Product("Product 3", 19.99, R.drawable.monga));
        productList.add(new Product("Product 4", 39.99, R.drawable.monnuoc));
        productList.add(new Product("Product 5", 59.99, R.drawable.monnuong));

        productAdapter = new ProductAdapter(productList);

        // Thiết lập LinearLayoutManager cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productAdapter);
    }
}