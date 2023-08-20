package com.debanjan.currencyconversion.kmm.shared.cache


internal class DatabaseImpl(databaseDriverFactory: DatabaseDriverFactory) : Database{
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.exchangeDatabaseQueries

    override fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllExchangeRates()
        }
    }

    override fun getAllExchangeRates(): Map<String, Double> {
        return dbQuery.selectAllExchangeRatesInfo { currency, rate ->
            currency to rate
        }.executeAsList().toMap()
    }
    override fun getAllExchangeCurrenciesNames(): List<String> {
        return dbQuery.selectAllExchangeCurrencyNames().executeAsList()
    }
    override fun insertExchangeRates(exchangeRates: Map<String, Double>) {
        dbQuery.transaction {
            exchangeRates.forEach { exchangeEntry ->
                dbQuery.insertExchannge(exchangeEntry.key, exchangeEntry.value)
            }
        }
    }
}