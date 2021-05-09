package com.ron.listush.ui.views.items

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ron.listush.R
import com.ron.listush.databinding.FragmentAddItemBinding
import com.ron.listush.ui.adapters.categoriesadapter.CategoriesAdapter
import com.ron.listush.ui.viewmodels.AddItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class AddItemFragment : Fragment(R.layout.fragment_add_item) {

    private val viewModel: AddItemViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddItemBinding.bind(view)

        setBinding(binding)

        setEventListener(binding)
    }

    private fun setBinding(binding: FragmentAddItemBinding) {


        binding.apply {
            editName.setText(viewModel.itemName)
            editQuantity.setText(viewModel.itemQuantity)

            editName.addTextChangedListener {
                viewModel.itemName = it.toString()
            }

            editQuantity.addTextChangedListener {
                viewModel.itemQuantity = it.toString()
            }

            save.setOnClickListener {
                viewModel.onSaveClick()
            }
        }
    }

    private fun setEventListener(binding: FragmentAddItemBinding) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addItemEvent.collect { event ->
                when (event) {
                    is AddItemViewModel.AddItemEvent.ShowInvalidInput -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddItemViewModel.AddItemEvent.NavigateBackWithResult -> {
                        binding.editName.clearFocus()
                        binding.editQuantity.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

}