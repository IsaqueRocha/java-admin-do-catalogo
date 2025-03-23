package com.isaque.admin.catalogo.application.genre.create;

import java.util.List;

public record CreateGenreCommand(
        String name,
        boolean isActive,
        List<String> categories
) {
    public static CreateGenreCommand with(
            final String name,
            final Boolean isActive,
            final List<String> categories
    ) {
        // Create a new CreateGenreCommand, defaulting isActive to true if null
        return new CreateGenreCommand(
                name,
                isActive != null ? isActive : Boolean.TRUE,
                categories
        );
    }
}
