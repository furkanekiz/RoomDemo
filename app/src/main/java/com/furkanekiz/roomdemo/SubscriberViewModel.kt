package com.furkanekiz.roomdemo

import androidx.lifecycle.*
import com.furkanekiz.roomdemo.db.Subscriber
import com.furkanekiz.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel() {

    val subscribers = repository.subscribers

    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    val inputName = MutableLiveData<String?>()
    val inputEmail = MutableLiveData<String?>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (isUpdateOrDelete) {
            subscriberToUpdateOrDelete.name = inputName.value!!
            subscriberToUpdateOrDelete.email = inputEmail.value!!
            update(subscriberToUpdateOrDelete)
        } else {
            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(Subscriber(0, name, email))

            inputName.value = null
            inputEmail.value = null
        }

    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    private fun insert(subscriber: Subscriber) =
        viewModelScope.launch {
            repository.insert(subscriber)
            statusMessage.value = Event("Subscriber Inserted Successfully")
        }

    private fun update(subscriber: Subscriber) = viewModelScope.launch {
        repository.update(subscriber)

        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"

        statusMessage.value = Event("Subscriber Updated Successfully")
    }

    private fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)

        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"

        statusMessage.value = Event("Subscriber Deleted Successfully")
    }

    private fun clearAll() = viewModelScope.launch {
        repository.deleteAll()
        statusMessage.value = Event("All Subscriber Deleted Successfully")
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"

    }

    fun getSaveSubscribers() = liveData {
        repository.subscribers.collect {
            emit(it)
        }
    }

}