package com.c3transcribe.transribeapi.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTranscribtionsDto {
    private Integer userId;
    private String email;
    private RecentTranscribtionDto recentTranscribtionDto;
}
