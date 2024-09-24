## NeoNest(게이밍 기어 쇼핑몰)
- NeoNest는 사용자가 게이밍 제품을 구매하고, 장바구니에 담고, 주문 및 결제할 수 있는 쇼핑몰 플랫폼입니다. 관리자는 상품을 관리하고, 주문 상태를 변경하며, 사용자의 주문을 확인할 수 있습니다.

## 요구사항
1) **회원가입 및 로그인**: 사용자는 이메일과 비밀번호를 통해 회원가입을 할 수 있고, JWT를 통한 인증이 필요합니다.
2) **상품 관리**: 관리자는 상품을 등록하고 수정할 수 있으며, 상품 리스트를 조회하여 관리할 수 있습니다.
3) **주문 관리**: 사용자는 상품을 장바구니에 추가하고, 주문을 생성하고 결제를 진행할 수 있습니다. 관리자는 주문 상태를 관리할 수 있습니다.
4) **결제 기능**: 사용자는 결제 페이지를 통해 결제를 진행하고, 결제 성공 시 주문 상태가 업데이트됩니다.

## 기능 목록
1) **사용자 기능**
   - 회원가입 및 로그인
   - 상품 검색 및 카테고리별 필터링
   - 장바구니에 상품 추가 및 수량 관리
   - 주문 생성 및 결제
   - 마이페이지에서 주문 내역 확인

2) **관리자 기능**
   - 상품 등록, 수정 및 삭제
   - 모든 주문 목록 조회 및 주문 상태 변경
   - 주문 상세 내역 조회
   - 

## 도메인 설계
- **Member**: 사용자 정보 관리
- **Role**: 사용자 권한 관리
- **Product**: 상품 정보 관리
- **ProductCategory**: 상품 별 카테고리 관리
- **Category**: 카테고리 관리
- **Order**: 주문 정보 관리
- **OrderProduct**: 주문 별 상품 관리
- **Payment**: 결제 정보 관리
- **Delivery**: 배송 정보 관리
- **Cart** : 장바구니 정보 관리

## 활용 기술
- **Spring Boot** 3.2.1 : 웹 애플리케이션 개발을 위한 기본 프레임워크
- **Java 17** : Spring Boot 3.x.x에 대응하는 Java 버전을 사용
- **Spring Data JPA** : 데이터베이스 객체 관리
- **Querydsl** : 타입 안전 쿼리 SQL 쿼리 작성
- **MySQL** : 데이터베이스 서버
- **Spring Security** : 인증 및 인가 처리
- **JWT(JSON Web Tokens)** : 안전한 사용자 인증을 위한 토큰 기반 인증 시스템
- **Thymeleaf**: 서버 사이드 템플릿 엔진(화면 만 띄워 줌)
- **Iamport(포트 원)**: 결제 처리 서비스

## ERD
업로드 예정

## API 문서
<details markdown="1">
  <summary>API 문서 목록</summary>

사용자 정보 조회
- URL: `/api/member`
- Method: `GET`
- Description: JWT 토큰 파싱을 통해 사용자 정보 조회합니다.
- Headers:
  - Authorization: Bearer {token}: 요청 헤더에 JWT 토큰 포함

사용자 정보 생성
- URL: `/api/member`
- Method: `POST`
- Description: 회원 가입을 통해 사용자 정보를 생성합니다.
- Request Body:
  - username (String, required): 사용자 이름
  - password (String, required): 비밀번호
  - email (String, required): 이메일
  - phoneNumber (String, required): 연락처

장바구니 조회
- URL: `/api/cart`
- Method: `GET`
- Description: 로그인된 사용자의 장바구니 목록을 조회합니다.
- Headers:
  - Authorization: Bearer {token}: 요청 헤더에 JWT 토큰 포함

장바구니 추가
- URL: `/api/cart`
- Method: `POST`
- Description: 장바구니에 새로운 상품을 추가합니다.
- Headers:
  - productId (Long, required): 상품 ID
  - quantity (int, required): 상품 수량

장바구니 수정
- URL: `/api/cart`
- Method: `PATCH`
- Description: 장바구니에 있는 상품의 수량을 수정합니다.
- Headers:
  - Authorization: Bearer {token}: 요청 헤더에 JWT 토큰 포함
- Request Body:
  - cartId (Long, required): 장바구니 항목 ID
  - quantity (int, required): 수정할 상품 수량

장바구니 항목 삭제
- URL: `/api/cart/{cartId}`
- Method: `DELETE`
- Description: 장바구니에서 특정 상품을 제거합니다.
- PathVariable:
  - cartId (Long, required): 장바구니 항목 ID

주문 생성
- URL: `/api/orders`
- Method: `POST`
- Description: 장바구니에 담긴 상품들을 주문으로 생성합니다.
- Request Body:
  - cartIds (List<Long>, required): 장바구니 항목 ID 리스트
  - deliveryAddress (String, required): 배송지 주소
  - paymentMethod (String, required): 결제 방식

주문 조회
- URL: `/api/orders/{orderUid}`
- Method: `GET`
- Description: 주문 상세 정보를 조회합니다.
- PathVariable:
  - orderUid (String, required): 오더 uid // 변경 예정

관리자 주문 목록 조회
- URL: `/api/admin/orders`
- Method: `GET`
- Description: 모든 주문의 목록을 관리자 권한으로 조회합니다.

관리자 주문 상태 변경
- URL: `/api/admin/orders/{orderId}/orderStatus`
- Method: `PATCH`
- Description: 관리자가 주문의 상태를 변경합니다. 
- PathVariable:
  - orderId (String, required): 오더 id
- Request Body:
  - status (String, required): 변경할 주문 상태 (예: COMPLETE, CANCEL)

상품 목록 조회
- URL: `/api/products`
- Method: `GET`
- Description:
  - 특정 카테고리에 속한 상품 목록을 조회합니다.
- Query Parameters: 
  - category (String, required): 조회할 상품 카테고리

상품 상세 조회
- URL: `/api/products/{productId}`
- Method: `GET`
- Description: 특정 상품의 상세 정보를 조회합니다.
- Headers:
  - Authorization: Bearer {token}: 요청 헤더에 JWT 토큰 포함
- PathVariable:
  - productId(Long, required)

상품 등록
- URL: `/api/products`
- Method: `POST`
- Description: 새로운 상품을 등록합니다.
- Request Body:
  - image (MultipartFile, required): 상품 이미지
  - product (ProductRegisterDto, required): 상품 정보

상품 수정
- URL: `/api/products/{productId}`
- Method: `PATCH`
- Description: 기존 상품의 정보를 수정합니다.
- Request Body:
  - image (MultipartFile, optional): 변경할 상품 이미지
  - product (ProductEditDto, required): 상품 정보

결제 요청
- URL: `/api/payment`
- Method: `POST`
- Description: 결제 정보를 검증하고, 결제를 처리합니다.
- Request Body:
  - paymentUid (String, required): 결제 고유 번호
  - orderUid (String, required): 주문 고유 번호
  - payMethod (String, required): 결제 방식
  - recipientName (String, required): 수령인 이름
  - phoneNumber (String, required): 수령인 전화번호
  - postcode (String, required): 우편번호
  - address (String, required): 주소
  - deliveryRequest (String, optional): 배송 요청 사항
  - paymentItems (List, required): 결제 항목 정보

</details>

## 주요 화면
- 로그인 화면 페이지
  - 추가예정
- 상품 목록 페이지
  - 추가예정
- 상품 상세 페이지
  - 추가예정
- 장바구니 페이지
  - 추가예정
- 주문 페이지
  - 추가예정
- 결제 성공 페이지
  - 추가예정
- 마이페이지 주문 내역 페이지
  - 추가예정
- 관리자 상품 관리 페이지
  - 추가예정
- 관리자 주문 관리 페이지
  - 추가예정
