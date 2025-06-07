package br.com.code.codechella.Repository;

import br.com.code.codechella.Class.Vendas;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface VendaRepository extends ReactiveCrudRepository<Vendas, Long> {
}
