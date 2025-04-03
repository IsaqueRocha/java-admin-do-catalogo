package com.isaque.admin.catalogo.application.genre.delete;

import com.isaque.admin.catalogo.application.UseCaseTest;
import com.isaque.admin.catalogo.domain.genre.Genre;
import com.isaque.admin.catalogo.domain.genre.GenreGateway;
import com.isaque.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteGenreUseCaseTest extends UseCaseTest {
  @InjectMocks
  private DefaultDeleteGenreUseCase useCase;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(genreGateway);
  }

  @Test
  void givenAValidCommand_whenCallsDeleteGenre_thenShouldDeleteGenre() {
    // given
    final var genre = Genre.newGenre("Ação", true);
    final var expectedId = genre.getId();

    Mockito.doNothing().when(genreGateway).deleteById(Mockito.any());

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    // then
    Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
  }

  @Test
  void givenAnInvalidGenreId_whenCallsDeleteGenre_thenShouldBeOk() {
    // given
    final var expectedId = GenreID.from("123");

    Mockito.doNothing().when(genreGateway).deleteById(Mockito.any());

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    // then
    Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
  }

  @Test
  void givenAValidGenreId_whenCallsDeleteGenreAndGatewayThowsUnexpectedError_thenShouldReceiveException() {
    // given
    final var genre = Genre.newGenre("Ação", true);
    final var expectedId = genre.getId();

    Mockito.doThrow(new IllegalStateException("Gateway error")).when(genreGateway).deleteById(Mockito.any());

    // when
    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
    
    // then
    Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
  }
}
