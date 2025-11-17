package org.example.model.numerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumeratorRequestTestAndSet {
        private Double oldValue;
        private Double newValue;

        // Lombok genera automáticamente:
        // - Constructor con todos los campos (si usas @AllArgsConstructor)
        // - Getters y Setters

}
