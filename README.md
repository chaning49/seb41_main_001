# Heart(seb41_main_001)
<div align="center">
  <img alt="로고 이미지" src="https://github.com/codestates-seb/seb41_main_001/assets/79316402/b722bc4b-b3bc-4ea0-bb00-22e2205bd7a5">
</div>

> Heart는 위치 기반의 동네 운동 메이트를 만나고, 친목도모를 할 수 있는 커뮤니티 사이트입니다. '심박수'라는 일종의 점수를 통해 상호 평가가 이루어지고, 회원의 신뢰도를 확인할 수 있습니다.

```markdown
☝🏻 현재 페이지는 Heart의 Server README 입니다.
☝🏻 마지막 업데이트는 2023년 6월 8일입니다. :)
☝🏻 이전에는 배포된 사이트 주소가 있었지만 현재는 배포 상태가 아닙니다.
```

## 📎 배포 링크(Deprecated)
~**https://heartone.site**~

## 🌟 기본 기능
### 🙋🏻‍♂️ 회원
- 회원 CRUD
    - 필요 시 소셜 회원가입 가능(Kakao, Google)
- 마이페이지
    - 작성한 글, 댓글, 신청한 모집글 확인 가능
    - 좋아요 누른 글을 저장하여 확인할 수 있음
### 🏃🏻‍♂️ 운동 메이트 모집 게시판
- 모집글 CRUD
    - 모집글은 회원이 설정한 위치 주변 5, 10, 20, 거리 설정 X 옵션을 통해 탐색 가능
- 운동별 또는 모집 상태별 모집글 검색 및 조회
- 모집글
  - 댓글 CRUD
  - 좋아요를 표시하여 회원 관심 목록에 저장
  - 끌어올리기
  - 모임 이후 회원 간 리뷰 작성 가능, 모임에 대한 점수 및 후기를 작성하고 워스트 멤버를 선택 가능(Optional)
  - 모집 신청하기
### 📢 자유 게시판
- 게시글
  - 게시글 CRUD
  - 댓글 CRUD
  - 좋아요를 표시하여 회원 관심 목록에 저장

## 👥 팀원 소개
|FE|FE|FE|BE|BE|BE|
|:------:|:---:|:---:|:---:|:---:|:---:|
|![Logo](https://github.com/codestates-seb/seb41_main_001/assets/79316402/b722bc4b-b3bc-4ea0-bb00-22e2205bd7a5)|![Logo](https://github.com/codestates-seb/seb41_main_001/assets/79316402/b722bc4b-b3bc-4ea0-bb00-22e2205bd7a5)|![Logo](https://github.com/codestates-seb/seb41_main_001/assets/79316402/b722bc4b-b3bc-4ea0-bb00-22e2205bd7a5)|![Logo](https://github.com/codestates-seb/seb41_main_001/assets/79316402/b722bc4b-b3bc-4ea0-bb00-22e2205bd7a5)|![Logo](https://github.com/codestates-seb/seb41_main_001/assets/79316402/b722bc4b-b3bc-4ea0-bb00-22e2205bd7a5)|![Logo](https://github.com/codestates-seb/seb41_main_001/assets/79316402/b722bc4b-b3bc-4ea0-bb00-22e2205bd7a5)|
|우하늘|민인애|조유지|[변찬중](https://github.com/chaning49)|윤인수|정의현|

## 📊 ERD 설계
![ERD](https://github.com/codestates-seb/seb41_main_001/assets/79316402/7f6987c1-d1fd-4466-983c-2c43bce5c522)

## 🔧 기술 스택 - Backend
- Java 11
- Spring
  - Boot
  - Data JPA
  - Security
  - OAuth 2.0
- JWT
- MySQL
- Swagger
- Redis

## 🌐 프로젝트 구조
![Heart_Architecture](https://github.com/chaning49/seb41_main_001/assets/79316402/27075cc8-f265-4eed-b7f0-503a6b369410)
