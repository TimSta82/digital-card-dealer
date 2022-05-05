package de.digitaldealer.cardsplease.ui.main.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
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
import de.digitaldealer.cardsplease.ui.theme.half_GU
import de.digitaldealer.cardsplease.ui.theme.one_GU
import de.digitaldealer.cardsplease.ui.theme.two_GU
import de.digitaldealer.cardsplease.ui.util.CardUtils

@Composable
fun CardFace(
    modifier: Modifier = Modifier,
    card: Card,
    elevation: Dp? = half_GU,
) {
    Card(
        shape = RoundedCornerShape(one_GU),
        modifier = modifier.clip(RoundedCornerShape(one_GU)),
        backgroundColor = Color.White,
        elevation = elevation ?: one_GU,
    ) {
        Box(
            modifier = modifier.aspectRatio(CARD_ASPECT_RATIO),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = modifier.padding(one_GU),
                backgroundColor = Color.White,
                border = BorderStroke(2.dp, color = colorResource(id = CardUtils.getSuitColor(card.suit)))
            ) {
                ConstraintLayout {
                    val (icon, cardValue) = createRefs()
                    Image(
                        modifier = modifier.constrainAs(icon) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        },
                        painter = painterResource(id = CardUtils.getSuitIcon(card.suit)),
                        contentDescription = ""
                    )
                    Text(
                        modifier = modifier.constrainAs(cardValue) {
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
    elevation: Dp? = half_GU,
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            colorResource(id = R.color.primaryColor),
            colorResource(id = R.color.card_red)
        )
    )
    Card(
        shape = RoundedCornerShape(one_GU),
        modifier = modifier.clip(RoundedCornerShape(one_GU)),
        backgroundColor = Color.DarkGray,
        elevation = elevation ?: one_GU,
    ) {
        Box(
            modifier = modifier
                .aspectRatio(CARD_ASPECT_RATIO)
                .background(brush = gradient),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = two_GU),
                painter = painterResource(id = R.drawable.ic_clubs),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
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
        shape = RoundedCornerShape(one_GU),
        modifier = modifier
            .graphicsLayer {
                if (axis == RotationAxis.AxisX) {
                    rotationX = rotation.value
                } else {
                    rotationY = rotation.value
                }
                cameraDistance = 12f * density
            }
            .clip(RoundedCornerShape(one_GU)),
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
