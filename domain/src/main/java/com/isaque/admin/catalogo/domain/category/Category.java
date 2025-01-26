package com.isaque.admin.catalogo.domain.category;

import com.isaque.admin.catalogo.domain.AggregateRoot;
import com.isaque.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID id,
            final String name,
            final String description,
            final boolean isActive,
            final Instant creationDate,
            final Instant updateDate,
            final Instant deleteDate
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(creationDate, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(updateDate, "'updateAt' should not be null");
        this.deletedAt = deleteDate;
    }

    public static Category newCategory(
            final String name,
            final String description,
            final boolean isActive
    ) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(
                id,
                name,
                description,
                isActive,
                now,
                now,
                deletedAt
        );
    }

    public static Category with(final Category category) {
        return category.clone();
    }

    public static Category with(
        final CategoryID id,
        final String name,
        final String description,
        final boolean active,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt
        ){
        return new Category(
                id,name,description,active,createdAt,updatedAt,deletedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }

        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(
            final String name,
            final String description,
            final boolean isActive
    ) {
        if (isActive) {
            this.activate();
        } else {
            deactivate();
        }
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}