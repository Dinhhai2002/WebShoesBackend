package com.shoes.webshoes.dao;

import com.shoes.webshoes.entity.UserRegister;

public interface UserRegisterDao {
	void create(UserRegister userRegister) throws Exception;

	UserRegister findOne(int id) throws Exception;

	void update(UserRegister userRegister) throws Exception;

	void delete(UserRegister userRegister);

	UserRegister findUsersRegisterByUsersNameAndEmail(String UsersName, String email) throws Exception;
}
