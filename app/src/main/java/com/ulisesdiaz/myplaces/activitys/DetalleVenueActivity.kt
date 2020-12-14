package com.ulisesdiaz.myplaces.activitys

import android.content.DialogInterface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.ulisesdiaz.myplaces.GridDetalleVenue.AdaptadorGridView
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.foursquare.Foursquare
import com.ulisesdiaz.myplaces.foursquare.Rejilla
import com.ulisesdiaz.myplaces.foursquare.models.Venue
import java.net.URLEncoder
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetalleVenueActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null

    var btnCheckin: Button? = null
    var btnLike: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_venue)
        val imgFoto = findViewById<ImageView>(R.id.imgFoto)
        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtState = findViewById<TextView>(R.id.txtState)
        val txtCountry = findViewById<TextView>(R.id.txtCountry)

        val rejilla = findViewById<GridView>(R.id.gridRejilla)
        btnCheckin = findViewById(R.id.btnCheckin)
        btnLike = findViewById(R.id.btnLike)

        val venueActualString = intent.getStringExtra(PantallaPrincipalActivity.VENUE_ACTUAL)
        val gson = Gson()
        val venueActual = gson.fromJson(venueActualString, Venue::class.java)

        initToolbar(venueActual.name)

        Picasso.get()
            .load(venueActual.imagePreview)
            .placeholder(R.drawable.placeholder_venue)
            .into(imgFoto)

        val  listaRejilla = ArrayList<Rejilla>()
        listaRejilla.add(Rejilla(venueActual.name, R.drawable.ic_categories, ContextCompat.getColor(this, R.color.teal_200)))
        listaRejilla.add(Rejilla(String.format("%s checkins", NumberFormat.getNumberInstance(Locale.US).format(venueActual.stats?.checkinsCount)), R.drawable.ic_location_rejilla, ContextCompat.getColor(this, R.color.teal_700)))
        listaRejilla.add(Rejilla(String.format("%s usuarios", NumberFormat.getNumberInstance(Locale.US).format(venueActual.stats?.usersCount)), R.drawable.ic_usuarios_rejilla, ContextCompat.getColor(this, R.color.purple_500)))
        listaRejilla.add(Rejilla(String.format("%s tips", NumberFormat.getNumberInstance(Locale.US).format(venueActual.stats?.tipCount)), R.drawable.ic_tips_rejilla, ContextCompat.getColor(this, R.color.purple_200)))

        val adaptador = AdaptadorGridView(this, listaRejilla)
        rejilla.adapter = adaptador

        txtNombre.text = venueActual.name
        txtState.text = venueActual.location?.state
        txtCountry.text = venueActual.location?.country

        val foursquare = Foursquare(this, DetalleVenueActivity())
        if (foursquare.hayToken()){
            btnCheckin?.setOnClickListener {
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

            btnLike?.setOnClickListener {
                foursquare.nuevoLike(venueActual.id)
            }
        }else{
            foursquare?.mandarInciarSesion()
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