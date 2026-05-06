package com.tenco.blog.user;

import org.springframework.data.jpa.repository.JpaRepository;

// User의 JpaRepository 생성
// @Repository 작성 불필요 -> JpaRepository를 상속하고 있기 때문
public interface UserRepository extends JpaRepository<User,Integer> {

    // 1. 사용자 등록 및 수정 : save(User user)
    // 2. 사용자 단건 조회 기능 : findByAll(Integer Id)
    // 3.
}
