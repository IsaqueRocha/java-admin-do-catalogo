package com.isaque.admin.catalogo.application.category.create;

import com.isaque.admin.catalogo.application.UseCase;
import com.isaque.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
