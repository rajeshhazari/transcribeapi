package com.rajesh.transcribe.transribeapi.api.model.dto;

import com.rajesh.transcribe.transribeapi.api.models.sphinx4.TranscribtionResponseDto;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class RecentTranscribtionDto {
    List<TranscribtionResponseDto> recentTranDtoList;
}
