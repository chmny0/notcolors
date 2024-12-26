package com.example.colors

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.colors.PizzaDao
import com.example.colors.PizzaViewModel
import com.example.colors.PizzaViewModelFactory
import com.example.colors.R
import com.google.android.material.snackbar.Snackbar

class MainActivity : ComponentActivity() {

    private lateinit var editTextPizzaName: EditText
    private lateinit var editTextPizzaPrice: EditText
    private lateinit var buttonAddPizza: Button
    private lateinit var listViewPizzas: ListView

    private lateinit var pizzaDao: PizzaDao
    private lateinit var viewModel: PizzaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        editTextPizzaName = findViewById(R.id.editTextPizzaName)
        editTextPizzaPrice = findViewById(R.id.editTextPizzaPrice)
        buttonAddPizza = findViewById(R.id.buttonAddPizza)
        listViewPizzas = findViewById(R.id.listViewPizzas)

        pizzaDao = PizzaDao(this)

        viewModel = ViewModelProvider(this, PizzaViewModelFactory(pizzaDao)).get(PizzaViewModel::class.java)

        viewModel.pizzas.observe(this, Observer { pizzas ->
            updatePizzaList(pizzas)
        })

        buttonAddPizza.setOnClickListener {
            val name = editTextPizzaName.text.toString()
            val priceString = editTextPizzaPrice.text.toString()

            if (name.isNotBlank() && priceString.isNotBlank()) {
                val price = priceString.toDoubleOrNull()
                if (price != null && price >= 0) {
                    viewModel.addPizza(name, price)

                    // Показать Snackbar с сообщением об успешном добавлении пиццы
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Пицца $name успешно добавлена!",
                        Snackbar.LENGTH_SHORT
                    ).setAction("ОК") {}.show()

                    editTextPizzaName.text.clear()
                    editTextPizzaPrice.text.clear()
                }
            }
        }

        viewModel.fetchPizzas()
    }

    private fun updatePizzaList(pizzas: List<Pair<String, Double>>) {
        val pizzaNames = pizzas.map { "${it.first} - ${it.second}₽" }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pizzaNames)
        listViewPizzas.adapter = adapter
    }
}
