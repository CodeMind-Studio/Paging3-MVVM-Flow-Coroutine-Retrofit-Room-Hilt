package com.example.pagingrickandmorty.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pagingrickandmorty.adapter.PagingErrorHandlingAdapter
import com.example.pagingrickandmorty.adapter.PersonAdapter
import com.example.pagingrickandmorty.databinding.ActivityMainBinding
import com.example.pagingrickandmorty.viewmodel.PersonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var personAdapter: PersonAdapter
    private val personViewModel: PersonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRecycler()
        getData()

    }

    private fun getData() {
        lifecycleScope.launch {
            personViewModel.getPerson().collectLatest {
                binding.apply {
                    recyclerMain.visibility = VISIBLE
                    progressBarMain.visibility = GONE
                }
                personAdapter.submitData(it)
            }
        }

    }

    private fun setRecycler() {
        binding.apply {
            recyclerMain.apply {
                adapter =
                    personAdapter
                        .withLoadStateHeaderAndFooter(header = PagingErrorHandlingAdapter { personAdapter.retry() },
                            footer = PagingErrorHandlingAdapter { personAdapter.retry() })
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(this@MainActivity, 2)
            }
        }
    }
}