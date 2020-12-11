package com.ulisesdiaz.myplaces.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.adapters.categories.AdaptadorCustom
import com.ulisesdiaz.myplaces.adapters.categories.ClickListener
import com.ulisesdiaz.myplaces.adapters.categories.LongClickListener
import com.ulisesdiaz.myplaces.foursquare.Foursquare
import com.ulisesdiaz.myplaces.foursquare.models.Category
import com.ulisesdiaz.myplaces.interfaces.CategoriasVenuesInterfaces

class CategoriasActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null

    var recyclerListaCategorias: RecyclerView? = null
    var adaptador: AdaptadorCustom? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    companion object{
        val CATEGORIA_ACTUAL = "checkins.CategoriaActual"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorias)
        initToolbar()
        initRecyclerView()


        var fqsr = Foursquare(this, CategoriasActivity())
        fqsr.cargarCategorias(object: CategoriasVenuesInterfaces{
            override fun categoriasVenues(categorias: ArrayList<Category>) {
                implementcionRecyclerView((categorias))
            }

        })
    }

    private fun initToolbar(){
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
    }


    private fun  initRecyclerView(){
        recyclerListaCategorias = findViewById<RecyclerView>(R.id.recyclerCategorias)
        recyclerListaCategorias?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        recyclerListaCategorias?.layoutManager = layoutManager
    }

    private fun implementcionRecyclerView(categorias: ArrayList<Category>){
        adaptador =
            com.ulisesdiaz.myplaces.adapters.categories.AdaptadorCustom(categorias, object : ClickListener {
                override fun onClickListener(view: View, index: Int) {

                    val categoriaToJosn = Gson()
                    val categoriaActualString = categoriaToJosn.toJson(categorias[index])
                    val intent = Intent(applicationContext, VenuesPorCategoriaActivity::class.java)
                    intent.putExtra(CATEGORIA_ACTUAL, categoriaActualString)
                    startActivity(intent)

                }
            }, object : LongClickListener {
                override fun longClickListener(view: View, index: Int) {

                }
            })
        recyclerListaCategorias?.adapter = adaptador
    }

}