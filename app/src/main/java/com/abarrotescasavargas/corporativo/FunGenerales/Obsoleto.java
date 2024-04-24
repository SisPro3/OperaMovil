package com.abarrotescasavargas.corporativo.FunGenerales;

import static com.abarrotescasavargas.corporativo.FunGenerales.Dialogo.creaPopUp;

import android.view.View;

import com.abarrotescasavargas.corporativo.Restablecer.ActivityRestablecer;

public class Obsoleto {
//    ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀        ⢠⡾⠛⠛⠳⢦⣄⡀⢀⣀⣀⣀⣠⣤⣤⣄⣀⣀⡀⠀⢀⣤⣶⣦⣤⣶⠛⠛⢷⡄⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⡟⠀⠀⠀⠀⠀⠈⠛⢯⣍⠁⠀⠀⠀⠀⠀⠀⠉⠉⣻⣿⡿⣯⣟⡿⣿⣦⠀⠀⠹⣆⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠻⢧⠀⠀⠀⠀⠀⠀⠀⢸⣿⣟⡿⣧⣟⣿⣻⣿⣤⣀⡀⢻⡄⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⣴⢾⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣯⢿⣽⣿⣿⣿⣟⡿⣿⢿⣿⣿⣷⡀⠀⠀
//            ⠀⠀⠀⠀⠀⣠⡞⠁⢸⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠸⣿⣽⢯⣿⣿⣿⢷⣯⢿⣽⣻⣿⣿⣿⡇⠀⠀
//            ⠀⠀⠀⠀⣰⠏⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠻⣿⣟⣾⣻⣿⣿⣾⣯⣷⣿⣿⣿⣿⡇⠀⠀
//            ⠀⠀⠀⢰⡏⠀⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠛⠛⠁⠈⠉⠛⢿⣿⣽⣷⡿⠁⠀⠀
//            ⠀⠀⠀⡿⠀⠀⠀⠀⠉⠀⠀⠀⠀⠀⠀⠀⢀⣴⠞⠛⠉⠛⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠛⣿⡀⠀⠀⠀
//            ⠀⠀⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⡟⠁⠀⠀⠀⠀⠸⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡇⠀⠀⠀                                Apartado para almacenar codigo
//            ⠘⠒⢿⠷⠶⠤⣤⣀⣀⡀⠀⠀⠀⠀⠀⠋⠀⠀⠀⠀⠀⠀⠀⢻⡄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣤⡤⢾⡗⠒⠒⠂                             que puedo usar mas adelante
//            ⠀⠀⢸⡆⠀⠀⠀⠈⠩⣏⡛⠲⠆⠀⠀⠀⠀⠀⠀⢀⣦⣄⠀⠈⣷⣿⡄⠀⠀⠀⠀⠀⠀⣿⠀⢀⣠⡶⠀⠁⠀⣼⠇⠀⠀⠀
//            ⠀⠀⠈⣧⠀⠀⠀⠀⠀⠙⢿⣄⠀⠀⠀⠀⠀⠀⢰⣿⣿⣿⡆⠀⠘⠛⠀⠀⠀⠀⠀⠀⠀⠹⣿⣿⣿⠃⠀⠚⢛⡟⠛⠒⠀⠀
//            ⠀⠀⠀⢹⣆⣀⣤⡤⠶⠞⠛⠻⣷⣄⠀⠀⠀⠀⢸⣿⣿⣿⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠹⣿⡟⠀⢀⣀⣼⡁⠀⠀⠀⠀
//            ⠀⢠⡴⠞⠻⣍⠀⠀⠀⠀⠀⠀⣹⣿⣧⡀⠀⠀⠀⢿⣿⡿⠃⠀⠀⠀⠀⠀⢠⣴⡶⣤⠀⠀⠀⠀⠀⠀⢠⣾⠿⠿⠷⣦⡀⠀
//            ⠀⠀⠀⠀⠀⠹⣷⠀⠀⠀⣀⡾⠁⠈⢿⣿⣆⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠸⣿⡿⠿⠀⠀⠀⠀⢀⣰⡿⠀⠀⠀⠀⠈⢿⠀
//            ⠀⠀⠀⠀⠀⠀⠘⢳⣤⡾⠋⠀⠀⠀⠈⠻⣿⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣤⣴⣾⣿⣿⠇⠀⠀⠀⠀⠀⣨⡧
//            ⠀⠀⠀⠀⠀⠀⢀⡾⠋⠛⠶⣄⡀⠀⠀⠀⠘⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⠀⠜⢀⡇
//            ⠀⠀⠀⠀⠀⠰⠟⠁⠀⠀⠀⠈⠉⣳⡶⢦⣤⣼⣿⣿⣿⣿⣿⣿⣿⣏⣾⣿⣿⣿⣿⣿⢻⣿⣿⣿⡟⠁⠀⠀⠀⢀⠎⠀⣸⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡾⠋⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣳⣿⣿⣿⣿⣿⣿⢣⡟⣼⣛⣿⡀⠀⠀⠀⠀⡞⠀⣰⠃⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⠟⠁⠀⠀⠀⢸⣟⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⣟⡼⣓⢾⡱⢮⡽⣷⣄⣀⡀⣘⣥⢾⠇⠀⠐
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⣯⣤⣄⣀⣀⠀⢸⣟⡞⡼⣭⣛⠿⣹⢻⠽⣭⢳⡹⢶⣙⢮⡳⣝⣣⢞⣽⠏⠉⠉⡋⠀⡨⠀⠀⠐
//            ⠀⠀⠀⠀⠀⠀⠀⠀⢠⡟⠀⠀⠀⠉⠉⠛⠻⣿⣜⡳⢧⣝⣫⢗⣫⡝⣮⢳⡝⣣⢏⡾⣱⢧⣋⢾⡿⠀⠀⠠⠁⠀⠃⠀⠀⠁
//            ⠀⠀⠀⠀⠀⠀⠀⠀⣿⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣝⡳⣎⡳⣎⢧⡻⡜⣧⡛⣵⢫⢞⡵⢮⣝⣾⠃⠀⠀⡈⠀⠀⠀⠀⠀⠁
//            ⠀⠀⠀⠀⠀⠀⠀⠀⢻⡄⠀⠀⠀⠀⠀⢀⣤⣶⠿⣎⡵⣫⢵⢫⣞⣱⢻⡴⣛⡼⣹⢞⡼⣳⣾⡇⠀⠀⠀⢐⡀⠈⠀⠀⠨⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⣄⡀⠀⣀⣤⡿⣏⣗⢻⣬⢳⡝⣮⢳⢎⣧⢳⡝⡮⣵⢣⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠐⡄⢀⠃⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠹⣿⡵⢫⣜⡳⣎⠷⣹⠖⣯⢺⣬⢳⣾⣵⣿⣿⣿⠟⠋⣽⠃⠀⠀⠀⠀⠀⠀⠁⠈⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⣿⢣⡏⣷⡎⡟⣥⢻⡜⣷⣿⣿⣿⢹⡏⠁⠀⣴⣾⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢻⣮⣝⣶⣭⢳⡝⣎⣷⣧⣷⢾⡏⠀⠙⠛⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠻⣍⠉⠙⠛⠙⠉⠉⠁⢀⡾⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
//            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠛⠲⠶⠶⠖⠖⠚⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀

    // Crea la animacion de que un campo se haga grande y chiquito, usado para el recuperar contraseña pero no se implemento en la version final


//    enviar.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            String codigoText = codigo.getText().toString();
//            String usaurioText = usuario.getText().toString();
//            if (!lleno) {
//
//                if ( !usaurioText.isEmpty()) {
//
//                    lleno = true;
//
//                    codigo.startAnimation(scaleAnimation);
//                    codigo.setEnabled(true);
//                } else {
//
//                    creaPopUp(ActivityRestablecer.this,"Alerta","Coloca un usuario para continuar","OK");
//                }
//            } else {
//
//                if (!codigoText.isEmpty())
//                {
//                    codigo.clearAnimation();
//
//                    presionado = false;
//                    lleno = false;
//                }
//            }
//
//            // Marca el botón como presionado
//            presionado = true;
//        }
//    });



}
