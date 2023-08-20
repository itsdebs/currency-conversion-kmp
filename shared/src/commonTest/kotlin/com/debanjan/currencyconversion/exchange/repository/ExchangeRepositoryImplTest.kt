import com.debanjan.currencyconversion.core.data.LocalExchangeDataSource
import com.debanjan.currencyconversion.core.data.RemoteExchangeDataSource
import com.debanjan.currencyconversion.exchange.exception.ExchangeRateFetchError
import com.debanjan.currencyconversion.exchange.repository.ExchangeRatesRepositoryImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlinx.coroutines.runBlocking

class ExchangeRatesRepositoryImplTest {

    @Test
    fun `getExchangeRates returns local rates if available`() = runBlocking {
        val localDataSource = mockLocalExchangeDataSource(mapOf("USD" to 1.0, "EUR" to 0.85))
        val remoteDataSource = mockRemoteExchangeDataSource(mapOf("USD" to 1.1, "EUR" to 0.9))
        val repository = ExchangeRatesRepositoryImpl(localDataSource, remoteDataSource)

        val rates = repository.getExchangeRates()

        assertEquals(2, rates.size)
        assertEquals(1.0, rates["USD"])
        assertEquals(0.85, rates["EUR"])
    }

    @Test
    fun `getExchangeRates fetches remote rates if local is empty`() = runBlocking {
        val localDataSource = mockLocalExchangeDataSource(mapOf())
        val remoteDataSource = mockRemoteExchangeDataSource(mapOf("USD" to 1.1, "EUR" to 0.9))
        val repository = ExchangeRatesRepositoryImpl(localDataSource, remoteDataSource)

        val rates = repository.getExchangeRates()

        assertEquals(2, rates.size)
        assertEquals(1.1, rates["USD"])
        assertEquals(0.9, rates["EUR"])
    }

    @Test
    fun `getExchangeRates throws ExchangeRateFetchError if both local and remote are empty`() =
        runBlocking {
            val localDataSource = mockLocalExchangeDataSource(mapOf())
            val remoteDataSource = mockRemoteExchangeDataSource(mapOf())
            val repository = ExchangeRatesRepositoryImpl(localDataSource, remoteDataSource)

            try {
                repository.getExchangeRates()
                fail("Expected ExchangeRateFetchError")
            } catch (e: ExchangeRateFetchError) {
                // Expected
            }
        }

 
    @Test
    fun `getCurrencies fetches remote currencies if local is empty`() = runBlocking {
        val localDataSource = mockLocalExchangeDataSource(emptyMap())
        val remoteDataSource = mockRemoteExchangeDataSource(mapOf("USD" to 1.1, "EUR" to 0.9))
        val repository = ExchangeRatesRepositoryImpl(localDataSource, remoteDataSource)

        val currencies = repository.getCurrencies()

        assertEquals(2, currencies.size)
        assertTrue(currencies.contains("USD"))
        assertTrue(currencies.contains("EUR"))
    }

    @Test
    fun `getCurrencies throws ExchangeRateFetchError if both local and remote are empty`() =
        runBlocking {
            val localDataSource = mockLocalExchangeDataSource(emptyMap())
            val remoteDataSource = mockRemoteExchangeDataSource(emptyMap())
            val repository = ExchangeRatesRepositoryImpl(localDataSource, remoteDataSource)

            try {
                repository.getCurrencies()
                fail("Expected ExchangeRateFetchError")
            } catch (e: ExchangeRateFetchError) {
                // Expected
            }
        }

    @Test
    fun `updateExchangeRates succeeds and returns true on successful update`() =
        runBlocking {
            val localDataSource = mockLocalExchangeDataSource(emptyMap())
            val remoteDataSource = mockRemoteExchangeDataSource(mapOf("USD" to 1.1, "EUR" to 0.9))
            val repository = ExchangeRatesRepositoryImpl(localDataSource, remoteDataSource)

            val result = repository.updateExchangeRates()

            assertTrue(result)
        }

    @Test
    fun `updateExchangeRates fails and returns false on exception`() = runBlocking {
        val localDataSource = mockLocalExchangeDataSource(emptyMap())
        val remoteDataSource = mockFailingRemoteExchangeDataSource()
        val repository = ExchangeRatesRepositoryImpl(localDataSource, remoteDataSource)

        val result = repository.updateExchangeRates()

        assertFalse(result)
    }

    private fun mockLocalExchangeDataSource(exchangeRates: Map<String, Double>): LocalExchangeDataSource {
        return object : LocalExchangeDataSource {
            private var exchangeRates = exchangeRates
            override suspend fun getExchangeRates(): Map<String, Double> = this.exchangeRates
            override suspend fun getExchangeRateNames(): List<String> = this.exchangeRates.keys.toList()
            override suspend fun updateExchangeRates(exchangeRates: Map<String, Double>) {
                this.exchangeRates = exchangeRates
            }
        }
    }

    private fun mockRemoteExchangeDataSource(exchangeRates: Map<String, Double>): RemoteExchangeDataSource {
        return object : RemoteExchangeDataSource {
            override suspend fun getExchangeRates(): Map<String, Double> = exchangeRates
        }
    }

    private fun mockFailingRemoteExchangeDataSource(): RemoteExchangeDataSource {
        return object : RemoteExchangeDataSource {
            override suspend fun getExchangeRates(): Map<String, Double> {
                throw Exception("Remote data source failure")
            }
        }
    }
}
