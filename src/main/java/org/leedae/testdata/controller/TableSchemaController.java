package org.leedae.testdata.controller;


import org.leedae.testdata.dto.request.TableSchemaRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TableSchemaController {

    @GetMapping("/table-schema")
    public String tableSchema(TableSchemaRequest tableSchemaRequest) {
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
    public String mySchemas() {
        return "my-schemas";
    }

    @PostMapping("/table-schema/my-schemas/{schemaName}")
    public String deleteSchema(
            @PathVariable String schemaName,
            RedirectAttributes redirectAttrs){
        return "redirect:/my-schemas";
    }


    @GetMapping("/table-schema/export")
    public ResponseEntity<String> exportSchema(TableSchemaRequest tableSchemaRequest) {

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=table-schema.txt")
                .body("download complete!"); //TODO: 나중에 작업 예정
    }
}
