package org.sopt.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.domain.article.entity.Article;
import org.sopt.domain.member.constant.Gender;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "member",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_member_email", columnNames = "email")
        }
)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    public void addArticle(Article article) {
        articles.add(article);
        article.setMember(this);
    }

    public static Member create(String name, String password, LocalDate birthDate, String email, Gender gender){
        return Member.builder()
                .name(name)
                .password(password)
                .birthDate(birthDate)
                .email(email)
                .gender(gender)
                .build();
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public int getAge(){
        return Period.between(this.birthDate, LocalDate.now()).getYears() + 1;
    }
}
