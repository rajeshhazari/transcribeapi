package com.rajesh.transcribe.transribeapi.api.models.dto;

import com.rajesh.transcribe.transribeapi.api.models.dto.sphinx.TranscriptionResponseDto;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor @AllArgsConstructor
@Data
public class RecentTranscriptionDto {
    List<TranscriptionResponseDto> recentTranDtoList;
}
