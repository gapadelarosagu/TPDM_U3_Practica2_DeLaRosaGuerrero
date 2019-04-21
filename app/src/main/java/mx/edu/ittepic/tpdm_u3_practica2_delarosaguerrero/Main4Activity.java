package mx.edu.ittepic.tpdm_u3_practica2_delarosaguerrero;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main4Activity extends AppCompatActivity {
    Button regresa;
    ListView listaPint;
    CollectionReference pinturas;
    FirebaseFirestore servicioFirestore;
    List<Map> pinturasLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        regresa = findViewById(R.id.regresa);
        listaPint = findViewById(R.id.listaPint);
        servicioFirestore= FirebaseFirestore.getInstance();
        pinturas=servicioFirestore.collection("pinturas");
        listaPint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (i >= 0) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(Main4Activity.this);
                    alerta.setTitle("Modificacion de datos")
                            .setMessage("Desea modificar/eliminar la pintura " + pinturasLocal.get(i).get("nombre").toString())
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int in) {
                                    Intent ventana=new Intent(Main4Activity.this,Main6Activity.class);
                                    ventana.putExtra("id",pinturasLocal.get(i).get("id").toString());
                                    ventana.putExtra("nombre",pinturasLocal.get(i).get("nombre").toString());
                                    ventana.putExtra("autor",pinturasLocal.get(i).get("autor").toString());
                                    ventana.putExtra("tecnica",pinturasLocal.get(i).get("tecnica").toString());
                                    ventana.putExtra("anio",Integer.parseInt(pinturasLocal.get(i).get("anio").toString()));
                                    startActivity(ventana);
                                }
                            })
                            .setNegativeButton("NO",null).show();
                }
            }
        });
        regresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main4Activity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cargarDatos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opciones,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.insertar:
                Intent insertar = new Intent(this,Main2Activity.class);
                startActivity(insertar);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void cargarDatos(){
        pinturas.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if  (queryDocumentSnapshots.size()<=0){
                    mensaje("No hay datos para mostrar");
                    return;
                }
                pinturasLocal=new ArrayList<>();
                for (QueryDocumentSnapshot otro:queryDocumentSnapshots){
                    Pintura producto=otro.toObject(Pintura.class);
                    Map<String,Object> datos=new HashMap<>();
                    datos.put("nombre",producto.getNombre());
                    datos.put("autor",producto.getAutor());
                    datos.put("anio",producto.getAnio());
                    datos.put("tecnica",producto.getTecnica());
                    datos.put("id",otro.getId());
                    pinturasLocal.add(datos);
                    llenarLista();
                }
            }
        });
    }
    private void llenarLista(){
        String data[]=new String[pinturasLocal.size()];
        for (int i=0;i<data.length;i++){
            String cad=pinturasLocal.get(i).get("nombre").toString();
            data[i]=cad;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Main4Activity.this,android.R.layout.simple_list_item_1,data);
        listaPint.setAdapter(adapter);
    }
    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}

