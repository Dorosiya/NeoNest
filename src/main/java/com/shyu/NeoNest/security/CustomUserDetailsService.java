package com.shyu.NeoNest.security;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.exception.MemberNotFoundException;
import com.shyu.NeoNest.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {

        Member memberData = memberRepository.findByMemberName(memberName)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        if (memberData != null) {
            return new CustomUserDetails(memberData);
        }

        return null;
    }

}
