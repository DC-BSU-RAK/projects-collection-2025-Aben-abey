package com.example.meaninglesscalculator // Make sure this matches your package name

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog // Import AlertDialog
import com.example.meaninglesscalculator.databinding.ActivityMainBinding // Import view binding class
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val absurdResults = listOf(
        "Tuesday", "A flock of philosophical seagulls", "The square root of a pickle",
        "Infinite monkeys typing Shakespeare (badly)", "A cat wearing a tiny hat",
        "The color of surprise", "42, but only on Wednesdays", "A whispered secret",
        "Probably a banana", "Ask again later... or don't", "An existential void with glitter",
        "The sound of one hand clapping (off-key)", "A dream about cheese", "Definitely not math",
        "A confused platypus", "Cosmic background radiation, but jazzier", "The meaning of lint",
        "A polite shrug", "Spaghetti code, literally", "Quantum entanglement of socks",
        "A silent disco for dust bunnies", "The airspeed velocity of an unladen swallow (African or European?)",
        "A sentient cloud of question marks", "The collective sigh of forgotten passwords", "A potato that believes it's a king",
        "A polite unicorn asking for directions", "The echo of a thought you almost had", "Seven",
        "Perhaps a cup of tea would help?", "The universe just winked."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up listeners for operation buttons
        binding.buttonAdd.setOnClickListener { showAbsurdResult() }
        binding.buttonSubtract.setOnClickListener { showAbsurdResult() }
        binding.buttonMultiply.setOnClickListener { showAbsurdResult() }
        binding.buttonDivide.setOnClickListener { showAbsurdResult() }
        binding.buttonEquals.setOnClickListener { showAbsurdResult() }

        binding.buttonClear.setOnClickListener {
            binding.editTextOperand1.text.clear()
            binding.editTextOperand2.text.clear()
            binding.textViewResult.text = "Result will appear here"
            Log.d("MeaninglessCalculator", "Fields Cleared")
        }

        // Listener for the "About" button
        binding.buttonAbout.setOnClickListener {
            showAppExplanationDialog()
        }
    }

    private fun showAbsurdResult() {
        val operand1 = binding.editTextOperand1.text.toString()
        val operand2 = binding.editTextOperand2.text.toString()

        // We log the inputs, but they don't affect the result!
        Log.d("MeaninglessCalculator", "Operand 1: '$operand1', Operand 2: '$operand2'")
        Log.d("MeaninglessCalculator", "An operation was attempted. Good luck with that!")

        val randomIndex = Random.nextInt(absurdResults.size)
        val result = absurdResults[randomIndex]
        binding.textViewResult.text = result
        Log.d("MeaninglessCalculator", "Absurd Result: '$result'")
    }

    private fun showAppExplanationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Welcome to The Meaningless Calculator!")
            .setMessage("Curious how this works?\n\nIt doesn't! At least, not like a normal calculator.\n\nSimply enter any text or numbers into the fields, choose an operation (+, -, ×, ÷, =), and discover a wonderfully random and absurd 'result'. Your inputs are just for show – the fun lies in the unpredictable answers!\n\nEnjoy the meaninglessness!")
            .setPositiveButton("Got it!") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog when the button is clicked
            }
            .create()
            .show()
        Log.d("MeaninglessCalculator", "App explanation dialog shown.")
    }
}