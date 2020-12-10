package com.ulisesdiaz.myplaces.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationResult
import com.google.gson.Gson
import com.ulisesdiaz.myplaces.foursquare.Foursquare
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.utils.Ubicacion
import com.ulisesdiaz.myplaces.adapters.AdaptadorCustom
import com.ulisesdiaz.myplaces.adapters.ClickListener
import com.ulisesdiaz.myplaces.adapters.LongClickListener
import com.ulisesdiaz.myplaces.foursquare.models.Venue
import com.ulisesdiaz.myplaces.interfaces.ObtenerVenuesInterface
import com.ulisesdiaz.myplaces.interfaces.UbicacionListener

class PantallaPrincipalActivity : AppCompatActivity() {

    var ubicacion: Ubicacion? = null
    var foursquare: Foursquare? = null

    var recyclerListaLugares: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    var toolbar: Toolbar? = null

    companion object{
        val VENUE_ACTUAL = "myplaces.pantallaPrincipal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_principal)
        initToolbar()
        recyclerListaLugares = findViewById<RecyclerView>(R.id.recyclerListaLugares)
        recyclerListaLugares?.setHasFixedSize(true)


        layoutManager = LinearLayoutManager(this)
        recyclerListaLugares?.layoutManager = layoutManager


        foursquare = Foursquare(this, this)
        if (foursquare?.hayToken()!!){
            ubicacion = Ubicacion(this, object: UbicacionListener {
                override fun ubicacionResponse(locationResult: LocationResult) {

                    val lat = locationResult.lastLocation.latitude.toString()
                    val lon = locationResult.lastLocation.longitude.toString()

                    foursquare?.obtenerVenues(lat, lon, object: ObtenerVenuesInterface {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun implementcionRecyclerView(lugares: ArrayList<Venue>){
        adaptador = AdaptadorCustom(lugares, object : ClickListener {
            override fun onClickListener(view: View, index: Int) {
                //Toast.makeText(applicationContext, lugares[index].name, Toast.LENGTH_SHORT).show()
                val venueToJosn = Gson()
                val venueActualString = venueToJosn.toJson(lugares[index])
                val intent = Intent(applicationContext, DetalleVenueActivity::class.java)
                intent.putExtra(VENUE_ACTUAL, venueActualString)
                startActivity(intent)

            }
        }, object : LongClickListener {
            override fun longClickListener(view: View, index: Int) {
                /*if (!isActionMode){
                    startSupportActionMode(callBack)
                    isActionMode = true
                    adaptador?.selecionarItem(index)

                }else{
                    // Hacer selecciones o deselecciones
                    adaptador?.selecionarItem(index)
                }
                actionMode?.title = adaptador?.obtenerElelementosSelecionados().toString() + " Seleccionados"
                */
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