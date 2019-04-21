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

public class Main2Activity extends AppCompatActivity {
    EditText nombre, autor, ano, tecnica;
    Button inserta,regresar;
    CollectionReference pinturas;
    FirebaseFirestore servicioFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nombre = findViewById(R.id.nombre);
        autor = findViewById(R.id.autor);
        ano = findViewById(R.id.ano);
        tecnica = findViewById(R.id.tecnica);
        inserta = findViewById(R.id.btninserta);
        regresar=findViewById(R.id.regresapintura);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this,Main4Activity.class));
            }
        });
        servicioFirestore= FirebaseFirestore.getInstance();
        pinturas=servicioFirestore.collection("pinturas");
        inserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserta.setEnabled(false);
                insertar_datos();
            }
        });
    }
    private void insertar_datos() {
        servicioFirestore.collection("pinturas").add(obtenerCampos()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                mensaje("Se ha insertado con exito");
                Intent i=new Intent(Main2Activity.this,Main4Activity.class);
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
        data.put("nombre",nombre.getText().toString());
        data.put("anio",Integer.parseInt(ano.getText().toString()));
        data.put("tecnica",tecnica.getText().toString());
        data.put("autor",autor.getText().toString());
        return data;
    }
    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
