package com.c3transcribe.transribeapi.api.model.dto;

import com.c3transcribe.transribeapi.api.models.sphinx4.TranscribtionResponseDto;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor @AllArgsConstructor
@Data
public class RecentTranscribtionDto {
    List<TranscribtionResponseDto> recentTranDtoList;
}
