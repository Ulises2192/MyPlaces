package com.ulisesdiaz.myplaces.interfaces

import com.google.android.gms.location.LocationResult

interface UbicacionListener {

    fun ubicacionResponse(locationResult: LocationResult)
}