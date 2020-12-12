package com.ulisesdiaz.myplaces.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.adapters.likes.AdaptadorCustom
import com.ulisesdiaz.myplaces.adapters.likes.ClickListener
import com.ulisesdiaz.myplaces.adapters.likes.LongClickListener
import com.ulisesdiaz.myplaces.foursquare.Foursquare
import com.ulisesdiaz.myplaces.foursquare.models.Venue
import com.ulisesdiaz.myplaces.interfaces.VenuesPorLikeinterface

class LikesActivity : AppCompatActivity() {

    var recyclerListaVenuesPorLikes: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    var toolbar: Toolbar? = null
    var foursquare: Foursquare? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes)

        initToolbar()
        initRecyclerView()

        foursquare = Foursquare(this, this)
        if (foursquare?.hayToken()!!){
            foursquare?.obtenerVenuesDeLike(object: VenuesPorLikeinterface{
                override fun venuesGenerados(venues: ArrayList<Venue>) {
                    implementcionRecyclerView(venues)
                }
            })
        }else{
            foursquare?.mandarInciarSesion()
        }
    }

    private fun initToolbar(){
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.favoritos)
        setSupportActionBar(toolbar)

        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setNavigationOnClickListener { finish() }
    }

    private fun  initRecyclerView(){
        recyclerListaVenuesPorLikes = findViewById<RecyclerView>(R.id.recyclerListaVenuesPorLikes)
        recyclerListaVenuesPorLikes?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        recyclerListaVenuesPorLikes?.layoutManager = layoutManager
    }

    private fun implementcionRecyclerView(venuesLikes: ArrayList<Venue>){
        adaptador = AdaptadorCustom(venuesLikes, object :
            ClickListener {
                override fun onClickListener(view: View, index: Int) {

                    val venueToJosn = Gson()
                    val venueActualString = venueToJosn.toJson(venuesLikes[index])
                    val intent = Intent(applicationContext, DetalleVenueActivity::class.java)
                    intent.putExtra(PantallaPrincipalActivity.VENUE_ACTUAL, venueActualString)
                    startActivity(intent)

                }
            }, object : LongClickListener {
                override fun longClickListener(view: View, index: Int) {

                }
            })
        recyclerListaVenuesPorLikes?.adapter = adaptador
    }
}