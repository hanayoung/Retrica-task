# 필터 토글 앱 (Filter Toggle App)

## 지원자 정보
**이름:** 한아영   
**이메일:** hay6586@gmail.com  
**GitHub:** https://github.com/hanayoung

**과제 소요시간:** 총 14시간
- 기능 구현: 7시간
- 문서 작성 및 발표 준비: 7시간

---
## 구현한 코드의 간단한 설명

ColorFilter의 하위 클래스인 **ColorMatrixColorFilter**와 **ColorMatrix**를 활용하여 이미지에 흑백 필터, 밝기 증가, 원본으로 되돌리기를 적용하는 방식을 구현했습니다.

### 필터 적용 방식
`FilterViewModel`에서 `selectedFilter`에 현재 선택된 필터를 저장하고 ImageView에 선택된 필터를 적용하였습니다.

- **ColorMatrix 활용하여 필터 적용**: Bitmap을 직접 조작하지 않고, ImageView의 `colorFilter` 속성을 통해 선택된 필터를 적용
- **정적/동적 필터 분리**:   
  단계가 1개인 필터(흑백 필터)와 여러 개의 value를 가지며 단계가 나뉘어진 필터(밝기 증가 필터)를 분리하여 정적 필터와 동적 필터로 나누어 구현
    - **정적 필터 (흑백 필터)**: ColorMatrix에 `setSaturation(0f)`을 적용
    - **동적 필터 (밝기 증가 필터)**: 파라미터 값에 따라 ColorMatrix를 생성하여 적용
        - **BrightnessController**: 밝기 레벨을 단계별로 관리하는 별도 컨트롤러 구현, 클래스 내부 변수가 아닌 생성자 파라미터를 통해 밝기 레벨 리스트를 전달받을 수 있도록 하여 확장성 및 테스트 용이성 고려
    - **원본으로 되돌리기**: ImageView의 `clearColorFilter()`를 적용

### 폴더 구조
![폴더 구조](https://velog.velcdn.com/images/aoreo0017/post/47875458-c998-4fa3-a00d-8b551021d919/image.png)
- **ui**: UI와 관련된 코드 위치시킴. 화면인 `MainActivity`, MainActivity에서 filter 설정 시 사용하는 ViewModel인 `FilterViewModel`, filter 선택 버튼들을 recyclerview로 나타내 이를 괸리하는 `FilterRVAdapter`, MainActivity의 layout에서 사용되는 함수들이 있는 `BindingAdapter` 위치
- **model**: dto 위치시킴. FilterRVAdapter에서 사용되며, 이후 서버에서 받아오는 형태라고 가정하여 필터 이름, 필터 종류, 필터 아이콘 정보를 담는 `FilterInfo`, 존재하는 필터들의 타입을 sealed class로 나타내는 `FilterType` 위치
- **filter**: 적용되는 필터 기능 관련 코드 위치시킴. 동적, 정적 필터로 나누어 흑백 필터, 밝기 증가 필터, 원본으로 복귀의 작동을 하는 `ColorFilter`, 밝기 증가 단계를 조작하는 `BrightnessController` 위치


### 설계 구조
- **DataBinding 활용**: `@BindingAdapter`를 사용하여 ViewModel의 상태 변화를 자동으로 UI에 반영
- **sealed class 선택**: `enum` 대신 `sealed class`를 사용하여 확장성 및 런타임 성능 고려

## 어려웠던 점과 질문 사항

### 구현 중 겪은 문제들

1. **잘못된 ColorFilter 구현 선택**
    - 처음에 단순히 `setColorFilter`로 접근했더니 이미지에 색이 덮여버리는 문제 발생
    - `ColorMatrixColorFilter`를 사용하여 올바른 매트릭스 연산 기반 필터링으로 해결

2. **Data Binding에서의 리소스 ID 처리**
   ```kotlin
   app:colorFilter="@{viewModel.isMonochrome? @color/monochrome : null}"
   ```
    - 값으로 null을 넣으면 `Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'int java.lang.Integer.intValue()' on a null object reference` 오류 발생
        - 삼항연산자 대신 별도 메서드로 null 체크 로직 분리하여 해결
    - BindingAdapter에서 `@ColorInt` 대신 `@ColorRes` 로 전달 시, "cannot find resource ID" 오류 발생
        - `@{}` 내부에서 실제 ARGB 색상 값으로 변환되어 발생한 오류로 원인 파악하여, `@ColorInt`로 변경 시 오류 해결

3. **동적 필터의 상태 관리**
    - 밝기 필터의 단계별 처리 및 다른 필터와의 상호작용 처리
    - `BrightnessController`를 통한 인덱스 기반 관리로 해결

4. **버튼 나열 vs RecyclerView**
    - 초기에는 필요한 버튼 3개를 나열하는 형태로 구현했으나, 추후 확장성을 고려하여 필터 목록을 동적으로 구성할 수 있도록 변경
    - sealed class를 사용하여, 버튼 나열식으로 할 경우에는 sealed class가 런타임시 생성되며 컴파일 시 enum처럼 상수 접근이 되지 않아, layout에서 바로 접근할 수 없어 오류 발생했으나, recylcerview로 변경하며 클릭 이벤트를 adapter와 viewmodel에서 하며 해결

### 질문 사항
- **실제 필터 앱의 아키텍처**:    
  실제 상용 필터 앱에서는 필터 기능을 어떤 구조로 처리하는지 궁금합니다. 일반적으로 사용하는 패턴이나 구조가 있다면 알고 싶습니다.
- **필터 분류 전략**:   
  실무에서는 밝기 증감과 같이 동적으로 단계가 정해지는 필터와 단계가 하나로 이루어진 필터를 실제로 나누어 처리하는지 궁금합니다. 그렇다면 어떤 기준으로 분류하는지도 알고 싶습니다.
- **밝기 조절 기준**:   
  사용자 경험(UX) 측면에서 적절한 밝기 증감 폭은 어느 정도로 설정하는 것이 일반적인지 궁금합니다.

## 필터 적용에 대한 생각
필터를 통해서 사용자는 간단한 조작으로 이미지의 분위기를 즉각적으로 바꿀 수 있습니다.

### 흑백 필터 (Monochrome)
ColorMatrix의 `setSaturation(0f)`을 통해서 이미지 흑백처리를 하였습니다. Saturation이 채도를 의미하며 채도를 0f으로 설정하여 색상을 제거할 수 있는 것이 인상적이었습니다. 흑백 필터는 색상을 제거하여 형태와 명암에 집중할 수 있게 해주며, 특히 감성적인 분위기 연출에 효과적입니다.

### 밝기 증가 필터 (Brightness)
밝기 조절 구현 과정에서 적절한 단계 구분과 증가폭 설정에 대한 고민이 있었습니다. 1.0f~1.8f 범위에서 0.2씩 증가하는 5단계로 설정했는데, 이는 사용자가 변화를 체감할 수 있으면서도 과도하지 않은 수준으로 판단했습니다.
밝기 증가 필터는 저조도 환경에서 촬영된 사진의 가시성 개선이나 의도적인 밝은 톤의 분위기 연출에 유용합니다. 특히 어두운 환경에서 이미지의 세부 사항을 더 명확하게 확인하고자 할 때 실용적인 가치를 제공합니다.

## 코드 확장에 대한 생각

수백 개의 필터를 서버에서 다운로드하는 앱으로 확장할 때 고려해야 할 점

### 1. 필터 데이터 구조 설계
현재는 임의로 필요할 거라고 예측한 데이터로 구조를 설계했으나, 공통된 구조로 필터 정보를 주고받을 수 있도록 데이터 구조 정의가 필요합니다.

### 2. 필터 로딩 성능 최적화
초기 앱 실행 시 모든 필터를 한 번에 불러오지 않고, 페이지네이션 또는 필터 카테고리 별 로딩 등으로 트래픽을 최소화하여 필터 로딩 성능 최적화가 필요합니다.

### 3. 사용자 경험 개선
- **카테고리화**: 필터를 한 번에 다 보여주기만 하는 것이 아니라, 전체보기에 추가적으로 용도별(인물, 풍경, 빈티지 등)로 분류하여 제공
- **프리뷰 시스템**: 필터가 적용된 상태일 때, 화면을 눌러 원본과 비교할 수 있도록 제공
- **오프라인 지원**: 한 번 다운로드된 필터는 네트워크 없이도 사용 가능

### 4. 필터 버전 관리
필터가 업데이트될 수 있으므로 버전 정보를 관리하고, 기존 필터의 변경 여부를 감지해 업데이트할 수 있도록 해야 합니다.

## 결과물
- 기본 화면
  ![](https://velog.velcdn.com/images/aoreo0017/post/77e50b9e-03de-4069-82b6-b7fdab47afb2/image.png)

  ![](https://velog.velcdn.com/images/aoreo0017/post/104bf6d2-cec1-4589-b79b-eb2fe5de45d9/image.png)

- 흑백 필터 적용 화면
  ![](https://velog.velcdn.com/images/aoreo0017/post/ce0c8949-473a-41a3-907e-3d3126db5aab/image.png)

- 밝기 증가 적용 화면
  ![](https://velog.velcdn.com/images/aoreo0017/post/3d038b13-5bce-4571-95d5-34eeee6d6440/image.png)