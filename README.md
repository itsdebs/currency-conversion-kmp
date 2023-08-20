# Clean Architecture Sample for KMP
This is a simple currency conversion application for Kotlin Multiplatform, tested on Android and iOS.
The App uses [Ktor-client](https://api.ktor.io/index.html) for accessing REST API, [SQLDelight](https://github.com/cashapp/sqldelight) for database management, and [Precompose](https://github.com/Tlaster/PreCompose) for viewmodel support.
The App is based on [Open Exchange Rates](https://openexchangerates.org/) using a free account. Fork it and try it as you wish.

## Replacing the API key
Hi, to use the app, please go to [Open Exchange Rates](https://openexchangerates.org/), create a free account and replace the key in
**kotlin/com/debanjan/currencyconversion/kmm/shared/network/ExchangeApiImpl.kt** and replace the value of **OPEN_EXCHANGE_API_KEY** with your own key.

## Todo
Add navigation to different screen

