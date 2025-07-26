package com.example.test1_multiroute

import kotlin.math.ceil

object DeliveryCycleCalculator {

    fun calculateCycles(parcels: List<ExcelUtils.ParcelInfo>, vehicleCapacity: Int): Int {
        val totalParcelSize = parcels.sumOf { it.size }
        return ceil(totalParcelSize / vehicleCapacity.toDouble()).toInt()
    }
}