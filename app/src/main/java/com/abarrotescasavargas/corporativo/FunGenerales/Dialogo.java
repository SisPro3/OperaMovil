package com.abarrotescasavargas.corporativo.FunGenerales;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Dialogo {

    public static void creaPopUp(Context context,String titulo,String mensaje, String boton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(mensaje);
        alertDialogBuilder.setPositiveButton(boton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public static void popUpValidar(Context context, String titulo, String mensaje, String botonCerrar, String botonEjecutar, final Runnable accionEjecutar, boolean cierraDesdePrincipal) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(mensaje);

        alertDialogBuilder.setPositiveButton(botonCerrar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton(botonEjecutar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (accionEjecutar != null) {
                    accionEjecutar.run();
                }
                //Se coloco esta validacion para cuando desde la pantalla principal se ponga un finish como en en lcaso de ActvityGastos, donde cierra la pantalla al enviar por whatsapp
                if (cierraDesdePrincipal==false)
                {
                    dialog.dismiss();
                }


            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public static void creaPopUpConCopia(final Context context, String titulo, final String mensaje, String boton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(mensaje);

        // Agrega un botón para copiar el mensaje
        alertDialogBuilder.setNeutralButton("Copiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Copia el mensaje al portapapeles
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Mensaje Copiado", mensaje);
                clipboard.setPrimaryClip(clip);

                // Muestra un mensaje de confirmación
                Toast.makeText(context, "Mensaje copiado al portapapeles", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setPositiveButton(boton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
