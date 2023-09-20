package kz.techtask.weatherapp.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kz.techtask.weatherapp.databinding.CityDetailItemBinding

import kz.techtask.weatherapp.databinding.FragmentCityDetailBinding
import kz.techtask.weatherapp.model.CityWeather
import kz.techtask.weatherapp.model.Day
import kz.techtask.weatherapp.model.Forecastday

class CityDetailAdapter(
    private var values: List<Forecastday>
) : RecyclerView.Adapter<CityDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            CityDetailItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: CityDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val date: TextView = binding.tvDate
        private val image: ImageView = binding.imageWeather
        private val dayDegree: TextView = binding.tvTemp
        private val nightDegree: TextView = binding.tvTempNight

        fun bind(item: Forecastday) {
            date.text = item.date
            Glide.with(itemView).load("https:" + item.day.condition.icon)
                .override(500, 500)
                .into(image)
            dayDegree.text = item.day.maxtemp_c.toString()
            nightDegree.text = item.day.mintemp_c.toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Forecastday>) {
        values = newList
        notifyDataSetChanged()
    }
}