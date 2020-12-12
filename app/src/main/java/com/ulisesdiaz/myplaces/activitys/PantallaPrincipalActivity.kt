package com.ulisesdiaz.myplaces.activitys

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import com.ulisesdiaz.myplaces.foursquare.models.Venue
import com.ulisesdiaz.myplaces.interfaces.ObtenerVenuesInterface
import com.ulisesdiaz.myplaces.interfaces.UbicacionListener
import com.ulisesdiaz.myplaces.utils.Ubicacion

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
        initRecyclerView()

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
        }else{
            foursquare?.mandarInciarSesion()
        }
    }

    private fun initToolbar(){
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)

        actionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun  initRecyclerView(){
        recyclerListaLugares = findViewById<RecyclerView>(R.id.recyclerListaLugares)
        recyclerListaLugares?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        recyclerListaLugares?.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.menu_categorias ->{
                val intent = Intent(this, CategoriasActivity::class.java)
                startActivity(intent)
                return  true
            }

            R.id.menu_favoritos ->{
                val intent = Intent(this, LikesActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_perfil ->{
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.menu_cerrar_sesion ->{
                foursquare?.cerrarSesion()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }

            else ->{
                return super.onOptionsItemSelected(item)
            }
        }

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