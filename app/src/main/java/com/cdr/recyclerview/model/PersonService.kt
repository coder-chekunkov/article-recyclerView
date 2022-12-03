package com.cdr.recyclerview.model

import com.github.javafaker.Faker
import java.util.*
import kotlin.collections.ArrayList

typealias PersonListener = (persons: List<Person>) -> Unit

class PersonService {

    private var persons = mutableListOf<Person>() // Все пользователи

    private var listeners = mutableListOf<PersonListener>() // Все слушатели

    init {
        val faker = Faker.instance() // Переменная для создания случайных данных

        persons = (1..50).map {
            Person(
                id = it.toLong(),
                name = faker.name().fullName(),
                companyName = faker.company().name(),
                photo = IMAGES[it % IMAGES.size],
                isLiked = false
            )
        }.toMutableList()
    }

    fun getPersons(): List<Person> = persons

    fun likePerson(person: Person) {
        val index = persons.indexOfFirst { it.id == person.id } // Находим индекс человека в списке
        if (index == -1) return // Останавливаемся, если не находим такого человека

        persons = ArrayList(persons) // Создаем новый список
        persons[index] =
            persons[index].copy(isLiked = !persons[index].isLiked) // Меняем значение "лайка" на противоположное
        notifyChanges()
    }

    fun removePerson(person: Person) {
        val index = persons.indexOfFirst { it.id == person.id } // Находим индекс человека в списке
        if (index == -1) return // Останавливаемся, если не находим такого человека

        persons = ArrayList(persons) // Создаем новый список
        persons.removeAt(index) // Удаляем человека
        notifyChanges()
    }

    fun movePerson(person: Person, moveBy: Int) {
        val oldIndex =
            persons.indexOfFirst { it.id == person.id } // Находим индекс человека в списке
        if (oldIndex == -1) return // Останавливаемся, если не находим такого человека

        val newIndex =
            oldIndex + moveBy // Вычисляем новый индекс, на котором должен находится человек
        persons = ArrayList(persons) // Создаем новый список
        Collections.swap(persons, oldIndex, newIndex) // Меняем местами людей
        notifyChanges()
    }

    fun addListener(listener: PersonListener) {
        listeners.add(listener)
        listener.invoke(persons)
    }

    fun removeListener(listener: PersonListener) {
        listeners.remove(listener)
        listener.invoke(persons)
    }

    private fun notifyChanges() = listeners.forEach { it.invoke(persons) }

    companion object {
        private val IMAGES = mutableListOf(
            "https://images.unsplash.com/photo-1600267185393-e158a98703de?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NjQ0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1579710039144-85d6bdffddc9?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Njk1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODE0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1620252655460-080dbec533ca?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzQ1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1613679074971-91fc27180061?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzUz&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1485795959911-ea5ebf41b6ae?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzU4&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1545996124-0501ebae84d0?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/flagged/photo-1568225061049-70fb3006b5be?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Nzcy&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1567186937675-a5131c8a89ea?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODYx&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1546456073-92b9f0a8d413?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800"
        )
    }
}

data class Person(
    val id: Long, // Уникальный номер пользователя
    val name: String, // Имя человека
    val companyName: String, // Название комании
    val photo: String, // Ссылка на фото человека
    val isLiked: Boolean // Был ли лайкнут пользователь
)