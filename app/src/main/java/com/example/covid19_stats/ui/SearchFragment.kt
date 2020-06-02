package com.example.covid19_stats.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian3d
import com.example.covid19_stats.R
import com.example.covid19_stats.databinding.FragmentSearchBinding
import com.example.covid19_stats.viewmodels.SearchViewModel
import com.example.covid19_stats.viewmodels.SearchViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        val args = SearchFragmentArgs.fromBundle(requireArguments())
        Log.d("SearchFragment", args.query)

        val application = requireActivity().application
        val viewModelFactory = SearchViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.searchQuery.value = args.query
        viewModel.searchQuery.observe(viewLifecycleOwner, Observer {
            viewModel.getCountryAlpha2()
        })

        viewModel.alpha2.observe(viewLifecycleOwner, Observer {
            it?.let { Log.d("searchFragment", it) }
        })

        viewModel.country.observe(viewLifecycleOwner, Observer {
            Log.d("searchFragment", it.alpha2)
            viewModel.getCountryStats(it.alpha2)
        })

        val bar = AnyChart.column3d()

        viewModel.status.observe(viewLifecycleOwner, Observer {
            setGraph(binding, bar, it.cases.toInt(), it.recovered.toInt(), it.deaths.toInt())
        })

        binding.heading.text = getString(R.string.country_update, args.query)

        return binding.root
    }

    private fun setGraph(
        binding: FragmentSearchBinding,
        bar: Cartesian3d,
        totalCases: Int,
        totalRecoveries: Int,
        totalDeaths: Int
    ) {
        val data = mutableListOf<DataEntry>()
        data.add(ValueDataEntry("confirm", totalCases))
        data.add(ValueDataEntry("recoveries", totalRecoveries))
        data.add(ValueDataEntry("deaths", totalDeaths))
        bar.data(data)

        binding.caseGraph.setChart(bar)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
