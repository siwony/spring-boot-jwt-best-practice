package com.siwony.jwt.member.dto;

import com.siwony.jwt.member.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collections;

public final class  MemberDto {

    @Getter @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
    @EqualsAndHashCode
    public final static class Join {

        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
                message = "Invalid email format."
        )
        private String email;

        @NotBlank
        private String password;

        @NotBlank
        private String name;

        @Pattern(regexp = "01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$/",
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

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
    public final static class Login {
        private String email;
        private String password;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor
    public final static class Credential{
        private String accessToken;
        private String refreshToken;
    }
}
