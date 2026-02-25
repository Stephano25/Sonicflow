package com.exemple.sonicflow.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.exemple.sonicflow.R

@Composable
fun VinylRecord(
    imageUrl: Any?,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    size: Float = 250f
) {
    // Version simplifiée pour éviter les crashes
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUrl,
                    error = painterResource(id = android.R.drawable.ic_menu_gallery),
                    placeholder = painterResource(id = android.R.drawable.ic_menu_gallery)
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size.dp * 0.8f)
                    .clip(CircleShape)
            )
        } else {
            Icon(
                Icons.Default.MusicNote,
                contentDescription = null,
                modifier = Modifier.size(size.dp * 0.5f),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}