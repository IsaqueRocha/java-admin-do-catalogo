package com.isaque.admin.catalogo.application;

public abstract class UnitUseCase<IN> {
    public abstract void execute(final IN input);
}
