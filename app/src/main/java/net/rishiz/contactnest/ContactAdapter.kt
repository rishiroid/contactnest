package net.rishiz.contactnest

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.rishiz.contactnest.databinding.ContactRowBinding
import kotlin.random.Random

class ContactAdapter(private val contact: ArrayList<ContactDetails>, val context: Context) :
    RecyclerView.Adapter<ContactHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ContactRowBinding.inflate(layoutInflater, parent, false)
        return ContactHolder(view)
    }

    override fun getItemCount(): Int {
        return contact.size
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.apply {
            name.text = contact[position].name
            number.text = contact[position].number
            icon.setBackgroundColor(getcolor())
        }
    }

    private fun getcolor(): Int {
        val rnd = Random.Default //kotlin.random
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}

class ContactHolder(binding: ContactRowBinding) : RecyclerView.ViewHolder(binding.root) {
    val name = binding.name
    val number = binding.number
    val icon = binding.imageView
}