package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.dto.response.UserDashboardDto;
import org.springframework.stereotype.Repository;

public interface UserDashboardRepositoryCustom {

    UserDashboardDto getUserDashboardDtoByMemberId(Long memberId);

}
