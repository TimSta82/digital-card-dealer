package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import de.digitaldealer.cardsplease.CARD_ASPECT_RATIO
import de.digitaldealer.cardsplease.R
import de.digitaldealer.cardsplease.domain.model.Card
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.util.CardUtils

@Composable
fun CardFace(
    modifier: Modifier = Modifier,
    card: Card,
    elevation: Dp? = 4.dp,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        backgroundColor = Color.White,
        elevation = elevation ?: 8.dp,
    ) {
        Box(
            modifier = Modifier.aspectRatio(CARD_ASPECT_RATIO),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.padding(one_GU),
                backgroundColor = Color.White,
                border = BorderStroke(2.dp, color = colorResource(id = CardUtils.getSuitColor(card.suit)))
            ) {
                ConstraintLayout {
                    val (icon, cardValue) = createRefs()
                    Image(
                        modifier = Modifier.constrainAs(icon) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                        painter = painterResource(id = CardUtils.getSuitIcon(card.suit)),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier.constrainAs(cardValue) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                        text = card.value,
                        color = colorResource(id = R.color.white),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 24.sp),
                    )
                }
            }
        }
    }
}

@Composable
fun CardBack(
    modifier: Modifier = Modifier,
    elevation: Dp? = 4.dp,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        backgroundColor = Color.DarkGray,
        elevation = elevation ?: 8.dp,
    ) {
        Box(
            modifier = Modifier.aspectRatio(CARD_ASPECT_RATIO),
            contentAlignment = Alignment.Center
        ) {
            Image(Icons.Filled.Api, contentDescription = "")
        }
    }
}

@Preview
@Composable
fun Preview_CardFace(modifier: Modifier = Modifier) {
    CardFace(card = Card(value = "Queen", code = "Q", suit = "SPADES", image = ""))
}

@Preview
@Composable
fun Preview_CardBack(modifier: Modifier = Modifier) {
    CardBack()
}


enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

enum class RotationAxis {
    AxisX,
    AxisY,
}

@ExperimentalMaterialApi
@Composable
fun FlipCard(
    card: Card,
    cardFace: CardFace,
    onClick: (CardFace) -> Unit,
    modifier: Modifier = Modifier,
    axis: RotationAxis = RotationAxis.AxisY,
//    face: @Composable () -> Unit = {},
//    front: @Composable () -> Unit = {},
) {
    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        )
    )
    Card(
        onClick = { onClick(cardFace) },
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .graphicsLayer {
                if (axis == RotationAxis.AxisX) {
                    rotationX = rotation.value
                } else {
                    rotationY = rotation.value
                }
                cameraDistance = 12f * density
            }
            .clip(RoundedCornerShape(8.dp)),
    ) {
        if (rotation.value <= 90f) {
            Box {
                CardBack()
            }
        } else {
            Box(
                modifier = Modifier.graphicsLayer {
                    if (axis == RotationAxis.AxisX) {
                        rotationX = 180f
                    } else {
                        rotationY = 180f
                    }
                },
            ) {
                CardFace(card = card)
            }
        }
    }
}