package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.BookDTO;
import com.breno.DesafioJunior.Enums.BookENUM;
import com.breno.DesafioJunior.Models.BookModel;
import com.breno.DesafioJunior.Repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BooksService {

    private static final Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BookRepository bookRepository;

    public ResponseEntity<List<BookDTO>> ListAllBooks(){
        List<BookDTO> ListOfAllBooks = bookRepository.findAll().stream().map(this::toDTO).toList();
        if(ListOfAllBooks.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ListOfAllBooks);
    }


    public ResponseEntity<List<BookDTO>> FindBookById(Long id){
        List<BookDTO> UniqueBook = bookRepository.findById(id).stream().map(this::toDTO).toList();
        if(UniqueBook.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UniqueBook);
    }

    public ResponseEntity<BookDTO> RegisterNewBook(BookDTO bookDTO){
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
        //Adicionar Verificações de validação e tratamento de erros
        return ResponseEntity.ok(toDTO(RegisteredBook));
    }

    public ResponseEntity<BookDTO> UpdateBookInfo(Long id, BookDTO NewBookInfo){
        BookModel OldBookInfo = bookRepository.findById(id).orElse(null);

        //É possível diminuir evitar esses if's usando um Mapper
        if (OldBookInfo == null) {
            return ResponseEntity.notFound().build();
        }
        if(NewBookInfo.title() != null){
            OldBookInfo.setTitle(NewBookInfo.title());
        }
        if(NewBookInfo.author() != null){
            OldBookInfo.setAuthor(NewBookInfo.author());
        }
        if(NewBookInfo.isbn() != null){
            OldBookInfo.setIsbn(NewBookInfo.isbn());
        }
        if(NewBookInfo.published_date() != null){
            OldBookInfo.setPublished_date(NewBookInfo.published_date());
        }
        if(NewBookInfo.total_quantity() != null){
            OldBookInfo.setTotal_quantity(NewBookInfo.total_quantity());
        }
        if(NewBookInfo.available_quantity() != null){
            OldBookInfo.setAvailable_quantity(NewBookInfo.available_quantity());
        }
        if(NewBookInfo.status() != null){
            OldBookInfo.setStatus(BookENUM.valueOf(String.valueOf(NewBookInfo.status())));
        }

        bookRepository.save(OldBookInfo);
        return ResponseEntity.ok(toDTO(OldBookInfo));
    }

    public ResponseEntity<Void> DeleteBook(Long id){
        BookModel BookToDelete = bookRepository.findById(id).orElse(null);
        if(BookToDelete != null){
            bookRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
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
