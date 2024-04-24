package com.abarrotescasavargas.corporativo.FunGenerales;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abarrotescasavargas.corporativo.Gastos.ResponseGetGastos;
import com.abarrotescasavargas.corporativo.R;
import com.abarrotescasavargas.corporativo.Transferencias.ResponseGetTransferencias;

import java.util.ArrayList;
import java.util.List;

public class HistoricoGenericoAdapter extends RecyclerView.Adapter<HistoricoGenericoAdapter.HistoricoViewHolder>{
    private final ArrayList<ResponseGetGastos> filteredTransferenciasList;
    private List<ResponseGetGastos> transferenciasList;
    private Context context;

    public HistoricoGenericoAdapter(Context context, List<ResponseGetGastos> transferenciasList) {
        this.context = context;
        this.transferenciasList = transferenciasList;
        this.filteredTransferenciasList = new ArrayList<>(transferenciasList);
    }


    // Clase ViewHolder
    public class HistoricoViewHolder extends RecyclerView.ViewHolder {
        private TextView userTextView, userSucursalTextView, fechaCreacionTextView,amountTextView,userCodigoTextView, saldo, usuario;
        LinearLayout reenviarLayout,marco;

        public HistoricoViewHolder(View itemView) {
            super(itemView);
            userTextView = itemView.findViewById(R.id.editTextUser);
            userSucursalTextView = itemView.findViewById(R.id.editTextUserSucursal);
            fechaCreacionTextView = itemView.findViewById(R.id.editTextFechaCreacion);
            amountTextView = itemView.findViewById(R.id.editTextMonto);
            userCodigoTextView = itemView.findViewById(R.id.editTextUserCodigo);
            saldo = itemView.findViewById(R.id.editTextSaldo);
            usuario = itemView.findViewById(R.id.usuarioEdit);
            marco = itemView.findViewById(R.id.marco);


            reenviarLayout = itemView.findViewById(R.id.Reenviar);
            reenviarLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = transferenciasList.get(getAdapterPosition()).getUsuario();
                    String pronunciacion = Generales.convertToPronunciation(userCodigoTextView.getText().toString());
                    String mensaje = "Buen día, te *reenvio* la clave para validar la transferencia electrónica de fondos:\n\n"
                            + "Concepto: *" + userTextView.getText().toString() + "*\n"
                            + "Sucursal: *" + userSucursalTextView.getText().toString() + "*\n"
                            + "Monto: *" + amountTextView.getText().toString() + "*\n"
                            + "Fecha Generación: *" + fechaCreacionTextView.getText().toString() + "*\n"
                            + "Código Retiro: *" + userCodigoTextView.getText().toString() + "*\n"
                            + "Pronunciación: *" + pronunciacion + "*\n\n"
                            + "Gracias";

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Mensaje", mensaje);
                    clipboard.setPrimaryClip(clip);

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setPackage("com.whatsapp.w4b");

                    intent.putExtra(Intent.EXTRA_TEXT, mensaje);

                    try {
                        context.startActivity(intent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "WhatsApp no está instalado.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void filterBySucursal(String selectedSucursal) {
        transferenciasList.clear();

        if (selectedSucursal.isEmpty()) {
            transferenciasList.addAll(filteredTransferenciasList);
        } else {
            // Cambia la condición para verificar si el nombre de la sucursal contiene el texto ingresado
            for (ResponseGetGastos transferencia : filteredTransferenciasList) {
                if (transferencia.getSucursal().toLowerCase().contains(selectedSucursal.toLowerCase()) || transferencia.getUsuarioInsert().toLowerCase().contains(selectedSucursal.toLowerCase())) {
                    transferenciasList.add(transferencia);
                }
            }
        }

        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public HistoricoGenericoAdapter.HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ficha_transferencias_historico, parent, false);
        return new HistoricoGenericoAdapter.HistoricoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoViewHolder holder, int position) {
        ResponseGetGastos item = transferenciasList.get(position);
        // Configura las vistas con los datos del objeto ResponseGetTransferencias
        holder.usuario.setText(item.getUsuarioInsert());
        holder.userTextView.setText(item.getUsuario());
        holder.userSucursalTextView.setText(item.getSucursal());
        holder.fechaCreacionTextView.setText(item.getAutorizado());
        holder.amountTextView.setText("$" + String.valueOf(item.getMonto()));
        holder.userCodigoTextView.setText(item.getCodigo());
        holder.saldo.setText("$" + item.getSaldo());

        // Evitar llamadas repetitivas a setSelected()
        if (!holder.userTextView.isSelected()) {
            holder.userTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.userTextView.setMarqueeRepeatLimit(-1); // Para repetición infinita
            holder.userTextView.setSelected(true);
        }

        // Establecer el fondo según la condición
        if (Double.parseDouble(item.getSaldo()) > 0) {
            holder.marco.setBackgroundResource(R.drawable.marco_gasto_disponible); // Fondo rojo
        } else {
            holder.marco.setBackgroundResource(R.drawable.marco_gasto_quemado); // Fondo verde
        }
    }

    @Override
    public int getItemCount() {
        return transferenciasList.size();
    }
}
