package br.com.code.codechella.Repository;

import br.com.code.codechella.Class.Evento;
import br.com.code.codechella.Tipagem.TipoEvento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface EventoRepository extends ReactiveCrudRepository<Evento, Long> {
    Flux<Evento> findByTipo(TipoEvento tipoEvento);
}
