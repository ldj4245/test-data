package org.leedae.testdata.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.domain.constant.MockDataType;
import org.leedae.testdata.dto.request.TableSchemaExportRequest;
import org.leedae.testdata.dto.request.TableSchemaRequest;
import org.leedae.testdata.dto.response.SchemaFieldResponse;
import org.leedae.testdata.dto.response.SimpleTableSchemaResponse;
import org.leedae.testdata.dto.response.TableSchemaResponse;
import org.leedae.testdata.dto.security.GithubUser;
import org.leedae.testdata.service.SchemaExportService;
import org.leedae.testdata.service.TableSchemaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class TableSchemaController {

    private final TableSchemaService tableSchemaService;
    private final SchemaExportService schemaExportService;
    private final ObjectMapper mapper;

    @GetMapping("/table-schema")
    public String tableSchema(
            @AuthenticationPrincipal GithubUser githubUser,
            @RequestParam(required = false) String schemaName,
            Model model
    ) {
        TableSchemaResponse tableSchema = (githubUser != null && schemaName != null) ?
                TableSchemaResponse.fromDto(tableSchemaService.loadMySchema(githubUser.id(), schemaName)) :
                defaultTableSchema(schemaName);

        model.addAttribute("tableSchema", tableSchema);
        model.addAttribute("mockDataTypes", MockDataType.toObjects());
        model.addAttribute("fileTypes", Arrays.stream(ExportFileType.values()).toList());

        return "table-schema";
    }

    @PostMapping("/table-schema")
    public String createOrUpdateTableSchema(
            @AuthenticationPrincipal GithubUser githubUser,
            TableSchemaRequest tableSchemaRequest,
            RedirectAttributes redirectAttrs
    ) {
        tableSchemaService.upsertTableSchema(tableSchemaRequest.toDto(githubUser.id()));
        redirectAttrs.addAttribute("schemaName", tableSchemaRequest.getSchemaName());
        //addFlashAttribute는 저장된 정보를 그대로 보여줘야 하므로 사용했음.
        
        return "redirect:/table-schema";
    }

    @GetMapping("/table-schema/my-schemas")
    public String mySchemas(
            @AuthenticationPrincipal GithubUser githubUser,
            Model model
    ) {
        List<SimpleTableSchemaResponse> tableSchemas = tableSchemaService.loadMySchemas(githubUser.id())
                .stream()
                .map(SimpleTableSchemaResponse::fromDto)
                .toList();

        model.addAttribute("tableSchemas", tableSchemas);

        return "my-schemas";
    }

    @PostMapping("/table-schema/my-schemas/{schemaName}")
    public String deleteMySchema(
            @AuthenticationPrincipal GithubUser githubUser,
            @PathVariable String schemaName)
    {
        tableSchemaService.deleteTableSchema(githubUser.id(), schemaName);

        return "redirect:/table-schema/my-schemas";
    }

    @GetMapping("/table-schema/export")
    public ResponseEntity<String> exportTableSchema(
            @AuthenticationPrincipal GithubUser githubUser,
            TableSchemaExportRequest tableSchemaExportRequest) {
        String body = schemaExportService.export
                        (tableSchemaExportRequest.getFileType(),
                         tableSchemaExportRequest.toDto(githubUser != null ? githubUser.id() : null),
                         tableSchemaExportRequest.getRowCount());
        String filename = tableSchemaExportRequest.getSchemaName() + "." + tableSchemaExportRequest.getFileType().name().toLowerCase();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .body(body);
    }

    @GetMapping("/api/table-schema/{schemaName}")
    public ResponseEntity<TableSchemaResponse> getTableSchema(
            @AuthenticationPrincipal GithubUser githubUser,
            @PathVariable String schemaName
    ) {
        TableSchemaResponse tableSchema = TableSchemaResponse.fromDto(tableSchemaService.loadMySchema(githubUser.id(), schemaName));
        return ResponseEntity.ok(tableSchema);
    }

    @PostMapping("/api/table-schema")
    public ResponseEntity<Void> createOrUpdateTableSchema(
            @AuthenticationPrincipal GithubUser githubUser,
            @RequestBody TableSchemaRequest tableSchemaRequest
    ) {
        tableSchemaService.upsertTableSchema(tableSchemaRequest.toDto(githubUser.id()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/table-schema/{schemaName}")
    public ResponseEntity<Void> deleteTableSchema(
            @AuthenticationPrincipal GithubUser githubUser,
            @PathVariable String schemaName
    ) {
        tableSchemaService.deleteTableSchema(githubUser.id(), schemaName);
        return ResponseEntity.noContent().build();
    }

    private TableSchemaResponse defaultTableSchema(String schemaName) {
        return new TableSchemaResponse(
                schemaName != null ? schemaName : "schema_name",
                "Uno",
                List.of(
                        new SchemaFieldResponse("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
                        new SchemaFieldResponse("name", MockDataType.NAME, 2, 10, null, null),
                        new SchemaFieldResponse("age", MockDataType.NUMBER, 3, 20, null, null),
                        new SchemaFieldResponse("my_car", MockDataType.CAR, 4, 50, null, null)
                )
        );
    }


}
