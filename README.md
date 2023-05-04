
## 프로젝트 개요
- 이 프로젝트는 멀티모듈 프로젝트 구현시 일반적으로 선택하는 구조 및 코드들을 바로 사용할 수 있도록 만들어두는 일종의 템플릿이다.
- 모놀리스, MSA 어느 경우에도 사용될 수 있도록 범용성을 고려하여 구현했다.
- [멀티 모듈에 대한 포스팅](https://techblog.woowahan.com/2637/) 을 참고하였으나, 나에게 편한 방식으로 조금 바꾸었다.

## 모듈 이름 규칙
모듈 이름은 용도와 목적에 따라서 아래와 같이 구분하여 명명한다.

| 모듈 이름 규칙 | 설명 | 의존 가능 모듈 | 예시 |
|---------------|-------|----|---|
| -app          | 독립적으로 실행되는 어플리케이션이다. 즉, 실제로 서버로 기동되고 노출되어 운영되는 단위이다. | 모두 | product-app, external-gateway-app |
| -support      | 도메인 및 비즈니스 규칙을 알지 못한다. 오로지 지원 성격의 자원이 존재하는 모듈이다. | -shared, -library | product-client |
| -domain       | 도메인 규칙을 담당하는 모듈이다. | -shared, -library | product-domain |
| -shared       | DTO 및 Utils 등 오직 POJO 만 구현되는 모듈이다. | x | service-name-shared |
| -library      | 인프라 성격의 모듈이다. 특정 기술 라이브러리 수준의 모듈이다. | x | r2dbc-library, mysql-library, redis-library |

## todo (임시)

- app
- support
  - [ ] core-web 
  - [ ] webclient
- domain
- shared
- library
  - [ ] mysql
  - [ ] cache