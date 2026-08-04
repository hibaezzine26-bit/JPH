package com.ocp.jph.dto;

import java.util.ArrayList;
import java.util.List;

public class ImportResultDto {
    private int importedCount;
    private List<String> errors = new ArrayList<>();

    public ImportResultDto() {}

    public ImportResultDto(int importedCount, List<String> errors) {
        this.importedCount = importedCount;
        this.errors = errors == null ? new ArrayList<>() : errors;
    }

    public int getImportedCount() {
        return importedCount;
    }

    public void setImportedCount(int importedCount) {
        this.importedCount = importedCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
