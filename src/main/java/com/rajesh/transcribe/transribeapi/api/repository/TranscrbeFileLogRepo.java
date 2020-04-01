package com.rajesh.transcribe.transribeapi.api.repository;

import com.rajesh.transcribe.transribeapi.api.domian.TranscribeFileLog;
import org.springframework.data.repository.CrudRepository;

public interface TranscrbeFileLogRepo extends CrudRepository<TranscribeFileLog, Long> {
}
