package kz.techtask.weatherapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.techtask.weatherapp.databinding.CityItemBinding
import kz.techtask.weatherapp.model.CityWeather

class CityListAdapter(
    private var values: List<CityWeather>,
    private val onItemClickListener: (CityWeather) -> Unit
) : RecyclerView.Adapter<CityListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            CityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)

        holder.itemView.setOnClickListener { onItemClickListener(item) }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: CityItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val city: TextView = binding.tvCity
        private val image: ImageView = binding.imageWeather
        private val degree: TextView = binding.tvTemp
        private val condition: TextView = binding.tvWeatherDesc

        fun bind(item: CityWeather) {
            city.text = item.location.name
            Glide.with(itemView).load("https:" + item.current.condition.icon)
                .override(500, 500)
                .into(image)
            degree.text = item.current.temp_c.toString()
            condition.text = item.current.condition.text
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<CityWeather>) {
        values = newList
        notifyDataSetChanged()
    }
}