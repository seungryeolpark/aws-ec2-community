# aws-ec2-community
**AWS EC2** 를 이용한 게시판

## 개발 환경
### Back-end
+ Java
+ Spring Boot
+ Spring Data JPA
+ Querydsl
+ MySQL
+ Redis
+ Gradle

### Front-end
+ Thymeleaf
+ Html
+ Css
+ JavaScript

## 기능

[1. Validation, SweetAlert2 라이브러리를 이용한 검증](#Validation-SweetAlert2-라이브러리를-이용한-검증)  
[2. Spring 메일 라이브러리, Redis를 이용한 인증메일 구현](#Spring-메일-라이브러리-Redis를-이용한-인증메일-구현)  
[3. Spring Security를 이용한 로그인 구현](#Spring-Security를-이용한-로그인-구현)  
[4. 게시판 CURD 구현](#게시판-CURD-구현)  
[5. 대댓글 구현](#대댓글-구현)

### Validation, SweetAlert2 라이브러리를 이용한 검증
#### Validation 을 이용한 검증
![1](https://user-images.githubusercontent.com/48073115/149626917-a6c7f5ba-08d6-4ea9-95ac-22b1d9585366.png)

#### SweetAlert2 라이브러리를 이용한 검증
![1](https://user-images.githubusercontent.com/48073115/149627201-90fbe702-da07-41e7-9599-eb2fc290e4dc.png)

### Spring 메일 라이브러리, Redis를 이용한 인증메일 구현
![1](https://user-images.githubusercontent.com/48073115/149655431-2c5f32d6-7d6c-43b0-a5c6-68c9745872c3.png)

![2](https://user-images.githubusercontent.com/48073115/149655432-9101bade-dad6-4fc1-a952-690e3e09ad8e.png)

### Spring Security를 이용한 로그인 구현
![login1](https://user-images.githubusercontent.com/48073115/149671015-1892566c-97cf-4d23-8856-947b68746e93.gif)

### 게시판 CURD 구현
![curd](https://user-images.githubusercontent.com/48073115/149757958-ee3b083b-8af5-4f83-84f9-0f6dd43a4f47.gif)

### 대댓글 구현
![nestedComment](https://user-images.githubusercontent.com/48073115/149917420-291306a9-ef22-4164-b288-309654b34fd8.gif)

+ Spring Data JPA, MySQL 를 이용한 게시판 글 CRUD 구현
  + 게시판 글 댓글 CRUD 구현 (대댓글도 구현)
+ spring security 를 이용한 로그인 시스템
+ redis, spring mail 를 이용한 인증메일 시스템
