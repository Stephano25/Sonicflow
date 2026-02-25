package com.exemple.sonicflow.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.exemple.sonicflow.R
import com.exemple.sonicflow.data.model.Song
import com.exemple.sonicflow.data.room.Playlist
import com.exemple.sonicflow.utils.AlbumArtUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongMenuItem(
    song: Song,
    playlists: List<Playlist>,
    onSongClick: () -> Unit,
    onAddToPlaylist: (Playlist) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onSongClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pochette d'album
            AsyncImage(
                model = AlbumArtUtil.getAlbumArtUri(song.albumId),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                error = painterResource(id = R.drawable.default_album_art),
                placeholder = painterResource(id = R.drawable.default_album_art)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Informations de la chanson
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Text(
                    text = song.album,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }

            // Menu à 3 points
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Plus d'options")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // Option Ajouter à la playlist
                    DropdownMenuItem(
                        text = { Text("Ajouter à une playlist") },
                        leadingIcon = { Icon(Icons.Default.PlaylistAdd, null) },
                        onClick = { /* Ouvrir le sélecteur de playlist */ }
                    )

                    // Afficher les 3 premières playlists
                    playlists.take(3).forEach { playlist ->
                        DropdownMenuItem(
                            text = { Text("  → ${playlist.name}") },
                            onClick = {
                                onAddToPlaylist(playlist)
                                expanded = false
                            }
                        )
                    }

                    if (playlists.size > 3) {
                        DropdownMenuItem(
                            text = { Text("  Voir plus...") },
                            onClick = { /* Naviguer vers la liste des playlists */ }
                        )
                    }

                    Divider()

                    // Option Supprimer
                    DropdownMenuItem(
                        text = { Text("Supprimer", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Delete,
                                null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            onDelete()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}