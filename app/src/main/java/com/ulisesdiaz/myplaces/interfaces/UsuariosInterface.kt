package com.ulisesdiaz.myplaces.interfaces

import com.ulisesdiaz.myplaces.foursquare.models.User

interface UsuariosInterface {
    fun obtenerUsuarioActual(usuario: User)
}