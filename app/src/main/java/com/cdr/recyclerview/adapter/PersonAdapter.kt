package com.cdr.recyclerview.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cdr.recyclerview.R
import com.cdr.recyclerview.databinding.ItemPersonBinding
import com.cdr.recyclerview.model.Person

// DiffUtil, который не вошел в статью:
class PersonDiffUtil(
    private val oldList: List<Person>,
    private val newList: List<Person>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPerson = oldList[oldItemPosition]
        val newPerson = newList[newItemPosition]
        return oldPerson.id == newPerson.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPerson = oldList[oldItemPosition]
        val newPerson = newList[newItemPosition]
        return oldPerson == newPerson
    }
}

interface PersonActionListener {
    fun onPersonGetId(person: Person)
    fun onPersonLike(person: Person)
    fun onPersonRemove(person: Person)
    fun onPersonMove(person: Person, moveBy: Int)
}

class PersonAdapter(private val personActionListener: PersonActionListener) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>(), View.OnClickListener {

    var data: List<Person> = emptyList()
        set(newValue) {
            val personDiffUtil = PersonDiffUtil(field, newValue)
            val personDiffUtilResult = DiffUtil.calculateDiff(personDiffUtil)
            field = newValue
            personDiffUtilResult.dispatchUpdatesTo(this@PersonAdapter)
        }

    override fun getItemCount(): Int = data.size // Количество элементов в списке данных

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.more.setOnClickListener(this)
        binding.likedImageView.setOnClickListener(this)

        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = data[position] // Получение человека из списка данных по позиции
        val context = holder.itemView.context

        with(holder.binding) {
            holder.itemView.tag = person
            likedImageView.tag = person
            more.tag = person

            val color =
                if (person.isLiked) R.color.red else R.color.grey // Цвет "сердца", если пользователь был лайкнут

            nameTextView.text = person.name // Отрисовка имени пользователя
            companyTextView.text = person.companyName // Отрисовка компании пользователя
            likedImageView.setColorFilter( // Отрисовка цвета "сердца"
                ContextCompat.getColor(context, color),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            Glide.with(context).load(person.photo)
                .circleCrop() // Отрисовка фотографии пользователя с помощью библиотеки Glide
                .error(R.drawable.ic_person)
                .placeholder(R.drawable.ic_person).into(imageView)
        }
    }

    override fun onClick(view: View) {
        val person: Person = view.tag as Person // Получаем из тэга человека

        when (view.id) {
            R.id.more -> showPopupMenu(view)
            R.id.likedImageView -> personActionListener.onPersonLike(person)
            else -> personActionListener.onPersonGetId(person)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val person = view.tag as Person
        val position = data.indexOfFirst { it.id == person.id }

        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, "Up").apply {
            isEnabled = position > 0
        }
        popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, "Down").apply {
            isEnabled = position < data.size - 1
        }
        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Remove")

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_MOVE_UP -> personActionListener.onPersonMove(person, -1)
                ID_MOVE_DOWN -> personActionListener.onPersonMove(person, 1)
                ID_REMOVE -> personActionListener.onPersonRemove(person)
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()
    }

    companion object {
        private const val ID_MOVE_UP = 1
        private const val ID_MOVE_DOWN = 2
        private const val ID_REMOVE = 3
    }

    class PersonViewHolder(val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)
}