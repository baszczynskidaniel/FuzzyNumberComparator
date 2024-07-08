package com.example.fuzzynumbercomparator.comparator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.fuzzynumbercomparator.components.PreviewSurface

@Composable
fun RotatableIcon(
    rotated: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    imageVector: ImageVector,
    rotationDegrees: Float = 180f,
    animationDuration: Int = 500
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (rotated) rotationDegrees else 0f,
        animationSpec = tween(durationMillis = animationDuration)
    )

    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier
            .rotate(rotationAngle)
    )
}

@Preview
@Composable
internal fun RotatableIconPreview() {
    var rotated by remember {
        mutableStateOf(false)
    }
    PreviewSurface {
        RotatableIcon(
            rotated = rotated,
            contentDescription = null,
            imageVector = Icons.Default.KeyboardArrowDown,
            modifier = Modifier.clickable { rotated = !rotated }

        )
    }

}
