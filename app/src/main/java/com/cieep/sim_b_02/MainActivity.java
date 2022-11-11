package com.cieep.sim_b_02;

import android.content.Intent;
import android.os.Bundle;

import com.cieep.sim_b_02.adapters.ProductosAdapter;
import com.cieep.sim_b_02.modelos.ProductoModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;


import com.cieep.sim_b_02.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private ArrayList<ProductoModel> productoModelsList;
    private ActivityResultLauncher<Intent> launcherAddProducto;

    //
    private ProductosAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private NumberFormat numberFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        productoModelsList = new ArrayList<>();
        numberFormat = NumberFormat.getCurrencyInstance();

        calculaValoresFinales();

        adapter = new ProductosAdapter(productoModelsList, R.layout.producto_view_holder, this);
        layoutManager = new GridLayoutManager(this, 1);
        binding.contentMain.contenedor.setAdapter(adapter);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);

        inicializaLaunchers();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcherAddProducto.launch(new Intent(MainActivity.this, AddProductoActivity.class));
            }
        });
    }

    private void inicializaLaunchers() {
        launcherAddProducto = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null && result.getData().getExtras() != null) {
                                ProductoModel p = (ProductoModel) result.getData().getExtras().getSerializable("PROD");
                                productoModelsList.add(p);
                                adapter.notifyItemInserted(productoModelsList.size()-1);
                                calculaValoresFinales();
                            }

                        }
                    }
                }
        );
    }

    public void calculaValoresFinales() {
        int cantidadTotal = 0;
        float importeTotal = 0;

        for (ProductoModel p: productoModelsList) {
            cantidadTotal += p.getCantidad();
            importeTotal += p.getCantidad() * p.getPrecio();
        }

        binding.contentMain.lblCantidadTotalMain.setText(String.valueOf(cantidadTotal));
        binding.contentMain.lblPrecioTotalMain.setText(numberFormat.format(importeTotal));
    }

}