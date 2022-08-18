package com.furkanekiz.roomdemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkanekiz.roomdemo.databinding.AcMainBinding
import com.furkanekiz.roomdemo.db.Subscriber
import com.furkanekiz.roomdemo.db.SubscriberDatabase
import com.furkanekiz.roomdemo.db.SubscriberRepository

class ACMain : AppCompatActivity() {

    private lateinit var binding: AcMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.ac_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)

        subscriberViewModel = ViewModelProvider(this, factory)[SubscriberViewModel::class.java]

        binding.myViewModel = subscriberViewModel

        binding.lifecycleOwner = this

        initRecyclerView()

        subscriberViewModel.message.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvSubscriber.layoutManager = LinearLayoutManager(this)

        adapter = MyRecyclerViewAdapter { selectedItem: Subscriber ->
            listItemClicked(
                selectedItem
            )
        }
        binding.rvSubscriber.adapter = adapter

        displaySubscribersList()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displaySubscribersList() {
        subscriberViewModel.getSaveSubscribers().observe(this) {
            Log.i("MyTag", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun listItemClicked(subscriber: Subscriber) {
        //Toast.makeText(this, "Selected name is ${subscriber.name}", Toast.LENGTH_LONG).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}