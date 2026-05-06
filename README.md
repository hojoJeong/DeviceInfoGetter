# DeviceInfoGetter

Android 디바이스의 디스플레이 정보를 확인하고, 특정 dp 크기의 박스를 화면에 렌더링해 실제 물리적 크기를 검증하는 앱입니다. 폴더블 디바이스처럼 멀티 스크린 환경도 지원합니다.

Jetpack Compose와 build-logic(Convention Plugin) 학습을 목적으로 합니다.

## Features

### Device Info Screen
- 디바이스명, 모델명, 제조사
- 스크린별 해상도 (px), 밀도 (density), DPI, DPI 카테고리
- 화면 비율, dp 기준 디스플레이 크기, 디스플레이 사이즈 카테고리, 화면 방향
- 멀티 디스플레이 환경에서 스크린 인덱스별로 정보 표시

### Square Screen
- 원하는 dp 값을 입력하면 해당 크기의 빨간 박스를 화면 중앙에 렌더링
- 박스 내부에 현재 dp 크기 표시
- 스크린샷 저장 기능 (DCIM/Screenshots 폴더)

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material3
- **Navigation**: Navigation Compose (Type-safe)
- **Layout**: ConstraintLayout for Compose
- **DI**: Hilt
- **Min SDK**: 24 / Target SDK: 35

## Screens

```
DeviceInfo Screen  →  Square Screen
(디바이스 정보 목록)    (dp 박스 검증)
```

## Jetpack Compose

### 상태 관리 (State Management)

- `MutableStateFlow` + `StateFlow`로 ViewModel 상태를 선언하고, `collectAsState()`로 Compose에서 구독
- 상태 호이스팅(State Hoisting): `DeviceInfoScreen`은 ViewModel을 보유하고, 실제 UI는 `DeviceInfoContent`로 내려줘 테스트·프리뷰 용이성 확보
- `hiltViewModel()`로 Hilt가 관리하는 ViewModel 주입

```kotlin
// ViewModel — StateFlow로 상태 선언
private val _boxSize = MutableStateFlow(200)
val boxSize: StateFlow<Int> = _boxSize.asStateFlow()

// Composable — collectAsState()로 구독
val boxSize by viewModel.boxSize.collectAsState()
```

### Navigation Compose (Type-safe)

- `sealed interface`에 `@Serializable`을 붙인 object로 라우트를 타입으로 정의
- `composable<T> { }` 제네릭 DSL로 라우트 등록, `navController.navigate(Screen.Square)`로 이동
- 문자열 라우트 오타 없이 컴파일 타임에 라우트를 검증

```kotlin
sealed interface Screen {
    @Serializable object DeviceInfo : Screen
    @Serializable object Square : Screen
}

NavHost(navController, startDestination = Screen.DeviceInfo) {
    composable<Screen.DeviceInfo> { DeviceInfoScreen(...) }
    composable<Screen.Square> { SquareScreen() }
}
```

### ConstraintLayout for Compose

- `createRefs()`로 레퍼런스 선언, `Modifier.constrainAs { }` DSL로 각 컴포넌트의 앵커 연결
- `linkTo(parent.top)` / `linkTo(box.bottom)` 으로 상대 위치 지정

```kotlin
val (textField, box, button) = createRefs()

Box(modifier = Modifier.constrainAs(box) {
    top.linkTo(parent.top); bottom.linkTo(parent.bottom)
    start.linkTo(parent.start); end.linkTo(parent.end)
})
```

### 사이드 이펙트 & CompositionLocal

- `rememberLauncherForActivityResult`로 런타임 권한 요청 결과 처리
- `LocalContext.current`, `LocalView.current`로 Composition 트리에서 Android 컨텍스트 접근

### Preview

- `@Preview(showBackground = true, showSystemUi = true)`로 Android Studio에서 UI 확인
- 상태 호이스팅 덕분에 ViewModel 없이 더미 데이터만으로 `DeviceInfoContent` 프리뷰 가능

## build-logic (Convention Plugin)

`build-logic` 모듈에 Convention Plugin을 작성해 Gradle 설정의 중복 제거

### 구조

```
build-logic/
└── convention/
    ├── build.gradle.kts          # kotlin-dsl + 플러그인 등록
    └── src/main/kotlin/
        ├── AndroidApplicationConventionPlugin.kt         # 공통 Android 설정
        ├── AndroidApplicationComposeConventionPlugin.kt  # Compose 의존성
        └── AndroidHiltConventionPlugin.kt                # Hilt 의존성
```

### Convention Plugin 목록

| Plugin ID | 역할 |
|---|---|
| `convention.android.application` | `compileSdk`, `minSdk`, JVM 타깃, core-ktx 등 공통 Android 설정 |
| `convention.android.application.compose` | Compose BOM, Material3, UI 툴링 등 Compose 관련 의존성 |
| `convention.android.hilt` | Hilt + KSP + hilt-navigation-compose 의존성 |

### 핵심 학습 포인트

- `kotlin-dsl` 플러그인으로 Kotlin으로 Convention Plugin 작성
- `VersionCatalogExtension`으로 `libs.versions.toml` 의존성을 플러그인 내부에서 참조
- `app/build.gradle.kts`에서 plugin id 선언만으로 모든 설정 위임