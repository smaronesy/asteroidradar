package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.Filter

class MainFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        /**
         * Lazily initialize our [viewViewModel].
         */

        val application = this.requireActivity().application
        val viewModelFactory = AsteroidViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)


        binding.viewModel = viewModel

        viewModel.pod.observe(viewLifecycleOwner, Observer {
            Picasso.get().load(it.url).into(binding.activityMainImageOfTheDay)
        })

        val adapter = MainAdapter(AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
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
        viewModel.updateAsteroidWithFilter(
            when(item.itemId) {
            R.id.show_week_menu -> Filter.WEEK
            R.id.show_today_menu -> Filter.TODAY
            else -> Filter.SAVED
        })
        return true
    }
}
