package com.shyu.NeoNest.service;

import com.shyu.NeoNest.dto.response.UserDashboardDto;
import com.shyu.NeoNest.repository.UserDashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserDashboardRepository userDashboardRepository;

    public UserDashboardDto getUserDashboardDto(Long memberId) {
        return userDashboardRepository.getUserDashboardDtoByMemberId(memberId);
    }

}
