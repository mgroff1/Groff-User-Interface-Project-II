package com.groff.speakerdesigner

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SpeakerBoxDesignerScreen() {
    var boxWidthCm by remember { mutableFloatStateOf(36f) }
    var boxHeightCm by remember { mutableFloatStateOf(58f) }
    var boxDepthCm by remember { mutableFloatStateOf(30f) }
    var driverCount by remember { mutableFloatStateOf(2f) }
    var portDiameterMm by remember { mutableFloatStateOf(75f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1115))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Interactive Speaker Box Designer",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFFE8ECF3),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tune dimensions and instantly preview the front baffle.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFB8C1CC)
        )

        PreviewCard(
            boxWidthCm = boxWidthCm,
            boxHeightCm = boxHeightCm,
            driverCount = driverCount.toInt(),
            portDiameterMm = portDiameterMm
        )

        ControlCard(
            title = "Box Width: ${boxWidthCm.toInt()} cm",
            value = boxWidthCm,
            range = 20f..80f,
            onValueChange = { boxWidthCm = it }
        )
        ControlCard(
            title = "Box Height: ${boxHeightCm.toInt()} cm",
            value = boxHeightCm,
            range = 30f..120f,
            onValueChange = { boxHeightCm = it }
        )
        ControlCard(
            title = "Box Depth: ${boxDepthCm.toInt()} cm",
            value = boxDepthCm,
            range = 20f..80f,
            onValueChange = { boxDepthCm = it }
        )
        ControlCard(
            title = "Drivers: ${driverCount.toInt()}",
            value = driverCount,
            range = 1f..4f,
            steps = 2,
            onValueChange = { driverCount = it }
        )
        ControlCard(
            title = "Port Diameter: ${portDiameterMm.toInt()} mm",
            value = portDiameterMm,
            range = 30f..150f,
            onValueChange = { portDiameterMm = it }
        )

        SummaryRow("Internal volume (approx.)", "${calcVolumeLiters(boxWidthCm, boxHeightCm, boxDepthCm)} L")
        SummaryRow("Driver layout", when (driverCount.toInt()) {
            1 -> "Single full-range"
            2 -> "Vertical MT"
            3 -> "2-way + sub"
            else -> "Array stack"
        })
    }
}

@Composable
private fun PreviewCard(
    boxWidthCm: Float,
    boxHeightCm: Float,
    driverCount: Int,
    portDiameterMm: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B202A))
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            val margin = 36f
            val cabinetWidth = size.width - margin * 2
            val cabinetHeight = size.height - margin * 2
            drawRoundRect(
                color = Color(0xFF2B3240),
                topLeft = Offset(margin, margin),
                size = androidx.compose.ui.geometry.Size(cabinetWidth, cabinetHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(30f, 30f)
            )

            val diameter = (cabinetWidth * (boxWidthCm / 80f)).coerceIn(56f, 108f)
            val spacing = cabinetHeight / (driverCount + 1)
            repeat(driverCount) { idx ->
                val center = Offset(size.width / 2, margin + spacing * (idx + 1))
                drawCircle(Color(0xFF0E121A), diameter / 2, center)
                drawCircle(Color(0xFF8BA3C7), diameter / 2, center, style = Stroke(5f))
                drawLine(
                    color = Color(0xFF8BA3C7),
                    start = center.copy(x = center.x - diameter / 3),
                    end = center.copy(x = center.x + diameter / 3),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
            }

            val portRadius = (portDiameterMm / 4f).coerceIn(14f, 32f)
            drawCircle(
                color = Color(0xFF111723),
                radius = portRadius,
                center = Offset(size.width / 2, size.height - margin - portRadius - 6)
            )
            drawCircle(
                color = Color(0xFF5F7AA3),
                radius = portRadius,
                center = Offset(size.width / 2, size.height - margin - portRadius - 6),
                style = Stroke(3f)
            )
        }
    }
}

@Composable
private fun ControlCard(
    title: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    onValueChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171C25)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(text = title, color = Color(0xFFE0E6EF), style = MaterialTheme.typography.titleMedium)
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = range,
                steps = steps
            )
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFF99A7BB))
        Spacer(modifier = Modifier.width(8.dp))
        Text(value, color = Color(0xFFEAF0FA), fontWeight = FontWeight.Medium)
    }
}

private fun calcVolumeLiters(widthCm: Float, heightCm: Float, depthCm: Float): Int {
    val liters = (widthCm * heightCm * depthCm) / 1000f
    return liters.toInt()
}
