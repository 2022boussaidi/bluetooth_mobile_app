package com.cambotutorial.sovary.qrscanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Button Button2 = findViewById(R.id.button2);
        Button2.setBackgroundColor(Color.WHITE);

        Button Button3 = findViewById(R.id.button3);
        Button3.setBackgroundColor(Color.WHITE);
        // Ajout d'un listener pour le bouton
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code à exécuter lorsque le bouton est cliqué
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);



            }
        });

        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code à exécuter lorsque le bouton est cliqué
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);



            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Récupération de l'ID de l'item cliqué
        int id = item.getItemId();

        // Vérification de l'ID de l'item cliqué
        if (id == R.id.QR) {
            // Le clic a été effectué sur l'item "mon_item_de_menu"
            // Ajouter ici le code à exécuter lorsque l'item est cliqué
            Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
            startActivity(intent);

            return true;
        }

        // Si l'ID de l'item cliqué ne correspond à aucun item du menu,
        // on appelle la méthode de la classe parente
        return super.onOptionsItemSelected(item);
    }








}