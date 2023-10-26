# java spring SUPUL 프로젝트
- Server Repository: <a href="https://github.com/tztos104/teamproject2_Supul_java_spring">서버 Git 주소</a>

## 프로젝트 소개
* 프로젝트 명: Supul
  > “수수께끼를 풀어방" 을 줄인 말로 자연과 함께하는 느낌의 방탈출 카페의 이름으로 정했습니다.
  > 핵심기능은 예약, 관리자페이지, 통계

* 기획 의도
  > 방탈출은 16년부터 지금까지 지속적으로 인기가 있는 실내놀이로 언제든지 즐길 수 있고
  > 짜임새 있는 스토리와 제한적인 시간으로 게임 몰입도를 높일 수 있는 이점으로 많은 사람들에게 인기가 있습니다.
  > 기존 방탈출 사이트인 넥스트 에디션 사이트를 이용하는 과정에서 몇가지 불편한 점들을 발견했고, 이러한 문제점들을 개선하고자 방탈출 사이트  'Supul'을 개발하게 되었습니다. 
  > <br>**Figma**: <a href="https://www.figma.com/file/zffQCT0SZ5TDC3JORNaqUA/Project_Django_Team-3?type=whiteboard&node-id=0-1&t=Q6DA7FvR7HepJfcw-0">집집 설계</a>
  > <br>**PDF**: [수풀 프로젝트.pdf](https://github.com/pshho/team3_django_project/files/12905354/default.pdf)

### :mantelpiece_clock: 개발 기간
- 2023.09.11 - 2023.10.05

### :wrench: 개발 환경
- `java 17`
- `HTML | CSS | JavaScript`
- **Framework**: spring boot 3.2
- **Database**: MySql
- **개발 Tool**: Springtool, Figma, naver API. 포트원 API, chart.js

### :gear: 주요 기능
#### 1. 로그인/회원가입/마이페이지
- 로그인과 회원가입 구현
- 회원가입 시 아이디(영문, 숫자)/비밀번호 8자 이상(영, 숫자) 유효성 검사 구현
- 로그인 후 마이페이지를 통해 회원정보 수정, 회원탈퇴, 해당 아이디의 게시판 작성글 확인 기능, 찜목록 확인, 랭킹확인 구현
<details>
  <summary>마이페이지</summary>
  <p>
    <img src="https://github.com/pshho/team3_django_project/assets/128444203/8b114c60-a997-4ee5-a1bc-926ebb90d887" alt="MyPage">
    <img src="https://github.com/pshho/team3_django_project/assets/128444203/96c0dabd-ddd4-450a-a4b0-7a65a15eb122" alt="MyPage">
  </p>
</details>


#### 2. 예약 기능
- 해당 일 해당 시간 30분전에 자동으로 예약 불가.
- 당일 예약시 환불 불가 안내 메세지 출력.
- 예약 한 시간은 자동으로 예약 불가.
- 블랙리스트가 예약할 시 예약불가 메세지 출력.


#### 3. 관리자 페이지(지점장, 총괄)
- 총괄은 각지점을 관리 할 수 있는 관리자 페이지 구성.
- 지점장은 지점을 관리 할 수 있는 관리자 페이지 구성.
- 지점 대쉬보드에는 그지점에 오늘 예약 내역과 매출 1위 테마, 테마 매출내역을 확인 할 수 있게 구현.
- 총광 대쉬보드에는 전체지점 테마 통계와 전체 매출 1위테마, 테마별 매출 현황, 지점별, 테마별, 예약현황, 노쇼현향을 확인할수 있게 구현.




#### 4. 통계(지점장, 총괄)
- 지점장은 그 지점에 매출의 통계를 확인할 수 있는 페이지 구성.
- 총괄은 총별, 지점별, 테마별 매출의 통계를 확인 할 수 있는 페이지 구성.



#### 5. 게시판
- 공지게시판, 문의 게시판, 후기 게시판 구성
- 문의게시판의 댓글 추가, 수정, 삭제와 게시글 추천 기능 구현
- 후기 게시판은 이용객만 글쓰기 가능.






