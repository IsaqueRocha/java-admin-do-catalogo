package com.isaque.admin.catalogo.application.genre.update;

import com.isaque.admin.catalogo.domain.Identifier;
import com.isaque.admin.catalogo.domain.category.Category;
import com.isaque.admin.catalogo.domain.category.CategoryGateway;
import com.isaque.admin.catalogo.domain.category.CategoryID;
import com.isaque.admin.catalogo.domain.exceptions.DomainException;
import com.isaque.admin.catalogo.domain.exceptions.NotFoundException;
import com.isaque.admin.catalogo.domain.exceptions.NotificationException;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.domain.genre.GenreID;
import com.isaque.admin.catalogo.domain.validation.Error;
import com.isaque.admin.catalogo.domain.validation.ValidationHandler;
import com.isaque.admin.catalogo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {
  private final CategoryGateway categoryGateway;
  private final GenreGateway genreGateway;

  public DefaultUpdateGenreUseCase(
      final CategoryGateway categoryGateway,
      final GenreGateway genreGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
    this.genreGateway = Objects.requireNonNull(genreGateway);
  }

  @Override
  public UpdateGenreOutput execute(final UpdateGenreCommand command) {
    final var id = GenreID.from(command.id());
    final var name = command.name();
    final var isActive = command.isActive();
    final var categories = toCategoryID(command.categories());

    final var genre = this.genreGateway.findById(id).orElseThrow(notFound(id));

    final var notification = Notification.create();
    notification.append(validateCategories(categories));
    notification.validate(() -> genre.update(name, isActive, categories));

    if (notification.hasError()) {
      throw new NotificationException("Could not update aggregate genre %s".formatted(command.id()), notification);
    }

    return UpdateGenreOutput.from(genreGateway.update(genre));
  }

  private ValidationHandler validateCategories(final List<CategoryID> ids) {
    final var notification = Notification.create();

    if (ids == null || ids.isEmpty()) {
      return notification;
    }

    final var retrievedIds = categoryGateway.existsByIds(ids);

    if (ids.size() != retrievedIds.size()) {
      final var missingIds = new ArrayList<>(ids);
      missingIds.removeAll(retrievedIds);

      final var missingIdsMessage = missingIds.stream()
          .map(CategoryID::getValue)
          .collect(Collectors.joining(", "));

      notification.append(
          new Error("Some categories could not be found: %s".formatted(missingIdsMessage))
      );
    }

    return notification;
  }

  private List<CategoryID> toCategoryID(final List<String> categories) {
    return categories.stream().map(CategoryID::from).toList();
  }

  private static Supplier<DomainException> notFound(Identifier id) {
    return () -> NotFoundException.with(Category.class, id);
  }
}
