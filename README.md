### :book: RecyclerView для начинающего Android-разработчика.

Данная статья была написана [coder-chekunkov](https://github.com/coder-chekunkov) для начинающих Android-разработчиков. Тема: RecyclerView. <br/>
[Ссылка](https://habr.com/ru/sandbox/181814/) на статью на Habr.


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

В классе PersonService хранится лист пользователей, который мы заполняем в `init` (initializers blocks), и лист ссылок на фотографии.

После создания классов необходимо класс PersonService сделать `Singleton` для корректной работы. Для этого создадим класс *App*, и укажем в нем следующее:

```kotlin
class App : Application() {
    val personService = PersonService()
}
```

Теперь **реализуем адаптер** *PersonAdapter*, который будет обрабатывать наши данные и связывать их со списком.

Данный класс будет реализовать `RecyclerView.Adapter`, которому нужен ViewHolder. Соответственно необходимо создать *PersonViewHolder*, который будет реализовывать `RecyclerView.ViewHolder` и принимать наш binding.

Также PersonAdapter должен иметь данные, с которыми ему предстоит работать. Для этого создадим пустой список и перепишем его сеттер. В итоге получаем:

```kotlin
class PersonAdapter : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    var data: List<Person> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class PersonViewHolder(val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)
}
```

Но для работы адаптера необходимо переопределить минимум три метода (AndroidStudio подскажет нам).

Метод `getItemCount`, который будет возвращать количество элементов нашего списка с данными; <br/>
Метод `onCreateViewHolder`, в котором будет происходить создание ViewHolder. Данный метод принимает в себя *parent* и *viewType* (используется в том случае, если в списке будут разные типы элементов списка); <br/>
Метод `onBindViewHolder`, в котором будет происходить отрисовка всех элементов в объекте списка (имя человека, компания и т.д.):

После переопределения методов и их реализации получаем:

```kotlin
    override fun getItemCount(): Int = data.size // Количество элементов в списке данных

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPersonBinding.inflate(inflater, parent, false)

        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = data[position] // Получение человека из списка данных по позиции
        val context = holder.itemView.context

        with(holder.binding) {
            val color = if (person.isLiked) R.color.red else R.color.grey // Цвет "сердца", если пользователь был лайкнут

            nameTextView.text = person.name // Отрисовка имени пользователя
            companyTextView.text = person.companyName // Отрисовка компании пользователя
            likedImageView.setColorFilter( // Отрисовка цвета "сердца"
                ContextCompat.getColor(context, color),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            Glide.with(context).load(person.photo).circleCrop() // Отрисовка фотографии пользователя с помощью библиотеки Glide
                .error(R.drawable.ic_person) 
                .placeholder(R.drawable.ic_person).into(imageView)
        }
    }
```

На этом наш простой адаптер, который будет просто выводить горизонтальный список готов.

Теперь необходимо повесить на наш RecyclerView созданный адаптер и LayoutManager. Для этого в классе MainActivity пропишем следующее:

```kotlin
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
        adapter = PersonAdapter() // Создание объекта
        adapter.data = personService.getPersons() // Заполнение данными

        binding.recyclerView.layoutManager = manager // Назначение LayoutManager для RecyclerView
        binding.recyclerView.adapter = adapter // Назначение адаптера для RecyclerView
    }
}
```

Запускаем наше приложение на устройстве и получаем список пользователей!

На данном этапе наше приложение просто выводит данные, которые мы можем прокручивать, но взаимодействовать с ними у нас не получится. Исправим это.

В классе PersonService добавим три метода:

- *likePerson* - лайкаем человека;
- *removePerson* - удаляем человека; 
- *movePerson* - перемещаем человека (принимает человека и куда надо переместить: "1" - вниз, "-1" - вверх).
  

```kotlin
    fun likePerson(person: Person) {
        val index = persons.indexOfFirst { it.id == person.id } // Находим индекс человека в списке
        if (index == -1) return // Останавливаемся, если не находим такого человека

        persons = ArrayList(persons) // Создаем новый список
        persons[index] = persons[index].copy(isLiked = !persons[index].isLiked) // Меняем значение "лайка" на противоположное
    }

    fun removePerson(person: Person) {
        val index = persons.indexOfFirst { it.id == person.id } // Находим индекс человека в списке
        if (index == -1) return // Останавливаемся, если не находим такого человека

        persons = ArrayList(persons) // Создаем новый список
        persons.removeAt(index) // Удаляем человека
    }

    fun movePerson(person: Person, moveBy: Int) {
        val oldIndex = persons.indexOfFirst { it.id == person.id } // Находим индекс человека в списке
        if (oldIndex == -1) return // Останавливаемся, если не находим такого человека

        val newIndex = oldIndex + moveBy // Вычисляем новый индекс, на котором должен находится человек
        persons = ArrayList(persons) // Создаем новый список
        Collections.swap(persons, oldIndex, newIndex) // Меняем местами людей        
    }
```

После того, как были созданы методы взаимодействия с людьми, в классе PersonService необходимо **объявить слушателя**:

```kotlin
typealias PersonListener = (persons: List<Person>) -> Unit
```

Так же **создадим** список слушателей, два метода, которые будут добавлять и удалять слушателей, и один, который будет "регистрировать изменения":

```kotlin
    private var listeners = mutableListOf<PersonListener>() // Все слушатели

    fun addListener(listener: PersonListener) {
        listeners.add(listener)
        listener.invoke(persons)
    }

    fun removeListener(listener: PersonListener) {
        listeners.remove(listener)
        listener.invoke(persons)
    }

    private fun notifyChanges() = listeners.forEach { it.invoke(persons) }
```

Метод *notifyChanges* необходимо обязательно вызвать в методах, в которых происходит модификация данных, то есть в методах likePerson, removePerson и movePerson.

На этом наш сервис людей полностью готов. Перейдем в PersonAdapter, в котором **реализуем обработку событий** наших людей. Создадим интерфейс *PersonActionListener*, в котором буду четыре метода:

- *onPersonGetId* - получить уникальный номер выбранного человека;
- *onPersonLike* - человек был лайкнут;
- *onPersonRemove* - удалить человека;
- *onPersonMove* - переместить человека.
  

```kotlin
interface PersonActionListener {
    fun onPersonGetId(person: Person)
    fun onPersonLike(person: Person)
    fun onPersonRemove(person: Person)
    fun onPersonMove(person: Person, moveBy: Int)
}
```

Класс PersonAdapter во входные параметры будет принимать наш интерфейс. Также данный класс должен реализовать интерфейс *OnClickListener*. В итоге сигнатура объявления класса PersonAdaper выглядит следующим образом:

```kotlin
class PersonAdapter(private val personActionListener: PersonActionListener) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>(), View.OnClickListener {
```

Теперь в классе PersonAdapter в методе onBindViewHolder кладём в tag каждого view, на которую будет происходить нажатие, нужного человека:

```kotlin
holder.itemView.tag = person
holder.binding.likedImageView.tag = person
holder.binding.more.tag = person
```

Теперь в методе onCreateViewHolder необходимо проинициализировать слушателей при нажатии. В данном примере будет слушатель на нажатие на элемент списка, кнопку more (три точки) и likedImageView (сердце):

```kotlin
binding.root.setOnClickListener(this)
binding.more.setOnClickListener(this)
binding.likedImageView.setOnClickListener(this)
```

Теперь создадим метод *showPopupMenu*, который будет "рисовать" выпадающее меню с доступными действиями, а именно: удалить пользователя, переместить вверх, переместить вниз:

```kotlin
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
```

В методе *onClick* обработаем нажатия на элементы списка:

```kotlin
    override fun onClick(view: View) {
        val person: Person = view.tag as Person // Получаем из тэга человека

        when (view.id) {
            R.id.more -> showPopupMenu(view)
            R.id.likedImageView -> personActionListener.onPersonLike(person)
            else -> personActionListener.onPersonGetId(person)
        }
    }
```

На этом наш адаптер готов. Теперь перейдем в MainActivity и, при инициализации нашего адаптера, передадим реализацию интерфейса:

```kotlin
adapter = PersonAdapter(object : PersonActionListener { // Создание объекта
   override fun onPersonGetId(person: Person) =
      Toast.makeText(this@MainActivity, "Persons ID: ${person.id}", Toast.LENGTH_SHORT).show()

   override fun onPersonLike(person: Person) = personService.likePerson(person)

   override fun onPersonRemove(person: Person) = personService.removePerson(person)

   override fun onPersonMove(person: Person, moveBy: Int) = personService.movePerson(person, moveBy)

})
```

Также добавим слушателя в MainActivity, который будет прослушивать изменения, происходящие в PersonService:

```kotlin
private val listener: PersonListener = {adapter.data = it}
```

И в методе onCreate добавим этого слушателя:

```kotlin
personService.addListener(listener)
```

На этом наша работа выполнена. Запускаем проект и смотрим результат:

<p align="center">
 <img alt="GIF" src="https://github.com/coder-chekunkov/RecyclerView-Article/blob/main/wiki_images/003.gif" width="220"/> <br/>
</p>

Мы рассмотрели основы RecyclerView. Естественно это далеко не все, что позволяет сделать этот мощный инструмент. Всегда можно добавить DiffUtil, который поможет оптимизировать список, ItemDecorator, для декора наших элементов и т.д. <br/>
[Ссылка](https://habr.com/ru/sandbox/181814/) на статью на Habr.

---

🏆 Я надеюсь, что данная работа помогла Вам. <br/>
📧 При возникновении каких-либо вопросов и предложений - свяжитесь со мной. <br/>
🤝 Спасибо, что заинтересовались данной работой.
