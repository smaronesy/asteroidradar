package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = this.requireActivity().application

        val viewModelFactory = AsteroidViewModelFactory(application)

        val viewModel: MainViewModel by lazy {
            ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.pod.observe(viewLifecycleOwner, Observer {
            Picasso.get().load(it.url).into(binding.activityMainImageOfTheDay)
        })

        val adapter = MainAdapter(AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
            Log.i("TESTING", "${asteroid.codename}")
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner, Observer {asteroid -> asteroid?.let {
            this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
            viewModel.onAsteroidDetailNavigated()
        }})

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
