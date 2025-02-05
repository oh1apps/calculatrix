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

package com.sadellie.unitto.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadellie.unitto.data.common.stateIn
import com.sadellie.unitto.data.database.CurrencyRatesDao
import com.sadellie.unitto.data.userprefs.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
    private val userPrefsRepository: UserPreferencesRepository,
    private val currencyRatesDao: CurrencyRatesDao,
) : ViewModel() {
    val userPrefs = userPrefsRepository.generalPrefs
        .stateIn(viewModelScope, null)

    val cachePercentage = currencyRatesDao.size()
        .map {
            (it / 100_000f).coerceIn(0f, 1f)
        }
        .stateIn(viewModelScope, 0f)

    /**
     * @see UserPreferencesRepository.updateVibrations
     */
    fun updateVibrations(enabled: Boolean) = viewModelScope.launch {
        userPrefsRepository.updateVibrations(enabled)
    }

    fun clearCache() = viewModelScope.launch(Dispatchers.IO) {
        currencyRatesDao.clear()
    }
}
