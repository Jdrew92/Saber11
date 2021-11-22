package com.misiontic.proyecto.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.misiontic.proyecto.Entities.Usuario
import com.misiontic.proyecto.R
import com.misiontic.proyecto.database.Saber11Database
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class RegistroActivity : AppCompatActivity() {

    private var roles:ArrayList<String> = ArrayList()
    private var rol:String? = null

    private lateinit var edtEmail: EditText
    private lateinit var edtNombres: EditText
    private lateinit var edtApellidos: EditText
    private lateinit var edtTelefono: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtPassword2: EditText
    private lateinit var chkTerminos: CheckBox
    private lateinit var tvErrorTerminos: TextView
    private lateinit var spRol: SmartMaterialSpinner<String>
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        initSpinner()

        edtEmail = findViewById(R.id.edtEmail)
        edtNombres = findViewById(R.id.edtNombres)
        edtApellidos = findViewById(R.id.edtApellidos)
        edtTelefono = findViewById(R.id.edtTelefono)
        edtPassword = findViewById(R.id.edtPassword)
        edtPassword2 = findViewById(R.id.edtPassword2)
        chkTerminos = findViewById(R.id.chkTerminos)
        tvErrorTerminos = findViewById(R.id.tvErrorTerminos)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        spRol = findViewById(R.id.spRol)
        tvErrorTerminos.visibility = TextView.GONE

        spRol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                rol = spRol.selectedItem
                spRol.errorText = null
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                spRol.errorText = getString(R.string.rol_error_msg)
            }

        }

        edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isMailValid(s)
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidPasswordFormat(s.toString())) {
                    edtPassword.requestFocus()
                    edtPassword.error = getString(R.string.password_not_valid_msg)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        edtPassword2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidPasswordFormat(s.toString())) {
                    edtPassword2.requestFocus()
                    edtPassword2.error = getString(R.string.password_not_valid_msg)
                }
                if (!passwordMatches(edtPassword.text.toString(), s.toString())) {
                    edtPassword2.requestFocus()
                    edtPassword2.error = getString(R.string.does_not_match_passwords_msg)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        chkTerminos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) tvErrorTerminos.visibility = TextView.GONE
        }

        btnRegistrar.setOnClickListener {
            if (allInfoIsFilled()) {

                val db = Saber11Database.getDatabase(this)
                val usuarioDao = db.usuarioDao()
                val usuario = Usuario(
                    0, edtNombres.text.toString(),
                    edtApellidos.text.toString(), edtTelefono.text.toString(),
                    edtEmail.text.toString().trim().lowercase(), edtPassword.text.toString(),
                rol)

                runBlocking {
                    launch {
                        val emailSearched = usuarioDao.getUsuarioByEmail(edtEmail.text.toString())
                        if(emailSearched.isNullOrBlank()){
                            val result = usuarioDao.insert(usuario)
                            if(result != -1L){
                                Toast.makeText(this@RegistroActivity, "Se ha registrado con éxito!", Toast.LENGTH_LONG).show()
                                val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                                finish()
                                startActivity(intent)
                            }
                        } else {
                            edtEmail.requestFocus()
                            edtEmail.error = getString(R.string.email_exist_error)
                        }
                    }
                }
            }
        }
    }

    private fun initSpinner() {
        spRol = findViewById(R.id.spRol)
        roles.add("Docente")
        roles.add("Estudiante")
        spRol.item = roles
    }

    private fun isMailValid(text: CharSequence?): Boolean {
        return if (text.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            edtEmail.requestFocus()
            edtEmail.error = getString(R.string.email_error_msg)
            false
        } else {
            true
        }
    }

    private fun allInfoIsFilled(): Boolean {
        return when {
            edtNombres.text.isNullOrEmpty() -> {
                edtNombres.requestFocus()
                edtNombres.error = getString(R.string.blank_error_msg)
                false
            }
            edtApellidos.text.isNullOrEmpty() -> {
                edtApellidos.requestFocus()
                edtApellidos.error = getString(R.string.blank_error_msg)
                false
            }
            edtTelefono.text.isNullOrEmpty() -> {
                edtTelefono.requestFocus()
                edtTelefono.error = getString(R.string.blank_error_msg)
                false
            }
            edtEmail.text.isNullOrEmpty() -> {
                edtEmail.requestFocus()
                edtEmail.error = getString(R.string.blank_error_msg)
                false
            }
            edtPassword.text.isNullOrEmpty() -> {
                edtPassword.requestFocus()
                edtPassword.error = getString(R.string.blank_error_msg)
                false
            }
            edtPassword2.text.isNullOrEmpty() -> {
                edtPassword2.requestFocus()
                edtPassword2.error = getString(R.string.blank_error_msg)
                false
            }
            spRol.selectedItem.isNullOrEmpty() -> {
                spRol.requestFocus()
                spRol.errorText = getString(R.string.rol_error_msg)
                false
            }
            !chkTerminos.isChecked -> {
                chkTerminos.requestFocus()
                tvErrorTerminos.visibility = TextView.VISIBLE
                false
            }
            else -> {
                true
            }
        }
    }

    private fun isValidPasswordFormat(password: String): Boolean {
        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )
        return passwordREGEX.matcher(password).matches()
    }

    private fun passwordMatches(psw: String, psw2: String): Boolean {
        return psw == psw2
    }
}