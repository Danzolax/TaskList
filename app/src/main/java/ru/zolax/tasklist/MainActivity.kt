package ru.zolax.tasklist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ItemViewModelDelete {
    private val itemViewModel: ItemViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonAddItem: FloatingActionButton = findViewById(R.id.button_add_item)
        buttonAddItem.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        val adapter = ItemAdapter(this)
        val list = findViewById<RecyclerView>(R.id.list)
        list.adapter = adapter
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(list)

        itemViewModel.allItems.observe(this, { t ->
            adapter.setItems(t)
            adapter.notifyDataSetChanged()
        })
    }

    override fun delete(item: Item) {
        itemViewModel.delete(item)
    }


}