/*
 * Unitto is a unit converter for Android
 * Copyright (c) 2023 Elshan Agaev
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

package com.sadellie.unitto.core.ui.model

import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.sadellie.unitto.core.base.TopLevelDestinations

sealed class DrawerItems(
    val destination: TopLevelDestinations,
    val selectedIcon: ImageVector,
    val defaultIcon: ImageVector
) {
    data object Calculator : DrawerItems(
        destination = TopLevelDestinations.Calculator,
        selectedIcon = Icons.Filled.Calculate,
        defaultIcon = Icons.Outlined.Calculate
    )

    data object Converter : DrawerItems(
        destination = TopLevelDestinations.Converter,
        selectedIcon = Icons.Filled.SwapHoriz,
        defaultIcon = Icons.Outlined.SwapHoriz
    )

    data object DateDifference : DrawerItems(
        destination = TopLevelDestinations.DateCalculator,
        selectedIcon = Icons.Filled.Event,
        defaultIcon = Icons.Outlined.Event
    )

    data object TimeZones : DrawerItems(
        destination = TopLevelDestinations.TimeZone,
        selectedIcon = Icons.Filled.Schedule,
        defaultIcon = Icons.Outlined.Schedule
    )

    companion object {
        val ALL by lazy {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                listOf(
                    Calculator,
                    Converter,
                    DateDifference,
                    TimeZones
                )
            } else {
                listOf(
                    Calculator,
                    Converter,
                    DateDifference,
                )
            }
        }
    }
}
