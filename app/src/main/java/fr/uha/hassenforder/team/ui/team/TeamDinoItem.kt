package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Adb
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.Gender

@Composable
fun TeamDinoItem(dino: Dino) {
    val gender: ImageVector =
        when (dino.gender) {
            Gender.NO -> Icons.Outlined.DoNotDisturb
            Gender.MIXTE -> Icons.Outlined.Adb
        }
    ListItem(
        headlineContent = {
            Row() {
                Text(dino.name, modifier = Modifier.padding(end = 8.dp))
            }
        },
        supportingContent = {
            Row() {
                Icon(
                    imageVector = Icons.Outlined.Article,
                    contentDescription = "type",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(dino.type.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Row() {
                Icon(
                    imageVector = Icons.Outlined.Article,
                    contentDescription = "regime",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(dino.regime.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        leadingContent = {
            if (dino.picture != null) {
                AsyncImage(
                    model = dino.picture,
                    modifier = Modifier.size(64.dp),
                    contentDescription = null,
                    error = rememberVectorPainter(Icons.Outlined.Error),
                    placeholder = rememberVectorPainter(Icons.Outlined.Casino),
                )
            }
        },
        trailingContent = {
            Icon(
                imageVector = gender,
                contentDescription = "gender",
                modifier = Modifier.size(48.dp)
            )
        },
    )
}
