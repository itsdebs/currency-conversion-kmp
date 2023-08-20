package com.debanjan.currencyconversion.exchange.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.debanjan.currencyconversion.exchange.ui.ExchangeViewModel.ScreenState.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModel
import org.koin.mp.KoinPlatform.getKoin


private val exchangeViewModel
    get() = getKoin().get<ExchangeViewModel>()

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CurrencyScreen() {
    val viewModel: ExchangeViewModel = viewModel(
        modelClass = ExchangeViewModel::class, keys = listOf(Unit)
    ) {
        exchangeViewModel
    }
    val (screenVisibility, setScreenVisibility) = remember {
        mutableStateOf(false)
    }
    val baseCurrencyUiState = viewModel.baseCurrencyUiState.getAsState()
    val baseCurrencyGridVisibility = viewModel.baseCurrencyDropdownVisibility.getAsState()
    val screenState = viewModel.screenScreenState.getAsState()

    val baseCurrency = viewModel.baseCurrencyState.getAsState()
    val otherCurrencies = viewModel.otherCurrenciesState.getAsState()
    val otherCurrencyNames = viewModel.otherCurrencyNamesState.getAsState()
    val baseCurrencyAmount = viewModel.baseCurrencyAmountState.getAsState()

    AnimatedVisibility(
        visible = screenVisibility,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            InputCurrency(value = baseCurrencyAmount.value, onValueChange = {
                viewModel.onCurrencyValueChanged(it)
            })
            when (screenState.value) {
                LOADED -> {

                    Spacer(modifier = Modifier.height(10.dp))
                    InputBase(
                        value = baseCurrency.value, onClick = viewModel::onBaseCurrencyClicked
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    AnimatedVisibility(visible = baseCurrencyGridVisibility.value,
                        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {

                        if (baseCurrencyGridVisibility.value) {
                            showBaseCurrencyGrid(
                                baseCurrencyUiState, otherCurrencyNames, viewModel
                            )
                        }
                    }
                    if (baseCurrencyGridVisibility.value) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    CurrencyGrid(values = otherCurrencies.value)
                }

                LOADING -> {
                    Spacer(modifier = Modifier.height(10.dp))
                    GridWithLoading()
                }

                ERROR -> {

                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Error fetching data",
                            color = Color.Red,
                            modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                        )
                    }
                }
            }

        }
    }

    LaunchedEffect(Unit) {
        launch { viewModel.baseCurrencyUiState.observeInState(baseCurrencyUiState) }
        launch {
            viewModel.baseCurrencyDropdownVisibility.observeInState(baseCurrencyGridVisibility)
        }
        launch { viewModel.screenScreenState.observeInState(screenState) }
        launch { viewModel.baseCurrencyState.observeInState(baseCurrency) }
        launch { viewModel.otherCurrenciesState.observeInState(otherCurrencies) }
        launch { viewModel.otherCurrencyNamesState.observeInState(otherCurrencyNames) }
        launch { viewModel.baseCurrencyAmountState.observeInState(baseCurrencyAmount) }
        delay(50)
        setScreenVisibility(true)
    }
}

@Composable
fun GridWithLoading() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 64.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(18) { _ ->
            LoadingShimmerEffect(70.dp, 70.dp)
        }
    }
}

@Composable
private fun ColumnScope.showBaseCurrencyGrid(
    baseCurrencyUiState: State<ExchangeViewModel.BaseCurrencyUiState>,
    otherCurrencyNames: State<List<String>>,
    viewModel: ExchangeViewModel
) {
    when (baseCurrencyUiState.value) {
        ExchangeViewModel.BaseCurrencyUiState.LOADED -> {
            ExchangeCurrencyDropdownGrid(
                otherCurrencyNames.value, viewModel::onCurrencySelected
            )
        }

        ExchangeViewModel.BaseCurrencyUiState.LOADING -> {
            ExchangeCurrencyDropdownShimmer()
        }

        ExchangeViewModel.BaseCurrencyUiState.ERROR -> {
            Text(
                "Loading Error. Please try again later",
                modifier = Modifier.width(128.dp).align(Alignment.End),
                color = Color.Red
            )
        }


    }
}

@Composable
fun ExchangeCurrencyDropdownShimmer() {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth().heightIn(min = 90.dp, max = 120.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(15) { _ ->
            LoadingShimmerEffect(30.dp, 50.dp, 1.dp)
        }
    }
}

private suspend fun <T> StateFlow<T>.observeInState(setState: (T) -> Unit) {
    collect {
        setState(it)
    }
}

@Composable
private fun <T> StateFlow<T>.getAsState(): MutableState<T> {
    return remember {
        mutableStateOf(value)
    }
}

private suspend fun <T> StateFlow<T>.observeInState(state: MutableState<T>) {
    collect {
        state.value = it
    }
}

@Composable
fun LoadingShimmerEffect(height: Dp = 64.dp, width: Dp = 64.dp, borderWidth: Dp = 1.dp) {

    //These colors will be used on the brush. The lightest color should be in the middle

    val gradient = listOf(
        Color.LightGray.copy(alpha = 0.9f), //darker grey (90% opacity)
        Color.LightGray.copy(alpha = 0.3f), //lighter grey (30% opacity)
        Color.LightGray.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition() // animate infinite times

    val translateAnimation = transition.animateFloat( //animate the transition
        initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // duration for the animation
                easing = FastOutLinearInEasing
            )
        )
    )
    val brush = linearGradient(
        colors = gradient, start = Offset(200f, 200f), end = Offset(
            x = translateAnimation.value, y = translateAnimation.value
        )
    )
    ShimmerGridItem(brush = brush, height, width, borderWidth)
}

@Composable
fun ShimmerGridItem(brush: Brush, height: Dp, width: Dp, borderWidth: Dp) {
    Row(
        modifier = Modifier.width(width).height(height).border(
            width = borderWidth, color = Color.Black
        ).padding(all = 10.dp), verticalAlignment = Alignment.Top
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Spacer(
                modifier = Modifier.height(20.dp).clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(fraction = 0.5f).background(brush)
            )
            if (height > 60.dp) {
                Spacer(modifier = Modifier.height(10.dp)) //creates an empty space between
                Spacer(
                    modifier = Modifier.height(20.dp).clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(fraction = 0.7f).background(brush)
                )
            }

        }
    }
}

@Composable
fun ColumnScope.ExchangeCurrencyDropdownGrid(
    data: List<String>, onCurrencyClick: (String) -> Unit
) {

    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth().heightIn(min = 90.dp, max = 120.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(data.size, key = { data[it] }) { index ->
            Text(
                text = data[index],
                modifier = Modifier.width(50.dp).height(30.dp).clickable {
                    onCurrencyClick.invoke(data[index])
                }.border(
                    width = 1.dp, color = Color.Black
                ).padding(6.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Black,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun ColumnScope.InputBase(value: String, onClick: () -> Unit) {
    Row(modifier = Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Tap to select base ")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            modifier = Modifier.border(width = 1.dp, color = Color.Black).width(60.dp).clickable {
                onClick.invoke()
            }.padding(8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilityScope.InputCurrency(
    value: String, animationDelay: Int = 0, onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().animateEnterExit(
            enter = fadeIn(animationSpec = tween(delayMillis = animationDelay)) + slideInHorizontally(
                animationSpec = tween(delayMillis = animationDelay, durationMillis = 150),
            )
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),

        placeholder = { Text(text = "50000") },
        label = { Text(text = "Input Currency") },
        visualTransformation = VisualTransformation.None
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedVisibilityScope.CurrencyGrid(values: List<ExchangeUIModel>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.fillMaxWidth().animateEnterExit(
            enter = fadeIn(animationSpec = tween(delayMillis = 75)) /*+ slideInHorizontally(
                animationSpec = tween(delayMillis = 150, durationMillis = 150),
            )*/
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(values.size, key = { values[it].currencyName }) { index ->
            CurrencyItem(value = values[index])
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyGridItemScope.CurrencyItem(value: ExchangeUIModel) {
    Column(
        modifier = Modifier.width(70.dp).height(70.dp).border(
            width = 1.dp, color = Color.Black
        ).padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value.currencyName, modifier = Modifier.animateItemPlacement(
                animationSpec = tween(150)
            ), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = value.currencyValue, modifier = Modifier.animateItemPlacement(
                animationSpec = tween(delayMillis = 75, durationMillis = 150)
            ), fontSize = 12.sp
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun AnimatedContentTransitionScope<Boolean>.sizeTransform() =
    SizeTransform { initialSize, targetSize ->
        if (targetState) {
            keyframes {
                // Expand horizontally first.
                IntSize(targetSize.width, initialSize.height) at 150
                durationMillis = 300
            }
        } else {
            keyframes {
                // Shrink vertically first.
                IntSize(initialSize.width, targetSize.height) at 150
                durationMillis = 300
            }
        }
    }