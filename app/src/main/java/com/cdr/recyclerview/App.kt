package com.cdr.recyclerview

import android.app.Application
import com.cdr.recyclerview.model.PersonService

class App : Application() {
    val personService = PersonService()
}