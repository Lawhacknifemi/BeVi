package online.ship10x.bevi_bestview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import online.ship10x.bevi_bestview.ui.theme.BeViBestViewTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val imageProcessingViewModel: ImageProcessingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeViBestViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BeviScreen(
                        paddingValues = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun BeviScreen(
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.White)
    ) {

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Replace AsyncImage with Image using painterResource
            Image(
                painter = painterResource(id = R.drawable.splash_image),
                contentDescription = "BeVi logo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(20.dp))

            // App Name and Tagline
            Text(
                "BeVi - Best View",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Create photos that impress!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(R.drawable.baseline_camera_alt_24, "Open Camera")
                ActionButton(R.drawable.rounded_arrow_upward_alt_24, "Open From Device")
                ActionButton(R.drawable.round_access_time_filled_24, "Open Latest")
            }
        }
    }
}

@Composable
fun ActionButton(iconResId: Int, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { /* TODO: Implement action */ },
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFE6F0FF), CircleShape)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = text,
                tint = Color(0xFF007BFF),
                modifier = Modifier.size(30.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(80.dp).padding(top = 4.dp)
        )
    }
}

// ... rest of your Composable functions ...