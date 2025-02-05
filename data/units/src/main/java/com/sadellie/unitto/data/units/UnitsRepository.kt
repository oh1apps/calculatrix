/*
 * Unitto is a unit converter for Android
 * Copyright (c) 2022-2023 Elshan Agaev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sadellie.unitto.data.units

import android.content.Context
import android.util.Log
import com.sadellie.unitto.data.database.CurrencyRatesDao
import com.sadellie.unitto.data.database.CurrencyRatesEntity
import com.sadellie.unitto.data.database.UnitsDao
import com.sadellie.unitto.data.database.UnitsEntity
import com.sadellie.unitto.data.model.UnitGroup
import com.sadellie.unitto.data.model.UnitsListSorting
import com.sadellie.unitto.data.model.unit.AbstractUnit
import com.sadellie.unitto.data.model.unit.ReverseUnit
import com.sadellie.unitto.data.model.unit.filterByLev
import com.sadellie.unitto.data.units.collections.accelerationCollection
import com.sadellie.unitto.data.units.collections.angleCollection
import com.sadellie.unitto.data.units.collections.areaCollection
import com.sadellie.unitto.data.units.collections.currencyCollection
import com.sadellie.unitto.data.units.collections.dataCollection
import com.sadellie.unitto.data.units.collections.dataTransferCollection
import com.sadellie.unitto.data.units.collections.electrostaticCapacitance
import com.sadellie.unitto.data.units.collections.energyCollection
import com.sadellie.unitto.data.units.collections.flowRateCollection
import com.sadellie.unitto.data.units.collections.fluxCollection
import com.sadellie.unitto.data.units.collections.forceCollection
import com.sadellie.unitto.data.units.collections.fuelConsumptionCollection
import com.sadellie.unitto.data.units.collections.lengthCollection
import com.sadellie.unitto.data.units.collections.luminanceCollection
import com.sadellie.unitto.data.units.collections.massCollection
import com.sadellie.unitto.data.units.collections.numberBaseCollection
import com.sadellie.unitto.data.units.collections.powerCollection
import com.sadellie.unitto.data.units.collections.prefixCollection
import com.sadellie.unitto.data.units.collections.pressureCollection
import com.sadellie.unitto.data.units.collections.speedCollection
import com.sadellie.unitto.data.units.collections.temperatureCollection
import com.sadellie.unitto.data.units.collections.timeCollection
import com.sadellie.unitto.data.units.collections.torqueCollection
import com.sadellie.unitto.data.units.collections.volumeCollection
import com.sadellie.unitto.data.units.remote.CurrencyApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class UnitsRepository @Inject constructor(
    private val unitsDao: UnitsDao,
    private val currencyRatesDao: CurrencyRatesDao,
    @ApplicationContext private val mContext: Context,
) {
    private val myUnits = MutableStateFlow(
        lengthCollection +
                currencyCollection +
                massCollection +
                speedCollection +
                temperatureCollection +
                areaCollection +
                timeCollection +
                volumeCollection +
                dataCollection +
                pressureCollection +
                accelerationCollection +
                energyCollection +
                powerCollection +
                angleCollection +
                dataTransferCollection +
                fluxCollection +
                numberBaseCollection +
                electrostaticCapacitance +
                prefixCollection +
                forceCollection +
                torqueCollection +
                flowRateCollection +
                luminanceCollection +
                fuelConsumptionCollection
    )

    val allUnits: Flow<List<AbstractUnit>> = combine(
        unitsDao.getAllFlow(),
        myUnits
    ) { basedList, inMemoryList ->
        return@combine inMemoryList.map { inMemoryUnit ->
            val inBaseUnit = basedList.find { it.unitId == inMemoryUnit.id }
                ?: return@map inMemoryUnit
            inMemoryUnit.clone(
                isFavorite = inBaseUnit.isFavorite,
                counter = inBaseUnit.frequency,
                pairId = inBaseUnit.pairedUnitId
            )
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun getById(id: String): AbstractUnit {
        return allUnits.first().first { it.id == id }
    }

    suspend fun getCollection(group: UnitGroup): List<AbstractUnit> {
        return allUnits.first().filter { it.group == group }
    }

    suspend fun favorite(unit: AbstractUnit) = withContext(Dispatchers.IO) {
        val basedUnit = unitsDao.getById(unit.id)

        if (basedUnit == null) {
            unitsDao.insertUnit(
                UnitsEntity(
                    unitId = unit.id,
                    isFavorite = true
                )
            )
        } else {
            unitsDao.insertUnit(
                UnitsEntity(
                    unitId = basedUnit.unitId,
                    isFavorite = !basedUnit.isFavorite,
                    pairedUnitId = basedUnit.pairedUnitId,
                    frequency = basedUnit.frequency
                )
            )
        }
    }

    suspend fun incrementCounter(unit: AbstractUnit) = withContext(Dispatchers.IO) {
        val basedUnit = unitsDao.getById(unit.id)

        if (basedUnit == null) {
            unitsDao.insertUnit(
                UnitsEntity(
                    unitId = unit.id,
                    frequency = 1
                )
            )
        } else {
            unitsDao.insertUnit(
                UnitsEntity(
                    unitId = basedUnit.unitId,
                    isFavorite = basedUnit.isFavorite,
                    pairedUnitId = basedUnit.pairedUnitId,
                    frequency = basedUnit.frequency + 1
                )
            )
        }
    }

    suspend fun setPair(unit: AbstractUnit, pair: AbstractUnit) = withContext(Dispatchers.IO) {
        val basedUnit = unitsDao.getById(unit.id)

        if (basedUnit == null) {
            unitsDao.insertUnit(
                UnitsEntity(
                    unitId = unit.id,
                    pairedUnitId = pair.id
                )
            )
        } else {
            unitsDao.insertUnit(
                UnitsEntity(
                    unitId = basedUnit.unitId,
                    isFavorite = basedUnit.isFavorite,
                    pairedUnitId = pair.id,
                    frequency = basedUnit.frequency
                )
            )
        }
    }

    suspend fun updateRates(unit: AbstractUnit): LocalDate? = withContext(Dispatchers.IO) {
        var basedConversions = currencyRatesDao.getLatestRates(baseId = unit.id)
        val epochDay = LocalDate.now().toEpochDay()

        if (basedConversions.firstOrNull()?.date != epochDay) {
            try {
                val conversions = CurrencyApi.service.getCurrencyPairs(unit.id)
                val rates = conversions.currency
                    .map { (pairId, pairValue) ->
                        CurrencyRatesEntity(
                            baseUnitId = unit.id,
                            date = epochDay,
                            pairUnitId = pairId,
                            pairUnitValue = BigDecimal.valueOf(pairValue)
                        )
                    }
                currencyRatesDao.insertRates(rates)

                basedConversions = currencyRatesDao.getLatestRates(baseId = unit.id)
            } catch (e: Exception) {
                Log.d("UnitsRepository", "Skipped update: $e")
            }
        }
        myUnits.update { units ->
            units.map { localUnit ->
                if (localUnit.group != UnitGroup.CURRENCY) return@map localUnit
                if (localUnit !is ReverseUnit) return@map localUnit

                val rate = basedConversions
                    .firstOrNull { localUnit.id == it.pairUnitId }
                    ?.pairUnitValue ?: BigDecimal.ZERO

                return@map if (rate > BigDecimal.ZERO) {
                    localUnit.copy(basicUnit = rate)
                } else {
                    localUnit.copy(basicUnit = BigDecimal.ZERO)
                }
            }
        }

        return@withContext basedConversions
            .firstOrNull()
            ?.date
            ?.let { LocalDate.ofEpochDay(it) }
    }

    suspend fun filterUnits(
        query: String,
        unitGroup: UnitGroup?,
        favoritesOnly: Boolean,
        hideBrokenUnits: Boolean,
        sorting: UnitsListSorting,
        shownUnitGroups: List<UnitGroup> = emptyList(),
    ): Map<UnitGroup, List<AbstractUnit>> {
        // Leave only shown unit groups
        var units: Sequence<AbstractUnit> = if (unitGroup == null) {
            allUnits.first().filter { it.group in shownUnitGroups }
        } else {
            getCollection(unitGroup)
        }.asSequence()

        if (favoritesOnly) {
            units = units.filter { it.isFavorite }
        }
        if (hideBrokenUnits) {
            units = units.filter { it.basicUnit > BigDecimal.ZERO }
        }

        units = when (sorting) {
            UnitsListSorting.USAGE -> units.sortedByDescending { it.counter }
            UnitsListSorting.ALPHABETICAL -> units.sortedBy { mContext.getString(it.displayName) }
            UnitsListSorting.SCALE_ASC -> units.sortedBy { it.basicUnit }
            UnitsListSorting.SCALE_DESC -> units.sortedByDescending { it.basicUnit }
            else -> units
        }

        units = if (query.isEmpty()) {
            units.sortedByDescending { it.isFavorite }
        } else {
            // For search we sort by popularity and Levenshtein distance (short and long name).
            units.filterByLev(query, mContext)
        }
        return units.groupBy { it.group }
    }
}
