package com.example.stockwatch_assistant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.toSpannable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockwatch_assistant.alphaVantageAPI.StockMeta

import com.example.stockwatch_assistant.databinding.FragmentChangeNameBinding
import com.example.stockwatch_assistant.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ChangeNameFragment: Fragment(R.layout.fragment_change_name) {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentChangeNameBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeNameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.CurrentName.text = "Current Name: " + viewModel.observeUserName().value!!

        binding.cancelButton.setOnClickListener{
            (activity as MainActivity).replaceFragment(HomeFragment())
        }

        binding.addButton.setOnClickListener{
            val newName = binding.editTextTextPersonName.getText().toString()
            Log.d("XXX", "newName: $newName")

            if (newName.isEmpty()){
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else{
                val user = FirebaseAuth.getInstance().currentUser
                val update = UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build()
                user!!.updateProfile(update)

                viewModel.updateUserName(newName)

                (activity as MainActivity).replaceFragment(HomeFragment())
            }
        }

        }
    }









