package com.ledger.task.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ledger.task.domain.model.Tag
import com.ledger.task.ui.theme.getTextMuted

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSelector(
    allTags: List<Tag>,
    selectedTagIds: List<Long>,
    onTagToggle: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (allTags.isEmpty()) {
        Text(
            text = "暂无标签，请前往台账中心创建标签",
            color = getTextMuted(),
            modifier = modifier.padding(8.dp)
        )
        return
    }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        allTags.forEach { tag ->
            TagChip(
                tag = tag,
                selected = selectedTagIds.contains(tag.id),
                onClick = { onTagToggle(tag.id) }
            )
        }
    }
}
