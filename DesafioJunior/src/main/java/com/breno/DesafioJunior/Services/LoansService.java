package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.BookDTO;
import com.breno.DesafioJunior.Dtos.LoanDTO;
import com.breno.DesafioJunior.Enums.BookENUM;
import com.breno.DesafioJunior.Enums.LoanENUM;
import com.breno.DesafioJunior.Models.BookModel;
import com.breno.DesafioJunior.Models.LoanModel;
import com.breno.DesafioJunior.Models.UserModel;
import com.breno.DesafioJunior.Repositories.BookRepository;
import com.breno.DesafioJunior.Repositories.LoanRepository;
import com.breno.DesafioJunior.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;

@Service
public class LoansService {

    private static final Logger logger = LoggerFactory.getLogger(LoansService.class);

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookReposiroty;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<LoanDTO>> ListLoans(Long after, int size){
        logger.info("Buscando todos os empréstimos registrados.");

        Pageable pageable = PageRequest.of(0, size);
        List<LoanDTO> ListOfLoans;

        if(after == null){
            ListOfLoans = loanRepository.findByOrderByIdAsc(pageable).stream().map(this::toDTO).toList();
            if(ListOfLoans.isEmpty()){
                logger.info("Nenhum livro encontrado no sistema.");
                return ResponseEntity.notFound().build();
            } else {
                logger.info("Listando os 10 primeiros livros.");
            }
        } else {
            ListOfLoans = loanRepository.findByLoanIdGreaterThanOrderByIdAsc(after, pageable).stream().map(this::toDTO).toList();
            if(ListOfLoans.isEmpty()){
                logger.info("Nenhum livro encontrado no sistema.");
                return ResponseEntity.notFound().build();
            }else {
                logger.info("Listando os 10 livros após o ID: {}", after);
            }
        }

        logger.info("Encontrados {} livros no sistema.", ListOfLoans.size());
        return ResponseEntity.ok(ListOfLoans);
    }

    public ResponseEntity<List<LoanDTO>> FindLoanByUserId(Long id){
        logger.info("Buscando empréstimos para o usuário com ID: {}", id);
        List<LoanDTO> AllUserLoans = loanRepository.FindAllLoansByUserId(id).stream().map(this::toDTO).toList();
        if(AllUserLoans.isEmpty()){
            logger.info("Nenhum empréstimo encontrado para o usuário com ID: " + id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Empréstimos encontrados para o usuário com ID: {} ", id);
        return ResponseEntity.ok(AllUserLoans);
    }

    public ResponseEntity<LoanDTO> RegisterNewLoan(LoanDTO loanDTO){
        logger.info("Registrando novo empréstimo para o usuário de ID: {} e livro de ID: {}", loanDTO.userid(), loanDTO.bookid());
        BookModel book = bookReposiroty.findById(loanDTO.bookid()).orElse(null);
        UserModel user = userRepository.findById(loanDTO.userid()).orElse(null);

        if(book == null || user == null){
            logger.warn("Falha ao registrar empréstimo: Livro ou usuário não encontrado.");
            return ResponseEntity.notFound().build();
        }

        if(book.getAvailable_quantity() < 1 || book.getStatus().equals(BookENUM.INDISPONIVEL)) {
            logger.warn("Falha ao registrar empréstimo: Livro indisponível.");
            book.setStatus(BookENUM.INDISPONIVEL);
            return ResponseEntity.badRequest().build();
        } else if (loanRepository.countByUserId(user.getUser_id()) >= 3) {
            logger.warn("Falha ao registrar empréstimo: Usuário atingiu o limite de empréstimos.");
            return ResponseEntity.badRequest().build();
        }

        book.setAvailable_quantity(book.getAvailable_quantity() - 1);
        bookReposiroty.save(book);

        LoanModel RegisteredLoan = new LoanModel(
                user,
                book,
                LocalDate.now(),
                LocalDate.now().plusDays(14),
                null,
                LoanENUM.ATIVO
        );
        LoanModel savedLoan = loanRepository.save(RegisteredLoan);

        logger.info("Empréstimo registrado com sucesso para o usuário de ID: {} e livro de ID: {}", loanDTO.userid(), loanDTO.bookid());
        return ResponseEntity.ok(toDTO(savedLoan));
    }

    public ResponseEntity<LoanDTO> RegisterLoanReturn(Long id){
        logger.info("Registrando devolução do empréstimo com ID: {}", id);
        LoanModel ExistingLoan = loanRepository.findById(id).orElse(null);
            if(ExistingLoan != null) {
                BookModel book = ExistingLoan.getBook();
                book.setAvailable_quantity(book.getAvailable_quantity() + 1);
                book.setStatus(BookENUM.DISPONIVEL);
                bookReposiroty.save(book);

                ExistingLoan.setActual_return_date(LocalDate.now());
                ExistingLoan.setStatus(LoanENUM.DEVOLVIDO);

                loanRepository.save(ExistingLoan);
                logger.info("Devolução registrada com sucesso para o empréstimo com ID: {}", id);
                return ResponseEntity.ok(toDTO(ExistingLoan));
            }
        logger.warn("Empréstimo não encontrado");
        return ResponseEntity.notFound().build();
    }

    public LoanDTO toDTO(LoanModel model) {

        return new LoanDTO(
                model.getLoan_id(),
                model.getBook().getBook_id(),
                model.getUser().getUser_id(),
                model.getLoan_date(),
                model.getReturn_date(),
                model.getStatus()
        );
    }

}
