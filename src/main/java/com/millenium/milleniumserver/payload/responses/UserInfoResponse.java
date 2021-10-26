package com.millenium.milleniumserver.payload.responses;

import com.millenium.milleniumserver.entity.auth.TeamEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserInfoResponse {
    List<TeamEntity> teams = new ArrayList<>();
    List<String> roles = new ArrayList<>();
}
