package ru.zolax.tasklist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat


class AddItemActivity : AppCompatActivity() {
    private val itemViewModel: ItemViewModel by viewModels()
    private lateinit var title: EditText;
    private lateinit var description: EditText;
    private lateinit var priority: SwitchCompat;
    private lateinit var itemToUpdate : Item
    private var actionFlag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        title = findViewById(R.id.titleEditText)
        description = findViewById(R.id.descriptionEditText)
        priority = findViewById(R.id.important)
        val position = intent.getIntExtra("position",-1)

        if(position!= -1){
            itemViewModel.allItems.observe(this, { t ->
                itemToUpdate = t[position]
                title.setText(t[position].title)
                description.setText(t[position].description)
                priority.isChecked = t[position].priority
            })
            actionFlag = 1
        }


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_item_menu, menu)
        return true
    }






    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.save_item -> {
                if (actionFlag == 0)
                    saveItem()
                else
                    updateItem()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateItem() {
        itemToUpdate.title =  title.text.toString()
        itemToUpdate.description =  description.text.toString()
        itemToUpdate.priority =  priority.isChecked
        itemViewModel.update(
            itemToUpdate
        )
        finish()
    }

    private fun saveItem() {
        itemViewModel.insert(
            Item(
                title.text.toString(),
                description.text.toString(),
                priority.isChecked
            )
        )
        finish()
    }
}