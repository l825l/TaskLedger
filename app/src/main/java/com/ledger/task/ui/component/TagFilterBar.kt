package com.ledger.task.ui.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ledger.task.domain.model.Tag
import com.ledger.task.ui.theme.getTextMuted

@Composable
fun TagFilterBar(
    tags: List<Tag>,
    selectedTagId: Long?,
    onTagSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (tags.isEmpty()) {
        return
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "全部",
            color = if (selectedTagId == null) getTextMuted() else getTextMuted().copy(alpha = 0.5f),
            modifier = Modifier.padding(end = 8.dp)
        )

        tags.forEach { tag ->
            TagChip(
                tag = tag,
                selected = selectedTagId == tag.id,
                onClick = {
                    onTagSelected(if (selectedTagId == tag.id) null else tag.id)
                }
            )
        }
    }
}
