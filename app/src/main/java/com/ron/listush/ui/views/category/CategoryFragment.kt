package com.ron.listush.ui.views.category


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ron.listush.OnItemClickListener
import com.ron.listush.R
import com.ron.listush.data.Item
import com.ron.listush.databinding.FragmentCategoryBinding
import com.ron.listush.ui.adapters.categoryadapter.CategoryAdapter
import com.ron.listush.ui.viewmodels.ItemViewModel
import com.ron.listush.utils.DataUtil.defaultName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category), OnItemClickListener {

    private val viewModel: ItemViewModel by viewModels()
    private var name = defaultName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = viewModel.name
        val categoryAdapter = CategoryAdapter(this)

        setBinding(view, categoryAdapter)

        setFragmentResultListener()

        setObserve(categoryAdapter)

        setEventListener()
    }

    private fun setBinding(view: View, categoryAdapter: CategoryAdapter) {
        val binding = FragmentCategoryBinding.bind(view)
        binding.apply {
            list_items.apply {
                adapter = categoryAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

                //onMove it's only for up and down
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item = categoryAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onItemSwiped(item)
                }

            }).attachToRecyclerView(list_items)

            addItem.setOnClickListener {
                viewModel.onAddNewItemClick()
            }
        }
    }

    private fun setFragmentResultListener() {
        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }
    }

    private fun setObserve(categoryAdapter: CategoryAdapter) {
        if (name == defaultName) {
            viewModel.allItems.asLiveData().observe(viewLifecycleOwner) {
                categoryAdapter.submitList(it)
            }
        } else {
            viewModel.itemsByCategory(name).observe(viewLifecycleOwner) {
                categoryAdapter.submitList(it)
            }
        }
    }

    private fun setEventListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.itemsEvent.collect { event ->
                when (event) {
                    ItemViewModel.ItemsEvent.NavigateToAddItemScreen -> {
                        val action =
                            CategoryFragmentDirections.actionCategoryFragmentToAddItemFragment(
                                null,
                                "New Item",
                                name
                            )
                        findNavController().navigate(action)
                    }
                    is ItemViewModel.ItemsEvent.NavigateToEditItemScreen -> {
                        val action =
                            CategoryFragmentDirections.actionCategoryFragmentToAddItemFragment(
                                event.item,
                                "Edit Item",
                                name
                            )
                        findNavController().navigate(action)
                    }
                    is ItemViewModel.ItemsEvent.ShowItemConfirmation -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onItemClick(item: Item) {
        viewModel.onItemClick(item)
    }

    override fun onCheckboxClick(item: Item, isChecked: Boolean) {
        viewModel.onCheckboxClicked(item, isChecked)
    }


}