package com.abarrotescasavargas.corporativo.FunGenerales;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EspacioEntreFichasItemDecoration extends RecyclerView.ItemDecoration {
    private final int espacioVertical;

    public EspacioEntreFichasItemDecoration(Context context, int espacioVertical) {
        this.espacioVertical = espacioVertical;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        // Establecer el espacio vertical entre las fichas
        outRect.top = espacioVertical;
    }
}
