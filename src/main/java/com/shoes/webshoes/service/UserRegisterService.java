package com.shoes.webshoes.service;

import com.shoes.webshoes.entity.UserRegister;

public interface UserRegisterService {
	void create(UserRegister userRegister) throws Exception;

	UserRegister findOne(int id) throws Exception;

	void update(UserRegister userRegister) throws Exception;
	
	void delete(UserRegister userRegister);

	UserRegister findUsersRegisterByUsersNameAndEmail(String UsersName, String email) throws Exception;
}
