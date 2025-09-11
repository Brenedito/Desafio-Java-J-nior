package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.BookDTO;
import com.breno.DesafioJunior.Enums.BookENUM;
import com.breno.DesafioJunior.Models.BookModel;
import com.breno.DesafioJunior.Repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Service
public class BooksService {

    private static final Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BookRepository bookRepository;

    public ResponseEntity<List<BookDTO>> ListBooks(Long after, int size){
        logger.info("Buscando os livros registrados.");

        Pageable pageable = PageRequest.of(0, size);
        List<BookDTO> ListOfBooks;

        if(after == null){
            ListOfBooks = bookRepository.findByOrderByIdAsc(pageable).stream().map(this::toDTO).toList();
            if(ListOfBooks.isEmpty()){
                logger.info("Nenhum livro encontrado no sistema.");
                return ResponseEntity.notFound().build();
            } else {
                logger.info("Listando os 10 primeiros livros.");
            }
        } else {
            ListOfBooks = bookRepository.findByBookIdGreaterThanOrderByIdAsc(after, pageable).stream().map(this::toDTO).toList();
            if(ListOfBooks.isEmpty()){
                logger.info("Nenhum livro encontrado no sistema.");
                return ResponseEntity.notFound().build();
            }else {
                logger.info("Listando os 10 livros após o ID: {}", after);
            }
        }


        logger.info("Encontrados {} livros no sistema.", ListOfBooks.size());
        return ResponseEntity.ok(ListOfBooks);
    }


    public ResponseEntity<List<BookDTO>> FindBookById(Long id){
        logger.info("Buscando livro com ID: {}", id);
        List<BookDTO> UniqueBook = bookRepository.findById(id).stream().map(this::toDTO).toList();
        if(UniqueBook.isEmpty()){
            logger.warn("Livro com ID: {} não encontrado no sistema.", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Livro com ID: {} encontrado.", id);
        return ResponseEntity.ok(UniqueBook);
    }

    public ResponseEntity<BookDTO> RegisterNewBook(BookDTO bookDTO){
        logger.info("Registrando novo livro com os seguintes dados, título: {} autor: {} isbn: {}", bookDTO.title(), bookDTO.author(), bookDTO.isbn());
        BookModel RegisteredBook = new BookModel(
                bookDTO.title(),
                bookDTO.author(),
                bookDTO.isbn(),
                bookDTO.published_date(),
                bookDTO.total_quantity(),
                bookDTO.available_quantity(),
                BookENUM.valueOf(String.valueOf(bookDTO.status()))
        );

        bookRepository.save(RegisteredBook);
        logger.info("Livro {} registrado com sucesso.", RegisteredBook);
        return ResponseEntity.ok(toDTO(RegisteredBook));
    }

    public ResponseEntity<BookDTO> UpdateBookInfo(Long id, BookDTO NewBookInfo){
        logger.info("Atualizando informações do livro com ID: {}", id);
        BookModel OldBookInfo = bookRepository.findById(id).orElse(null);

        //É possível diminuir evitar esses if's usando um Mapper
        if (OldBookInfo == null) {
            logger.info("Livro com ID: {} não encontrado.", id);
            return ResponseEntity.notFound().build();
        }
        if(NewBookInfo.title() != null){
            logger.info("Atualizando título do livro com ID: {} para {}", id, NewBookInfo.title());
            OldBookInfo.setTitle(NewBookInfo.title());
        }
        if(NewBookInfo.author() != null){
            logger.info("Atualizando autor do livro com ID: {} para {}", id, NewBookInfo.author());
            OldBookInfo.setAuthor(NewBookInfo.author());
        }
        if(NewBookInfo.isbn() != null){
            logger.info("Atualizando ISBN do livro com ID: {} para {}", id, NewBookInfo.isbn());
            OldBookInfo.setIsbn(NewBookInfo.isbn());
        }
        if(NewBookInfo.published_date() != null){
            logger.info("Atualizando data de publicação do livro com ID: {} para {}", id, NewBookInfo.published_date());
            OldBookInfo.setPublished_date(NewBookInfo.published_date());
        }
        if(NewBookInfo.total_quantity() != null){
            logger.info("Atualizando quantidade total do livro com ID: {} para {}", id, NewBookInfo.total_quantity());
            OldBookInfo.setTotal_quantity(NewBookInfo.total_quantity());
        }
        if(NewBookInfo.available_quantity() != null){
            logger.info("Atualizando quantidade disponível do livro com ID: {} para {}", id, NewBookInfo.available_quantity());
            OldBookInfo.setAvailable_quantity(NewBookInfo.available_quantity());
        }
        if(NewBookInfo.status() != null){
            logger.info("Atualizando status do livro com ID: {} para {}", id, NewBookInfo.status());
            OldBookInfo.setStatus(BookENUM.valueOf(String.valueOf(NewBookInfo.status())));
        }

        bookRepository.save(OldBookInfo);
        logger.info("Informações do livro com ID: {} atualizadas com sucesso.", id);
        return ResponseEntity.ok(toDTO(OldBookInfo));
    }

    public ResponseEntity<Void> DeleteBook(Long id){
        logger.info("Iniciando deleção do livro com ID: {}", id);
        BookModel BookToDelete = bookRepository.findById(id).orElse(null);
        if(BookToDelete != null){
            logger.info("Deletando livro com ID: {} do sistema.", id);
            bookRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            logger.warn("Livro com ID: {} não encontrado.", id);
            return ResponseEntity.notFound().build();
        }
    }


    public BookDTO toDTO(BookModel model) {
        return new BookDTO(
                model.getBook_id(),
                model.getTitle(),
                model.getAuthor(),
                model.getIsbn(),
                model.getPublished_date(),
                model.getTotal_quantity(),
                model.getAvailable_quantity(),
                model.getStatus()
        );
    }

}
