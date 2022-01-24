package com.siwony.jwt.security.service;


import com.siwony.jwt.member.Member;
import com.siwony.jwt.member.repository.MemberRepository;
import com.siwony.jwt.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * username은 email이다.
     *
     * @see Member
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found | email : " + username));
    }

}
