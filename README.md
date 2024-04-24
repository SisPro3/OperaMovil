Alta Usuarios
Basarse en la imagen (diagrama.png)

•	En KEMPLEADOS extraer el KE_ID para ponerlo en USUARIOROL.UR_USERID

•	Vamos a KROLES y extraemos el idKROLES para ponerlo en USUARIOROL.UR_ROLEID

•	EN KEMPLEADOS validamos que tenga el campo user_id y KU_USUARI llenos, en caso de que estén vacíos los extraemos de la tabla  GASTOS.user, y en caso de que no este el registro del personal en esta tabla solo es dar de alta el registro con los datos que pide la tabla

•	Correr el siguiente query cambiando el KE_ID por el de la persona en cuestión y el password por el correcto “update CONTROLPERSONAL.KEMPLEADOS set KE_PASSWORD = aes_encrypt(‘PASSWORD_PERSONALIZADO’,'Refuerz0COVIDx19') where KE_ID = 295;”
