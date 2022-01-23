package com.siwony.jwt.member.dto;

import com.siwony.jwt.member.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class  MemberDto {

    @Getter @Builder @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
    public static final class Join {

        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$",
                message = "Invalid email format."
        )
        private String email;

        @NotBlank
        private String password;

        @NotBlank
        private String name;

        @Pattern(regexp = "01(?:0|1[6-9])(?:\\d{3}|\\d{4})\\d{4}$/",
            message = "Invalid phonenumber format"
        )
        private String phonenumber;
        
        public Member toEntity(String encryptPassword, Member.Role role){
            return Member.builder()
                    .email(this.email)
                    .password(encryptPassword)
                    .name(this.name)
                    .phonenumber(this.phonenumber)
                    .roles(Collections.singletonList(role))
                    .build();
        }
    }

    @Getter @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
    public static final class Login {
        private String email;
        private String password;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
    public static final class Credential{
        private String accessToken;
        private String refreshToken;
    }
}
