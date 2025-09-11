// no pacote de exceções
package com.breno.DesafioJunior.Exceptions;

public class DataIntegrityException extends RuntimeException {
    public DataIntegrityException(String message) {
        super(message);
    }
}