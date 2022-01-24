package com.siwony.jwt.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder @Getter
@NoArgsConstructor(access = PROTECTED) @AllArgsConstructor(access = PRIVATE)
public class Member {

    public enum Role {
        CLIENT, ADMIN
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberIdx;

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phonenumber", nullable = false)
    private String phonenumber;

    @ElementCollection
    @CollectionTable(
            name = "role",
            joinColumns = @JoinColumn(name = "member_id")
    )
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Member member = (Member) o;
        return memberIdx != null && Objects.equals(memberIdx, member.memberIdx);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


