package com.millenium.milleniumserver.payload.requests.teams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamLimitEditRequest {
    @NotNull
    private Double limit;
    @NotNull
    private Integer month;
    @NotNull
    private Integer year;
    @NotNull
    private Integer teamId;
}
