package com.devnoir.dsclient.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devnoir.dsclient.dto.ClientDTO;
import com.devnoir.dsclient.entities.Client;
import com.devnoir.dsclient.repositories.ClientRepository;
import com.devnoir.dsclient.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public List<ClientDTO> findAll() {
		List<Client> list = repository.findAll();
		return list.stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> opt = repository.findById(id);
		Client client = opt.orElseThrow(() -> new ResourceNotFoundException("Client not found"));
		return new ClientDTO(client);
	}
}
