package com.abarrotescasavargas.corporativo.Transferencias;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abarrotescasavargas.corporativo.Gastos.ResponseGetGastos;
import com.abarrotescasavargas.corporativo.R;

import java.util.ArrayList;
import java.util.List;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder> {
    private final ArrayList<ResponseGetTransferencias> filteredTransferenciasList;
    private List<ResponseGetTransferencias> transferenciasList; // Lista de datos
    private Context context;

    public HistoricoAdapter(Context context, List<ResponseGetTransferencias> transferenciasList) {
        this.context = context;
        this.transferenciasList = transferenciasList;
        this.filteredTransferenciasList = new ArrayList<>(transferenciasList);
    }


    // Clase ViewHolder
    public class HistoricoViewHolder extends RecyclerView.ViewHolder {
        private TextView userTextView;
        private TextView userSucursalTextView;
        private TextView fechaCreacionTextView;
        private TextView amountTextView;
        private TextView userCodigoTextView,usuario;
        private TextView saldo;
        LinearLayout reenviarLayout;

        public HistoricoViewHolder(View itemView) {
            super(itemView);
            userTextView = itemView.findViewById(R.id.editTextUser);
            userSucursalTextView = itemView.findViewById(R.id.editTextUserSucursal);
            fechaCreacionTextView = itemView.findViewById(R.id.editTextFechaCreacion);
            amountTextView = itemView.findViewById(R.id.editTextMonto);
            userCodigoTextView = itemView.findViewById(R.id.editTextUserCodigo);
            saldo = itemView.findViewById(R.id.editTextSaldo);
            usuario = itemView.findViewById(R.id.usuarioEdit);

            reenviarLayout = itemView.findViewById(R.id.Reenviar);
            reenviarLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userId = transferenciasList.get(getAdapterPosition()).getUser_id();

                    String mensaje = "Buen día, te *reenvio* la clave para validar la transferencia electrónica de fondos:\n\n"
                            + "Monto: " + amountTextView.getText().toString() + "\n"
                            + "Fecha Generación: " + fechaCreacionTextView.getText().toString() + "\n"
                            + "Código Retiro: *" + userCodigoTextView.getText().toString() + "*\n\n"
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
            for (ResponseGetTransferencias transferencia : filteredTransferenciasList) {
                if (transferencia.getKS_NOMSUC().toLowerCase().contains(selectedSucursal.toLowerCase()) || transferencia.getUser_id().toLowerCase().contains(selectedSucursal.toLowerCase())) {
                    transferenciasList.add(transferencia);
                }
            }
        }

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ficha_transferencias_historico, parent, false);
        return new HistoricoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoViewHolder holder, int position) {
        ResponseGetTransferencias item = transferenciasList.get(position);
        // Configura las vistas con los datos del objeto ResponseGetTransferencias
        holder.userTextView.setText(item.getUser_id());
        holder.usuario.setText(item.getUser_id());
        holder.userSucursalTextView.setText(item.getKS_NOMSUC());
        holder.fechaCreacionTextView.setText(item.getDate_authorized());
        holder.amountTextView.setText("$" + String.valueOf(item.getAmount()));
        holder.userCodigoTextView.setText(item.getCode());
        holder.saldo.setText("$" + item.getSaldo());
    }

    @Override
    public int getItemCount() {
        return transferenciasList.size();
    }
}
