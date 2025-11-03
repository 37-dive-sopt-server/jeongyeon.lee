package org.sopt.domain.member.entity;

import jakarta.persistence.*;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.member.constant.Gender;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private LocalDate birthDate;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "member")
    private List<Article> articles;

    protected Member() {
    }

    private Member(Long id, String name, LocalDate birthDate, String email, Gender gender) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public Gender getGender() {
        return gender;
    }

    public static Member create(Long id, String name, LocalDate birthDate, String email, Gender gender){
        return new Member(id, name, birthDate, email, gender);
    }

    public int getAge(){
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }
}
