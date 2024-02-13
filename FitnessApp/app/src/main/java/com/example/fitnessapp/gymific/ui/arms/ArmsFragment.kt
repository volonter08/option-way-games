package com.example.fitnessapp.gymific.ui.arms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.ersiver.gymific.ui.common.WorkoutAdapter
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FramgentArmsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArmsFragment : Fragment() {
    private val armsViewModel: ArmsViewModel by navGraphViewModels(R.id.mobile_navigation) { defaultViewModelProviderFactory }
    private lateinit var binding: FramgentArmsBinding
    private lateinit var adapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FramgentArmsBinding.inflate(inflater, container, false)
        binding.apply {
            viewModel = armsViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = WorkoutAdapter(true)
        binding.apply {
            armList.adapter = adapter
            val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            armList.addItemDecoration(decoration)
        }
    }
}