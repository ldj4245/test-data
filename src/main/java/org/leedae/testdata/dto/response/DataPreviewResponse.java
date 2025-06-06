package org.leedae.testdata.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataPreviewResponse {
    private String schemaName;
    private List<String> headers;
    private List<Map<String, String>> rows;
    private boolean success;
    private String message;
}
