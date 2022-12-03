package com.cdr.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdr.recyclerview.adapter.PersonActionListener
import com.cdr.recyclerview.adapter.PersonAdapter
import com.cdr.recyclerview.databinding.ActivityMainBinding
import com.cdr.recyclerview.model.Person
import com.cdr.recyclerview.model.PersonListener
import com.cdr.recyclerview.model.PersonService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PersonAdapter // Объект Adapter
    private val personService: PersonService // Объект PersonService
        get() = (applicationContext as App).personService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = LinearLayoutManager(this) // LayoutManager
        adapter = PersonAdapter(object : PersonActionListener { // Создание объекта
            override fun onPersonGetId(person: Person) =
                Toast.makeText(this@MainActivity, "Persons ID: ${person.id}", Toast.LENGTH_SHORT).show()

            override fun onPersonLike(person: Person) = personService.likePerson(person)

            override fun onPersonRemove(person: Person) = personService.removePerson(person)

            override fun onPersonMove(person: Person, moveBy: Int) = personService.movePerson(person, moveBy)

        })
        personService.addListener(listener)

        binding.recyclerView.layoutManager = manager // Назначение LayoutManager для RecyclerView
        binding.recyclerView.adapter = adapter // Назначение адаптера для RecyclerView
    }

    private val listener: PersonListener = {adapter.data = it}
}