package com.furkanekiz.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.furkanekiz.roomdemo.databinding.AcMainBinding
import com.furkanekiz.roomdemo.db.SubscriberDatabase
import com.furkanekiz.roomdemo.db.SubscriberRepository

class ACMain : AppCompatActivity() {

    private lateinit var binding: AcMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.ac_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)

        subscriberViewModel = ViewModelProvider(this,factory)[SubscriberViewModel::class.java]

        binding.myViewModel = subscriberViewModel

        binding.lifecycleOwner = this

        displaySubscribersList()

    }

    private fun displaySubscribersList(){
        subscriberViewModel.getSaveSubscribers().observe(this) {
            Log.i("MyTag",it.toString())
        }

    }
}