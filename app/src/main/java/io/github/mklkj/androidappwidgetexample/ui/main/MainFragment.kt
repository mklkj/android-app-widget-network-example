package io.github.mklkj.androidappwidgetexample.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.mklkj.androidappwidgetexample.R
import io.github.mklkj.androidappwidgetexample.databinding.MainFragmentBinding

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: MainFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }

        viewModel.openInBrowser.observe(viewLifecycleOwner) {
            if (it != null) {
                startActivity(Intent(Intent.ACTION_VIEW, it.toUri()))
            }
        }
    }
}
