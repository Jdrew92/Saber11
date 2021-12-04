package com.misiontic.saber11.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.misiontic.saber11.R
import com.misiontic.saber11.databinding.ActivityRegistroBinding
import com.misiontic.saber11.entities.Usuario
import com.misiontic.saber11.enums.Rol
import com.misiontic.saber11.utils.Database
import java.util.*
import java.util.regex.Pattern

class RegistroActivity : AppCompatActivity() {

    private var roles: MutableList<Any> = mutableListOf()
    private var rol: String? = null
    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        initSpinner()

        binding.tvErrorTerminos.visibility = TextView.GONE

        binding.spRol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                rol = binding.spRol.selectedItem.toString()
                binding.spRol.errorText = null
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.spRol.errorText = getString(R.string.rol_error_msg)
            }

        }

        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isMailValid(s)
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidPasswordFormat(s.toString())) {
                    binding.edtPassword.requestFocus()
                    binding.edtPassword.error = getString(R.string.password_not_valid_msg)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtPassword2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidPasswordFormat(s.toString())) {
                    binding.edtPassword2.requestFocus()
                    binding.edtPassword2.error = getString(R.string.password_not_valid_msg)
                }
                if (!passwordMatches(binding.edtPassword.text.toString(), s.toString())) {
                    binding.edtPassword2.requestFocus()
                    binding.edtPassword2.error = getString(R.string.does_not_match_passwords_msg)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.chkTerminos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.tvErrorTerminos.visibility = TextView.GONE
        }

        binding.btnRegistrar.setOnClickListener {
            if (allInfoIsFilled()) {
                val email = binding.edtEmail.text.toString().trim().lowercase()
                val password = binding.edtPassword.text.toString()
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val usuario = Usuario(
                                user!!.uid,
                                binding.edtNombres.text.toString(),
                                binding.edtApellidos.text.toString(),
                                binding.edtTelefono.text.toString(),
                                email,
                                password,
                                rol!!
                            )
                            Database.getUsuariosReference().child(usuario.id!!).setValue(usuario)
                            Toast.makeText(
                                this@RegistroActivity,
                                getString(R.string.registro_exitoso),
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                            finish()
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                baseContext,
                                getString(R.string.registro_error), Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            }
        }
    }

    private fun initSpinner() {
        for (rol in Rol.values()) {
            roles.add(rol.value)
        }
        binding.spRol.item = roles
    }

    private fun isMailValid(text: CharSequence?): Boolean {
        return if (text.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            binding.edtEmail.requestFocus()
            binding.edtEmail.error = getString(R.string.email_error_msg)
            false
        } else {
            true
        }
    }

    private fun allInfoIsFilled(): Boolean {
        return when {
            binding.edtNombres.text.isNullOrEmpty() -> {
                binding.edtNombres.requestFocus()
                binding.edtNombres.error = getString(R.string.blank_error_msg)
                false
            }
            binding.edtApellidos.text.isNullOrEmpty() -> {
                binding.edtApellidos.requestFocus()
                binding.edtApellidos.error = getString(R.string.blank_error_msg)
                false
            }
            binding.edtTelefono.text.isNullOrEmpty() -> {
                binding.edtTelefono.requestFocus()
                binding.edtTelefono.error = getString(R.string.blank_error_msg)
                false
            }
            binding.edtEmail.text.isNullOrEmpty() -> {
                binding.edtEmail.requestFocus()
                binding.edtEmail.error = getString(R.string.blank_error_msg)
                false
            }
            binding.edtPassword.text.isNullOrEmpty() -> {
                binding.edtPassword.requestFocus()
                binding.edtPassword.error = getString(R.string.blank_error_msg)
                false
            }
            binding.edtPassword2.text.isNullOrEmpty() -> {
                binding.edtPassword2.requestFocus()
                binding.edtPassword2.error = getString(R.string.blank_error_msg)
                false
            }
            binding.spRol.selectedItem.toString().isEmpty() -> {
                binding.spRol.requestFocus()
                binding.spRol.errorText = getString(R.string.rol_error_msg)
                false
            }
            !binding.chkTerminos.isChecked -> {
                binding.chkTerminos.requestFocus()
                binding.tvErrorTerminos.visibility = TextView.VISIBLE
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