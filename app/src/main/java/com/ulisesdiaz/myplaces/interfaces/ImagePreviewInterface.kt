package com.ulisesdiaz.myplaces.interfaces

import com.ulisesdiaz.myplaces.foursquare.models.Photo

interface ImagePreviewInterface {

    fun obtenerImagePreview(photos: ArrayList<Photo>)
}