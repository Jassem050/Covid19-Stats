package com.example.covid19_stats.ui

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CursorAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.covid19_stats.NavGraphDirections
import com.example.covid19_stats.R
import com.example.covid19_stats.viewmodels.CovidStatsViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProvider(this@MainActivity).get(CovidStatsViewModel::class.java)

        viewModel.searchQuery.observe(this, Observer {
            findNavController(R.id.nav_host_fragment).navigate(NavGraphDirections.actionGlobalSearchFragment(it))
        })

        val searchView = findViewById<SearchView>(R.id.search_view)
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = SimpleCursorAdapter(this, R.layout.search_item, null,
            from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
//        val suggestions = listOf("Apple", "Blueberry", "Carrot", "Daikon")
        val sugg = mutableListOf<String>()
        viewModel.countries.observe(this, Observer {
            if (sugg.size > 0) {
                sugg.clear()
            }
            it?.let {
                it.map {
                    sugg.add(it.name)
                }
            }
        })

        searchView.suggestionsAdapter = cursorAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard(searchView)
                viewModel.setSearchQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                newText?.let {
                    sugg.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(newText, true)) {
                            cursor.addRow(arrayOf(index, suggestion))
                            Log.d("MainActivity", index.toString())
                        }
                    }
                }

                cursorAdapter.changeCursor(cursor)
                return false
            }

        })

        searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                hideKeyboard(searchView)
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)
                findNavController(R.id.nav_host_fragment).navigate(NavGraphDirections.actionGlobalSearchFragment(selection))
                // Do something with selection
                return true
            }
        })




    }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

//    fun Fragment.hideKeyboard() {
//        view?.let {
//            activity?.hideKeyboard(it)
//        }
//    }
}
