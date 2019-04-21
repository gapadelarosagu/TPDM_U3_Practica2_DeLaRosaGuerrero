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

public class Main5Activity extends AppCompatActivity {
    Button regresa;
    ListView lista;
    CollectionReference esculturas;
    FirebaseFirestore servicioFirestore;
    List<Map> esculturasLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        regresa = findViewById(R.id.regresa);
        lista = findViewById(R.id.listaEsc);
        servicioFirestore= FirebaseFirestore.getInstance();
        esculturas=servicioFirestore.collection("esculturas");
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (i >= 0) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(Main5Activity.this);
                    alerta.setTitle("Modificacion de datos")
                            .setMessage("Desea modificar/eliminar la escultura " + esculturasLocal.get(i).get("nombre").toString())
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int in) {
                                    Intent ventana=new Intent(Main5Activity.this,Main7Activity.class);
                                    ventana.putExtra("id",esculturasLocal.get(i).get("id").toString());
                                    ventana.putExtra("nombre",esculturasLocal.get(i).get("nombre").toString());
                                    ventana.putExtra("autor",esculturasLocal.get(i).get("autor").toString());
                                    ventana.putExtra("material",esculturasLocal.get(i).get("material").toString());
                                    ventana.putExtra("anio",Integer.parseInt(esculturasLocal.get(i).get("anio").toString()));
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
                Intent i=new Intent(Main5Activity.this,MainActivity.class);
                startActivity(i);
            }
        });
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
                Intent insertar = new Intent(this,Main3Activity.class);
                startActivity(insertar);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cargarDatos();
    }

    private void cargarDatos(){
        esculturas.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if  (queryDocumentSnapshots.size()<=0){
                    mensaje("No hay datos para mostrar");
                    return;
                }
                esculturasLocal=new ArrayList<>();
                for (QueryDocumentSnapshot otro:queryDocumentSnapshots){
                    Escultura escultura=otro.toObject(Escultura.class);
                    Map<String,Object> datos=new HashMap<>();
                    datos.put("nombre",escultura.getNombre());
                    datos.put("autor",escultura.getAutor());
                    datos.put("anio",escultura.getAnio());
                    datos.put("material",escultura.getMaterial());
                    datos.put("id",otro.getId());
                    esculturasLocal.add(datos);
                    llenarLista();
                }
            }
        });
    }
    private void llenarLista(){
        String data[]=new String[esculturasLocal.size()];
        for (int i=0;i<data.length;i++){
            String cad=esculturasLocal.get(i).get("nombre").toString();
            data[i]=cad;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Main5Activity.this,android.R.layout.simple_list_item_1,data);
        lista.setAdapter(adapter);
    }
    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}
