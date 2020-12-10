package com.ulisesdiaz.myplaces.models

class FoursquareApiRequestVenues {

    var meta : Meta? = null
    var response: FoursquareResponseVenues? = null
}

class FoursquareApiNuevoCheckIn{
    var meta: Meta? = null
}

class Meta{
    var code: Int = 0
    var errorDetail: String = ""
}

class FoursquareResponseVenues{
    var venues: ArrayList<Venue>? = null
}

class Venue{
    var id: String = ""
    var name: String = ""
    var location: Location? = null
    var categories: ArrayList<Category>? = null
    var stats: Stats? = null
}

class  Location{
    var lat:Double = 0.0
    var lng: Double = 0.0
    var state: String = ""
    var country: String = ""
}

class Category{
    var id: String = ""
    var name: String = ""
    var icon: Icon? = null
}

class Icon{
    var prefix: String = ""
    var suffix: String = ""
}

class Stats{
    var checkinsCount = 0
    var usersCount = 0
    var tipCount = 0
}