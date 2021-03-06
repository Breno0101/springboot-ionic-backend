package com.brenolins.cursomc.service.validations;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.brenolins.cursomc.domain.Cliente;
import com.brenolins.cursomc.domain.enums.TipoCliente;
import com.brenolins.cursomc.dto.ClienteNewDto;
import com.brenolins.cursomc.repository.ClienteRepository;
import com.brenolins.cursomc.resources.exceptions.FieldMessage;
import com.brenolins.cursomc.service.validations.utils.BR;

public class ClienteInsertValidation implements ConstraintValidator<ClienteInsert, ClienteNewDto> {
	
	@Autowired
	private ClienteRepository repo;
	
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDto objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();

		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCpf(objDto.getCpfOuCnpj())) {

			list.add(new FieldMessage("tipo", "CPF não é válido"));

		}

		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCnpj(objDto.getCpfOuCnpj())) {

			list.add(new FieldMessage("tipo", "CNPJ não é válido"));

		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if(aux!=null){
			list.add(new FieldMessage("email", "Email já cadastrado"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}