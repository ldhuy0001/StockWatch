package com.example.stockwatch_assistant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels


import com.example.stockwatch_assistant.databinding.RecyclerMainBinding
import com.example.stockwatch_assistant.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


//    private var _bindingHome: FragmentHomeBinding? = null
//    private val bindingHome get() = _bindingHome!!




    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        Log.d(TAG, "onCreateView ${viewModel.selected}")

//        binding.hello.text = "test"





        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeUserName().observe(viewLifecycleOwner){
            binding.hello.text = "Hello $it! Welcome to StockWatch-Assistant!"
        }




    }


    }