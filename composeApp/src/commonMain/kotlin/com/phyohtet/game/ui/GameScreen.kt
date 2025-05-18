package com.phyohtet.game.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phyohtet.game.model.Cell
import com.phyohtet.game.model.Direction
import com.phyohtet.game.ui.theme.BackgroundColor
import com.phyohtet.game.ui.theme.BoardColor
import com.phyohtet.game.ui.theme.ButtonColor
import com.phyohtet.game.ui.theme.GridColor
import com.phyohtet.game.ui.theme.TitleColor
import com.phyohtet.game.ui.theme.cellColors
import kotlinx.coroutines.delay
import kotlin.math.abs

const val gridSize = 4
const val cornerSize = 4
const val gridGap = 4

@Composable
fun GameScreen(
    state: GameState,
    onSwipe: (Direction) -> Unit,
    restart: () -> Unit,
) {

    val openRestartAlertDialog = remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .background(BackgroundColor)
            .systemBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .widthIn(max = 480.dp)
                .padding(top = 46.dp)
                .fillMaxSize(),
        ) {
            Row {
                Text(
                    text = "2048",
                    color = TitleColor,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.weight(1f),
                )

                ScoreBoard("BEST", state.bestScore)
                Spacer(Modifier.width(8.dp))
                ScoreBoard("SCORE", state.score, true)
            }

            Spacer(Modifier.height(24.dp))

            Board(
                cells = state.cells,
                onSwipe = onSwipe
            )

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Swipe on board to play",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        openRestartAlertDialog.value = true
                    },
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ButtonColor,
                        contentColor = Color.White,
                    )
                ) {
                    Text(
                        text = "Restart",
                    )
                }
            }
        }
    }

    when {
        openRestartAlertDialog.value -> {
            AlertDialog(
                title = {
                    Text("Confirm restart")
                },
                text = {
                    Text("Are you sure to restart game?")
                },
                onDismissRequest = {
                    openRestartAlertDialog.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openRestartAlertDialog.value = false
                            restart()
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openRestartAlertDialog.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun Board(cells: List<Cell>, onSwipe: ((Direction) -> Unit)) {
    val density = LocalDensity.current

    var endOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                color = BoardColor,
                shape = RoundedCornerShape(cornerSize.dp)
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        val (dx, dy) = endOffset
                        val offset = 0
                        val direction = when {
                            abs(dx) > abs(dy) && dx > offset -> Direction.RIGHT
                            abs(dx) > abs(dy) && dx < offset -> Direction.LEFT
                            abs(dy) > abs(dx) && dy > offset -> Direction.DOWN
                            abs(dy) > abs(dx) && dy < offset -> Direction.UP
                            else -> null
                        }
                        direction?.let { onSwipe(it) }
                    }
                ) { change, dragAmount ->
                    change.consume()

                    endOffset = dragAmount
                }
            }
    ) {
        val cellSize = with(density) { (maxWidth / gridSize).toPx() }

        repeat(gridSize) { x ->
            repeat(gridSize) { y ->
                val paddingStart = if (x == 0) gridGap * 2 else gridGap
                val paddingEnd = if (x == gridSize - 1) gridSize * 2 else gridGap
                val paddingTop = if (y == 0) gridGap * 2 else gridGap
                val paddingBottom = if (y == gridSize - 1) gridSize * 2 else gridGap
                val offset = IntOffset((x * cellSize).toInt(), (y * cellSize).toInt())

                Box(
                    modifier = Modifier
                        .offset { offset }
                        .size(maxWidth / gridSize)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = paddingStart.dp,
                                end = paddingEnd.dp,
                                top = paddingTop.dp,
                                bottom = paddingBottom.dp,
                            )
                            .background(
                                color = GridColor,
                                shape = RoundedCornerShape(cornerSize.dp)
                            )
                    )
                }

            }
        }

        cells.forEach { cell ->
            key(cell.id) {
                val (x, y) = cell
                val paddingStart = if (y == 0) gridGap * 2 else gridGap
                val paddingEnd = if (y == gridSize - 1) gridSize * 2 else gridGap
                val paddingTop = if (x == 0) gridGap * 2 else gridGap
                val paddingBottom = if (x == gridSize - 1) gridSize * 2 else gridGap
                val offset = IntOffset((y * cellSize).toInt(), (x * cellSize).toInt())

                AnimatedCellBox(
                    cell = cell,
                    intOffset = offset,
                    modifier = Modifier
                        .size(maxWidth / gridSize)
                ) {
                    val (background, color) = cellColors[cell.value] ?: cellColors.values.last()
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = paddingStart.dp,
                                end = paddingEnd.dp,
                                top = paddingTop.dp,
                                bottom = paddingBottom.dp,
                            )
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(cornerSize.dp)
                            )
                            .background(
                                color = background,
                                shape = RoundedCornerShape(cornerSize.dp)
                            )
                    ) {
                        Text(
                            text = "${cell.value}",
                            color = color,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedCellBox(
    cell: Cell,
    intOffset: IntOffset,
    modifier: Modifier = Modifier,
    content: @Composable (BoxScope.() -> Unit)
) {
    val offset by animateIntOffsetAsState(
        targetValue = intOffset,
        label = "Offset"
    )

    var isNewCell by remember { mutableStateOf(true) }

    val scale by animateFloatAsState(
        targetValue = if (isNewCell) 0f else 1f,
        animationSpec = tween(durationMillis = 200)
    )

    LaunchedEffect(cell.id) {
        delay(150)
        isNewCell = false
    }

    Box(
        modifier = modifier
            .offset { offset }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin.Center
            ),
        content = content
    )
}

@Composable
fun ScoreBoard(
    title: String,
    score: Int,
    animate: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .border(
                border = BorderStroke(1.dp, BoardColor),
                shape = RoundedCornerShape(cornerSize.dp)
            )
            .padding(vertical = 4.dp)
            .width(75.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color.Gray,
                fontSize = 12.sp
            )
            if (animate) {
                var scaleTarget by remember { mutableStateOf(1f) }
                val scale by animateFloatAsState(
                    targetValue = scaleTarget,
                    animationSpec = tween(200),
                    label = "Scale anim"
                )

                LaunchedEffect(score) {
                    if (score > 0) {
                        scaleTarget = 1.5f
                        delay(200)
                        scaleTarget = 1f
                    }
                }

                Text(
                    text = "$score",
                    color = Color.Gray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.scale(scale)
                )
            } else {
                Text(
                    text = "$score",
                    color = Color.Gray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}