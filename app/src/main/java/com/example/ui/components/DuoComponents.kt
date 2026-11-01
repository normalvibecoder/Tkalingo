package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DuoBorderGray
import com.example.ui.theme.DuoBorderGrayDark
import com.example.ui.theme.DuoCardSurface
import com.example.ui.theme.DuoFeatherGreen
import com.example.ui.theme.DuoFeatherGreenDark
import com.example.ui.theme.DuoFlameOrange
import com.example.ui.theme.DuoHeartRed
import com.example.ui.theme.DuoInkDark
import com.example.ui.theme.DuoSkyBlue
import com.example.ui.theme.DuoSunGold
import com.example.util.SoundEffectsManager
import kotlin.math.roundToInt

enum class MascotMood {
    CHEERFUL,
    EXCITED,
    THINKING,
    ENCOURAGING
}

/**
 * Draws "Bimo", the expressive Duolingo-inspired Wise Owl TKA mascot using Jetpack Compose Canvas.
 */
@Composable
fun BimoOwlMascot(
    modifier: Modifier = Modifier,
    size: Dp = 84.dp,
    mood: MascotMood = MascotMood.CHEERFUL,
    primaryColor: Color = DuoFeatherGreen
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mascot_bob")
    val bobOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobOffset"
    )

    Canvas(
        modifier = modifier
            .size(size)
            .offset { IntOffset(0, bobOffset.roundToInt()) }
    ) {
        val w = this.size.width
        val h = this.size.height

        // Left & Right Wings
        drawOval(
            color = primaryColor.copy(red = (primaryColor.red * 0.82f).coerceIn(0f, 1f)),
            topLeft = Offset(w * 0.04f, h * 0.36f),
            size = Size(w * 0.24f, h * 0.42f)
        )
        drawOval(
            color = primaryColor.copy(red = (primaryColor.red * 0.82f).coerceIn(0f, 1f)),
            topLeft = Offset(w * 0.72f, h * 0.36f),
            size = Size(w * 0.24f, h * 0.42f)
        )

        // Owl Rounded Body
        drawRoundRect(
            color = primaryColor,
            topLeft = Offset(w * 0.15f, h * 0.20f),
            size = Size(w * 0.70f, h * 0.70f),
            cornerRadius = CornerRadius(w * 0.32f, w * 0.32f)
        )

        // Cream Belly Patch
        drawOval(
            color = Color(0xFFE9FFD9),
            topLeft = Offset(w * 0.26f, h * 0.50f),
            size = Size(w * 0.48f, h * 0.34f)
        )

        // Little Feet
        drawRoundRect(
            color = DuoFlameOrange,
            topLeft = Offset(w * 0.30f, h * 0.86f),
            size = Size(w * 0.14f, h * 0.09f),
            cornerRadius = CornerRadius(8f, 8f)
        )
        drawRoundRect(
            color = DuoFlameOrange,
            topLeft = Offset(w * 0.56f, h * 0.86f),
            size = Size(w * 0.14f, h * 0.09f),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // White Eye Goggles
        val eyeRadius = w * 0.145f
        val leftEyeCenter = Offset(w * 0.37f, h * 0.41f)
        val rightEyeCenter = Offset(w * 0.63f, h * 0.41f)
        drawCircle(color = Color.White, radius = eyeRadius, center = leftEyeCenter)
        drawCircle(color = Color.White, radius = eyeRadius, center = rightEyeCenter)

        // Golden Scholarly Glasses Frame
        drawCircle(
            color = DuoSunGold,
            radius = eyeRadius,
            center = leftEyeCenter,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.032f)
        )
        drawCircle(
            color = DuoSunGold,
            radius = eyeRadius,
            center = rightEyeCenter,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.032f)
        )

        // Pupils
        val pupilRadius = if (mood == MascotMood.EXCITED) eyeRadius * 0.58f else eyeRadius * 0.50f
        drawCircle(color = DuoInkDark, radius = pupilRadius, center = leftEyeCenter)
        drawCircle(color = DuoInkDark, radius = pupilRadius, center = rightEyeCenter)

        // Eye Sparkles
        drawCircle(
            color = Color.White,
            radius = pupilRadius * 0.35f,
            center = Offset(leftEyeCenter.x - pupilRadius * 0.3f, leftEyeCenter.y - pupilRadius * 0.3f)
        )
        drawCircle(
            color = Color.White,
            radius = pupilRadius * 0.35f,
            center = Offset(rightEyeCenter.x - pupilRadius * 0.3f, rightEyeCenter.y - pupilRadius * 0.3f)
        )

        // Beak
        val beakPath = Path().apply {
            moveTo(w * 0.44f, h * 0.45f)
            lineTo(w * 0.56f, h * 0.45f)
            lineTo(w * 0.50f, h * 0.55f)
            close()
        }
        drawPath(path = beakPath, color = DuoFlameOrange)

        // Graduation Cap (TKA Scholar)
        val capPath = Path().apply {
            moveTo(w * 0.50f, h * 0.05f)
            lineTo(w * 0.82f, h * 0.16f)
            lineTo(w * 0.50f, h * 0.26f)
            lineTo(w * 0.18f, h * 0.16f)
            close()
        }
        drawPath(path = capPath, color = DuoInkDark)
        // Golden Tassel
        drawCircle(color = DuoSunGold, radius = w * 0.035f, center = Offset(w * 0.78f, h * 0.22f))
    }
}

/**
 * Tactile 3D Duolingo-style button with a raised 4.dp bottom lip that depresses when tapped.
 */
@Composable
fun DuoTactileButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = DuoFeatherGreen,
    lipColor: Color = DuoFeatherGreenDark,
    contentColor: Color = Color.White,
    enabled: Boolean = true,
    testTag: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val lipHeight = 4.dp
    val pressOffset = if (isPressed && enabled) lipHeight else 0.dp

    val effectiveContainer = if (enabled) containerColor else Color(0xFFE5E5E5)
    val effectiveLip = if (enabled) lipColor else Color(0xFFBFBFBF)
    val effectiveContent = if (enabled) contentColor else Color(0xFF9E9E9E)

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
            .height(54.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = {
                    SoundEffectsManager.playTap()
                    onClick()
                }
            )
    ) {
        // Bottom 3D lip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(16.dp))
                .background(effectiveLip)
        )
        // Top interactive face
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .offset(y = pressOffset)
                .clip(RoundedCornerShape(16.dp))
                .background(effectiveContainer)
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = effectiveContent,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Tactile 3D Card with rounded border and bottom lip like Duolingo unit/option cards.
 */
@Composable
fun DuoChunkyCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = DuoCardSurface,
    borderColor: Color = DuoBorderGray,
    bottomLipColor: Color = DuoBorderGrayDark,
    cornerRadius: Dp = 20.dp,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .background(bottomLipColor)
            .padding(bottom = 4.dp)
            .clip(shape)
            .background(backgroundColor)
            .border(BorderStroke(2.dp, borderColor), shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable {
                        SoundEffectsManager.playTap()
                        onClick()
                    }
                } else {
                    Modifier
                }
            )
            .padding(contentPadding)
    ) {
        content()
    }
}

/**
 * Top Duolingo-style gamified status bar showing Streak, Gems, Hearts, and SFX toggle.
 */
@Composable
fun DuoTopStatsBar(
    subjectShortName: String,
    subjectColor: Color,
    streakDays: Long,
    gems: Long,
    hearts: Long,
    isSoundEnabled: Boolean,
    onSubjectSelectorClick: () -> Unit,
    onToggleSound: () -> Unit,
    onRefillHeartsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = DuoCardSurface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Subject selector pill
            Surface(
                color = subjectColor.copy(alpha = 0.14f),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(2.dp, subjectColor),
                modifier = Modifier
                    .testTag("subject_selector_pill")
                    .clickable {
                        SoundEffectsManager.playTap()
                        onSubjectSelectorClick()
                    }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(subjectColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$subjectShortName ▾",
                        style = MaterialTheme.typography.labelLarge,
                        color = DuoInkDark
                    )
                }
            }

            // Gamified counters
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Streak
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocalFireDepartment,
                        contentDescription = "Streak Hari",
                        tint = DuoFlameOrange,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "$streakDays",
                        style = MaterialTheme.typography.titleMedium,
                        color = DuoFlameOrange,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Gems
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Diamond,
                        contentDescription = "Permata Gems",
                        tint = DuoSkyBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "$gems",
                        style = MaterialTheme.typography.titleMedium,
                        color = DuoSkyBlue,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Hearts (tappable to refill)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            SoundEffectsManager.playTap()
                            onRefillHeartsClick()
                        }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Energi Hati",
                        tint = DuoHeartRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "$hearts",
                        style = MaterialTheme.typography.titleMedium,
                        color = DuoHeartRed,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Sound FX toggle
                IconButton(
                    onClick = onToggleSound,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("toggle_sound_button")
                ) {
                    Icon(
                        imageVector = if (isSoundEnabled) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff,
                        contentDescription = if (isSoundEnabled) "Matikan Suara SFX" else "Nyalakan Suara SFX",
                        tint = if (isSoundEnabled) DuoFeatherGreen else DuoInkDark.copy(alpha = 0.45f)
                    )
                }
            }
        }
    }
}

@Composable
fun MascotSpeechBanner(
    title: String,
    message: String,
    mood: MascotMood = MascotMood.CHEERFUL,
    accentColor: Color = DuoFeatherGreen,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BimoOwlMascot(size = 76.dp, mood = mood, primaryColor = accentColor)
        Spacer(modifier = Modifier.width(12.dp))
        DuoChunkyCard(
            modifier = Modifier.weight(1f),
            borderColor = accentColor.copy(alpha = 0.45f),
            bottomLipColor = accentColor.copy(alpha = 0.30f),
            contentPadding = PaddingValues(14.dp)
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DuoInkDark
                )
            }
        }
    }
}
