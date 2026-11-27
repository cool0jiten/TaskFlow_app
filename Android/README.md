# TaskFlow Android App

An Android task-management client built with Kotlin and Jetpack Compose. The app connects to the TaskFlow REST backend to display, filter, create, and complete tasks.

> Backend setup and API documentation are maintained separately in [`../Backend/README.md`](../Backend/README.md).

## Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- AndroidX Core, Activity, Lifecycle, and ViewModel
- Navigation Compose
- Hilt for dependency injection
- Retrofit and OkHttp
- Gson
- Kotlin Coroutines
- JUnit, Espresso, and Compose testing

## Project Structure

```text
Android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/taskflow/app/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── TaskFlowApp.kt
│   │   │   │   ├── data/
│   │   │   │   ├── di/
│   │   │   │   ├── domain/
│   │   │   │   └── ui/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   ├── test/          # Local JVM tests
│   │   └── androidTest/   # Instrumented tests
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── gradlew
```

## Features

- View and filter tasks
- Filter tasks by assignee or completion status
- Create tasks with a title, description, due date, priority, and assignee
- Toggle task completion
- Use a modern Compose and Material 3 interface
- Communicate with the backend through Retrofit

## Requirements

- Android Studio with the Kotlin plugin
- JDK 11
- Android SDK 37 for compilation
- Android SDK 35 for the target platform
- Android 10 or newer for the minimum supported environment
- A running TaskFlow backend

## Android Configuration

| Setting | Value |
| --- | --- |
| Application ID | `com.taskflow` |
| Namespace | `com.taskflow` |
| Minimum SDK | 29 |
| Target SDK | 35 |
| Compile SDK | 37 |
| Java/Kotlin target | 11 |
| Compose | Enabled |

## Getting Started

1. Clone the repository.
2. Start the TaskFlow backend according to `Backend/README.md`.
3. Open the `Android/` directory in Android Studio.
4. Wait for Gradle synchronization to complete.
5. Select an emulator or connected Android device.
6. Run the `app` configuration.

## Backend Connection

Configure the app to use the backend API base URL for its environment.

### Android Emulator

The emulator cannot access the computer's `localhost` directly. Use:

```text
http://10.0.2.2:3000/
```

### Physical Device

Replace `localhost` with the computer's LAN IP address:

```text
http://<COMPUTER_LAN_IP>:3000/
```

Keep the device and computer on the same network. Local HTTP traffic may require a cleartext-network configuration in the app.

## API Usage

The app uses the following backend resources:

- `GET /api/tasks` — retrieve tasks with optional assignee or status filters
- `POST /api/tasks` — create a task
- `PATCH /api/tasks/:id/toggle` — toggle task completion

For request formats and backend details, see [`../Backend/README.md`](../Backend/README.md).

## Build Commands

Run these commands from the `Android/` directory:

```bash
# Debug build
./gradlew assembleDebug

# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# Android lint
./gradlew lint
```

On Windows:

```bat
gradlew.bat assembleDebug
gradlew.bat test
gradlew.bat connectedAndroidTest
gradlew.bat lint
```

## Testing

- `app/src/test/` contains local unit tests.
- `app/src/androidTest/` contains instrumented and Android UI tests.
- Run unit tests in Android Studio or with `./gradlew test`.
- Run instrumented tests on an emulator or connected device with `./gradlew connectedAndroidTest`.

## Development Notes

- The app uses Hilt modules for dependency injection.
- UI code is organized under `com/taskflow/app/ui`.
- Data and network code is organized under `com/taskflow/app/data`.
- Domain models and use-case-oriented code is organized under `com/taskflow/app/domain`.
- Use a configurable API URL so the app can be switched between emulator, physical-device, and production environments.

## Screen Structure

Task List → Task Detail → Back to Task List  
Task List → Add Task → Back to Task List

## Mobile-Specific Decisions

### Decision 1 — Optimistic vs. Pessimistic Updates

**Chosen approach: Optimistic updates.** When a user marks a task as complete or incomplete, the UI updates immediately for a responsive experience, and if the API request fails, the previous state is restored with an error message.

### Decision 2 — Filter Persistence

**Chosen approach: Preserve filters.** The selected assignee and completion-status filters remain applied when navigating from the Task List to Task Detail and back, so users can continue where they left off without reselecting filters.

## Debugging Question — `isTaskOverdue()`

### Bug 1 — Timezone Handling

`new Date().toISOString()` converts the current date and time to UTC before extracting the date. Depending on the user's local timezone, the UTC date can differ from the local date, which may incorrectly mark a task as overdue or not overdue.

### Bug 2 — Silent Failure When Data Is Missing or Invalid

If `dueDate` is missing, null, or contains an invalid date string, the comparison may silently return `false` instead of identifying invalid data. This can hide data-quality issues and make debugging difficult.

### Android Implementation — Overdue Handling

- Tasks without a due date (`null`) are treated as not overdue.
- Invalid due-date strings are safely handled and treated as not overdue.
- Completed tasks with past due dates are not displayed as overdue.
- Incomplete tasks with past due dates are displayed as overdue.
- Tasks due today are not considered overdue.


## AI Usage

ChatGPT was used as an AI assistance tool during development to help design the MVVM architecture, generate initial Kotlin/Jetpack Compose code, implement Hilt dependency injection, Flow-based state management, Navigation Compose, Retrofit API integration, and the Node.js/Express backend.

AI assistance was also used to understand implementation decisions, handle edge cases, and prepare README documentation. The generated code was reviewed and adapted to the assessment requirements. Manual verification, testing, debugging, and final implementation review were performed by the developer.

## Contributing

1. Fork the repository.
2. Create a feature branch.
3. Implement and test your Android changes.
4. Run the relevant Gradle test and lint tasks.
5. Open a pull request.
