package com.cdr.recyclerview
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdr.recyclerview.ViewModel.PersonViewModel
import com.cdr.recyclerview.adapter.PersonActionListener
import com.cdr.recyclerview.adapter.PersonAdapter
import com.cdr.recyclerview.databinding.ActivityMainBinding
import com.cdr.recyclerview.model.Person

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PersonAdapter
    private lateinit var  viewModel: PersonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = PersonViewModel()

        val manager = LinearLayoutManager(this)
        adapter = PersonAdapter(object : PersonActionListener {
            override fun onPersonGetId(person: Person) {
                Toast.makeText(this@MainActivity, "Persons ID: ${person.id}", Toast.LENGTH_SHORT).show()
            }

            override fun onPersonLike(person: Person) {
                viewModel.likePerson(person)
            }

            override fun onPersonRemove(person: Person) {
                viewModel.removePerson(person)
            }

            override fun onPersonMove(person: Person, moveBy: Int) {
                viewModel.movePerson(person, moveBy)
            }
        })

        viewModel.personList.observe(this, Observer {
            adapter.data = it
        })

        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
    }
}
