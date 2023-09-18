# VitaTracker

![Language](https://img.shields.io/badge/Kotlin-1.8.20-blue) ![Compose](https://img.shields.io/badge/Compose-1.4.3-magenta) ![License](https://img.shields.io/aur/license/android-studio)

VitaTracker
Веб-сайт и мобильное приложение-напоминалка для приема БАДов
![Title](/img/title.png)

## Цели продукта

Цель данного приложения - помочь пользователям следить за ежедневным приемом БАД/препаратов и не пропустить ни одного приема. 
### Приложение позволит:
- сохранять информацию о своих лекарствах, включая дозировку, время и частоту приема;
- сохранять информацию о курсах БАДов (дате начала, продолжительности, перерывах между курсами);
- рассчитывать информацию о следующем курсе приема БАД/препаратов и напоминать о нем пользователю за установленный пользователем период до начала курса;
- устанавливать напоминания для каждого из БАД/препаратов и отправлять уведомления на телефон пользователя, чтобы напомнить ему о необходимости принять БАД/препарат.


## Architecture

- [MVI]
- [Clean-Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Use Case](https://en.wikipedia.org/wiki/Use_case)

## Technologies

- [Jetpack Compose](https://developer.android.com/jetpack/compose) Jetpack Compose is Android’s recommended modern toolkit for building native UI
- [Kotlin](https://kotlinlang.org) - %100 Kotlin
- [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous operations
- [Lifecycle-ktx](https://developer.android.com/kotlin/ktx)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Navigation Component Compose](https://developer.android.com/jetpack/compose/navigation)
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for Dependency Injection
- [Retrofit](https://github.com/square/retrofit) for network operations
- [OkHttp](https://github.com/square/okhttp)
- [Coil](https://coil-kt.github.io/coil/compose/) for image loading

## License

```
   Copyright 2023 VitaTracker (https://github.com/Vitatracker).

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
