package com.ulisesdiaz.myplaces.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationResult
import com.google.gson.Gson
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.adapters.venue.AdaptadorCustom
import com.ulisesdiaz.myplaces.adapters.venue.ClickListener
import com.ulisesdiaz.myplaces.adapters.venue.LongClickListener
import com.ulisesdiaz.myplaces.foursquare.Foursquare
import com.ulisesdiaz.myplaces.foursquare.models.Category
import com.ulisesdiaz.myplaces.foursquare.models.Venue
import com.ulisesdiaz.myplaces.interfaces.ObtenerVenuesInterface
import com.ulisesdiaz.myplaces.interfaces.UbicacionListener
import com.ulisesdiaz.myplaces.utils.Ubicacion

class VenuesPorCategoriaActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null

    var recyclerListaLugares: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    var ubicacion: Ubicacion? = null
    var foursquare: Foursquare? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venues_por_categoria)
        initToolbar()
        initRecyclerView()

        val categoriaActualString = intent.getStringExtra(CategoriasActivity.CATEGORIA_ACTUAL)
        val gson = Gson()
        val categoriaActual = gson.fromJson(categoriaActualString, Category::class.java)

        foursquare = Foursquare(this, this)
        if (foursquare?.hayToken()!!){
            ubicacion = Ubicacion(this, object: UbicacionListener {
                override fun ubicacionResponse(locationResult: LocationResult) {

                    val lat = locationResult.lastLocation.latitude.toString()
                    val lon = locationResult.lastLocation.longitude.toString()
                    val categoryId = categoriaActual.id

                    foursquare?.obtenerVenues(lat, lon,categoryId, object: ObtenerVenuesInterface {
                        override fun venuesGenerados(venues: ArrayList<Venue>) {
                            implementcionRecyclerView(venues)
                        }
                    })
                }
            })
        }
    }

    private fun initToolbar(){
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
    }

    private fun  initRecyclerView(){
        recyclerListaLugares = findViewById<RecyclerView>(R.id.recyclerListaVenuesPorCategorias)
        recyclerListaLugares?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        recyclerListaLugares?.layoutManager = layoutManager
    }

    private fun implementcionRecyclerView(lugares: ArrayList<Venue>){
        adaptador = AdaptadorCustom(lugares, object : ClickListener {
            override fun onClickListener(view: View, index: Int) {
                val venueToJosn = Gson()
                val venueActualString = venueToJosn.toJson(lugares[index])
                val intent = Intent(applicationContext, DetalleVenueActivity::class.java)
                intent.putExtra(PantallaPrincipalActivity.VENUE_ACTUAL, venueActualString)
                startActivity(intent)

            }
        }, object : LongClickListener {
            override fun longClickListener(view: View, index: Int) {

            }
        })
        recyclerListaLugares?.adapter = adaptador
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        ubicacion?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        ubicacion?.inicializarUbicacion()
    }

    override fun onPause() {
        super.onPause()
        ubicacion?.detenerActualizacionUbicacion()
    }


}