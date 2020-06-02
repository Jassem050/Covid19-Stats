package com.example.covid19_stats.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian3d
import com.example.covid19_stats.R
import com.example.covid19_stats.databinding.FragmentCovidStatsBinding
import com.example.covid19_stats.viewmodels.CovidStatsViewModel
import com.example.covid19_stats.viewmodels.CovidStatsViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CovidStatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CovidStatsFragment : Fragment() {
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
        val binding = FragmentCovidStatsBinding.inflate(inflater, container, false)

        val bar = AnyChart.column3d()

        val application = requireActivity().application
        val viewModelFactory = CovidStatsViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(CovidStatsViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.timeLine.observe(viewLifecycleOwner, Observer {
            it?.let {
                setGraph(binding, bar, it.totalCases.toInt(), it.totalRecovered.toInt(), it.totalDeaths.toInt())
            }
        })

        viewModel.searchQuery.observe(viewLifecycleOwner, Observer {
            Log.d("fragment", "search enterd")
                findNavController().navigate(R.id.action_covidStatsFragment_to_searchFragment)
        })



        return binding.root
    }

    private fun setGraph(
        binding: FragmentCovidStatsBinding,
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

    override fun onResume() {
        super.onResume()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CovidStatsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
