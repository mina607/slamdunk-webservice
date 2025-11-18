# 🚀 호텔 로봇 배달 웹 서비스 (Serving GO)

## 1. 📌 소개
   
호텔 고객이 웹을 통해 룸서비스·물품을 주문하고, 관리자는 주문을 관리하며 로봇을 호출해 배달을 수행하는 웹 기반 로봇 연동 서비스
웹–서버–로봇(ROS2)을 연결하여 실제 호텔 환경에서 사용할 수 있는 배달 자동화 기능을 구현

## 2. 📘 프로젝트 개요 
### 🎯 2.1 목표

고객이 웹에서 객실을 선택해 주문 가능

관리자 페이지에서 주문 확인 및 상태 변경

로봇과 WebSocket 통신을 통해 배달 명령 전달

### 🛠 2.2 개발 환경
- 언어 : 	JAVA / JavaScript / HTML / CSS
- 프레임워크 : 	Spring Boot
- 통신 : 	REST API, WebSocket, rosbridge
- DB : 	H2 Database

## 3. 🌐 주요 기능 (Features)

### 👤 3.1 고객 페이지 (User)

1. 객실 번호 선택
2. 룸서비스 / 물품 메뉴 선택 및 주문  
3. 수량 변경 및 요청 사항 입력  
4. 결제 방식 선택 (즉시 결제 / 나중 결제)
5. 주문 완료

<img width="1885" height="903" alt="스크린샷 2025-11-17 150815" src="https://github.com/user-attachments/assets/ac51c68c-8b42-4732-bc80-64dfcabba858" />


### 👨‍✈️ 3.2 관리자 페이지 (Admin)

1. 주문 목록 실시간 확인  
   ↓  
2. 주문 상태 변경 (접수 → 준비 → 배달 → 완료)  
   ↓  
3. 메뉴 · 물품 관리  
   ↓  
4. 로봇 상태 확인 (배터리 · 위치)  
   ↓  
5. 로봇 호출 (특정 객실로 이동 명령)

<img width="1895" height="898" alt="스크린샷 2025-11-17 151113" src="https://github.com/user-attachments/assets/111f3e2e-80d2-4adf-8e7c-ab73f1d36a8b" />

 
## 4. 🏗️ 시스템 구조 (System Architecture)

웹 서비스 전체 흐름:

[고객 웹]  →  주문 요청  →  [Spring 서버]  →  WebSocket  →  [ROS2 로봇]

[로봇]  →  배달 완료 콜백  →  [서버]  →  관리자 페이지 업데이트


고객/관리자 웹은 서버와 REST API로 통신

로봇 명령은 WebSocket으로 ROS2에 전달

로봇은 Nav2(Navigation2) 기반 경로 이동 수행

## 5. ▶️ 실행 방법 (How to Run)

```
# 1. 서버 실행
./gradlew bootRun

# 2. 웹 페이지 접속
http://localhost:8080

# 3. 로봇 WebSocket 브릿지 실행
ros2 run <your-package> <bridge-node>

# 4. 고객/관리자 페이지에서 주문 테스트
```

## 8. ✔️ 결과 (Result)

고객–관리자–로봇 전체 플로우 정상 구현

실제 호텔 로봇 서비스 시나리오 완성

UI/서버/로봇이 모두 연동된 실사용 수준 프로토타입 구축
