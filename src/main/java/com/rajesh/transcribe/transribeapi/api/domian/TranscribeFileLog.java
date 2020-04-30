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
    @Column("log_id")
    public Long logId;
    @Column("transcribe_req_id")
    public Long tReqId;
    @Column("username")
    public String username;
    @Column("transcribe_res_type")
    public String tResType;
    @Column("file_name")
    public String fileName;
    @Column("session_id")
    public String sessionId;
    @Column("token")
    public String token;
    
}
