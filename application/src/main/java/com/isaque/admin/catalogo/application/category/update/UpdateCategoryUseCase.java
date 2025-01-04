package com.isaque.admin.catalogo.application.category.update;

import com.isaque.admin.catalogo.application.UseCase;
import com.isaque.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
