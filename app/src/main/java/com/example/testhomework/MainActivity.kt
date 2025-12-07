package com.example.testhomework

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testhomework.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var timerDisposable: Disposable? = null
    private lateinit var testAdapter: TestAdapter
    private lateinit var sensorManager: SensorManager

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()

        binding.btGo.setOnClickListener {
            doSomething()

        }
        binding.btGo2.setOnClickListener {
            timer(binding.tvTimer)
        }
        recycler()
        debounceText(binding.ed1)

        /*
       Задача 4.1 - Выполнить параллельно два сетевых запроса на перевод
       слова "fox" с английского на русский и с английского на французский,
       объединить результат
       */
        binding.btParal.setOnClickListener {
            @SuppressLint("CheckResult")
            val translation1 = translate("en", "ru", "fox")
            val translation2 = translate("en", "fr", "fox")
            parallel(translation1, translation2, binding.tvParal)
        }

        /*
        Задача 4.2
        Выполните последовательно два сетевых запроса - "fox" с английского на французский и результат с французского на русский
         */
        binding.btQuery.setOnClickListener {
            sequentialTranslation("fox")
        }


        /*
        Задача 5 - Observable для акселерометра
         */
        createAccelerometerObservable()

    }

    private fun setupRecyclerView() {
        testAdapter = TestAdapter()

        // Отключаем вложенный скроллинг, так как RecyclerView находится внутри ScrollView
        binding.recyclerView.isNestedScrollingEnabled = false

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = testAdapter

        // Добавляем тестовые данные при запуске приложения
        val testItems = listOf(
            TestItem("Элемент 1", "Описание первого элемента"),
            TestItem("Элемент 2", "Описание второго элемента"),
            TestItem("Элемент 3", "Описание третьего элемента"),
            TestItem("Элемент 4", "Описание четвертого элемента"),
            TestItem("Элемент 5", "Описание пятого элемента"),
            TestItem("Элемент 6", "Описание шестого элемента"),
            TestItem("Элемент 7", "Описание седьмого элемента"),
            TestItem("Элемент 8", "Описание восьмого элемента"),
        )
        testAdapter.updateItems(testItems)
    }


    /*
    Подзадача 1 - интернет запрос с Rx
     */
    @SuppressLint("CheckResult")
    fun doSomething() {
        RetrofitClient.apiService.getSomething()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    binding.tv1.text = "Результат запроса : ${result.success}"
                },
                { error ->
                    binding.tv1.text = "Ошибочка ${error.message}"
                }
            )
    }

    /*
    Подзадача 2 - timer
     */

    fun timer(tv: TextView) {
        timerDisposable?.dispose()
        timerDisposable = Observable.interval(1L, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { time ->
                    tv.text = time.toString()
                },
                { error ->
                    tv.text = error.message
                }
            )
    }


    /*
    Подзадача 3
     */

    fun recycler() {
        val dispose = ItemClickSubject.positionSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { pos ->
                    Toast.makeText(this, "$pos", Toast.LENGTH_SHORT).show()

                }
            )
    }

    object ItemClickSubject {
        val positionSubject: PublishSubject<Int> = PublishSubject.create()
    }


    /*
    Подзадача 4 ( debounce )
     */
    fun debounceText(ed: EditText) {
        val searchSubject = PublishSubject.create<String>()
        ed.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //empty
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //empty
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchSubject.onNext(p0?.toString() ?: "")
            }
        })
        val disposableEditText = searchSubject
            .debounce(3L, TimeUnit.SECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("tv", "$it")
            })
    }


    /**
     * Задача 4
     */

    @SuppressLint("CheckResult")
    fun translate(sl: String, tl: String, q: String): Single<String> {
        return RetrofitClient.translateService.translate(
            sourceLanguage = sl,
            targetLanguage = tl,
            text = q
        )
            .subscribeOn(Schedulers.io())
            .map { response ->
                when {
                    response.isNotEmpty() -> {
                        val firstElement = response[0]
                        when {
                            firstElement is List<*> -> {
                                val translations = firstElement as List<*>
                                if (translations.isNotEmpty() && translations[0] is String) {
                                    translations[0] as String
                                } else {
                                    Log.e("Translate", "Переводы пусты: $translations")
                                    throw IllegalStateException("Не удалось извлечь перевод из ответа")
                                }
                            }

                            firstElement is String -> {
                                firstElement
                            }

                            else -> {
                                Log.e(
                                    "Translate",
                                    "Неожиданный тип первого элемента: ${firstElement?.javaClass?.name}"
                                )
                                throw IllegalStateException("Неожиданный формат ответа: первый элемент не список и не строка")
                            }
                        }
                    }

                    else -> {
                        Log.e("Translate", "Ответ пуст")
                        throw IllegalStateException("Ответ от API пуст")
                    }
                }
            }
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    fun parallel(translation1: Single<String>, translation2: Single<String>, tv: TextView) {

        Single.zip(translation1, translation2) { resultRu: String, resultFr: String ->
            // Объединяем результаты
            mapOf(
                "russian" to resultRu,
                "french" to resultFr
            )
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { combinedResult ->
                    // Обработка объединенного результата
                    Log.d("Translate", "Перевод на русский: ${combinedResult["russian"]}")
                    Log.d("Translate", "Перевод на французский: ${combinedResult["french"]}")
                    tv.text = combinedResult["russian"] + combinedResult["french"]

                    // Можно отобразить результат в UI, например:
                    // binding.textView.text = "RU: ${combinedResult["russian"]}\nFR: ${combinedResult["french"]}"
                },
                { error ->
                    Log.e("Translate", "Ошибка при переводе: ${error.message}", error)
                    Toast.makeText(this, "Ошибка: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

    }

    /**
     * Задача 4.2 - Последовательные запросы на перевод
     * "fox" с английского на французский, затем результат с французского на русский
     */
    @SuppressLint("CheckResult", "SetTextI18n")
    private fun sequentialTranslation(text: String) {
        translate("en", "fr", text)
            .flatMap { frenchTranslation ->
                Log.d("Translate", "Первый перевод (en->fr): $frenchTranslation")
                binding.tvQuery.text = "EN->FR->RU: $frenchTranslation"

                translate("fr", "ru", frenchTranslation)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { finalTranslation ->
                    // Обработка финального результата
                    Log.d("Translate", "Финальный перевод (en->fr->ru): $finalTranslation")
                    binding.tvQuery.text = "EN->FR->RU: $finalTranslation"
                },
                { error ->
                    Log.e(
                        "Translate",
                        "Ошибка при последовательном переводе: ${error.message}",
                        error
                    )
                }
            )
    }

    /**
     * Создает горячий Observable<FloatArray> для показаний акселерометра.
     * Подписывается на сенсор при первой подписке на Observable.
     * Отписывается от сенсора когда от Observable отписываются все подписчики.
     * Observable является "горячим" - все подписчики получают одни и те же данные.
     */
    fun createAccelerometerObservable(): Observable<FloatArray> {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        return Observable.create<FloatArray> { emitter ->
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        // Создаем копию массива значений (FloatArray содержит 3 значения: x, y, z)
                        val values = FloatArray(event.values.size)
                        System.arraycopy(event.values, 0, values, 0, event.values.size)

                        if (!emitter.isDisposed) {
                            emitter.onNext(values)
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Можно логировать изменения точности, но обычно не требуется
                }
            }

            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

            if (accelerometer == null) {
                emitter.onError(IllegalStateException("Акселерометр недоступен на этом устройстве"))
                return@create
            }

            // Регистрируем слушатель сенсора
            val success = sensorManager.registerListener(
                listener,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            if (!success) {
                emitter.onError(IllegalStateException("Не удалось зарегистрировать слушатель акселерометра"))
                return@create
            }

            // Отписываемся от сенсора когда от Observable отписываются
            emitter.setDisposable(Disposable.fromAction {
                sensorManager.unregisterListener(listener)
            })
        }
            .subscribeOn(AndroidSchedulers.mainThread()) // SensorManager должен вызываться из главного потока
            .observeOn(AndroidSchedulers.mainThread())
            .replay(1) // Сохраняем последнее значение для новых подписчиков
            .refCount() // Делает Observable "горячим": автоматически подписывается при первой подписке 
        // и отписывается когда количество подписчиков становится 0
        // Все подписчики получают одни и те же данные
    }


}




