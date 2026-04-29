# DeviceInfoGetter

Android 디바이스의 디스플레이 정보를 확인하고, 특정 dp 크기의 박스를 화면에 렌더링해 실제 물리적 크기를 검증하는 앱입니다. 폴더블 디바이스처럼 멀티 스크린 환경도 지원합니다.

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
- **Min SDK**: 24 / Target SDK: 35

## Screens

```
DeviceInfo Screen  →  Square Screen
(디바이스 정보 목록)    (dp 박스 검증)
```

## Build

```bash
./gradlew assembleDebug
```
