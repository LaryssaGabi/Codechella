package br.com.code.codechella.Service;

import br.com.code.codechella.Tipagem.TipoEvento;
import br.com.code.codechella.Repository.EventoRepository;
import br.com.code.codechella.Dto.EventoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class EventoService {

    @Autowired
    private EventoRepository repository;

    public Flux<EventoDto> obterTodos() {
        return repository.findAll()
                .map(EventoDto::toDto);
    }

    public Mono<EventoDto> obterPorId(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(EventoDto::toDto);
    }

    public Flux<EventoDto> obterPorTipo(String tipo) {
        TipoEvento tipoEvento = TipoEvento.valueOf(tipo.toUpperCase());
        return repository.findByTipo(tipoEvento)
                .map(EventoDto::toDto);
    }

    public Mono<EventoDto> cadastrar(EventoDto dto) {
        return repository.save(dto.toEntity())
                .map(EventoDto::toDto);
    }

    public Mono<Void> excluir(Long id) {
        return repository.findById(id)
                .flatMap(repository::delete);
    }

    public Mono<EventoDto> alterar(Long id, EventoDto dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Id do evento não encontrado.")))
                .flatMap(eventoExistente -> {
                    eventoExistente.setTipo(dto.tipo());
                    eventoExistente.setNome(dto.nome());
                    eventoExistente.setData(dto.data());
                    eventoExistente.setDescricao(dto.descricao());
                    return repository.save(eventoExistente);
                })
                .map(EventoDto::toDto);
    }
}
