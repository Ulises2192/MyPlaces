package com.ulisesdiaz.myplaces.utils

import com.google.android.gms.location.LocationResult

interface UbicacionListener {

    fun ubicacionResponse(locationResult: LocationResult)
}