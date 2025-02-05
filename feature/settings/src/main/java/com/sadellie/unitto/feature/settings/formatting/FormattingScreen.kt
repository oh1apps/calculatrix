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

package com.sadellie.unitto.feature.settings.formatting

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.EMobiledata
import androidx.compose.material.icons.filled._123
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sadellie.unitto.core.base.MAX_PRECISION
import com.sadellie.unitto.core.base.OutputFormat
import com.sadellie.unitto.core.base.R
import com.sadellie.unitto.core.base.Separator
import com.sadellie.unitto.core.ui.common.NavigateUpButton
import com.sadellie.unitto.core.ui.common.SegmentedButton
import com.sadellie.unitto.core.ui.common.SegmentedButtonsRow
import com.sadellie.unitto.core.ui.common.UnittoEmptyScreen
import com.sadellie.unitto.core.ui.common.UnittoListItem
import com.sadellie.unitto.core.ui.common.UnittoScreenWithLargeTopBar
import com.sadellie.unitto.core.ui.common.UnittoSlider
import com.sadellie.unitto.core.ui.common.squashable
import com.sadellie.unitto.core.ui.common.textfield.formatExpression
import com.sadellie.unitto.core.ui.theme.LocalNumberTypography
import kotlin.math.roundToInt

@Composable
fun FormattingRoute(
    viewModel: FormattingViewModel = hiltViewModel(),
    navigateUpAction: () -> Unit,
) {
    when (val uiState = viewModel.uiState.collectAsStateWithLifecycle().value) {
        null -> UnittoEmptyScreen()
        else -> {
            FormattingScreen(
                navigateUpAction = navigateUpAction,
                uiState = uiState,
                onPrecisionChange = viewModel::updatePrecision,
                onSeparatorChange = viewModel::updateSeparator,
                onOutputFormatChange = viewModel::updateOutputFormat,
                togglePreview = viewModel::togglePreview
            )
        }
    }
}

@Composable
fun FormattingScreen(
    navigateUpAction: () -> Unit,
    uiState: FormattingUIState,
    onPrecisionChange: (Int) -> Unit,
    onSeparatorChange: (Int) -> Unit,
    onOutputFormatChange: (Int) -> Unit,
    togglePreview: () -> Unit,
    precisions: ClosedFloatingPointRange<Float> = 0f..16f, // 16th is a MAX_PRECISION (1000)
) {
    val resources = LocalContext.current.resources

    val precisionText: String by remember(uiState.precision, uiState.formatterSymbols) {
        derivedStateOf {
            return@derivedStateOf if (uiState.precision >= precisions.endInclusive) {
                resources.getString(
                    R.string.settings_precision_max,
                    MAX_PRECISION.toString().formatExpression(uiState.formatterSymbols)
                )
            } else {
                uiState.precision.toString()
            }
        }
    }

    UnittoScreenWithLargeTopBar(
        title = stringResource(R.string.settings_formatting),
        navigationIcon = { NavigateUpButton(navigateUpAction) },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            item("preview") {
                Column(
                    Modifier
                        .padding(16.dp)
                        .squashable(
                            onClick = togglePreview,
                            cornerRadiusRange = 8.dp..32.dp,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.settings_formatting_preview),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = uiState.preview,
                        style = LocalNumberTypography.current.displayMedium,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            item("precision_label") {
                UnittoListItem(
                    leadingContent = {
                        Icon(Icons.Default.Architecture, stringResource(R.string.settings_precision))
                    },
                    headlineContent = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.settings_precision))
                            Text(precisionText)
                        }
                    },
                    supportingContent = {
                        Text(stringResource(R.string.settings_precision_support))
                    }
                )
            }

            item("precision_slider") {
                UnittoSlider(
                    modifier = Modifier.padding(start = 56.dp, end = 16.dp),
                    value = uiState.precision.toFloat(),
                    valueRange = precisions,
                    onValueChange = { onPrecisionChange(it.roundToInt()) },
                )
            }

            item("separator_label") {
                UnittoListItem(
                    leadingContent = {
                        Icon(Icons.Default._123, stringResource(R.string.settings_separator))
                    },
                    headlineContent = { Text(stringResource(R.string.settings_separator)) },
                    supportingContent = { Text(stringResource(R.string.settings_separator_support)) },
                )
            }

            item("separator") {
                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState())
                        .wrapContentWidth()
                        .padding(start = 56.dp)
                ) {
                    SegmentedButtonsRow {
                        SegmentedButton(
                            label = stringResource(R.string.settings_space),
                            onClick = { onSeparatorChange(Separator.SPACE) },
                            selected = Separator.SPACE == uiState.separator
                        )
                        SegmentedButton(
                            label = stringResource(R.string.settings_period),
                            onClick = { onSeparatorChange(Separator.PERIOD) },
                            selected = Separator.PERIOD == uiState.separator
                        )
                        SegmentedButton(
                            label = stringResource(R.string.settings_comma),
                            onClick = { onSeparatorChange(Separator.COMMA) },
                            selected = Separator.COMMA == uiState.separator
                        )
                    }
                }
            }

            item("output_format_label") {
                UnittoListItem(
                    leadingContent = {
                        Icon(Icons.Default.EMobiledata, stringResource(R.string.settings_precision))
                    },
                    headlineContent = { Text(stringResource(R.string.settings_exponential_notation)) },
                    supportingContent = { Text(stringResource(R.string.settings_exponential_notation_support)) }
                )
            }

            item("output_format") {
                Row(
                    Modifier
                        .horizontalScroll(rememberScrollState())
                        .wrapContentWidth()
                        .padding(start = 56.dp)
                ) {
                    SegmentedButtonsRow {
                        SegmentedButton(
                            label = stringResource(R.string.settings_auto),
                            onClick = { onOutputFormatChange(OutputFormat.ALLOW_ENGINEERING) },
                            selected = OutputFormat.ALLOW_ENGINEERING == uiState.outputFormat
                        )
                        SegmentedButton(
                            label = stringResource(R.string.enabled_label),
                            onClick = { onOutputFormatChange(OutputFormat.FORCE_ENGINEERING) },
                            selected = OutputFormat.FORCE_ENGINEERING == uiState.outputFormat
                        )
                        SegmentedButton(
                            label = stringResource(R.string.disabled_label),
                            onClick = { onOutputFormatChange(OutputFormat.PLAIN) },
                            selected = OutputFormat.PLAIN == uiState.outputFormat
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFormattingScreen() {
    var currentPrecision by remember { mutableIntStateOf(6) }
    var currentSeparator by remember { mutableIntStateOf(Separator.COMMA) }
    var currentOutputFormat by remember { mutableIntStateOf(OutputFormat.PLAIN) }

    FormattingScreen(
        uiState = FormattingUIState(
            preview = "123456.789",
            precision = 16,
            separator = Separator.SPACE,
            outputFormat = OutputFormat.PLAIN
        ),
        onPrecisionChange = { currentPrecision = it },
        onSeparatorChange = { currentSeparator = it },
        onOutputFormatChange = { currentOutputFormat = it },
        navigateUpAction = {},
        togglePreview = {}
    )
}
