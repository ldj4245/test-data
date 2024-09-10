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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class TableSchemaController {

    private final ObjectMapper mapper;

    @GetMapping("/table-schema")
    public String tableSchema(
            @RequestParam(required = false) String schemaName,
            Model model) {
        var tableSchema = defaultTableSchema(schemaName);


        model.addAttribute("tableSchema",tableSchema);
        model.addAttribute("mockDataTypes",MockDataType.toObjects());
        model.addAttribute("fileTypes", Arrays.stream(ExportFileType.values()).toList());
        return "table-schema";
    }



    @PostMapping("/table-schema")
    public String createOrUpdateTableSchema(
            TableSchemaRequest tableSchemaRequest,
            RedirectAttributes redirectAttrs
    ) {
        redirectAttrs.addFlashAttribute("tableSchemaRequest", tableSchemaRequest);

        return "redirect:/table-schema";
    }

    @GetMapping("/table-schema/my-schemas")
    public String mySchemas(Model model) {
        var tableSchemas = mySampleSchemas();

        model.addAttribute("tableSchemas", tableSchemas);



        return "my-schemas";
    }



    @PostMapping("/table-schema/my-schemas/{schemaName}")
    public String deleteSchema(
            @PathVariable String schemaName,
            RedirectAttributes redirectAttrs){
        return "redirect:/my-schemas";
    }


    @GetMapping("/table-schema/export")
    public ResponseEntity<String> exportSchema(TableSchemaExportRequest tableSchemaExportRequest) {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=table-schema.txt")
                .body(json(tableSchemaExportRequest)); //TODO: 나중에 작업 예정
    }


    private TableSchemaResponse defaultTableSchema(String schemaName) {
        return new TableSchemaResponse(
                schemaName != null ? schemaName : "schema_name",
                "Lee",
                List.of(
                        new SchemaFieldResponse("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
                        new SchemaFieldResponse("name", MockDataType.NAME, 2, 10, null, null),
                        new SchemaFieldResponse("age", MockDataType.NUMBER, 3, 20, null, null),
                        new SchemaFieldResponse("my_car", MockDataType.CAR, 4, 50, null, null)

                )

        );
    }

    private static List<SimpleTableSchemaResponse> mySampleSchemas() {
        return List.of(
                new SimpleTableSchemaResponse("schema_name1", "Lee", LocalDate.of(2024, 1, 1).atStartOfDay()),
                new SimpleTableSchemaResponse("schema_name2", "Lee", LocalDate.of(2024, 2, 2).atStartOfDay()),
                new SimpleTableSchemaResponse("schema_name3", "Lee", LocalDate.of(2024, 3, 3).atStartOfDay())
        );
    }

    private String json(Object object){
        try{
            return mapper.writeValueAsString(object);
        }catch (JsonProcessingException jpe){
            throw new RuntimeException(jpe);
        }
    }

}
