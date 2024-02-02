package com.josesorli.misamigos

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var provinceEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var resultButton: Button
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
        resultText = findViewById(R.id.textViewResultados)

        resultText.movementMethod = ScrollingMovementMethod.getInstance()
        db = DatabaseHandler(this)

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                val id = db.addContact(name, email)
                if (id != -1L) {
                    // Éxito al guardar en la base de datos
                    // Puedes mostrar un mensaje de éxito o realizar alguna otra acción aquí
                    nameEditText.text.clear()
                    emailEditText.text.clear()
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
                resultText.text = "${resultText.text} ID: ${contact.id} Name: ${contact.name} Email: ${contact.email} \n"
                //Log.d("Contacto", "ID: ${contact.id} Name: ${contact.name} Email: ${contact.email}")
            }
        }
    }
}
