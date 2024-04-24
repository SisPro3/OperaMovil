package com.abarrotescasavargas.corporativo;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.abarrotescasavargas.corporativo.Comercial.ActivityDevolucion;
import com.abarrotescasavargas.corporativo.DataBases.DbHelper;
import com.abarrotescasavargas.corporativo.DataBases.OperaMovilContract;
import com.abarrotescasavargas.corporativo.FunGenerales.Globales;
import com.abarrotescasavargas.corporativo.FunGenerales.HistoricoGenerico;
import com.abarrotescasavargas.corporativo.FunGenerales.MenuNuevaHistorico;
import com.abarrotescasavargas.corporativo.FunGenerales.PreLoader;
import com.abarrotescasavargas.corporativo.Gastos.ActivityGastos;
import com.abarrotescasavargas.corporativo.Gastos.Inventarios.ActivityGastosInventarios;
import com.abarrotescasavargas.corporativo.Login.LoginRepository;
import com.abarrotescasavargas.corporativo.Login.ObjLogin;
import com.abarrotescasavargas.corporativo.Login.PeticionLogin;
import com.abarrotescasavargas.corporativo.Login.RetrofitLogin;
import com.abarrotescasavargas.corporativo.Restablecer.ActivityRestablecer;
import com.abarrotescasavargas.corporativo.Transferencias.ActivityTransferenciasHistorico;
import com.abarrotescasavargas.corporativo.Transferencias.ActivityTransferenciasPrincipal;
import com.abarrotescasavargas.corporativo.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private String perfilUsuario;
    DrawerLayout drawerLayout;
    NavController navController;
    NavigationView navigationView;
    View headerView;
    TextView textViewUsername;
    TextView textViewPerfil;
    LoginRepository loginRepository ;
    private static DbHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setup();
        events();
    }
    private void setup()
    {
        loginRepository= new LoginRepository(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        perfilUsuario = loginRepository.getPerfil("US_PERFIL");
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        mostrarOcultarOpcionesDelMenu(navigationView.getMenu());
        drawerLayout = findViewById(R.id.drawer_layout);

        headerView = navigationView.getHeaderView(0);
        textViewUsername = headerView.findViewById(R.id.nombreUser);
        textViewUsername.setText(loginRepository.getPerfil("US_CVEUSR"));

        textViewPerfil = headerView.findViewById(R.id.perfilUser);
        textViewPerfil.setText(loginRepository.getPerfil("US_PERFIL"));

    }
    private void events()
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = new Intent(MainActivity.this, MenuNuevaHistorico.class);
                Intent nuevoButtonIntent , historicoButtonIntent = null;
                if (id == R.id.nav_gastos_contabilidad ) {
                    nuevoButtonIntent=new Intent(MainActivity.this, ActivityGastos.class);

                    historicoButtonIntent=new Intent(MainActivity.this, HistoricoGenerico.class);
                    historicoButtonIntent.putExtra("origen", Globales.contabilidad);

                    intent.putExtra("backgroundResource", R.drawable.fondo_contabilidad);
                    intent.putExtra("textoBotonNuevo", "Nuevo");
                    intent.putExtra("textoBotonHistorico", "Historico");
                    intent.putExtra("nuevoButtonAction", nuevoButtonIntent);
                    intent.putExtra("historicoButtonAction", historicoButtonIntent);

                } else if (id == R.id.nav_transferencia_tesoreria) {
                    intent = new Intent(MainActivity.this, ActivityTransferenciasPrincipal.class);
                }else if (id == R.id.nav_gastos_inventarios) {
                    nuevoButtonIntent=new Intent(MainActivity.this, ActivityGastosInventarios.class);

                    historicoButtonIntent=new Intent(MainActivity.this, HistoricoGenerico.class);
                    historicoButtonIntent.putExtra("origen", Globales.inventarios);

                    intent.putExtra("backgroundResource", R.drawable.fondo_inventario);
                    intent.putExtra("textoBotonNuevo", "Nuevo");
                    intent.putExtra("textoBotonHistorico", "Historico");
                    intent.putExtra("nuevoButtonAction", nuevoButtonIntent);
                    intent.putExtra("historicoButtonAction", historicoButtonIntent);
                }
                else if (id == R.id.nav_devolucion_comercial) {
                    nuevoButtonIntent=new Intent(MainActivity.this, ActivityDevolucion.class);

                    historicoButtonIntent=new Intent(MainActivity.this, HistoricoGenerico.class);
                    historicoButtonIntent.putExtra("origen", Globales.comercial);

                    intent.putExtra("backgroundResource", R.drawable.fondo_inventario);
                    intent.putExtra("textoBotonNuevo", "Nuevo");
                    intent.putExtra("textoBotonHistorico", "Historico");
                    intent.putExtra("nuevoButtonAction", nuevoButtonIntent);
                    intent.putExtra("historicoButtonAction", historicoButtonIntent);
                    intent.putExtra("mantenimiento", "mantenimiento");
                }
                if (intent != null) {
                    startActivity(intent);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            dbHelper = new DbHelper(MainActivity.this);

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.delete(OperaMovilContract.USUARIO.Table, null, null);
            finish();
            Intent intent = new Intent(MainActivity.this, Principal.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void mostrarOcultarOpcionesDelMenu(Menu menu) {
        MenuItem itemContabilidad = menu.findItem(R.id.nav_contabilidad);
        MenuItem itemTesoreria = menu.findItem(R.id.nav_tesoreria);
        MenuItem itemInventarios = menu.findItem(R.id.nav_inventarios);
        MenuItem itemRecursosHumanos = menu.findItem(R.id.nav_recursos_humanos);
        MenuItem itemComercial = menu.findItem(R.id.nav_comercial);
        setVisibleItems(false, itemContabilidad, itemTesoreria, itemInventarios, itemRecursosHumanos, itemComercial);
        switch (perfilUsuario) {
            case "ADMIN":
                setVisibleItems(true, itemContabilidad, itemTesoreria, itemInventarios, itemRecursosHumanos, itemComercial);
                break;
            case "CONTABILIDAD":
                setVisibleItems(true, itemContabilidad);
                break;
            case "TESORERIA":
                setVisibleItems(true, itemTesoreria);
                break;
            case "INVENTARIO":
                setVisibleItems(true, itemInventarios);
                break;
            case "RECURSOS HUMANOS":
                setVisibleItems(true, itemRecursosHumanos);
                break;
            case "COMERCIAL":
                setVisibleItems(true, itemComercial);
                break;
            default:
                setVisibleItems(false, itemContabilidad, itemTesoreria, itemInventarios, itemRecursosHumanos, itemComercial);
                break;
        }
    }
    private void setVisibleItems(boolean visible, MenuItem... items) {
        for (MenuItem item : items) {
            item.setVisible(visible);
        }
    }



}
