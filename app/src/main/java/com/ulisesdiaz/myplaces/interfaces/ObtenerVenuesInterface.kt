package com.ulisesdiaz.myplaces.interfaces

import com.ulisesdiaz.myplaces.foursquare.models.Category
import com.ulisesdiaz.myplaces.foursquare.models.Venue

interface ObtenerVenuesInterface {

    fun venuesGenerados(venues: ArrayList<Venue>)

}