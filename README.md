# MyPlaces
Es una aplicación en la cual se implementa la autenticacion Oauth a la api de fourscuare para obtener los avenues cercanos a la posición del dispositivo y poder realizar un checkin por medio de una solicitud con metodo POST.

Hace uso de las libreriras de Volley para las solicitudes http, Gson para el manejo de json, google location para obtener la ubicación del dispositvo

```xml
implementation 'com.android.volley:volley:1.1.0'
implementation 'com.google.code.gson:gson:2.8.5'
implementation 'com.foursquare:foursquare-android-oauth:1.1.1'
implementation 'com.google.android.gms:play-services-location:17.1.0'
```
