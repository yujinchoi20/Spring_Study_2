package jpa.Book_Store.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpa.Book_Store.Domain.Member.Member;
import org.springframework.stereotype.Repository;
import java.util.List;

//스프링 빈으로 등록
@Repository
public class MemberRepository {

    @PersistenceContext //엔티티 매니저 주입
    EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();

        //위의 JPQL 구문에서 특정 name 값을 같은 member를 찾기 위해 m.name = :name 을 where절로 사용
        //이때 :name 의 값은 setParameter("name", name) 메서드를 통해서 쿼리에 값을 전달한다.
        //setParameter()는 보안과 재사용성 측면에서 중요한 역할을 한다.
        //getResult()는 쿼리의 실행 결과를 List 형식으로 가져오는 메서드이다.
    }
}
