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

package com.sadellie.unitto.core.ui.common.textfield

import com.sadellie.unitto.core.base.Token
import kotlin.math.abs

fun String.fixCursor(pos: Int, grouping: String): Int {

    if (isEmpty()) return pos

    // Best position if we move cursor left
    var leftCursor = pos
    while (this.isPlacedIllegallyAt(leftCursor, grouping)) leftCursor--

    // Best position if we move cursor right
    var rightCursor = pos
    while (this.isPlacedIllegallyAt(rightCursor, grouping)) rightCursor++

    return listOf(leftCursor, rightCursor).minBy { abs(it - pos) }
}

/**
 * Same as [String.tokenAhead], but more efficient. We only need a number, not string.
 * Don't replace!
 */
fun String.tokenLengthAhead(pos: Int): Int {
    Token.Func.allWithOpeningBracket.forEach {
        if (pos.isAfterToken(this, it)) return it.length
    }

    return 1
}

fun String.tokenAhead(pos: Int): String {
    Token.Func.allWithOpeningBracket.forEach {
        if (pos.isAfterToken(this, it)) return it
    }

    return substring((pos - 1).coerceAtLeast(0), pos)
}

private fun String.isPlacedIllegallyAt(pos: Int, grouping: String): Boolean {
    // For things like "123,|456" - this is illegal
    if (pos.isAfterToken(this, grouping)) return true

    // For things like "123,456+c|os(8)" - this is illegal
    Token.Func.allWithOpeningBracket.forEach {
        if (pos.isAtToken(this, it)) return true
    }

    return false
}

private fun Int.isAtToken(str: String, token: String): Boolean {
    val checkBound = (token.length - 1).coerceAtLeast(1)
    return str
        .substring(
            startIndex = (this - checkBound).coerceAtLeast(0),
            endIndex = (this + checkBound).coerceAtMost(str.length)
        )
        .contains(token)
}

private fun Int.isAfterToken(str: String, token: String): Boolean {
    return str
        .substring((this - token.length).coerceAtLeast(0), this)
        .contains(token)
}


// This can also make [TextFieldValue.addTokens] better by checking tokens both ways. Needs more tests
fun String.tokenAfter(pos: Int): String {
    Token.Func.allWithOpeningBracket.forEach {
        if (pos.isBeforeToken(this, it)) return it
    }

    return substring(pos, (pos + 1).coerceAtMost(this.length))
}

//private fun String.numberNearby(cursor: Int): String {
//    val text = this
//
//    var aheadCursor = cursor
//    var afterCursor = cursor
//
//    while (text.tokenAhead(aheadCursor) in Token.Digit.allWithDot) {
//        aheadCursor--
//    }
//
//    while (text.tokenAfter(afterCursor) in Token.Digit.allWithDot) {
//        afterCursor++
//    }
//
//    return text.substring(aheadCursor, afterCursor)
//}

private fun Int.isBeforeToken(str: String, token: String): Boolean {
    return str
        .substring(this, (this + token.length).coerceAtMost(str.length))
        .contains(token)
}
