package com.ulisesdiaz.myplaces.adapters.likes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ulisesdiaz.myplaces.R
import com.ulisesdiaz.myplaces.foursquare.models.Venue

class AdaptadorCustom(items: ArrayList<Venue>, var listener: ClickListener, var longClickListener: LongClickListener): RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {

    var items: ArrayList<Venue>? = null
    var itemsSeleccionados: ArrayList<Int>? = null
    var multiSeleccion = false
    var viewHolder: ViewHolder? = null

    init {
        this.items = items
        this.itemsSeleccionados = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_venues_likes, parent, false)
        viewHolder = ViewHolder(view, listener, longClickListener)

        return viewHolder!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items?.get(position)
        holder.nombre?.text = item?.name

    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }

    class ViewHolder(view: View, listener: ClickListener, longClickListener: LongClickListener): RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener{
        val view = view
        var nombre: TextView? = null

        var listener: ClickListener? = null
        var longListener: LongClickListener? = null

        init {
            nombre = view.findViewById(R.id.txtNombre)

            this.listener = listener
            this.longListener = longClickListener
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            this.listener?.onClickListener(v!!, adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            this.longListener?.longClickListener(v!!, adapterPosition)
            return true
        }
    }

    fun  iniciarActionMode(){
        multiSeleccion = true
    }

    fun  destruirActionMode(){
        multiSeleccion = false
        itemsSeleccionados?.clear()
        notifyDataSetChanged()
    }

    fun terminarActionMode(){
        // Eliminar elementos selecionados
        for (item in itemsSeleccionados!!){
            itemsSeleccionados?.remove(item)
        }
        multiSeleccion = false
        notifyDataSetChanged()
    }

    fun selecionarItem(index: Int){
        if (multiSeleccion){
            if (itemsSeleccionados?.contains(index)!!){
                itemsSeleccionados?.remove(index)
            }else{
                itemsSeleccionados?.add(index)
            }
            notifyDataSetChanged()
        }
    }

    fun obtenerElelementosSelecionados(): Int{
        return itemsSeleccionados?.count()!!
    }

    fun eliminarSeleccionados(){
        if (itemsSeleccionados?.count()!! > 0){
            var itemsEliminados = ArrayList<Venue>()

            for (index in itemsSeleccionados!!){
                itemsEliminados.add(items?.get(index)!!)
            }

            items?.removeAll(itemsEliminados)
            itemsSeleccionados?.clear()
        }
    }

}