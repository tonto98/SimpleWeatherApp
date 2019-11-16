package com.example.weather_api_application.citydata

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.weather_api_application.main.MainActivity
import com.example.weather_api_application.R
import com.example.weather_api_application.citydata.model.Cities
import com.example.weather_api_application.data.SharedPreference

class CityListRecyclerAdapter(var cityList: List<Cities>, val sharedPref: SharedPreference, val activity: CityListActivity): RecyclerView.Adapter<CityListRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.city_list_layout, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val city: Cities = cityList[p1]

        p0.cityName.text = city.name
        p0.countryName.text = city.country

        p0.itemView.setOnClickListener {
            sharedPref.save(SharedPreference.KEY_SELECTED_CITY, "${city.name}, ${city.country}")
//            val intent = Intent(context, MainActivity::class.java)
//            startActivity(context,intent,null)
            activity.finish()
        }
    }

    fun update(cityList: List<Cities>){
        this.cityList = cityList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var cityName = itemView.findViewById<TextView>(R.id.city_name_recycler_text_view)
        var countryName = itemView.findViewById<TextView>(R.id.country_name_recycler_text_view)
    }
}