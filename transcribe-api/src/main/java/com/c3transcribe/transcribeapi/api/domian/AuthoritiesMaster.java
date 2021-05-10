package com.c3transcribe.transcribeapi.api.domian;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("authorities_master")
@Data
public class AuthoritiesMaster {


    @Column("id")
    private Integer id;
    @Column("role_id")
    private String roleId;
    @Column("roledesc")
    private String roleDesc;
    @Column("max_file_size")
    private Integer maxUploadFileSize;
    @Column("max_number_files")
    private Integer maxFilesCount;

}
