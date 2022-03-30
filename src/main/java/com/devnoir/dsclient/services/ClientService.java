package com.devnoir.dsclient.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
		Client client = opt.orElseThrow(() -> new ResourceNotFoundException("Client id not found: " + id + ". Enter a valid id"));
		return new ClientDTO(client);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client client = new Client();
		copyDtoToEntity(dto, client);
		repository.save(client);
		return new ClientDTO(client);
	}
	
	@Transactional
	public ClientDTO update(ClientDTO dto, Long id) {
		try {
			Client client = repository.getById(id);
			copyDtoToEntity(dto, client);
			repository.save(client);
			return new ClientDTO(client);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Client id not found: " + id + ". Enter a valid id");
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Client id not found: " + id + ". Enter a valid id");
		} 
	}

	private void copyDtoToEntity(ClientDTO dto, Client client) {
		client.setName(dto.getName());
		client.setCpf(dto.getCpf());
		client.setIncome(dto.getIncome());
		client.setBirthDate(dto.getBirthDate());
		client.setChildren(dto.getChildren());
	}
}
