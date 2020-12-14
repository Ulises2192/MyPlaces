package com.ulisesdiaz.myplaces.GridDetalleVenue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.foursquare.Rejilla

class AdaptadorGridView(var context: Context, items:ArrayList<Rejilla>): BaseAdapter() {

    var items:ArrayList<Rejilla>? = null

    init {
        this.items = items
    }
    override fun getCount(): Int {
        return items?.count()!!
    }

    override fun getItem(position: Int): Any {
        return items?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var holder:ViewHolder? = null

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.template_grid_detalle_venue, null)
            holder = ViewHolder(view)
            view.tag = holder
        }else{
            holder = view.tag as? ViewHolder
        }

        val item = items?.get(position) as? Rejilla

        holder?.nombre?.text = item?.nombre
        holder?.imagen?.setImageResource(item?.icono!!)
        holder?.container?.setBackgroundColor(item?.color!!)

        return view!!
    }

    private class ViewHolder(view: View){
        var nombre: TextView? = null
        var imagen: ImageView? = null
        var container: LinearLayout? = null

        init {
            nombre = view.findViewById(R.id.txtNombre)
            imagen = view.findViewById(R.id.imgFoto)
            container= view.findViewById(R.id.container)
        }
    }
}
