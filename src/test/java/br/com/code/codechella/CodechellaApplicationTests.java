package br.com.code.codechella;

import br.com.code.codechella.Dto.EventoDto;
import br.com.code.codechella.Tipagem.TipoEvento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodechellaApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void cadastraNovoEvento() {
        EventoDto dto = new EventoDto(null, TipoEvento.SHOW, "Justin Bieber", LocalDate.parse("2025-01-28"), "Show do masior pop que existe");

        webTestClient.post().uri("/eventos").bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EventoDto.class)
                .value(response -> {
                    assertNotNull(response.id());
                    assertEquals(dto.tipo(), response.tipo());
                    assertEquals(dto.nome(), response.nome());
                    assertEquals(dto.data(), response.data());
                    assertEquals(dto.descricao(), response.descricao());
                });
    }

    @Test
    void buscarEvento() {
        EventoDto dto = new EventoDto(14L, TipoEvento.WORKSHOP, "Café com Ideias", LocalDate.parse("2025-01-25"), "Um evento que ensina técnicas de brainstorming e priorização para alavancar projetos.");

        webTestClient.get().uri("/eventos")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(EventoDto.class)
                .value(response -> {
                    EventoDto eventoResponse = response.get(13);
                    assertEquals(dto.id(), eventoResponse.id());
                    assertEquals(dto.tipo(), eventoResponse.tipo());
                    assertEquals(dto.nome(), eventoResponse.nome());
                    assertEquals(dto.data(), eventoResponse.data());
                    assertEquals(dto.descricao(), eventoResponse.descricao());
                });
    }

    @Test
    void alterarEvento() {
        EventoDto dtoAtualizado = new EventoDto(14L, TipoEvento.CONCERTO, "Metallica", LocalDate.parse("2025-01-25"), "Concerto da banda Metallica");

        webTestClient.put().uri("/eventos/{id}", 14L).bodyValue(dtoAtualizado)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventoDto.class)
                .value(response -> {
                    assertEquals(dtoAtualizado.id(), response.id());
                    assertEquals(dtoAtualizado.tipo(), response.tipo());
                    assertEquals(dtoAtualizado.nome(), response.nome());
                    assertEquals(dtoAtualizado.data(), response.data());
                    assertEquals(dtoAtualizado.descricao(), response.descricao());
                });
    }

    @Test
    void excluiEvento() {
        webTestClient.delete().uri("/eventos/{id}", 10L)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri("/eventos/{id}", 10L)
                .exchange()
                .expectStatus().isNotFound();
    }
}
