package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.LoanDTO;
import com.breno.DesafioJunior.Enums.BookENUM;
import com.breno.DesafioJunior.Enums.LoanENUM;
import com.breno.DesafioJunior.Enums.UserENUM;
import com.breno.DesafioJunior.Exceptions.AvailabilityException;
import com.breno.DesafioJunior.Exceptions.ResourceNotFoundException;
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
                logger.info("Nenhum empréstimo encontrado no sistema.");
                throw new ResourceNotFoundException("Nenhum empréstimo encontrado no sistema");
            } else {
                logger.info("Listando os 10 primeiros empréstimos.");
            }
        } else {
            ListOfLoans = loanRepository.findByLoanIdGreaterThanOrderByIdAsc(after, pageable).stream().map(this::toDTO).toList();
            if(ListOfLoans.isEmpty()){
                logger.info("Nenhum livro encontrado no sistema.");
                throw new ResourceNotFoundException("Nenhum empréstimo encontrado no sistema apos o ID: " + after);
            }else {
                logger.info("Listando os 10 empréstimos após o ID: {}", after);
            }
        }

        logger.info("Encontrados {} empréstimos no sistema.", ListOfLoans.size());
        return ResponseEntity.ok(ListOfLoans);
    }

    public ResponseEntity<List<LoanDTO>> FindLoanByUserId(Long id){
        logger.info("Buscando empréstimos para o usuário com ID: {}", id);
        List<LoanDTO> AllUserLoans = loanRepository.FindAllLoansByUserId(id).stream().map(this::toDTO).toList();
        if(AllUserLoans.isEmpty()){
            logger.info("Nenhum empréstimo encontrado para o usuário com ID: {}", id);
            throw new ResourceNotFoundException("Nenhum empréstimo encontrado para o usuário com ID: " + id);
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
            throw new ResourceNotFoundException("Livro ou usuário não encontrado.");
        }
        if(user.getStatus() == UserENUM.INATIVO) {
            logger.warn("Falha ao registrar empréstimo: Usuário inativo.");
            throw new AvailabilityException("Usuário inativo.");
        }
        if(book.getAvailable_quantity() < 1 || book.getStatus() == BookENUM.INDISPONIVEL) {
            logger.warn("Falha ao registrar empréstimo: Livro indisponível.");
            book.setStatus(BookENUM.INDISPONIVEL);
            throw new AvailabilityException("Livro indisponível.");
        } else if (loanRepository.countByUserId(user.getUser_id()) >= 3) {
            logger.warn("Falha ao registrar empréstimo: Usuário atingiu o limite de empréstimos.");
            throw new AvailabilityException("Usuário atingiu o limite de empréstimos.");
        }

        book.setAvailable_quantity(book.getAvailable_quantity() - 1);
        if(book.getAvailable_quantity() == 0){
            book.setStatus(BookENUM.INDISPONIVEL);
        }
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
                if(ExistingLoan.getActual_return_date().isAfter(ExistingLoan.getReturn_date())){
                    ExistingLoan.setStatus(LoanENUM.ATRASADO);
                    logger.info("Empréstimo devolvido com atraso.");
                } else {
                    ExistingLoan.setStatus(LoanENUM.DEVOLVIDO);
                    logger.info("Empréstimo devolvido no prazo.");
                }

                loanRepository.save(ExistingLoan);
                logger.info("Devolução registrada com sucesso para o empréstimo com ID: {}", id);
                return ResponseEntity.ok(toDTO(ExistingLoan));
            }else {
                logger.warn("Empréstimo não encontrado");
                throw new ResourceNotFoundException("Empréstimo com ID: " + id + " não encontrado.");
            }
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
