package com.example.stockwatch_assistant

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.stockwatch_assistant.databinding.FragmentChangeNameBinding
import com.example.stockwatch_assistant.databinding.FragmentChangeThemeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

//unused
class ChangeThemeFragment : Fragment(R.layout.fragment_change_theme) {


    private var _binding: FragmentChangeThemeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangeThemeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val string = viewModel.observeUserName().value
//        binding.editTextTextPersonName.setText(string)





        fun onRadioButtonClicked(view: View) {
            if (view is RadioButton) {
                // Is the button now checked?
                val checked = view.isChecked

                // Check which radio button was clicked
                when (view.getId()) {
                    R.id.light ->
                        if (checked) {
                            // Pirates are the best
                            Toast.makeText(requireContext(), "light", Toast.LENGTH_SHORT).show()
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                    R.id.dark ->
                        if (checked) {
                            // Ninjas rule

                            Toast.makeText(requireContext(), "dark", Toast.LENGTH_SHORT).show()
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


                        }
                }
            }
        }

        binding.dark.setOnClickListener {
            onRadioButtonClicked(view= binding.dark)
        }

        binding.light.setOnClickListener {
            onRadioButtonClicked(view= binding.light)
        }

        binding.homeButton.setOnClickListener {
            (activity as MainActivity).replaceFragment(HomeFragment())
        }








    }




}