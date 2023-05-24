package jpa.Book_Store.Service;

import jpa.Book_Store.Domain.Member.Member;
import jpa.Book_Store.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //트랜잭션, 영속성 컨텍스트
//읽기 전용으로 하는 이유: 영속성 컨텍스트를 플러시 하지 않기 때문에 약간의 성능 향상
public class MemberService {

    //필드 주입
//    @Autowired
//    MemberRepository memberRepository;

    private final MemberRepository memberRepository;

    //생성자 주입
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    //회원 가입
    @Transactional //위에 읽기 전용으로 사용했기 때문에 별도로 어노테이션을 지정해줘야 함.
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 체크
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getUsername());

        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조희
    public List<Member> findMember() {
        return memberRepository.findAll();
    }

    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }
}
