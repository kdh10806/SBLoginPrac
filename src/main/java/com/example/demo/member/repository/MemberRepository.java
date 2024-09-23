package com.example.demo.member.repository;

import com.example.demo.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//<엔티티클래스 이름, 엔티티클래스의 pk 타입>
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    //이메일로 회원 정보 조회(select * from member_table where member_email = email)
    //optional -> null 방지
    Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
