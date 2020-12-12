package com.ulisesdiaz.myplaces.activitys

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.foursquare.Foursquare
import com.ulisesdiaz.myplaces.foursquare.models.Venue
import java.net.URLEncoder

class DetalleVenueActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null

    var btnCheckin: Button? = null
    var btnLike: Button? = null

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
        btnCheckin = findViewById(R.id.btnCheckin)
        btnLike = findViewById(R.id.btnLike)


        val venueActualString = intent.getStringExtra(PantallaPrincipalActivity.VENUE_ACTUAL)
        val gson = Gson()
        val venueActual = gson.fromJson(venueActualString, Venue::class.java)

        initToolbar(venueActual.name)

        txtNombre.text = venueActual.name
        txtState.text = venueActual.location?.state
        txtCountry.text = venueActual.location?.country
        txtCategory.text = venueActual.categories?.get(0)?.name
        txtCheckins.text = venueActual.stats?.checkinsCount.toString()
        txtUsers.text = venueActual.stats?.usersCount.toString()
        txtTips.text = venueActual.stats?.tipCount.toString()

        val foursquare = Foursquare(this, DetalleVenueActivity())


        btnCheckin?.setOnClickListener {
            if (foursquare.hayToken()){
                val editMensaje = EditText(this)
                editMensaje.hint = "Holla"

                AlertDialog.Builder(this)
                    .setTitle("Nuevo checkin")
                    .setMessage("Ingresa un mensaje")
                    .setView(editMensaje)
                    .setPositiveButton("Checkin", DialogInterface.OnClickListener { dialog, which ->
                        val mensaje = URLEncoder.encode(editMensaje.text.toString(), "UTF-8")
                        foursquare.nuevoCheckin(venueActual.id, venueActual.location!!, mensaje)
                    })
                    .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which ->  })
                    .show()
            }
        }

        btnLike?.setOnClickListener {
            if (foursquare.hayToken()){
                foursquare.nuevoLike(venueActual.id)
            }
        }
    }

    private fun initToolbar(categoria: String){
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(categoria)
        setSupportActionBar(toolbar)

        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar?.setNavigationOnClickListener { finish() }
    }


}