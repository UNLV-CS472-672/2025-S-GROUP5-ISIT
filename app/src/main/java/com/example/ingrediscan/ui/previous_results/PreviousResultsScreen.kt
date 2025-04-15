package com.example.ingrediscan.ui.previous_results

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ingrediscan.R
import com.example.ingrediscan.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PreviousResultsScreen(viewModel: PreviousResultsViewModel = viewModel()) {
    val scannedItems by viewModel.scannedItems.observeAsState(emptyList()) // LiveData observer
    var expanded by remember { mutableStateOf(false) } // State to control dropdown menu visibility
    var selectedItem: ScannedItem? by remember { mutableStateOf(null) } // State to hold the currently selected scanned item

    // When scannedItems load for the first time, automatically select the most recent one
    LaunchedEffect(scannedItems) {
        if (scannedItems.isNotEmpty() && selectedItem == null) {
            selectedItem = scannedItems.first() // First = most recent in your mock list
        }
    }
    // Outer container of the screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDCDCDC))
    ) {
        Column {
            // Top Bar with Dropdown
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        brush = SolidColor(Color.DarkGray),
                        shape = RectangleShape
                    )
                    .padding(top = 22.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // dropdown menu for selecting a scanned item
                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(text = selectedItem?.name ?: "Select Scan", fontSize = 25.sp)
                    }
                    // Dropdown menu with all scanned items
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        scannedItems.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.name) },
                                onClick = {
                                    selectedItem = item // Update the selected item
                                    expanded = false // Close dropdown
                                }
                            )
                        }
                    }
                }
            }
        }

        // 2. Scrollable Content Below Top Bar
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(770.dp)
                .padding(top = 70.dp) // Account for top bar
                .padding(horizontal = 16.dp)
        ) {
            // 3. Calorie Summary Box
            selectedItem?.let { item ->
                // Calorie Summary for selected item only
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    CalBox(
                        totalCalories = item.calories,
                        protein = item.protein,
                        carbs = item.carbs,
                        fat = item.fat
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 4. All Scanned Items (BlueBoxes)
                item {
                    BlueBox(
                        name = item.name,
                        brand = item.brand,
                        calories = item.calories,
                        protein = item.protein,
                        carbs = item.carbs,
                        fat = item.fat,
                        grade = item.grade
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // 5. Recent Scans Section
            item {
                HistoryBox(scannedItems)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 6. Manual Log Button (Fixed at the bottom)
            item {
                ManualLog()
            }
        }
    }
}
// ====== CALORIE SUMMARY BOX ======
@Composable
private fun CalBox(
    totalCalories: Int,
    protein: Int,
    carbs: Int,
    fat: Int
) {
    // Convert macronutrients to their calorie contributions
    val proteinCalories = protein * 4
    val carbCalories = carbs * 4
    val fatCalories = fat * 9

    // Calculate the percentage of total calories each macro contributes
    val proteinPercent = (proteinCalories.toFloat() / totalCalories).coerceIn(0f, 1f)
    val carbPercent = (carbCalories.toFloat() / totalCalories).coerceIn(0f, 1f)
    val fatPercent = (fatCalories.toFloat() / totalCalories).coerceIn(0f, 1f)

    // Container box for the calorie and macro summary
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        Row {
            Spacer(modifier = Modifier.width(24.dp))

            // Circular calorie meter
            CircleWithMeter(
                progress = (totalCalories / 2000f).coerceAtMost(1f),
                text1 = "$totalCalories",
                text2 = "Calories",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))

            // Macro nutrient breakdown bars
            Column(
                modifier = Modifier.height(150.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrientProgressBar("PROTEIN", protein, proteinPercent, Color(0xFF4CAF50))
                NutrientProgressBar("CARBS", carbs, carbPercent, Color(0xFF2196F3))
                NutrientProgressBar("FAT", fat, fatPercent, Color(0xFFFF6F61))
            }
        }
    }
}

// ====== INDIVIDUAL SCAN DISPLAY BOX ======
@Composable
private fun BlueBox(
    name: String,
    brand: String,
    calories: Int,
    protein: Int,
    carbs: Int,
    fat: Int,
    grade: String
) {
    // Container for individual scan entry
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Left side: Nutritional info
            Column(modifier = Modifier.width(200.dp)) {
                Text(
                    text = name,
                    fontSize = 30.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = brand,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = buildString {
                        append("Calories: ${calories}kcal\n")
                        append("Protein: ${protein}g\n")
                        append("Carbs: ${carbs}g\n")
                        append("Fat: ${fat}g")
                    },
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            // Right side: Share icon + grade bubble
            Column (
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.Top)
            ){
                Image(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = "Share",
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.End)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { /* Handle share click */ },
                    colorFilter = ColorFilter.tint(Color.Black)
                )

                WavyCircleExample(text = grade) // Displays the grade
            }
        }
    }
}

// ====== RECENT SCANS LIST BOX ======
@Composable
private fun HistoryBox(items: List<ScannedItem>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Title bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(lightGreen),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Recent Scans",
                    fontSize = 28.sp,
                    color = Color.White,
                    modifier = Modifier.padding(5.dp)
                )
            }

            // Scrollable list of items
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(items.size) { index ->
                    Text(
                        text = items[index].name,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

// ====== BUTTON FOR MANUAL ENTRY ======
@Composable
private fun ManualLog() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(DarkForegroundColor)
            .clickable { } // Dialog to enter manually
    ) {
        // Cutout circle for design
        Canvas(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(80.dp)
        ) {
            drawCircle(
                color = Color.DarkGray,
                radius = size.minDimension / 1f,
                center = Offset(size.width / 2, size.height / 2)
            )
        }

        // Button content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Enter Manually",
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.Normal
            )
            Image(
                painter = painterResource(id = R.drawable.barpng),
                contentDescription = "Barcode Icon",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(90.dp)
                    .padding(end = 25.dp)
            )
        }
    }
}

// ====== CIRCULAR CALORIE METER ======
@Composable
private fun CircleWithMeter(
    progress: Float,
    text1: String,
    text2: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            // Background circle
            drawCircle(
                color = Color.LightGray,
                radius = size.minDimension / 2,
                style = Stroke(width = 30f)
            )

            // Progress arc (calories)
            drawArc(
                color = lightGreen,
                startAngle = 90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(width = 30f, cap = StrokeCap.Round)
            )
        }

        // Text labels in the center
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = text1, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(text = text2, fontSize = 20.sp)
        }
    }
}

// ====== MACRONUTRIENT PROGRESS BAR ======
@Composable
private fun NutrientProgressBar(
    label: String,
    amount: Int,       // e.g. 10g
    percent: Float,    // e.g. 0.25 (25% of calories)
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "${(percent * 100).toInt()}% of calories", // Shows percent of total kcal
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Background track
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(8.dp)
                .background(Color.LightGray, RoundedCornerShape(5.dp))
        ) {
            // Colored progress portion
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent)
                    .height(10.dp)
                    .background(color, RoundedCornerShape(5.dp))
            )
        }
    }
}

// ====== GRADE BADGE (WAVY CIRCLE) ======
@Composable
private fun WavyCircleExample(text: String) {
    // Calculate colors based on grade
    val (fillColor, borderColor) = gradeToColors(text)
    Box {
        Canvas(modifier = Modifier.size(300.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val baseRadius = size.minDimension / 2 * 0.8f
            val amplitude = 20f
            val frequency = 9f

            // Draw wavy circular path
            val path = androidx.compose.ui.graphics.Path().apply {
                for (i in 0..360 step 1) {
                    val angle = Math.toRadians(i.toDouble()).toFloat()
                    val waveOffset = amplitude * sin(frequency * angle)
                    val radius = baseRadius + waveOffset
                    val x = center.x + radius * cos(angle)
                    val y = center.y + radius * sin(angle)

                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
                close()
            }

            // Fill and stroke the wavy circle
            drawPath(path = path, color = fillColor, style = Fill)
            drawPath(path = path, color = borderColor, style = Stroke(width = 20f))
        }

        // Grade text centered inside
        Text(
            text = text,
            color = Color.White,
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

// Grading function for the colors of the grade
private fun gradeToColors(grade: String): Pair<Color, Color> {
    val baseGrade = grade.trim().uppercase().firstOrNull()

    return when (baseGrade) {
        'A' -> Pair(GradeGreen, GradeDarkGreen)
        'B' -> Pair(GradeBlue, GradeDarkBlue)
        'C' -> Pair(GradeYellow, GradeDarkYellow)
        'D' -> Pair(GradeOrange, GradeDarkOrange)
        'F' -> Pair(GradeRed, GradeDarkRed)
        else -> Pair(GradeGray, GradeDarkGray)
    }
}
