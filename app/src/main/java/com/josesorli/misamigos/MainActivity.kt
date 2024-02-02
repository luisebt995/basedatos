package com.josesorli.misamigos

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var provinceEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var resultButton: Button
    private lateinit var searchButton: Button
    private lateinit var spnProvinces: Spinner
    private lateinit var resultText: TextView

    private lateinit var db: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        provinceEditText = findViewById(R.id.provinceEditText)
        saveButton = findViewById(R.id.saveButton)
        resultButton = findViewById(R.id.btnRevision)
        searchButton = findViewById(R.id.btnSearch)
        spnProvinces = findViewById(R.id.spnProvinces)
        resultText = findViewById(R.id.textViewResultados)



        //Set up a scroll view
        resultText.movementMethod = ScrollingMovementMethod.getInstance()

        //Initialize handler
        db = DatabaseHandler(this)

        //The ArrayAdapter will be responsible for rendering every item in the languages string array to the screen when the Kotlin dropdown menu is accessed
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, db.getDistinctProvinces())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //The adapter that we declared above is useless unless it is attached to our dropdown menu
        spnProvinces.adapter = adapter

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val province = provinceEditText.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && province.isNotEmpty()) {
                val id = db.addContact(name, email, province)
                if (id != -1L) {
                    // Éxito al guardar en la base de datos
                    // Puedes mostrar un mensaje de éxito o realizar alguna otra acción aquí
                    nameEditText.text.clear()
                    emailEditText.text.clear()
                    provinceEditText.text.clear()

                    val adapterRefresh = ArrayAdapter(this, android.R.layout.simple_spinner_item, db.getDistinctProvinces())
                    adapterRefresh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spnProvinces.adapter = adapterRefresh

                } else {
                    // Ocurrió un error al guardar en la base de datos
                    // Puedes mostrar un mensaje de error o realizar alguna otra acción aquí
                }
            } else {
                // Los campos están vacíos, muestra un mensaje de error o realiza alguna otra acción aquí
                Toast.makeText(applicationContext, "Te falta algún campo por rellenar", Toast.LENGTH_SHORT).show()
            }
        }

        resultButton.setOnClickListener {
            val contactList = db.getAllContact()
            resultText.text = ""

            //Quickest way to show it, but print is a mess
            //resultText.text = contactList.joinToString()

            //Iteration for showing register in TextView
            for(contact in contactList){
                resultText.text = "${resultText.text} ID: ${contact.id} Name: ${contact.name} Email: ${contact.email} Provincia: ${contact.province} \n"
                //Log.d("Contacto", "ID: ${contact.id} Name: ${contact.name} Email: ${contact.email}")
            }
        }

        searchButton.setOnClickListener {
            val provincia = provinceEditText.text.toString().trim()
            if (provincia.isNotEmpty()) {
                resultText.text = ""
                val contactList = db.getProvinceContact(provincia)
                for(contact in contactList){
                    resultText.text = "${resultText.text} ID: ${contact.id} Name: ${contact.name} Email: ${contact.email} Provincia: ${contact.province} \n"
                    //Log.d("Contacto", "ID: ${contact.id} Name: ${contact.name} Email: ${contact.email}")
                }
            }
            else Toast.makeText(applicationContext, "Especifique una provincia", Toast.LENGTH_SHORT).show()
        }

        spnProvinces.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected item here
                val selectedItem = parentView.getItemAtPosition(position).toString()
                // Do something with the selected item
                if(selectedItem != ""){
                    resultText.text = ""
                    val contactList = db.getProvinceContact(selectedItem)
                    for(contact in contactList){
                        resultText.text = "${resultText.text} ID: ${contact.id} Name: ${contact.name} Email: ${contact.email} Provincia: ${contact.province} \n"
                        //Log.d("Contacto", "ID: ${contact.id} Name: ${contact.name} Email: ${contact.email}")
                    }
                }else resultText.text = ""
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing here
            }
        }
    }
}
