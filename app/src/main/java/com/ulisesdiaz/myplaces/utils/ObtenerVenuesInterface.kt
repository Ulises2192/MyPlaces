package com.ulisesdiaz.myplaces.utils

import com.ulisesdiaz.myplaces.models.Venue

interface ObtenerVenuesInterface {

    fun venuesGenerados(venues: ArrayList<Venue>)
}