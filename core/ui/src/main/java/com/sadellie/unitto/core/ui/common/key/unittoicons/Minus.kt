package com.sadellie.unitto.core.ui.common.key.unittoicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.sadellie.unitto.core.ui.common.key.UnittoIcons

@Suppress("UnusedReceiverParameter")
val UnittoIcons.Minus: ImageVector
    get() {
        if (_minus != null) {
            return _minus!!
        }
        _minus = Builder(name = "Minus", defaultWidth = 170.0.dp, defaultHeight = 170.0.dp,
                viewportWidth = 170.0f, viewportHeight = 170.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(37.0f, 79.0f)
                horizontalLineTo(133.69f)
                verticalLineTo(91.691f)
                horizontalLineTo(37.0f)
                verticalLineTo(79.0f)
                close()
            }
        }
        .build()
        return _minus!!
    }

private var _minus: ImageVector? = null
