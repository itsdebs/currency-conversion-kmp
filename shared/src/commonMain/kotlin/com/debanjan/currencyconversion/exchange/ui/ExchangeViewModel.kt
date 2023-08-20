package com.debanjan.currencyconversion.exchange.ui

import com.debanjan.currencyconversion.core.usecase.UseCase
import com.debanjan.currencyconversion.core.utils.Throttler
import com.debanjan.currencyconversion.exchange.model.ExchangeRatesDomainModel
import com.debanjan.currencyconversion.exchange.model.ExchangeValueRequestDomainModel
import com.debanjan.currencyconversion.kmm.shared.utils.format
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel


internal const val AMOUNT_FETCH_THROTTLE_MILLIS = 300L

class ExchangeViewModel(
    private val getAllCurrenciesUseCase: UseCase<String?, List<String>>,
    private val getExchangeAmountsUseCase: UseCase<ExchangeValueRequestDomainModel, List<ExchangeRatesDomainModel>>
) : ViewModel(), CoroutineScope by MainScope() {


    companion object {
        private const val BASE_CURRENCY = "USD"
    }

    private val _screenScreenState = MutableStateFlow(ScreenState.LOADING)
    val screenScreenState: StateFlow<ScreenState>
        get() = _screenScreenState

    private val _baseCurrencyUiState = MutableStateFlow(BaseCurrencyUiState.LOADING)
    val baseCurrencyUiState: StateFlow<BaseCurrencyUiState>
        get() = _baseCurrencyUiState
    private val _baseCurrencyDropdownVisibility = MutableStateFlow(false)
    val baseCurrencyDropdownVisibility: StateFlow<Boolean>
        get() = _baseCurrencyDropdownVisibility

    private val _baseCurrency = MutableStateFlow(BASE_CURRENCY)
    val baseCurrencyState: StateFlow<String>
        get() = _baseCurrency

    private val _otherCurrencies = MutableStateFlow(listOf<ExchangeUIModel>())
    val otherCurrenciesState: StateFlow<List<ExchangeUIModel>>
        get() = _otherCurrencies

    private val _otherCurrencyNames = MutableStateFlow(listOf<String>())
    val otherCurrencyNamesState: StateFlow<List<String>>
        get() = _otherCurrencyNames

    private val _baseCurrencyAmount = MutableStateFlow("")

    val baseCurrencyAmountState: StateFlow<String>
        get() = _baseCurrencyAmount

    init {
        initialize()
    }

    private fun initialize() {
        try {
            launch {
                resetCurrencies()
            }

        } catch (e: Throwable) {
            println("errrroooooorrrrrrrr")
            println(e.stackTraceToString())
            _screenScreenState.value = ScreenState.ERROR
        }

    }

    private val amountFetchingThrottler = Throttler()
    fun onCurrencyValueChanged(value: String) {
        _baseCurrencyAmount.value = value
        with(amountFetchingThrottler) {
            throttle(AMOUNT_FETCH_THROTTLE_MILLIS) {
                currencyRefreshCall()
            }
        }
    }

    private suspend fun currencyRefreshCall() {
        _screenScreenState.value = ScreenState.LOADING
        try {
            async {
                updateCurrencyAmounts().await()
            }
            _screenScreenState.value = ScreenState.LOADED
        } catch (e: Throwable) {
            _screenScreenState.value = ScreenState.ERROR
        }
    }

    private suspend fun resetCurrencies() {
        _screenScreenState.value = ScreenState.LOADING

        try {
            awaitAll(updateCurrencyList(), updateCurrencyAmounts())
            _screenScreenState.value = ScreenState.LOADED
        } catch (e: Throwable) {
            println("errrroooooorrrrrrrr")
            println(e.stackTraceToString())
            _screenScreenState.value = ScreenState.ERROR
        }

    }

    private fun updateCurrencyList() = async {
        _baseCurrencyUiState.value = BaseCurrencyUiState.LOADING
        _otherCurrencyNames.value = getAllCurrenciesUseCase.execute(
            _baseCurrency.value
        )
        _baseCurrencyUiState.value = BaseCurrencyUiState.LOADED
    }

    private fun updateCurrencyAmounts() = async {
        _otherCurrencies.value = getExchangeAmountsUseCase.execute(
            ExchangeValueRequestDomainModel(
                _baseCurrency.value, _baseCurrencyAmount.value.asDoubleValue
            )
        ).map { it.uiModel }
    }

    fun onBaseCurrencyClicked() {
        _baseCurrencyUiState.value = BaseCurrencyUiState.LOADING
        _baseCurrencyDropdownVisibility.value = !_baseCurrencyDropdownVisibility.value
        launch {
            try {
                updateCurrencyList().await()
            } catch (e: Throwable) {
                _baseCurrencyUiState.value = BaseCurrencyUiState.ERROR
            }
        }
    }

    fun onCurrencySelected(currency: String) {
        _baseCurrency.value = currency
        _baseCurrencyDropdownVisibility.value = false
        launch {
            currencyRefreshCall()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
    }

//    private val _coroutineContext = SupervisorJob() + Dispatchers.Main
//    override val coroutineContext: CoroutineContext
//        get() =

    enum class ScreenState {
        LOADED, LOADING, ERROR
    }

    enum class BaseCurrencyUiState {
        LOADED, LOADING, ERROR
    }

    private val String.asDoubleValue
        get() = toDoubleOrNull() ?: 0.0
    private val ExchangeRatesDomainModel.uiModel
        get() = ExchangeUIModel(
            currency, rate.coerceAtLeast(0.0).format(2)
        )
}