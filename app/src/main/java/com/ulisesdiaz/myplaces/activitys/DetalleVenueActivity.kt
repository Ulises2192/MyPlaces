package com.ulisesdiaz.myplaces.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.ulisesdiaz.myplaces.foursquare.Foursquare
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.foursquare.models.User
import com.ulisesdiaz.myplaces.foursquare.models.Venue
import com.ulisesdiaz.myplaces.interfaces.UsuariosInterface

class DetalleVenueActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_venue)
        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtState = findViewById<TextView>(R.id.txtState)
        val txtCountry = findViewById<TextView>(R.id.txtCountry)
        val txtCategory = findViewById<TextView>(R.id.txtCategory)
        val txtCheckins = findViewById<TextView>(R.id.txtCheckins)
        val txtUsers = findViewById<TextView>(R.id.txtUsers)
        val txtTips = findViewById<TextView>(R.id.txtTips)


        val venueActualString = intent.getStringExtra(PantallaPrincipalActivity.VENUE_ACTUAL)
        val gson = Gson()
        val venueActual = gson.fromJson(venueActualString, Venue::class.java)

        txtNombre.text = venueActual.name
        txtState.text = venueActual.location?.state
        txtCountry.text = venueActual.location?.country
        txtCategory.text = venueActual.categories?.get(0)?.name
        txtCheckins.text = venueActual.stats?.checkinsCount.toString()
        txtUsers.text = venueActual.stats?.usersCount.toString()
        txtTips.text = venueActual.stats?.tipCount.toString()

        val foursquare = Foursquare(this, DetalleVenueActivity())
        if (foursquare.hayToken()){
            //foursquare.nuevoCheckin(venueActual.id, venueActual.location!!, "Hola%20mundo")
            foursquare.obtenerUsuarioActual(object: UsuariosInterface {
                override fun obtenerUsuarioActual(usuario: User) {
                    Toast.makeText(applicationContext, usuario.firstName, Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}