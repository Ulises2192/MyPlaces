package com.ulisesdiaz.myplaces.interfaces

import com.ulisesdiaz.myplaces.foursquare.models.Venue

interface VenuesPorLikeinterface {
    fun venuesGenerados(venues: ArrayList<Venue>)
}