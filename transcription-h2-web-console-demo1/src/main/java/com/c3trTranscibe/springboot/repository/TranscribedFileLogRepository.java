package com.c3trTranscibe.springboot.repository;
/**
 *
 */


import org.springframework.data.repository.CrudRepository;

import com.c3trTranscibe.springboot.model.TranscribeFileLog;

/**
 * @author rajesh
 *
 */

public interface TranscribedFileLogRepository  extends CrudRepository<TranscribeFileLog, Long>{
}
