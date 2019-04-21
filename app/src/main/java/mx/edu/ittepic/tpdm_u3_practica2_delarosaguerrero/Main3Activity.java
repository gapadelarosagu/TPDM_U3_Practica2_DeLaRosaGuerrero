package mx.edu.ittepic.tpdm_u3_practica2_delarosaguerrero;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {
    EditText nombreE, autorE, anoE, material;
    Button inserta,regresar;
    CollectionReference esculturas;
    FirebaseFirestore servicioFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        nombreE = findViewById(R.id.nombreE);
        autorE = findViewById(R.id.autorE);
        anoE = findViewById(R.id.anoE);
        material = findViewById(R.id.material);
        inserta = findViewById(R.id.inserta);
        regresar=findViewById(R.id.regresaescultura);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main3Activity.this,Main5Activity.class));
            }
        });
        servicioFirestore= FirebaseFirestore.getInstance();
        esculturas=servicioFirestore.collection("esculturas");
        inserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserta.setEnabled(false);
                insertar_datos();
            }
        });
    }
    private void insertar_datos() {
        esculturas.add(obtenerCampos()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                mensaje("Se ha insertado con exito");
                Intent i=new Intent(Main3Activity.this,Main5Activity.class);
                startActivity(i);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                inserta.setEnabled(true);
                mensaje("Error al insertar");
            }
        });
    }
    private Map<String,Object> obtenerCampos(){
        Map<String,Object> data=new HashMap<>();
        data.put("nombre",nombreE.getText().toString());
        data.put("anio",Integer.parseInt(anoE.getText().toString()));
        data.put("material",material.getText().toString());
        data.put("autor",autorE.getText().toString());
        return data;
    }
    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
