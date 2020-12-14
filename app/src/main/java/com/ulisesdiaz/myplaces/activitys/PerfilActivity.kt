package com.ulisesdiaz.myplaces.activitys

import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.ulisesdiaz.myplaces.GridDetalleVenue.AdaptadorGridView
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.foursquare.Foursquare
import com.ulisesdiaz.myplaces.foursquare.Rejilla
import com.ulisesdiaz.myplaces.foursquare.models.User
import com.ulisesdiaz.myplaces.interfaces.UsuariosInterface
import de.hdodenhof.circleimageview.CircleImageView
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class PerfilActivity : AppCompatActivity() {

    var toolbar: Toolbar? = null

    var foursquare: Foursquare? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        val imgPerfil = findViewById<CircleImageView>(R.id.imgProfile)
        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtFriends = findViewById<TextView>(R.id.txtFriends)
        val txtTips = findViewById<TextView>(R.id.txtTips)
        val txtPhotos = findViewById<TextView>(R.id.txtPhotos)
        val txtCheckins = findViewById<TextView>(R.id.txtCheckins)

        val rejilla = findViewById<GridView>(R.id.gridRejilla)
        val  listaRejilla = ArrayList<Rejilla>()


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

                    Picasso.get()
                        .load(usuario.photo?.urlIcono)
                        .into(imgPerfil)

                    listaRejilla.add(
                        Rejilla(String.format("%s %s", NumberFormat.getNumberInstance(
                            Locale.US).format(usuario.photos?.count), getString(R.string.photos)),
                            R.drawable.ic_photo_rejilla, ContextCompat.getColor(applicationContext, R.color.teal_700))
                    )
                    listaRejilla.add(
                        Rejilla(String.format("%s %s", NumberFormat.getNumberInstance(
                            Locale.US).format(usuario.checkins?.count), getString(R.string.checkins)),
                            R.drawable.ic_location_rejilla, ContextCompat.getColor(applicationContext, R.color.teal_700))
                    )
                    listaRejilla.add(
                        Rejilla(String.format("%s %s", NumberFormat.getNumberInstance(
                            Locale.US).format(usuario.friends?.count), getString(R.string.friends)),
                            R.drawable.ic_usuarios_rejilla, ContextCompat.getColor(applicationContext, R.color.purple_500))
                    )
                    listaRejilla.add(
                        Rejilla(String.format("%s %s", NumberFormat.getNumberInstance(
                            Locale.US).format(usuario.tips?.count), getString(R.string.tips)),
                            R.drawable.ic_tips_rejilla, ContextCompat.getColor(applicationContext, R.color.purple_200))
                    )

                    val adaptador = AdaptadorGridView(applicationContext, listaRejilla)
                    rejilla.adapter = adaptador


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