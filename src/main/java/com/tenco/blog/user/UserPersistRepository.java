package com.tenco.blog.user;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

// SRP - 단일 책임의 원칙
@Repository // IOC 대상
@RequiredArgsConstructor
public class UserPersistRepository {

    // 영속성 컨텍스트 관리 및
    // @Autowired // DI - 스프링 프레임 워크가 주소값 자동 주입
    private final EntityManager em;

    // 회원 조회 기능
    public User findById (Integer id){
       // 조회
        User user = em.find(User.class,id);
        if (user == null){
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
       return user;

    } // end of findById

    // 회원 가입 요청 시 -> INSERT 기능 요구
    @Transactional //트랜잭션 처리
    public User save(User user) {
        // 매개 변수로 들어온 User Object는 비영속 상태인가? 영속인가? -> 비영속이다(1차캐시에 저장안됨)
        em.persist(user); // 1차 캐시에 할당 및 insert 작용

        // 리턴 시 User Object는 영속화 된 상태이다.
        return user;
    } // end of save

    // 사용자 이름 중복 확인
    public User findByUserName(String username) {
        String jpqlStr = """
                SELECT u FROM User u WHERE u.username = :username
                """;
        // username이 unique키라서 다른 변수들은 안들어온다.

        try {
            return em.createQuery(jpqlStr, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }

    } // end of findByUserName

    // 로그인 요청 시 -> SELECT 기능 요구
    public User findByUserNameAndPassword(String username, String password) {
        String jpqlStr = """
                SELECT u FROM User u WHERE u.username = :username AND u.password =  :password
                """;

        try {
            return em.createQuery(jpqlStr, User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }

    } // end of findByUserNameAndPassword

    @Transactional
    public User updateById(Integer id, UserRequest.UpdateDTO updateDTO) {

        User userEntity = findById(id); // 코드 재활용 , 영속성 컨텍스트에 관리됨
        userEntity.setPassword(updateDTO.getPassword()); // 객체의 상태값 변경

        return userEntity;
    } // end of updateById
} // end of class
