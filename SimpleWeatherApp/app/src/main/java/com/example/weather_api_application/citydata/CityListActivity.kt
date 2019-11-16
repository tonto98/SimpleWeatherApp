package com.example.weather_api_application.citydata

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.example.weather_api_application.R
import com.example.weather_api_application.citydata.model.Cities
import com.example.weather_api_application.citydata.presenter.CityListPresenterImpl
import com.example.weather_api_application.data.JsonResourceWrapper
import com.example.weather_api_application.data.SharedPreference
import kotlinx.android.synthetic.main.activity_city_list.*

class CityListActivity : AppCompatActivity(), CityListView {

    private lateinit var adapter: CityListRecyclerAdapter
    private lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)
        showLoading()

        sharedPreference = SharedPreference(this)

        city_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val cityList = JsonResourceWrapper(resources).cityList

        val cityRecyclerPresenter = CityListPresenterImpl(this, cityList)
        cityRecyclerPresenter.initialLoad()

        editText_search_CityList.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Log.i("CityListActivity", p0.toString())
                cityRecyclerPresenter.loadSearchedList(p0.toString())
            }
        })

        imageButton_search_CityList.setOnClickListener {
            cityRecyclerPresenter.loadSearchedList(editText_search_CityList.text.toString())
        }

    }

    override fun onInitialLoad(cityList: List<Cities>) {
        Log.i("CityRecycler", "on response")
        adapter = CityListRecyclerAdapter(cityList, sharedPreference, this)
        city_recycler_view.adapter = adapter
        hideLoading()
    }

    override fun onLoad(cityList: List<Cities>) {
        adapter.update(cityList)
        hideLoading()
    }

    fun showLoading(){
        loading_animation_city_list.visibility = View.VISIBLE
        city_recycler_view.visibility = View.INVISIBLE
    }

    fun hideLoading(){
        loading_animation_city_list.visibility = View.INVISIBLE
        city_recycler_view.visibility = View.VISIBLE
    }
}