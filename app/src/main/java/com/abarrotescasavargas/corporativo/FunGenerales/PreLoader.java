package com.abarrotescasavargas.corporativo.FunGenerales;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.abarrotescasavargas.corporativo.R;


public class PreLoader {
    private ProgressDialogFragment progressDialogFragment;

    public PreLoader() {
        // Constructor vacío
    }

    public void showProgressDialog(Context context, String message) {
        progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.setMessage(message);

        if (context instanceof AppCompatActivity) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            progressDialogFragment.show(fragmentManager, "ProgressDialog");
        }
    }

    public void dismissProgressDialog() {
        if (progressDialogFragment != null) {
            progressDialogFragment.dismiss();
        }
    }

    public static class ProgressDialogFragment extends DialogFragment {

        private ProgressBar progressBar;
        private TextView textViewMessage;

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.preloader, null);
            progressBar = view.findViewById(R.id.progressBar2);
            textViewMessage = view.findViewById(R.id.txtvAccion);

            Dialog dialog = new Dialog(requireContext(), R.style.TransparentDialog);
            // Aplicando el estilo TransparentDialog al dialog.

            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        }

        public void setMessage(String message) {
            if (textViewMessage != null) {
                textViewMessage.setText(message);
            }
        }
    }
}
