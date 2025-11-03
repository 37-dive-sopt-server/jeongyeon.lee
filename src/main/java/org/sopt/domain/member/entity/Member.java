package org.sopt.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.member.constant.Gender;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    public static Member create(String name, LocalDate birthDate, String email, Gender gender){
        return Member.builder()
                .name(name)
                .birthDate(birthDate)
                .email(email)
                .gender(gender)
                .build();
    }

    public int getAge(){
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }
}
