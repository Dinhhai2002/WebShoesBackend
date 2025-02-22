package com.shoes.webshoes.service;

import java.util.List;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.model.StoreProcedureListResult;

public interface UserService {

	Users spUCreateUsers(String userName, String fullName, String email, String phone, String password, int gender,
			String birthday, int wardId, int districtId, int cityId, String fullAddress) throws Exception;

	void create(Users user) throws Exception;

	Users findOne(int id) throws Exception;

	void update(Users user) throws Exception;

	StoreProcedureListResult<Users> spGUsers(String keyword, int status, int role, Pagination pagination)
			throws Exception;

	int deleteUsers(int id) throws Exception;

	void findUsersByIdAndUpdateIsActive(int id, int isActive);

	Users findUsersByPhone(String phone) throws Exception;

	Users findUsersByUsersName(String UsersName) throws Exception;

	Users findUsersByUsersNameAndPassword(String usersName, String password) throws Exception;

	Users findUsersByUsersNameAndEmail(String UsersName, String email) throws Exception;

	Users findUsersByEmail(String email, int isGoogle) throws Exception;

	List<Users> getAll() throws Exception;

}
