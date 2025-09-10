package com.breno.DesafioJunior.Services;

import com.breno.DesafioJunior.Dtos.LoanDTO;
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

    public ResponseEntity<List<LoanDTO>> ListAllLoans(){
        List<LoanDTO> AllLoans = loanRepository.findAll().stream().map(this::toDTO).toList();
        if(AllLoans.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(AllLoans);
    }

    public ResponseEntity<List<LoanDTO>> FindLoanByUserId(Long id){
        List<LoanDTO> AllUserLoans = loanRepository.FindAllLoansByUserId(id).stream().map(this::toDTO).toList();
        if(AllUserLoans.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(AllUserLoans);
    }

    public ResponseEntity<LoanDTO> RegisterNewLoan(LoanDTO loanDTO){
        BookModel book = bookReposiroty.findById(loanDTO.bookid()).orElse(null);
        UserModel user = userRepository.findById(loanDTO.userid()).orElse(null);

        if(book == null || user == null){
            return ResponseEntity.notFound().build();
        }

        if(book.getAvailable_quantity() < 1 || loanRepository.countByUserId(user.getUser_id()) >= 3){
            System.out.println("Empréstimo não pode ser realizado.");
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

        loanRepository.save(RegisteredLoan);
        return ResponseEntity.ok(toDTO(RegisteredLoan));
    }

    public ResponseEntity<LoanDTO> RegisterLoanReturn(Long id){
        LoanModel ExistingLoan = loanRepository.findById(id).orElse(null);
            if(ExistingLoan != null){
                BookModel book = ExistingLoan.getBook();
                book.setAvailable_quantity(book.getAvailable_quantity() + 1);
                bookReposiroty.save(book);

                ExistingLoan.setActual_return_date(LocalDate.now());
                ExistingLoan.setStatus(LoanENUM.DEVOLVIDO);

                loanRepository.save(ExistingLoan);
                return ResponseEntity.ok(toDTO(ExistingLoan));
            }
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
