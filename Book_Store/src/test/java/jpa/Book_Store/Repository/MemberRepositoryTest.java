package jpa.Book_Store.Repository;

import jpa.Book_Store.Domain.Member.Member;
import jpa.Book_Store.Service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class) //Spring과 통합 테스트를 진행할 때 사용됨.
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired MemberService memberService; //@Autowired 의존성 주입
    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback(value = true)
    public void 회원_가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("choi");

        //when
        Long saveId = memberService.join(member);

        //then
        Assertions.assertThat(memberRepository.findOne(saveId)).isEqualTo(member);
    }

    @Test(expected = IllegalStateException.class) //중복으로 인한 오류 예외처리
    public void 중복_회원_체크() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("choi");

        Member member2 = new Member();
        member2.setName("choi");

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("예외가 발생해야 한다. ");
    }
}