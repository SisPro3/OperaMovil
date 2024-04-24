package com.abarrotescasavargas.corporativo.Gastos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abarrotescasavargas.corporativo.Gastos.Gastos;
import com.abarrotescasavargas.corporativo.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AdapterGastos extends RecyclerView.Adapter<AdapterGastos.ViewHolder> {
    private final ArrayList<Gastos> filteredTransferenciasList;
    private List<Gastos> elementos;
    private Context context;
    private String colorBordeDinamico;
    private RecyclerView recyclerView;
    private AdapterView.OnItemClickListener itemClickListener;
    private boolean isCorporativoChecked = false;
    private boolean isDeducibleChecked = false;

    public AdapterGastos(Context context, List<Gastos> elementos, String colorBordeDinamico, boolean isCorporativoChecked, boolean isDeducibleChecked) {
        this.context = context;
        this.elementos = elementos;
        this.colorBordeDinamico = colorBordeDinamico;
        this.isCorporativoChecked = isCorporativoChecked;
        this.isDeducibleChecked = isDeducibleChecked;
        this.filteredTransferenciasList = new ArrayList<>(elementos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gastos_sucursal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gastos elemento = elementos.get(position);
        holder.sucursalTextView.setText(elemento.getSucursal());
        Double monto = elemento.getMonto();
        if (monto == null) {
            holder.montoEditText.setText("");
            holder.montoEditText.setHint("Monto");
        } else {
            holder.montoEditText.setText(String.format("%.2f", monto));
        }
        holder.idRondines.setText(elemento.getIdRondines());
        holder.checkBox.setChecked(elemento.isSelected());
        holder.corporativoCheckBox.setChecked(isCorporativoChecked);
        holder.deducibleCheckBox.setChecked(isDeducibleChecked);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {


                }
            }

        });
        holder.corporativoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                elementos.get(position).setCorporativo(isChecked);
            }
        });

        holder.deducibleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                elementos.get(position).setDeducible(isChecked);
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Gastos gasto = elementos.get(adapterPosition);
                    gasto.setSelected(holder.checkBox.isChecked());
                }
            }
        });
        Drawable background = ContextCompat.getDrawable(context, R.drawable.marco_sucursal);
        if (elemento.getColor() != null && !elemento.getColor().isEmpty()) {
            background.setColorFilter(Color.parseColor(elemento.getColor()), PorterDuff.Mode.SRC_ATOP);
        } else {
            background.setColorFilter(Color.parseColor(colorBordeDinamico), PorterDuff.Mode.SRC_ATOP);
        }
        ViewCompat.setBackground(holder.itemView, background);
    }
    @Override
    public int getItemCount() {
        return elementos.size();
    }
    public List<Gastos> getElementos() {
        return elementos;
    }

    public void agregarElementos(List<Gastos> nuevosElementos) {
        int insertPosition = elementos.size();
        elementos.addAll(nuevosElementos);
        filteredTransferenciasList.addAll(nuevosElementos); // Asegúrate de que filteredTransferenciasList también se actualice.
        notifyItemRangeInserted(insertPosition, nuevosElementos.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements TextWatcher {
        TextView sucursalTextView,idRondines;
        EditText montoEditText;
        CheckBox checkBox, corporativoCheckBox, deducibleCheckBox;
        LinearLayout principal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sucursalTextView = itemView.findViewById(R.id.TextViewSucursal);
            montoEditText = itemView.findViewById(R.id.editTextMonto);
            checkBox = itemView.findViewById(R.id.checkBox);
            corporativoCheckBox = itemView.findViewById(R.id.corporativoCheckBox);
            deducibleCheckBox = itemView.findViewById(R.id.deducibleCheckBox);
            principal = itemView.findViewById(R.id.principal);
            idRondines= itemView.findViewById(R.id.idRondines);
            montoEditText.addTextChangedListener(this);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String montoStr = editable.toString();
                if (!montoStr.isEmpty()) {
                    try {
                        Double nuevoMonto = Double.parseDouble(montoStr);
                        elementos.get(position).setMonto(nuevoMonto);
                    } catch (NumberFormatException e) {
                        elementos.get(position).setMonto(null);
                    }
                } else {
                    elementos.get(position).setMonto(null);
                }
            }
        }

        public void updateMonto(Double nuevoMonto) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                elementos.get(position).setMonto(nuevoMonto);
            }
        }
        public void updateCheckBox(boolean isChecked) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                elementos.get(position).setCheckBoxChecked(isChecked);
            }
        }
    }
    public void filterBySucursal(String selectedSucursal) {
        elementos.clear();

        if (selectedSucursal.isEmpty()) {
            elementos.addAll(filteredTransferenciasList);
        } else {
            for (Gastos gasto : filteredTransferenciasList) {
                if (gasto.getSucursal().toLowerCase().contains(selectedSucursal.toLowerCase())) {
                    elementos.add(gasto);
                }
            }
        }

        notifyDataSetChanged();
    }


    public void selectAll(boolean isChecked) {
        for (Gastos gastos : elementos) {
            gastos.setSelected(isChecked);
        }
        notifyDataSetChanged();
    }
    public void clear() {
        elementos.clear();
        filteredTransferenciasList.clear();
        notifyDataSetChanged();
    }
}
