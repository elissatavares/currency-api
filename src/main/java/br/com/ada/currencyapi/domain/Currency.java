package br.com.ada.currencyapi.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currency")
public class Currency implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    /**
     * Indica que este campo é uma coleção de elementos básicos ou embutidos.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    /**
     * Define uma tabela adicional chamada "exchanges" para armazenar a coleção `exchanges`
     * e conecta essa tabela à tabela principal usando a coluna `currency_id` que referencia o `id` da entidade `Currency`.
     */
    @CollectionTable(
            name = "exchanges",
            joinColumns = {@JoinColumn(name = "currency_id", referencedColumnName = "id")}
    )
    /**
     * Define que as chaves do mapa `exchanges` serão armazenadas na coluna "currency_name" na tabela "exchanges".
     */
    @MapKeyColumn(name = "currency_name")
    @Column(name = "exchange_rate")
    private Map<String, BigDecimal> exchanges;

}