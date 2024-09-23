package com.example.demo.member.service;

import com.example.demo.member.dto.MemberDto;
import com.example.demo.member.entity.MemberEntity;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    public final MemberRepository memberRepository;

    //회원가입
    public void signup(MemberDto memberDto){
        //1. dto -> entity 변환
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDto);
        //2. repository의 save 메서드 호출
        //repository의 save() 호출(조건, entity객체를 넘겨줘야 함)
        memberRepository.save(memberEntity);
    }

    //로그인
    public MemberDto signin(MemberDto memberDto){
        // 1. 회원이 입력한 이메일로 DB에서 조회
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDto.getMemberEmail());
        if(byMemberEmail.isPresent()){
            //이메일 조회 결과가 있는 경우
            MemberEntity memberEntity = byMemberEmail.get();
            // 2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 확인
            if(memberEntity.getMemberPassword().equals(memberDto.getMemberPassword())){
                //비밀번호 일치
                //entity -> dto 변환 후 리턴
                MemberDto signedMemberDto = MemberDto.toMemberDto(memberEntity);
                return signedMemberDto;
            } else {
                //비밀번호 불일치
                return null;
            }
        }else {
            //이메일 조회 결과가 없는 경우
            return null;
        }
    }

    //회원 목록
    public List<MemberDto> findAll() {
        //모든 엔티티의 리스트
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        //저장할 dto리스트
        List<MemberDto> memberDtoList = new ArrayList<>();

        for(MemberEntity memberEntity : memberEntityList){
            MemberDto memberDto = MemberDto.toMemberDto(memberEntity);
            memberDtoList.add(memberDto);
        }
//        System.out.println("memberDtoList = " + memberDtoList);

        return memberDtoList;
    }

    //회원 목록 상세
    public MemberDto selectById(Long id){
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if(optionalMemberEntity.isPresent()){
            return MemberDto.toMemberDto(optionalMemberEntity.get()); //optional로 싸여있는 것을 get으로 깐다.
        } else {
            return null;
        }
    }

    //회원 정보 수정 이동
    public MemberDto updateForm(String myEmail){
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if(optionalMemberEntity.isPresent()){
            return MemberDto.toMemberDto(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    //회원 정보 수정
    public void update(MemberDto memberDto){
        memberRepository.save(MemberEntity.toUpdateEntity(memberDto)); //id가 없으면 insert, id가 있으면 update쿼리를 날림.
    }

    //회원 정보 삭제
    public void deleteById(Long id){
        memberRepository.deleteById(id);
    }
}
