package com.siwony.jwt.member.dto;

import com.siwony.jwt.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public final class  MemberDto {

    @Getter
    @AllArgsConstructor
    public final static class Join {

        @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
                message = "Invalid email format."
        )
        private final String email;

        @NotBlank
        private final String password;

        @NotBlank
        private final String name;

        @Pattern(regexp = "01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$/",
            message = "Invalid phonenumber format"
        )
        private final String phonenumber;
        
        public Member toEntity(String encryptPassword){
            return Member.builder()
                    .email(this.email)
                    .password(encryptPassword)
                    .name(this.name)
                    .phonenumber(this.phonenumber)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    public final static class Login {
        private final String email;
        private final String password;
    }

    @Getter
    @AllArgsConstructor
    public final static class Credential{
        private final String accessToken;
        private final String refreshToken;
    }
}
