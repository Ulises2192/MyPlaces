package com.ulisesdiaz.myplaces.activitys

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.foursquare.Foursquare
import com.ulisesdiaz.myplaces.foursquare.models.User
import com.ulisesdiaz.myplaces.interfaces.UsuariosInterface

class PerfilActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null

    var foursquare: Foursquare? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtFriends = findViewById<TextView>(R.id.txtFriends)
        val txtTips = findViewById<TextView>(R.id.txtTips)
        val txtPhotos = findViewById<TextView>(R.id.txtPhotos)
        val txtCheckins = findViewById<TextView>(R.id.txtCheckins)

        initToolbar()

        foursquare = Foursquare(this, this)
        if (foursquare?.hayToken()!!){
            foursquare?.obtenerUsuarioActual(object : UsuariosInterface {
                override fun obtenerUsuarioActual(usuario: User) {
                    txtNombre.text = String.format("%s %s", usuario.firstName, usuario.lastName)
                    txtFriends.text = String.format("%d %s", usuario.friends?.count, getString(R.string.friends))
                    txtTips.text = String.format("%d %s", usuario.tips?.count, getString(R.string.tips))
                    txtPhotos.text = String.format("%d %s", usuario.photos?.count, getString(R.string.photos))
                    txtCheckins.text = String.format("%d %s", usuario.checkins?.count, getString(R.string.checkins))
                }
            })
        }else{
            foursquare?.mandarInciarSesion()
        }
    }

    private fun initToolbar(){
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setTitle(R.string.perfil)
        setSupportActionBar(toolbar)

        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setNavigationOnClickListener { finish() }

    }
}