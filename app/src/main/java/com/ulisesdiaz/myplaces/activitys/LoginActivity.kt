package com.ulisesdiaz.myplaces.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.foursquare.Foursquare


class LoginActivity : AppCompatActivity() {

    var foursquare: Foursquare? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        foursquare = Foursquare(this, PantallaPrincipalActivity())
        if (foursquare?.hayToken()!!){
            foursquare?.navegarSiguienteActividad()
        }

        btnLogin.setOnClickListener {
            foursquare?.iniciarSesion()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            foursquare?.validarActivityResult(requestCode, resultCode, data)
    }

}