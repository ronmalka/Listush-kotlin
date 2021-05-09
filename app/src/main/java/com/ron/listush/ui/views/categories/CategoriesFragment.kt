package com.ron.listush.ui.views.categories

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ron.listush.R
import com.ron.listush.data.Category
import com.ron.listush.databinding.FragmentAllCategoriesBinding
import com.ron.listush.ui.adapters.categoriesadapter.CategoriesAdapter
import com.ron.listush.ui.adapters.categoryadapter.CategoryAdapter
import com.ron.listush.ui.viewmodels.CategoryViewModel
import com.ron.listush.ui.viewmodels.ItemViewModel
import com.ron.listush.ui.views.category.CategoryFragmentDirections
import com.ron.listush.utils.DataUtil.categoriesName
import com.ron.listush.utils.DataUtil.categoriesResourceMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CategoriesFragment constructor(val categories: MutableList<Category> = ArrayList()) :
    Fragment(R.layout.fragment_all_categories) {

    private val viewModel: CategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCategories()

        val categoriesAdapter = CategoriesAdapter(viewModel)

        setBinding(view, categoriesAdapter)

        categoriesAdapter.submitList(categories)

        setHasOptionsMenu(true)

        setEventListener()

    }

    private fun initCategories() {
        for (category in categoriesName) {
            categoriesResourceMap.get(category)?.let { Category(category, it) }
                ?.let {
                    if (!categories.contains(it)) {
                        categories.add(it)
                    }
                }
        }
    }

    private fun setBinding(view: View, categoriesAdapter: CategoriesAdapter) {
        val binding = FragmentAllCategoriesBinding.bind(view)
        binding.apply {
            categories.apply {
                adapter = categoriesAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
                setHasFixedSize(true)
            }
        }
    }

    private fun setEventListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.categoryEvent.collect { event ->
                when (event) {
                    is CategoryViewModel.CategoryEvent.NavigateToCategoryScreen -> {
                        val action =
                            CategoriesFragmentDirections.actionCategoriesFragmentToCategoryFragment(
                                event.name
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun createNewList() {
        viewModel.createNewList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_list -> {
                createNewList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}