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

package com.sadellie.unitto.data.units.collections

import com.sadellie.unitto.core.base.R
import com.sadellie.unitto.data.model.UnitGroup
import com.sadellie.unitto.data.model.unit.AbstractUnit
import com.sadellie.unitto.data.model.unit.NormalUnit
import com.sadellie.unitto.data.units.MyUnitIDS
import java.math.BigDecimal

internal val electrostaticCapacitance: List<AbstractUnit> by lazy {
    listOf(
        NormalUnit(MyUnitIDS.attofarad,     BigDecimal.valueOf(1),              UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_attofarad,     R.string.unit_attofarad_short),
        NormalUnit(MyUnitIDS.picofarad,     BigDecimal.valueOf(1E+6),           UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_picofarad,     R.string.unit_picofarad_short),
        NormalUnit(MyUnitIDS.statfarad,     BigDecimal.valueOf(1112650.0561),   UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_statfarad,     R.string.unit_statfarad_short),
        NormalUnit(MyUnitIDS.nanofarad,     BigDecimal.valueOf(1E+9),           UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_nanofarad,     R.string.unit_nanofarad_short),
        NormalUnit(MyUnitIDS.microfarad,    BigDecimal.valueOf(1E+12),          UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_microfarad,    R.string.unit_microfarad_short),
        NormalUnit(MyUnitIDS.millifarad,    BigDecimal.valueOf(1E+15),          UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_millifarad,    R.string.unit_millifarad_short),
        NormalUnit(MyUnitIDS.farad,         BigDecimal.valueOf(1E+18),          UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_farad,         R.string.unit_farad_short),
        NormalUnit(MyUnitIDS.kilofarad,     BigDecimal.valueOf(1E+21),          UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_kilofarad,     R.string.unit_kilofarad_short),
        NormalUnit(MyUnitIDS.megafarad,     BigDecimal.valueOf(1E+24),          UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_megafarad,     R.string.unit_megafarad_short),
        NormalUnit(MyUnitIDS.gigafarad,     BigDecimal.valueOf(1E+27),          UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_gigafarad,     R.string.unit_gigafarad_short),
        NormalUnit(MyUnitIDS.petafarad,     BigDecimal.valueOf(1E+33),          UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_petafarad,     R.string.unit_petafarad_short),
        NormalUnit(MyUnitIDS.exafarad,      BigDecimal.valueOf(1E+36),          UnitGroup.ELECTROSTATIC_CAPACITANCE,    R.string.unit_exafarad,      R.string.unit_exafarad_short),
    )
}
