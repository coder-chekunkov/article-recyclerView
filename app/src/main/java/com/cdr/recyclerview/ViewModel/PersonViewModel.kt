package com.cdr.recyclerview.ViewModel

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cdr.recyclerview.model.Person
import com.cdr.recyclerview.model.PersonListener
import com.cdr.recyclerview.model.PersonService

class PersonViewModel : ViewModel() {

    private val personService = PersonService()

    // LiveData to observe the list of persons
    private val _personList = MutableLiveData<List<Person>>()
    val personList: LiveData<List<Person>> get() = _personList

    init {
        // Initial setup and observe changes in the PersonService
        personService.addListener(object : PersonListener {
            override fun invoke(persons: List<Person>) {
                _personList.postValue(persons)
            }
        })
    }

    // Functions to interact with the PersonService
    fun likePerson(person: Person) {
        personService.likePerson(person)
    }

    fun removePerson(person: Person) {
        personService.removePerson(person)
    }

    fun movePerson(person: Person, moveBy: Int) {
        personService.movePerson(person, moveBy)
    }
}
