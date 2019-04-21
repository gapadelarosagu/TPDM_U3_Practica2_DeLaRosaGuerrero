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

public class Main6Activity extends AppCompatActivity {
    EditText nombre, autor, ano, tecnica;
    Button regresa, modifica, elimina;
    CollectionReference pinturas;
    FirebaseFirestore servicioFirestore;
    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        servicioFirestore= FirebaseFirestore.getInstance();
        pinturas=servicioFirestore.collection("pinturas");
        datos=getIntent().getExtras();
        nombre = findViewById(R.id.nombre);
        autor = findViewById(R.id.autor);
        ano = findViewById(R.id.ano);
        tecnica = findViewById(R.id.tecnica);
        nombre.setText(datos.get("nombre").toString());
        autor.setText(datos.get("autor").toString());
        ano.setText(datos.get("anio").toString());
        tecnica.setText(datos.get("tecnica").toString());
        regresa = findViewById(R.id.regresa);
        modifica = findViewById(R.id.modifica);
        elimina = findViewById(R.id.elimina);
        modifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar_dato();
            }
        });
        elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar_dato();
            }
        });
        regresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main6Activity.this,Main4Activity.class));
                finish();
            }
        });
    }
    private void actualizar_dato() {
        pinturas.document(datos.get("id").toString()).update(obtenerCampos()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error al actualizar");
                startActivity(new Intent(Main6Activity.this,Main4Activity.class));
                finish();

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensaje("Se actualizo correctamente");
                startActivity(new Intent(Main6Activity.this,Main4Activity.class));
                finish();
            }
        });
    }
    private void eliminar_dato() {
        pinturas.document(datos.get("id").toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensaje("Se elimino correctamente");
                startActivity(new Intent(Main6Activity.this,Main4Activity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error al eliminar");
                startActivity(new Intent(Main6Activity.this,Main4Activity.class));
                finish();
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
