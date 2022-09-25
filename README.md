<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>


## 배운점
- ATDD에 대해 학습
  - TDD를 시나리오 테스팅 영역까지 확장
  - 요구사항이 잘 충족되는지 확인하기위해 수행되는 테스트로 시나리오에 대해 정리하여 도메인에 대한 이해도 올라갈 수 있음

## 주요 피드백
- 한글 변수명을 통해 가독성을 향상시켜보자
- 인자가 없는 public 생성자의 문제
- getter,setter의 사용을 지양해야하는 이유?
  - 클래스의 상태를 외부로 노출하는것을 줄임
- 정적메서드 팩토리사용
- 객체간 결합도를 낮춰라
- 테스트 코드에서도 단일책임 원칙을 위배하지 않았는지 생각해보자
- 테스트 코드 내에서 if, for구문이 있는경우 테스트코드가 아닌 로직이 될 수 있다.(테스트 코드에서 또다른 테스트 대상을 만들기도하고 테스트 코드 또한 복잡하게 만든다)
- 디미터의 법칙 (객체의 내부 구조가 외부로 노출되었는지)
- 표준예외를 사용하라(커스텀 예외가 많이 추가되는경우 관리측면이나 가독성 면에서 안좋을 수 있다.)




## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-admin/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-admin/blob/master/LICENSE.md) licensed.
