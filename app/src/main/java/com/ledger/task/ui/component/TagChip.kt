package com.ledger.task.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.task.domain.model.Tag

@Composable
fun TagChip(
    tag: Tag,
    modifier: Modifier = Modifier,
    showDelete: Boolean = false,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null
) {
    val backgroundColor = if (selected) {
        tag.color.copy(alpha = 0.25f)
    } else {
        tag.color.copy(alpha = 0.1f)
    }

    val borderModifier = if (selected) {
        Modifier.border(2.dp, tag.color, RoundedCornerShape(16.dp))
    } else {
        Modifier.border(1.dp, tag.color.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .then(borderModifier)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(tag.color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = tag.name,
            color = if (selected) tag.color else tag.color.copy(alpha = 0.8f),
            fontSize = 13.sp
        )
        if (showDelete && onDeleteClick != null) {
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "删除标签",
                    tint = tag.color,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
