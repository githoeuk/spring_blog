package com.tenco.blog.user;


import com.tenco.blog._core.errors.Exception400;
import com.tenco.blog._core.errors.Exception404;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
    User 관련 비즈니스 로직을 처리하는 Service 계층
    Controller 와 Repository를 사이에서 ㅅ길제 업무 로직을 담당
 */
@Slf4j
@Service // IoC
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적인 읽기 전용 트랜잭션 처리, 조회 시 더티 체킹 x
public class UserService {


    private final UserRepository userRepository;

    // user의 비즈니스 기능
    // 회원 가입
    // 로그인 처리
    // 회원정보 수정 처리

    /**
     * 회원 가입 처리
     *
     * @param joinDTO ( 사용자 회원가입 요청 정보)
     * @return User ( 저장된 사용자 정보)
     */
    @Transactional //  쓰기 처리
    public UserResponse.JoinDTO 회원가입(UserRequest.JoinDTO joinDTO) {

        log.info("회원 가입 서비스 시작");
        userRepository.findByUsername(joinDTO.getUsername()).ifPresent(user -> {
            log.warn("회원가입 실패 - 중복된 사용자명 : {}", user.getUsername());
            throw new Exception400("이미 존재하는 이름입니다.");
        });
        User user = joinDTO.toEntity(); // UserRequest -> User로 형변환
        User savedUserEntity = userRepository.save(user);
        log.info("회원 가입 서비스 완료 - id : {}", savedUserEntity.getId());

        return new UserResponse.JoinDTO(savedUserEntity = userRepository.save(user));

    } // end of join


    /**
     * 로그인 처리
     *
     * @param loginDTO (사용자가 요청한 로그인 정보)
     * @return User (조회된 정보 세션 저장용)
     */
    public UserResponse.SessionDTO 로그인(UserRequest.LoginDTO loginDTO) {

        log.info("로그인 서비스 시작");

        User userEntity = userRepository.FindByUsernameAndPassword(loginDTO.getUsername()
                , loginDTO.getPassword()).orElseThrow(() -> {
            //3
            log.warn("로그인 실패 - 사용자 이름 또는 사용자 비밀번호 잘못 입력");
            return new Exception400("사용자명 또는 비밀번호가 올바르지 않습니다.");
        });

        log.info("로그인 성공 - 사용자명 : {}", loginDTO.getUsername());

        return new UserResponse.SessionDTO(userEntity);

    } // end of login

    /**
     * 사용자 정보 조회 (프로필 정보 보기 활용)
     *
     * @param id (User PK)
     * @return UserEntity
     */
    public UserResponse.SessionDTO 회원정보수정화면(Integer id) {
        log.info("사용자 정보 서비스 시작");

        User userEntity = userRepository.findById(id).orElseThrow(() -> {
            log.warn("사용자 정보 조회 실패");
            return new Exception404("사용자 정보를 찾을 수 없습니다.");
        });

        return new UserResponse.SessionDTO(userEntity);
    } // end of findById


    /**
     * 사용자 정보 수정 처리 ( 세션 동기화 처리해야 함)
     *
     * @param id        (User PK)
     * @param updateDTO (사용자가 요청한 데이터)
     * @return User
     */
    @Transactional
    public UserResponse.SessionDTO 회원정보수정(
            Integer id,
            UserRequest.UpdateDTO updateDTO,
            HttpSession session
    ) {

        log.info("회원 정보 수정 서비스 시작");
        User userEntity = userRepository.findById(id).orElseThrow(() -> {
            return new Exception404("회원 정보가 없습니다.");
        });
        userEntity.update(updateDTO);
        log.info("회원 정보 수정 완료 - 사용자 ID : {}", userEntity.getId());
        UserResponse.SessionDTO sessionDTO = new UserResponse.SessionDTO(userEntity);

        //세션 동기화 처리
        session.setAttribute("sessionUser",sessionDTO);
        return sessionDTO;
    } // end of updateById


} // end of class
