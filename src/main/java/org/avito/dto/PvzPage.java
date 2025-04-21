package org.avito.dto;

import org.avito.model.Pvz;

import java.util.List;

public record PvzPage(List<Pvz> content, int pageNumber, int pageSize, long totalElements) {
    public int totalPages() {
        return (int) Math.ceil((double) totalElements / pageSize);
    }
}