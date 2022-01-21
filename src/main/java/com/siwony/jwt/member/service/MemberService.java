package com.siwony.jwt.member.service;

import com.siwony.jwt.member.Member;
import com.siwony.jwt.member.dto.MemberDto;

public interface MemberService {

    Member create(MemberDto.Join joinDto);

    MemberDto.Credential login(MemberDto.Login loginDto);

}
