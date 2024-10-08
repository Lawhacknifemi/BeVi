package online.ship10x.bevi_bestview.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetectionResult
import kotlin.math.sin

@Composable
fun ResultsOverlay(
    results: ObjectDetectionResult,
    frameWidth: Int,
    frameHeight: Int,
) {
    BoxWithConstraints(
        Modifier.fillMaxSize()
    ) {
        val detections = results.detections()
        detections?.forEach { detection ->
            val boundingBox = detection.boundingBox()
            val left = (boundingBox.left / frameWidth) * constraints.maxWidth
            val top = (boundingBox.top / frameHeight) * constraints.maxHeight
            val right = (boundingBox.right / frameWidth) * constraints.maxWidth
            val bottom = (boundingBox.bottom / frameHeight) * constraints.maxHeight

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val path = Path()
                    val meshSize = 10
                    val meshSpacingX = (right - left) / meshSize
                    val meshSpacingY = (bottom - top) / meshSize

                    // Draw mesh
                    for (i in 0..meshSize) {
                        for (j in 0..meshSize) {
                            val x = left + i * meshSpacingX
                            val y = top + j * meshSpacingY
                            val offset = 5f * sin((i + j) * 0.5f) // Create a wavy effect

                            if (i < meshSize) {
                                drawLine(
                                    color = Color.Cyan,
                                    start = Offset(x, y + offset),
                                    end = Offset(x + meshSpacingX, y + meshSpacingY + offset),
                                    strokeWidth = 2f
                                )
                            }
                            if (j < meshSize) {
                                drawLine(
                                    color = Color.Cyan,
                                    start = Offset(x, y + offset),
                                    end = Offset(x + meshSpacingX, y + offset),
                                    strokeWidth = 2f
                                )
                            }
                        }
                    }

                    // Draw bounding box
                    path.moveTo(left, top)
                    path.lineTo(right, top)
                    path.lineTo(right, bottom)
                    path.lineTo(left, bottom)
                    path.close()

                    drawPath(
                        path = path,
                        color = Color.Blue,
                        style = Stroke(width = 3f)
                    )

                    // Draw connecting lines for 3D effect
                    val depth = 20f
                    drawLine(Color.Blue, Offset(left, top), Offset(left + depth, top - depth), 3f)
                    drawLine(Color.Blue, Offset(right, top), Offset(right + depth, top - depth), 3f)
                    drawLine(Color.Blue, Offset(right, bottom), Offset(right + depth, bottom - depth), 3f)
                    drawLine(Color.Blue, Offset(left, bottom), Offset(left + depth, bottom - depth), 3f)

                    drawLine(Color.Blue, Offset(left + depth, top - depth), Offset(right + depth, top - depth), 3f)
                    drawLine(Color.Blue, Offset(right + depth, top - depth), Offset(right + depth, bottom - depth), 3f)
                    drawLine(Color.Blue, Offset(right + depth, bottom - depth), Offset(left + depth, bottom - depth), 3f)
                    drawLine(Color.Blue, Offset(left + depth, bottom - depth), Offset(left + depth, top - depth), 3f)
                }

                // Draw label
                val label = "${detection.categories().firstOrNull()?.categoryName() ?: "Unknown"}: " +
                        "${(detection.categories().firstOrNull()?.score() ?: 0f) * 100f}%"
                Text(
                    text = label,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        background = Color.Black.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .offset(x = left.dp, y = (top - 30).dp)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}