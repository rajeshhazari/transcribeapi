package com.rajesh.transcribe.transribeapi.api.domian;

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
@Data
public class TranscribeFileLog {
    
    @Id
    @Column("log_id")
    public Long logId;
    @Column("transcribe_req_id")
    public Long transcribeReqId;
    @Column("username")
    public String username;
    @Column("transcribe_res_type")
    public String transcribeResType;
    @Column("file_name")
    public String fileName;
    @Column("session_id")
    public String sessionId;
    
    
}
