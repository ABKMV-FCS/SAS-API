package com.sas.sasapi.payload.request;

import com.sas.sasapi.model.CourseBatch;
import lombok.Data;

import java.util.List;

@Data
public class BulkStudentListUpdateRequest {
    List<String> usernames;
    CourseBatch courseBatchEvent;
}
