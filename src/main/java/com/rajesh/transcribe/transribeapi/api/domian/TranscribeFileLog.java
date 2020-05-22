package com.rajesh.transcribe.transribeapi.api.domian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author rajesh
 *
 */
@Table("TRANSCRIBEFILELOG")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TranscribeFileLog {
    
    @Id
    @Column("tflog_id")
    public Long id;
    
    @Column("log_id")
    public Long logId;
    @Column("transcription_req_id")
    public Long tReqId;
    @Column("file_size")
    public Long fileSize;
    @Column("email")
    public String email;
    @Column("transcribe_res")
    public String tRes;
    @Column("file_name")
    public String fileName;
    @Column("session_id")
    public String sessionId;
    
}
