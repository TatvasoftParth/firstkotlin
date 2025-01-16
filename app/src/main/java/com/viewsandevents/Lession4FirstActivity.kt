package com.viewsandevents

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.viewsandevents.databinding.ActivityLession4FirstBinding
import java.util.Calendar

class Lession4FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLession4FirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val primaryBoard = arrayOf("SSC", "HHC")
        val secondaryBoard = arrayOf("BCA", "B.COM")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLession4FirstBinding
            .inflate(layoutInflater)
        setContentView(binding.root)

        // Create an Primary Board ArrayAdapter
        val primaryAdapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_dropdown_item, primaryBoard)
        binding.spinnerSscHsc.setAdapter(primaryAdapter)

        // Create an Secondary Board ArrayAdapter
        val secondaryAdapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_dropdown_item, secondaryBoard)
        binding.spinnerBcaBcom.setAdapter(secondaryAdapter)

        // Handle SSC/HSC Slider
        binding.sliderSscPercentage.addOnChangeListener { _, value, _ ->
            val sscPercentage = "${value.toInt()}%"
            binding.tvSscPercentageValue.text = sscPercentage
        }

        // Handle BCA/B.COM Slider
        binding.sliderBcaPercentage.addOnChangeListener { _, value, _ ->
            val bcaPercentage = "${value.toInt()}%"
            binding.tvBcaPercentageValue.text = bcaPercentage
        }

        binding.inputName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.inputName.text.toString().isEmpty()) {
                binding.inputNameLayout.error = getString(R.string.name_is_required)
            } else {
                binding.inputNameLayout.error = null
            }
        }

        // Validation for phone
        binding.inputPhone.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val phone = binding.inputPhone.text.toString()
                if (phone.isEmpty() || !phone.matches(Regex(getString(R.string._0_9_10_regex)))) {
                    binding.inputPhoneLayout.error = getString(R.string.enter_a_valid_10_digit_phone_number)
                } else {
                    binding.inputPhoneLayout.error = null
                }
            }
        }

        binding.inputEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.inputName.text.toString().isEmpty()) {
                binding.inputEmailLayout.error = getString(R.string.email_is_required)
            } else {
                binding.inputEmailLayout.error = null
            }
        }

        binding.inputCountry.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.inputName.text.toString().isEmpty()) {
                binding.inputCountryLayout.error = getString(R.string.country_is_required)
            } else {
                binding.inputCountryLayout.error = null
            }
        }

        binding.inputAddress.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.inputName.text.toString().isEmpty()) {
                binding.inputAddressLayout.error = getString(R.string.address_is_required)
            } else {
                binding.inputAddressLayout.error = null
            }
        }

        // Date picker for DOB
        binding.btnSelectDob.setOnClickListener {
            onClickDatePicker()
        }

        // Validate and Submit
        binding.btnSubmit.setOnClickListener {
            validatingAndSubmitData()
        }
    }

    private fun onClickDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.btnSelectDob.text = date
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun validatingAndSubmitData() {
        val name = binding.inputName.text.toString()
        val phone = binding.inputPhone.text.toString()
        val email = binding.inputEmail.text.toString()
        val country = binding.inputCountry.text.toString()
        val address = binding.inputAddress.text.toString()
        val selectedGenderId = binding.radioGender.checkedRadioButtonId
        val gender = if (selectedGenderId != -1) findViewById<RadioButton>(selectedGenderId).text else null
        val dob = binding.btnSelectDob.text.toString()

        if (TextUtils.isEmpty(name)) {
            binding.inputNameLayout.error = getString(R.string.name_is_required)
        } else if (TextUtils.isEmpty(phone) || !phone.matches(Regex(getString(R.string._0_9_10_regex)))) {
            binding.inputPhone.error = getString(R.string.enter_a_valid_10_digit_phone_number)
        } else if (TextUtils.isEmpty(email)) {
            binding.inputNameLayout.error = getString(R.string.email_is_required)
        } else if (TextUtils.isEmpty(country)) {
            binding.inputNameLayout.error = getString(R.string.country_is_required)
        } else if (TextUtils.isEmpty(address)) {
            binding.inputNameLayout.error = getString(R.string.address_is_required)
        } else if (gender == null) {
            Toast.makeText(this, getString(R.string.please_select_a_gender), Toast.LENGTH_SHORT).show()
        } else if (dob == getString(R.string.select_dob)) {
            Toast.makeText(this,
                getString(R.string.please_select_your_date_of_birth), Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(binding.spinnerSscHsc.text)) {
            Toast.makeText(this, "Please select SSC/HSC", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(binding.spinnerBcaBcom.text)) {
            Toast.makeText(this, "Please select BCA/B.COM", Toast.LENGTH_SHORT).show()
        } else {
            // Successful submission
            successfullySubmitted()
        }
    }

    private fun successfullySubmitted() {
        binding.inputName.text = null
        binding.inputPhone.text = null
        binding.btnSelectDob.text = getString(R.string.select_dob)
        binding.radioMale.isChecked = false
        binding.radioFemale.isChecked = false
        binding.checkboxReading.isChecked = false
        binding.checkboxSports.isChecked = false
        binding.sliderSscPercentage.value = 0F
        binding.sliderBcaPercentage.value = 0F
        binding.spinnerSscHsc.text = null
        binding.spinnerBcaBcom.text = null
        binding.tvSscPercentageValue.text = "0"
        binding.tvBcaPercentageValue.text = "0"
        Toast.makeText(this,
            getString(R.string.form_submitted_successfully), Toast.LENGTH_SHORT).show()
    }
}