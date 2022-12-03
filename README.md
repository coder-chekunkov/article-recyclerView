### :book: RecyclerView для начинающего Android-разработчика.

Данная статья была написана [coder-chekunkov](https://github.com/coder-chekunkov) для начинающих Android-разработчиков. Тема: RecyclerView.


<p align="center">
 <img alt="GIF" src="https://github.com/coder-chekunkov/RecyclerView-Article/blob/main/wiki_images/001.jpg" width="220"/>
 <img alt="GIF" src="https://github.com/coder-chekunkov/RecyclerView-Article/blob/main/wiki_images/002.jpg" width="220"/>
 <img alt="GIF" src="https://github.com/coder-chekunkov/RecyclerView-Article/blob/main/wiki_images/003.gif" width="220"/> <br/>
</p>

---

Здравствуй, дорогой читатель. Каждый Android-разработчик сталкивался с задачей, в которой необходимо создать какой-то список, для отображения данных. Данная статья поможет новичку разобраться с таким очень важным и интересным компонентом, как RecyclerView.

В статье будет рассказано о том, почему необходимо использовать именно RecyclerView, описаны его основные компоненты и также будет разобран базовый, не очень сложный пример.

Статья предназначена для новичков, которые хотят разобраться со списками в Android.

#### ListView или RecyclerView?

Для реализации какого-то прокручиваемого списка у Android разработчика существуют два пути - [ListView](https://developer.android.com/reference/android/widget/ListView) и [RecyclerView](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView).

Первый виджет интуитивно понятен и довольно прост. Но, к сожалению, имеет много недостатков, например, ListView позволяет создать только вертикальный список.

В свою же очередь RecyclerView "из коробки" предоставляет гораздо больше инструментов для кастомизации и оптимизации списка, чем ListView. Если кратко характеризовать RecyclerView, то можно сказать, что это список на стероидах.

RecyclerView работает следующим образом: на экране устройства отображаются видимые элементы списка; при прокрутке списка верхний элемент уходит за пределы экрана и очищается, а после помещается вниз экрана и заполняется новыми данными.

#### Основные компоненты RecyclerView.

Для корректной работы RecyckerView необходимо реализовать следующие компоненты:

- `RecyclerView`, который необходимо добавить в макет нашего Activity;
- `Adapter`, который содержит, обрабатывает и связывает данные со списком;
- `ViewHolder`, который служит для оптимизации ресурсов и является своеобразным контейнером для всех элементов, входящих в список;
- `ItemDecorator`, который позволяет отрисовать весь декор;
- `ItemAnimator`, который отвечает за анимацию элементов при добавлении, редактировании и других операций;
- `DiffUtil`, который служит для оптимизации списка и добавления стандартных анимаций.

#### Практический пример.

В качестве не сложного примера, создадим приложение со списком, в котором будут отображены данные о людях. Каждый человек будет иметь имя, название компании, фотографию и несколько операций над ним (показать уникальный номер, удалить, переместить вверх/вниз, лайкнуть).

Реализация примера будет выполнена на языке Kotlin. Также будут использованы библиотеки [Glide](https://github.com/bumptech/glide) и [Faker](https://github.com/DiUS/java-faker), которые никак не относятся к RecyclerView.

В первую очередь **укажем все зависимости**, которые будут использованы приложением, в файл сборки `build.gradle` нашего приложения:

```groovy
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    implementation 'com.github.javafaker:javafaker:1.0.2'
    implementation 'com.github.bumptech.glide:glide:4.14.2'
```

*Примечание: в последних версиях AndroidStudio не обязательно подключать библиотеку RecyclerView. Доступен в библиотеке Material.*

И необходимо **подключить** [ViewBinding](https://developer.android.com/topic/libraries/view-binding) в файле сборки `build.gradle` нашего приложения:

```groovy
buildFeatures {
        viewBinding = true
}
```

Также необходимо указать **разрешение** на доступ в Интернет в файле `AndroidManifest.xml` (для работы с библиотекой Glide):

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

После **создадим макетные файлы**: первый для ActivityMain, который хранит RecyclerView, второй для элемента списка (человек).

*activity_main.xml*:

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activityMain"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>
```

*item_person.xml*

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="?selectableItemBackground"
    android:paddingStart="10dp"
    android:paddingTop="5dp"
    android:paddingEnd="10dp"
    android:paddingBottom="5dp">

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:src="@drawable/ic_person"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/nameTextView"
        style="@style/TextAppearance.AppCompat.Body2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="15dp"
        app:layout_constraintStart_toEndOf="@id/imageView"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/companyTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="15dp"
        app:layout_constraintStart_toEndOf="@id/imageView"
        app:layout_constraintTop_toBottomOf="@id/nameTextView" />

    <ImageView
        android:id="@+id/likedImageView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="15dp"
        android:src="@drawable/ic_like"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@id/more"
        app:layout_constraintTop_toTopOf="parent" />

    <ImageView
        android:id="@+id/more"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/ic_more"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

После того, как все библиотеки были подключены, все макеты созданы и разрешения получены, необходимо **создать данные о людях** (в настоящем, боевом примере эти данные будут приходить, например, с сервера, но в нашем случае мы создадим их самостоятельно). Для этого создадим класс *PersonService* и data-class *Person*:

```kotlin
data class Person(
    val id: Long, // Уникальный номер пользователя
    val name: String, // Имя человека
    val companyName: String, // Название комании
    val photo: String, // Ссылка на фото человека
    val isLiked: Boolean // Был ли лайкнут пользователь
)
```

```kotlin
class PersonService {

    private var persons = mutableListOf<Person>() // Все пользователи

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
```

В классе PersonService хранится лист пользователей, который мы заполняем в `init` (initializers blocks) и лист ссылок на фотографии.

После создания классов необходимо класс PersonService сделать `Singleton` для корректной работы. Для этого создадим класс *App*, и укажем в нем следующее:

```kotlin
class App : Application() {
    val personService = PersonService()
}
```
