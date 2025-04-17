package com.example.ingrediscan.ui.search

import com.example.ingrediscan.R 
import android.content.Context
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import androidx.core.content.ContextCompat

data class SearchResult(
    val id: Int,
    val name: String,
    val calories: Int,      // in kcal
    val protein: Int,       // in grams
    val carbs: Int,         // in grams
    val fat: Int,           // in grams
    val grade: String,
    val description: String
) {
    fun getProteinString(): String {
        return "Protein\n${protein}g"
    }

    fun getCarbsString(): String {
        return "Carbs\n${carbs}g"
    }

    fun getFatString(): String {
        return "Fat\n${fat}g"
    }

    fun getProteinPerc(): Float {
        val grams_sum = protein + carbs + fat
        return (protein.toFloat() / grams_sum.toFloat()) * 100f
    }

    fun getCarbsPerc(): Float {
        val grams_sum = protein + carbs + fat
        return (carbs.toFloat() / grams_sum.toFloat()) * 100f
    }

    fun getFatPerc(): Float {
        val grams_sum = protein + carbs + fat
        return (fat.toFloat() / grams_sum.toFloat()) * 100f
    }

    fun makePieChart(context: Context, pieChart: PieChart) {
        // create entries
        val entries = listOf(
            PieEntry(getProteinPerc(), getProteinString()),
            PieEntry(getCarbsPerc(), getCarbsString()),
            PieEntry(getFatPerc(), getFatString())
        )

        // initialize slies colors
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(
            ContextCompat.getColor(context, R.color.green),
            ContextCompat.getColor(context, R.color.blue),
            ContextCompat.getColor(context, R.color.orange)
        )

        // instantiate pie chart
        val data = PieData(dataSet)
        pieChart.data = data

        // only show getString() values when slice is tapped
        val marker = PieMarkerView(context, R.layout.marker_view)
        marker.chartView = pieChart
        pieChart.marker = marker

        // disable default labels, descriptions, and legend
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false
        dataSet.setDrawValues(false)
        pieChart.setDrawEntryLabels(false)
        pieChart.setUsePercentValues(false)

        // enable pop out when slice is tapped
        pieChart.isHighlightPerTapEnabled = true
        
        // clear pie chart at end
        pieChart.invalidate()
    }
}