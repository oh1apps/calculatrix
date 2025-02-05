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

package com.sadellie.unitto.feature.timezone

import androidx.compose.ui.text.input.TextFieldValue
import com.sadellie.unitto.data.model.timezone.SearchResultZone

sealed class AddTimeZoneUIState {
    data object Loading: AddTimeZoneUIState()

    data class Ready(
        val query: TextFieldValue,
        val list: List<SearchResultZone>,
    ): AddTimeZoneUIState()
}
