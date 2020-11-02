package com.c3transcribe.transcribeapi.api.models.dto;

import com.c3transcribe.transcribeapi.api.models.dto.sphinx.TranscriptionResponseDto;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor @AllArgsConstructor
@Data
public class RecentTranscriptionDto {
    List<TranscriptionResponseDto> recentTranDtoList;
}
