package com.example.colors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PizzaViewModel(private val pizzaDao: PizzaDao) : ViewModel() {
    private val _pizzas = MutableLiveData<List<Pair<String, Double>>>()
    val pizzas: LiveData<List<Pair<String, Double>>> get() = _pizzas

    fun fetchPizzas() {
        _pizzas.value = pizzaDao.getAllPizzas()
    }

    fun addPizza(name: String, price: Double) {
        pizzaDao.insert(name, price)
        fetchPizzas()
    }
}

class PizzaViewModelFactory(private val pizzaDao: PizzaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PizzaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PizzaViewModel(pizzaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
