package com.siwony.jwt.security.service;


import com.siwony.jwt.member.Member;
import com.siwony.jwt.member.repository.MemberRepository;
import com.siwony.jwt.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * {@link org.springframework.security.core.userdetails.UserDetailsService} 구현체
 */
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found | username : " + username));
        return new CustomUserDetails(member);
    }

}
