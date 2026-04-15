# GitMeHub 🚀

**GitMeHub** is a modern Android application designed to explore GitHub repositories. The project serves as a showcase for **Multi-Module Clean Architecture**, **Offline-First** logic, and **Unidirectional Data Flow (UDF)**.

---

## 🏗 Architecture
This project follows **Clean Architecture** principles and is divided into distinct modules to ensure separation of concerns and scalability.

* **:app** – The UI layer. Built with **Jetpack Compose**, it follows the **MVVM** pattern with a **Single State Object** to ensure state consistency.
* **:domain** – The core business logic. This is a **pure Kotlin library** containing Entities and Use Cases. It has zero dependencies on the Android framework or the Data layer.
* **:data** – The implementation layer. Handles data sourcing from the GitHub REST API (Retrofit) and local persistence (Room).

---

## 🛠 Tech Stack
* **Language:** [Kotlin](https://kotlinlang.org/) + Coroutines & Flow
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* **Networking:** [Retrofit](https://square.github.io/retrofit/) + [OkHttp](https://square.github.io/okhttp/)
* **Database:** [Room](https://developer.android.com/training/data-storage/room)
* **Pagination:** [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data)

---

## ✨ Key Features
* **Smart Search:** Real-time repository searching with **debounced input**.
* **Offline Support:** Repositories are cached locally using a **Source of Truth** pattern.
* **Starred Repositories:** Save your favorite projects to local storage.