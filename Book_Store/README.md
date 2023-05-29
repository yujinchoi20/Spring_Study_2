## JPA 활용 1편 - Book Store

--------------------
도메인 모델

![](img/domain_model.png)

엔티티 설계

![](img/entity.png)

테이블 설계

![](img/table.png)

---------------------

### Member 엔티티, 레포지토리, 서비스 개발 + 테스트 

1. Member 엔티티 

@Entity
* city, street, zipcode는 Delivery 엔티티와 중복되는 코드 이므로 임베디드 타입으로 선언함.
* Order 엔티티와 일대다 연관관계 매핑함.

2. Member 레포지토리

@Repository

@PersistenceContext 
* save(): persist() 메서드를 통해 엔티티를 영속성 컨텍스트로 만든다. 
* findOne(): find() 메서드를 통해 특정 아이디를 찾는다. 
* findAll(): JPQL 쿼리를 사용해 회원 전체를 조회한다. 
* findByName(): JPQL 쿼리를 사용해 회원 이름으로 정보를 조회한다. 


* setParameter(): ':name' 의 값은 setParameter("name", name) 메서드를 통해서 쿼리에 값을 전달한다. 
* getResultList(): 쿼리의 실행 결과를 List 형식으로 가져오는 역할을 한다. 
3. Member 서비스

@Service

@Transactional(readOnly = true)
* join(): 회원 가입 진행
* validateDuplicateMember(): 중복 회원 검증
* findMembers(): 전체 회원 조회
* findOne(): id로 특정 회원 조회 


* 생성자 주입: 변경 불가능한 안전한 객체를 생성할 수 있다. @RequiredArgsConstructor 어노테이션을 사용하면 생성자를 직접 작성하지 않아도 된다. 

4. MemberRepositoryTest

* 회원가입, 중복회원체크, 회원조회(findOne)

-------------------------

### Item 엔티티, 레포지토리, 서비스  + 테스트

1. Item 엔티티

@Inheritance(strategy = InheritanceType.JOINED) 

* 조인전략으로 Book, Album, Movie 엔티티를 각각의 테이블을 만들어 필요 시 조인해서 사용함. 

@DiscriminatorColumn(name = "dtype")

* 부모 엔티티 클래스의 구분 컬럼을 설정하는데 사용됨. 
* 해당 어노테이션을 사용하여 테이블에 구분 컬럼을 추가하고, 각각의 엔티티가 어떤 자식 클래스를 나타내는지 식별함. 

비지니스 로직

* addStock(): 상품의 재고를 추가함. (주문 취소시 원래 재고로 돌아노는 기능)
* removeStock(): 상품의 재고를 줄임. (주문 접수시 주문 수량 만큼 재고를 줄인다.)

2. Item 레포지토리

* save(): 해당 상품이 없다면 추가하고, 이미 존재한다면 병합한다. (-> 추구 변경감지 기능으로 변경 예정!)
* findOne(): 상품 ID로 특정 상품을 찾음.
* findAll(): JPQL 쿼리문을 사용해서 등록된 상품 전체를 찾음.

3. Item 서비스

레포지토리에 작선한 메서드를 통해 상품을 등록하고 찾는 서비스를 제공함. 

4. Item 테스트

ItemRepository, ItemService를 주입받아 테스트 진행

일단은 상품을 추가하는 테스트만 진행함. 

* Book 상품을 생성하고, 이 상품을 등록함. 

![](img/itemTest.png)

* 추가한 Book 상품을 조회.

![](img/bookFind.png)

* 추가한 Item 전체 조회.

![](img/allItem.png)

--------------------

