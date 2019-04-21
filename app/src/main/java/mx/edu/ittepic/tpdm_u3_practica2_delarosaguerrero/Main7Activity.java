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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Main7Activity extends AppCompatActivity {
    EditText nombreE, autorE, anoE, material;
    Button regresaE, modificaE, eliminaE;
    CollectionReference esculturas;
    FirebaseFirestore servicioFirestore;
    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        servicioFirestore= FirebaseFirestore.getInstance();
        esculturas=servicioFirestore.collection("esculturas");
        datos=getIntent().getExtras();
        nombreE = findViewById(R.id.nombreE);
        autorE = findViewById(R.id.autorE);
        anoE = findViewById(R.id.anoE);
        material = findViewById(R.id.material);
        regresaE = findViewById(R.id.regresaE);
        modificaE = findViewById(R.id.modificaE);
        eliminaE = findViewById(R.id.eliminaE);
        nombreE.setText(datos.get("nombre").toString());
        autorE.setText(datos.get("autor").toString());
        anoE.setText(datos.get("anio").toString());
        material.setText(datos.get("material").toString());
        modificaE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar_dato();
            }
        });
        eliminaE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar_dato();
            }
        });
        regresaE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main7Activity.this,Main5Activity.class));
                finish();
            }
        });
    }
    private void actualizar_dato() {
        esculturas.document(datos.get("id").toString()).update(obtenerCampos()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error al actualizar");
                startActivity(new Intent(Main7Activity.this,Main5Activity.class));
                finish();

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensaje("Se actualizo correctamente");
                startActivity(new Intent(Main7Activity.this,Main5Activity.class));
                finish();
            }
        });
    }
    private void eliminar_dato() {
        esculturas.document(datos.get("id").toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensaje("Se elimino correctamente");
                startActivity(new Intent(Main7Activity.this,Main5Activity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error al eliminar");
                startActivity(new Intent(Main7Activity.this,Main5Activity.class));
                finish();
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
