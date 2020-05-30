package com.rajesh.transcribe.transribeapi.api.models.dto;

import com.rajesh.transcribe.transribeapi.api.models.dto.sphinx.TranscribtionResponseDto;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor @AllArgsConstructor
@Data
public class RecentTranscribtionDto {
    List<TranscribtionResponseDto> recentTranDtoList;
}
