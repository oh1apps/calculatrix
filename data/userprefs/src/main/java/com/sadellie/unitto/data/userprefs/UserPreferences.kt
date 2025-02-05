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

package com.sadellie.unitto.data.userprefs

import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.sadellie.unitto.core.base.OutputFormat
import com.sadellie.unitto.core.base.Separator
import com.sadellie.unitto.core.base.TopLevelDestinations
import com.sadellie.unitto.data.model.ALL_UNIT_GROUPS
import com.sadellie.unitto.data.model.UnitGroup
import com.sadellie.unitto.data.model.UnitsListSorting
import com.sadellie.unitto.data.model.unit.AbstractUnit
import com.sadellie.unitto.data.units.MyUnitIDS
import io.github.sadellie.themmo.MonetMode
import io.github.sadellie.themmo.ThemingMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val data = dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }

    val appPrefs: Flow<AppPreferences> = data
        .map { preferences ->
            AppPreferences(
                themingMode = preferences.getThemingMode(),
                enableDynamicTheme = preferences.getEnableDynamicTheme(),
                enableAmoledTheme = preferences.getEnableAmoledTheme(),
                customColor = preferences.getCustomColor(),
                monetMode = preferences.getMonetMode(),
                startingScreen = preferences.getStartingScreen(),
                enableToolsExperiment = preferences.getEnableToolsExperiment(),
                systemFont = preferences.getSystemFont()
            )
        }

    val generalPrefs: Flow<GeneralPreferences> = data
        .map { preferences ->
            GeneralPreferences(
                enableVibrations = preferences.getEnableVibrations(),
            )
        }

    val calculatorPrefs: Flow<CalculatorPreferences> = data
        .map { preferences ->
            CalculatorPreferences(
                radianMode = preferences.getRadianMode(),
                enableVibrations = preferences.getEnableVibrations(),
                separator = preferences.getSeparator(),
                middleZero = preferences.getMiddleZero(),
                partialHistoryView = preferences.getPartialHistoryView(),
                precision = preferences.getDigitsPrecision(),
                outputFormat = preferences.getOutputFormat(),
                acButton = preferences.getAcButton(),
            )
        }

    val converterPrefs: Flow<ConverterPreferences> = data
        .map { preferences ->
            ConverterPreferences(
                enableVibrations = preferences.getEnableVibrations(),
                separator = preferences.getSeparator(),
                middleZero = preferences.getMiddleZero(),
                precision = preferences.getDigitsPrecision(),
                outputFormat = preferences.getOutputFormat(),
                unitConverterFormatTime = preferences.getUnitConverterFormatTime(),
                unitConverterSorting = preferences.getUnitConverterSorting(),
                shownUnitGroups = preferences.getShownUnitGroups(),
                unitConverterFavoritesOnly = preferences.getUnitConverterFavoritesOnly(),
                enableToolsExperiment = preferences.getEnableToolsExperiment(),
                latestLeftSideUnit = preferences.getLatestLeftSide(),
                latestRightSideUnit = preferences.getLatestRightSide(),
                acButton = preferences.getAcButton(),
            )
        }

    val displayPrefs: Flow<DisplayPreferences> = data
        .map { preferences ->
            DisplayPreferences(
                systemFont = preferences.getSystemFont(),
                middleZero = preferences.getMiddleZero(),
                acButton = preferences.getAcButton(),
            )
        }

    val formattingPrefs: Flow<FormattingPreferences> = data
        .map { preferences ->
            FormattingPreferences(
                digitsPrecision = preferences.getDigitsPrecision(),
                separator = preferences.getSeparator(),
                outputFormat = preferences.getOutputFormat(),
            )
        }

    val unitGroupsPrefs: Flow<UnitGroupsPreferences> = data
        .map { preferences ->
            UnitGroupsPreferences(
                shownUnitGroups = preferences.getShownUnitGroups(),
            )
        }

    val addSubtractPrefs: Flow<AddSubtractPreferences> = data
        .map { preferences ->
            AddSubtractPreferences(
                separator = preferences.getSeparator(),
                enableVibrations = preferences.getEnableVibrations(),
            )
        }

    val aboutPrefs: Flow<AboutPreferences> = data
        .map { preferences ->
            AboutPreferences(
                enableToolsExperiment = preferences.getEnableToolsExperiment()
            )
        }

    val startingScreenPrefs: Flow<StartingScreenPreferences> = data
        .map { preferences ->
            StartingScreenPreferences(
                startingScreen = preferences.getStartingScreen(),
            )
        }

    suspend fun updateDigitsPrecision(precision: Int) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.DIGITS_PRECISION] = precision
        }
    }

    suspend fun updateSeparator(separator: Int) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.SEPARATOR] = separator
        }
    }

    suspend fun updateOutputFormat(outputFormat: Int) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.OUTPUT_FORMAT] = outputFormat
        }
    }

    suspend fun updateLatestPairOfUnits(unitFrom: AbstractUnit, unitTo: AbstractUnit) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.LATEST_LEFT_SIDE] = unitFrom.id
            preferences[PrefsKeys.LATEST_RIGHT_SIDE] = unitTo.id
        }
    }

    suspend fun updateThemingMode(themingMode: ThemingMode) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.THEMING_MODE] = themingMode.name
        }
    }

    suspend fun updateDynamicTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.ENABLE_DYNAMIC_THEME] = enabled
        }
    }

    suspend fun updateAmoledTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.ENABLE_AMOLED_THEME] = enabled
        }
    }

    suspend fun updateCustomColor(color: Color) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.CUSTOM_COLOR] = color.value.toLong()
        }
    }

    suspend fun updateMonetMode(monetMode: MonetMode) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.MONET_MODE] = monetMode.name
        }
    }

    suspend fun updateStartingScreen(startingScreen: String) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.STARTING_SCREEN] = startingScreen
        }
    }

    suspend fun updateShownUnitGroups(shownUnitGroups: List<UnitGroup>) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.SHOWN_UNIT_GROUPS] = shownUnitGroups.joinToString(",")
        }
    }

    suspend fun updateVibrations(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.ENABLE_VIBRATIONS] = enabled
        }
    }

    suspend fun updateMiddleZero(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.MIDDLE_ZERO] = enabled
        }
    }

    suspend fun updateToolsExperiment(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.ENABLE_TOOLS_EXPERIMENT] = enabled
        }
    }

    suspend fun updateRadianMode(radianMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.RADIAN_MODE] = radianMode
        }
    }

    suspend fun updateUnitConverterFavoritesOnly(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.UNIT_CONVERTER_FAVORITES_ONLY] = enabled
        }
    }

    suspend fun updateUnitConverterFormatTime(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.UNIT_CONVERTER_FORMAT_TIME] = enabled
        }
    }

    suspend fun updateUnitConverterSorting(sorting: UnitsListSorting) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.UNIT_CONVERTER_SORTING] = sorting.name
        }
    }

    suspend fun updateSystemFont(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.SYSTEM_FONT] = enabled
        }
    }

    suspend fun updatePartialHistoryView(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.PARTIAL_HISTORY_VIEW] = enabled
        }
    }

    suspend fun updateAcButton(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PrefsKeys.AC_BUTTON] = enabled
        }
    }
}

private fun Preferences.getEnableDynamicTheme(): Boolean {
    return this[PrefsKeys.ENABLE_DYNAMIC_THEME] ?: true
}

private fun Preferences.getThemingMode(): ThemingMode {
    return this[PrefsKeys.THEMING_MODE]
        ?.letTryOrNull { ThemingMode.valueOf(it) }
        ?: ThemingMode.AUTO
}

private fun Preferences.getEnableAmoledTheme(): Boolean {
    return this[PrefsKeys.ENABLE_AMOLED_THEME] ?: false
}

private fun Preferences.getCustomColor(): Color {
    return this[PrefsKeys.CUSTOM_COLOR]?.letTryOrNull { Color(it.toULong()) }
        ?: Color.Unspecified
}

private fun Preferences.getMonetMode(): MonetMode {
    return this[PrefsKeys.MONET_MODE]?.letTryOrNull { MonetMode.valueOf(it) }
        ?: MonetMode.TonalSpot
}

private fun Preferences.getStartingScreen(): String {
    return this[PrefsKeys.STARTING_SCREEN]
        ?: TopLevelDestinations.Calculator.graph
}

private fun Preferences.getEnableToolsExperiment(): Boolean {
    return this[PrefsKeys.ENABLE_TOOLS_EXPERIMENT] ?: false
}

private fun Preferences.getSystemFont(): Boolean {
    return this[PrefsKeys.SYSTEM_FONT] ?: false
}

private fun Preferences.getEnableVibrations(): Boolean {
    return this[PrefsKeys.ENABLE_VIBRATIONS] ?: true
}

private fun Preferences.getRadianMode(): Boolean {
    return this[PrefsKeys.RADIAN_MODE] ?: true
}

private fun Preferences.getSeparator(): Int {
    return this[PrefsKeys.SEPARATOR] ?: Separator.SPACE
}

private fun Preferences.getMiddleZero(): Boolean {
    return this[PrefsKeys.MIDDLE_ZERO] ?: false
}

private fun Preferences.getPartialHistoryView(): Boolean {
    return this[PrefsKeys.PARTIAL_HISTORY_VIEW] ?: true
}

private fun Preferences.getDigitsPrecision(): Int {
    return this[PrefsKeys.DIGITS_PRECISION] ?: 3
}

private fun Preferences.getOutputFormat(): Int {
    return this[PrefsKeys.OUTPUT_FORMAT] ?: OutputFormat.PLAIN
}

private fun Preferences.getUnitConverterFormatTime(): Boolean {
    return this[PrefsKeys.UNIT_CONVERTER_FORMAT_TIME] ?: false
}

private fun Preferences.getUnitConverterSorting(): UnitsListSorting {
    return this[PrefsKeys.UNIT_CONVERTER_SORTING]
        ?.let { UnitsListSorting.valueOf(it) } ?: UnitsListSorting.USAGE
}

private fun Preferences.getShownUnitGroups(): List<UnitGroup> {
    return this[PrefsKeys.SHOWN_UNIT_GROUPS]?.letTryOrNull { list ->
        list.ifEmpty { return@letTryOrNull listOf() }.split(",")
            .map { UnitGroup.valueOf(it) }
    } ?: ALL_UNIT_GROUPS
}

private fun Preferences.getUnitConverterFavoritesOnly(): Boolean {
    return this[PrefsKeys.UNIT_CONVERTER_FAVORITES_ONLY]
        ?: false
}

private fun Preferences.getLatestLeftSide(): String {
    return this[PrefsKeys.LATEST_LEFT_SIDE] ?: MyUnitIDS.kilometer
}

private fun Preferences.getLatestRightSide(): String {
    return this[PrefsKeys.LATEST_RIGHT_SIDE] ?: MyUnitIDS.mile
}

private fun Preferences.getAcButton(): Boolean {
    return this[PrefsKeys.AC_BUTTON] ?: false
}

private inline fun <T, R> T.letTryOrNull(block: (T) -> R): R? = try {
    this?.let(block)
} catch (e: Exception) {
    null
}
