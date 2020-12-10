package com.ulisesdiaz.myplaces

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.foursquare.android.nativeoauth.FoursquareOAuth
import com.google.gson.Gson
import com.ulisesdiaz.myplaces.models.FoursquareApiNuevoCheckIn
import com.ulisesdiaz.myplaces.models.FoursquareApiRequestVenues
import com.ulisesdiaz.myplaces.models.Location
import com.ulisesdiaz.myplaces.utils.*

class Foursquare(var activity: AppCompatActivity, var activityDestino: AppCompatActivity) {

    private val CODIGO_CONEXION = 200
    private val CODIGO_INTERCAMBIO_TOKEN = 201

    private val CLIEN_ID = "GJ34F312QSUQXNKFZDLVOYHBDEORLTRIBKKK0P5P4XLNOMC4"
    private val CLIENT_SECRET =  "UMNYVNUSGIQZOQXW3RB1KL5YKKWRJXCGRZLOJ0YGC2ENVCL5"

    private val SETTINGS = "settings"
    private val ACCESS_TOKEN = "accessToken"

    private val URL_BASE = "https://api.foursquare.com/v2/"
    private val VERSION = "v=20201208"

    init {

    }

    fun iniciarSesion(){
        val intent = FoursquareOAuth.getConnectIntent(activity.applicationContext, CLIEN_ID)

        if (FoursquareOAuth.isPlayStoreIntent(intent)){
            Mensaje.mensajeError(activity.applicationContext, Errores.NO_HAY_APP_FSQR)
            activity.startActivity(intent)
        }else{
            activity.startActivityForResult(intent, CODIGO_CONEXION)
        }
    }

    fun validarActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        when(requestCode){
            CODIGO_CONEXION ->{conexionCompleta(resultCode, data)}
            CODIGO_INTERCAMBIO_TOKEN ->{intercambioTokenCompleta(resultCode, data)}
        }
    }

    private fun conexionCompleta(resultCode: Int, data: Intent?){
        val codigoRespuesta = FoursquareOAuth.getAuthCodeFromResult(resultCode, data)
        val excepcion = codigoRespuesta.exception

        if (excepcion == null){
            val coddigo = codigoRespuesta.code
            realizarIntercambioToken(coddigo)
        }else{
            Mensaje.mensajeError(activity.applicationContext, Errores.ERROR_CONEXION_FSQR)
        }
    }

    private fun realizarIntercambioToken(codigo: String){
        val intent = FoursquareOAuth.getTokenExchangeIntent(activity.applicationContext, CLIEN_ID, CLIENT_SECRET, codigo)
        activity.startActivityForResult(intent, CODIGO_INTERCAMBIO_TOKEN)
    }

    private  fun  intercambioTokenCompleta(resultCode: Int, data: Intent?){
        val respuestaToken = FoursquareOAuth.getTokenFromResult(resultCode, data)
        val excepcion = respuestaToken.exception

        if (excepcion == null){
            val accessToken = respuestaToken.accessToken
            if (!guardarToken(accessToken)){
                Mensaje.mensajeError(activity.applicationContext, Errores.ERROR_GUARDAR_TOKEN)
            }else{
                navegarSiguienteActividad()
            }
        }else{
            Mensaje.mensajeError(activity.applicationContext, Errores.ERROR_INTERCAMBIO_TOKEN)
        }
    }

    fun hayToken(): Boolean{
        if (obtenerToken() == ""){
            return false
        }else{
            return true
        }
    }

    fun obtenerToken(): String? {
        val settings = activity.getSharedPreferences(SETTINGS, 0)
        val token = settings.getString(ACCESS_TOKEN, "")
        return token
    }

    private fun guardarToken(token: String) : Boolean{
        if (token.isEmpty()){
            return false
        }
        val settings = activity.getSharedPreferences(SETTINGS, 0)
        val editor = settings.edit()
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
        return true
    }

    fun navegarSiguienteActividad(){
        activity.startActivity(Intent(this.activity, activityDestino::class.java))
        activity.finish()
    }

    fun obtenerVenues(lat:String, lon:String, obtenerVenuesInterface: ObtenerVenuesInterface){
        val network = Network(activity)
        val seccion = "venues/"
        val metodo = "search/"
        val ll = "ll=${lat},${lon}"
        val token = "oauth_token=${obtenerToken()}"
        val url = "${URL_BASE}${seccion}${metodo}?${ll}&${token}&${VERSION}"
        network.httpRequest(activity.applicationContext, url, object:HttpResponse{
            override fun httpResponseSuccess(respose: String) {
                var gson = Gson()
                var objetoRespuesta = gson.fromJson(respose, FoursquareApiRequestVenues::class.java)

                var meta = objetoRespuesta.meta
                var venues = objetoRespuesta.response?.venues!!

                if (meta?.code ==  200){
                    // Se completo la solicutd correctamente
                    obtenerVenuesInterface.venuesGenerados(venues)
                }else{
                    if (meta?.code == 400){
                        // Mostrar problema al usuario
                        Mensaje.mensajeError(activity.applicationContext, meta?.errorDetail)
                    }else{
                        // Mostrar mensaje Generico
                        Mensaje.mensajeError(activity.applicationContext, Errores.ERROR_QUERY)
                    }
                }
            }
        })
    }

    fun nuevoCheckin(id: String, location: Location, mensaje: String){
        val network = Network(activity)
        val seccion = "checkins/"
        val metodo = "add"
        val token = "oauth_token=${obtenerToken()}"
        val query =  "?venueid=${id}&shout=${mensaje}&ll=${location.lat},${location.lng}${token}&${VERSION}"
        val url = "${URL_BASE}${seccion}${metodo}${query}"

        network.httpPostRequest(activity.applicationContext, url, object: HttpResponse{
            override fun httpResponseSuccess(respose: String) {
                var gson = Gson()
                var objetoRespuesta = gson.fromJson(respose, FoursquareApiNuevoCheckIn::class.java)

                var meta = objetoRespuesta.meta

                if (meta?.code ==  200){
                    // mandar un mensaje cuando la query se completo correctamente

                }else{
                    if (meta?.code == 400){
                        // Mostrar problema al usuario
                        Mensaje.mensajeError(activity.applicationContext, meta?.errorDetail)
                    }else{
                        // Mostrar mensaje Generico
                        Mensaje.mensajeError(activity.applicationContext, Errores.ERROR_QUERY)
                    }
                }
            }
        })
    }

    fun obtenerUsuarioActual(){
        val network = Network(activity)
        val seccion = "users/"
        val metodo = "self"
        val token = "oauth_token=${obtenerToken()}"
        val query =  "?${token}&${VERSION}"
        val url = "${URL_BASE}${seccion}${metodo}${query}"

        network.httpPostRequest(activity.applicationContext, url, object: HttpResponse{
            override fun httpResponseSuccess(respose: String) {
                var gson = Gson()
                var objetoRespuesta = gson.fromJson(respose, FoursquareApiNuevoCheckIn::class.java)

                var meta = objetoRespuesta.meta

                if (meta?.code ==  200){
                    // mandar un mensaje cuando la query se completo correctamente

                }else{
                    if (meta?.code == 400){
                        // Mostrar problema al usuario
                        Mensaje.mensajeError(activity.applicationContext, meta?.errorDetail)
                    }else{
                        // Mostrar mensaje Generico
                        Mensaje.mensajeError(activity.applicationContext, Errores.ERROR_QUERY)
                    }
                }
            }
        })
    }
}