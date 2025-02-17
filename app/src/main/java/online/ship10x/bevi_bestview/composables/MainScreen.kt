package online.ship10x.bevi_bestview.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import online.ship10x.bevi_bestview.R
import android.net.Uri

@Composable
fun MainScreen(onOpenCamera: () -> Unit, onImageSelected: (Uri) -> Unit, onOpenLatest: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background with logo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_image),
                contentDescription = "BeVi logo",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp)
                    .size(380.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Bottom sheet with content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.BottomCenter)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    clip = true
                )
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)
        ) {
            BeviBottomSheet(onOpenCamera = onOpenCamera, onImageSelected = onImageSelected, onOpenLatest = onOpenLatest)
        }
    }
}
