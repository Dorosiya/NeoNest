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
- **Review** : 리뷰 정보 관리
- **Wishlist** : 찜 정보 관리

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
- ![NeoNest 2024-11-19](https://github.com/user-attachments/assets/e96dc3f9-316d-4c59-8cd5-101b3e78e757)


## API 문서
<details markdown="1">
  <summary>API 문서 목록</summary>

사용자 정보 생성
- URL: `/api/member`
- Method: `POST`
- Description: 회원 가입을 통해 사용자 정보를 생성합니다.
- RequestBody:
  - memberName (String, required): 사용자 이름(ID)
  - password (String, required): 비밀번호
  - email (String, required): 이메일
  - name (String, required): 이름
  - age (String, required): 나이
  - phoneNumber (String, required): 연락처
  - address (String, required): 주소
  - detailAddress (String, required): 상세 주소
  - extraAddress (String, required): 참고 주소
  - postcode (String, required): 우편번호

사용자 정보 조회
- URL: `/api/member`
- Method: `GET`
- Description: 마이 프로필에서 사용자의 정보를 조회합니다.
- Headers:
  - Authorization: Bearer {token}: 쿠키에 JWT 토큰 포함

사용자 정보 수정
- URL: `/api/member`
- Method: `PATCH`
- Description: 사용자 정보 수정 요청을 통해 사용자 정보를 수정합니다.
- Headers:
  - Authorization: Bearer {token}: 쿠키에 JWT 토큰 포함
- RequestBody:
  - name (String, required): 이름
  - email (String, required): 이메일
  - age (String, required): 나이
  - phoneNumber (String, required): 연락처
  - address (String, required): 주소
  - detailAddress (String, required): 상세 주소
  - extraAddress (String, required): 참고 주소
  - postcode (String, required): 우편번호

사용자 로그인 식별 정보 조회
- URL: `/api/member/info`
- Method: `GET`
- Description: 페이지에서 사용자의 식별 정보를 조회합니다.
- Headers:
  - Authorization: Bearer {token}: 쿠키에 JWT 토큰 포함

상품 등록
- URL: `/api/products`
- Method: `POST`
- Description: 새로운 상품을 등록합니다.
- RequestParam:
  - image (MultipartFile, optional): 등록할 상품 이미지
- RequestPart:
  - productDto (ProductEditDto, required): 상품 정보
    - name (String, required): 이름
    - price (Long, required): 가격
    - stockQuantity (int, required): 재고 수량
    - description (String, required): 설명
    - category (String, required): 카테고리

상품 수정
- URL: `/api/products/{productId}`
- Method: `PATCH`
- Description: 기존 상품의 정보를 수정합니다.
- PathVariable:
  - productId (Long, required)
- RequestParam:
  - image (MultipartFile, optional): 변경할 상품 이미지
- RequestPart:
  - productEditDto (ProductEditDto, required): 상품 정보
    - name (String, required): 이름
    - price (Long, required): 가격
    - stockQuantity (int, required): 재고 수량
    - description (String, required): 설명
    - category (String, required): 카테고리

상품 목록 조회
- URL: `/api/products?category={}&sort={}`
- Method: `GET`
- Description: 상품 목록을 조회합니다.
- RequestParam:
  - category(String, optional): 카테고리
  - sort(String, optional): 정렬 정보

특정 상품 조회
- URL: `/api/products/{productId}`
- Method: `GET`
- Description: 특정 상품을 조회합니다.
- PathVariable
  - productId(String, required): 상품 ID

상품 이미지 조회
- URL: `/api/products/images/{filename}`
- Method: `GET`
- Description: 특정 상품의 이미지를 조회합니다.
- PathVariable
  - filename(String, required): 파일 이름

관리자 페이지 상품 목록 조회
- URL: `/api/admin/products`
- Method: `GET`
- Description: 관리자 페이지에서 상품 목록을 조회합니다.

관리자 페이지 상품 조회
- URL: `/admin/products/{productId}`
- Method: `GET`
- Description: 관리자 페이지에서 특정 상품을 조회합니다.
- PathVariable
  - productId(Long, required): 상품 ID

주문 생성
- URL: `/api/orders`
- Method: `POST`
- Description: 장바구니에 담긴 혹은 상품 상세 페이지에서 주문을 생성합니다.
- RequestBody:
  - orderCreateDto (OrderCreateDto, required): 장바구니 항목 ID 리스트
    - memberId (Long, required): 멤버 ID
      - orderItems (List<OrderItemDto>, required): 결제 방식
        - cartId(Long, optional): 장바구니 ID
        - productId(Long, required): 상품 ID
        - quantity(int, required): 수량

특정 주문 조회
- URL: `/orders/{orderUid}`
- Method: `GET`
- Description: 특정 주문을 조회합니다.
- PathVariable:
  - orderUid (String, required): 오더 UID

마이페이지 주문 조회
- URL: `/mypage/orders`
- Method: `GET`
- Description: 마이페이지에서의 주문들을 조회합니다.

마이페이지 특정 주문 조회
- URL: `/mypage/orders/{orderUid}`
- Method: `GET`
- Description: 마이페이지에서의 특정 주문을 조회합니다.
- PathVariable:
  - orderUid (String, required): 오더 UID

관리자 주문 조회
- URL: `/admin/orders`
- Method: `GET`
- Description: 관리자페이지에서 주문들을 조회합니다.

관리자 페이지 특정 주문 조회
- URL: `/admin/orders/{orderId}`
- Method: `GET`
- Description: 관리자페이지에서 특정 주문을 조회합니다.
- PathVariable:
  - orderId (String, required): 주문 ID

주문 상태 변경
- URL: `/admin/orders/{orderId}/orderStatus`
- Method: `PATCH`
- Description: 관리자페이지에서 특정 주문의 상태를 변경합니다.
- PathVariable:
  - orderId (String, required): 오더 ID
- RequestBody
  - statusCode (Map<String,String, required): 상태 변경 코드

결제 정보 리턴
- URL: `/api/payment`
- Method: `PATCH`
- Description: 결제를 정보 서버로 보냅니다.
- RequestBody
  - request (PaymentCallbackRequest, required): 상태 변경 코드
    - paymentUid (String, required): 결제 UID
    - orderUid (String, required): 주문 UID
    - payMethod (String, required): 결제 방법
    - recipientName (String, required): 결제자 이름
    - phoneNumber (String, required): 결제자 핸드폰 번호
    - postcode (String, required): 결제자 우편 번호
    - address (String, required): 결제자 주소
    - deliveryRequest (String, required): 요청사항
    - paymentItems (List<PaymentItemDto>, required): 결제 상품 DTO

결제 정보 가져오기
- URL: `/api/payment`
- Method: `PATCH`
- Description: 서버 저장된 결제 정보를 조회합니다.
- PathVariable
  - orderUid (String, required): 주문 UID

결제 정보 가져오기
- URL: `/success/{orderUid}`
- Method: `PATCH`
- Description: 결제 성공 후 결제 페이지 정보 리턴.
- PathVariable
  - orderUid (String, required): 주문 UID

장바구니 생성
- URL: `/api/cart`
- Method: `POST`
- Description: 장바구니를 생성합니다.
- RequestBody:
  - cartCreateDto (CartCreateDto, required): 장바구니 생성 DTO

장바구니 조회
- URL: `/api/cart`
- Method: `GET`
- Description: 장바구니를 조회합니다.

장바구니 수정
- URL: `/api/cart`
- Method: `PATCH`
- Description: 장바구니에 있는 상품의 수량을 수정합니다.
- RequestBody:
  - cartUpdateDto (CartUpdateDto, required): 장바구니 업데이트 DTO

장바구니 항목 삭제
- URL: `/api/cart/{cartId}`
- Method: `DELETE`
- Description: 장바구니에서 특정 상품을 제거합니다.
- PathVariable:
  - cartId (Long, required): 장바구니 항목 ID

리뷰 생성
- URL: `/api/reviews`
- Method: `POST`
- Description: 장바구니에서 특정 상품을 제거합니다.
- RequestBody:
  - reviewCreateDto (ReviewCreateDto, required): 리뷰 생성 DTO

주문 상품 리뷰 조회
- URL: `/api/reviews/{orderUid}/{productId}`
- Method: `GET`
- Description: 주문 상품에서 리뷰를 조회합니다.
- PathVariable:
  - orderUid (String, required): 주문 ID
  - productId (Long, required): 상품 ID

상품 리뷰 조회
- URL: `/api/products/{productId}/reviews`
- Method: `GET`
- Description: 상품에서 리뷰를 조회합니다.
- PathVariable:
  - productId (Long, required): 상품 ID

상품 찜 목록 추가(생성)
- URL: `/api/wishlist`
- Method: `POST`
- Description: 찜 목록에 추가(생성)합니다.
- RequestBody:
  - wishlistAddDto (WishlistAddDto, required): 찜 목록 추가 DTO

상품 찜 목록 확인(가져오기)
- URL: `/api/wishlist/{productId}`
- Method: `GET`
- Description: 찜 목록을 가져옵니다.
- PathVariable:
  - productId (Long, required): 상품 ID

상품 찜 목록 제거
- URL: `/api/wishlist/{productId}`
- Method: `GET`
- Description: 상품을 찜 목록에서 제거 합니다.
- PathVariable:
  - productId (Long, required): 상품 ID 

마이페이지 대시보드
- URL: `/api/mypage/summary`
- Method: `GET`
- Description: 마이페이지 대시보드의 
- PathVariable:
  - productId (Long, required): 상품 ID

</details>

## 주요 화면
- 회원 가입 페이지
  - ![1  회원가입 페이지](https://github.com/user-attachments/assets/428efab0-042e-451e-b4c8-cfd4108b0889)

- 로그인 화면 페이지
  - ![2  로그인 페이지](https://github.com/user-attachments/assets/eb7c8d53-d17e-4a1b-8c5a-887dcf3ecf90)

- 마이 페이지
  - ![3  마이페이지](https://github.com/user-attachments/assets/0434dc66-70c3-4c97-9aab-6be287313b9d)

- 메인화면 페이지
  - ![4  메인화면 페이지](https://github.com/user-attachments/assets/0e89c64e-bf2a-416c-8fde-2f163443f3a1)

- 상품 목록 페이지
  - ![5  상품 목록 페이지](https://github.com/user-attachments/assets/fa49599e-b426-4736-adbb-bde1f4ec1730)

- 상품 상세 페이지
  - ![6  상품 상세 페이지](https://github.com/user-attachments/assets/e4cd318b-8b15-422f-8a3c-4caac693a876)

- 장바구니 페이지
  - ![7  장바구니 페이지](https://github.com/user-attachments/assets/bf9f0ea7-51e4-41be-9828-51b4647b7f77)

- 주문 페이지
  - ![8  주문 페이지](https://github.com/user-attachments/assets/08794ee4-7e76-4a43-9a3e-96aee55ec7c8)

- 결제 성공 페이지
  - ![9  결제 성공 페이지](https://github.com/user-attachments/assets/6efa3aff-64d6-41db-98b2-0c244a8c5b6b)

- 마이페이지 주문 내역 페이지
  - ![10  마이페이지 주문 내역 페이지](https://github.com/user-attachments/assets/89b485de-a989-437b-9c9f-9dd22050a88f)

- 주문 상세 보기 페이지
  - ![11  주문 상세 보기 페이지](https://github.com/user-attachments/assets/f929b09c-1d56-4b98-bc11-f567abdb5a3d)

- 관리자 상품 등록 페이지
  - ![12  관리자 상품 등록 페이지](https://github.com/user-attachments/assets/0c6be0ae-b518-4118-877d-91f8c3f8f9bb)

- 관리자 상품 관리 페이지
  - ![13  관리자 상품 관리 페이지](https://github.com/user-attachments/assets/6de83fd1-0e1b-483b-989d-145daed172c8)

- 관리자 상품 관리 수정 페이지
  - ![14  관리자 상품 수정 페이지](https://github.com/user-attachments/assets/06f776a9-a0fb-45d3-9834-87f9bd362f82)

- 관리자 주문 관리 페이지
  - ![15  관리자 주문 관리 페이지](https://github.com/user-attachments/assets/d9579734-8734-472a-b8f5-e9fba5a2d45c)

- 관리자 주문 상세 보기 페이지
  - ![16  관리자 주문 상세 보기 페이지](https://github.com/user-attachments/assets/5af94a39-21ce-4bc4-b5a5-bfb1041d7c39)

- 리뷰 작성 페이지
  - ![17  리뷰 작성 페이지](https://github.com/user-attachments/assets/a04c99ca-e7fd-4252-a454-2179d396ff97)



