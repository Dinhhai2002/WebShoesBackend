package com.shoes.webshoes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.UserDao;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.model.StoreProcedureListResult;
import com.shoes.webshoes.service.UserService;

@Service("UsersService")
@Transactional(rollbackFor = Error.class)
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao usersDao;

	@Override
	public Users findOne(int id) throws Exception {
		return usersDao.findOne(id);
	}

	@Override
	public StoreProcedureListResult<Users> spGUsers(String keyword, int status, int role, Pagination pagination)
			throws Exception {
		return usersDao.spGUsers(keyword, status, role, pagination);
	}

	@Override
	public int deleteUsers(int id) throws Exception {
		return usersDao.deleteUsers(id);
	}

	@Override
	public Users findUsersByPhone(String phone) throws Exception {
		return usersDao.findUsersByPhone(phone);
	}

	@Override
	public Users findUsersByUsersName(String UsersName) throws Exception {
		return usersDao.findUsersByUsersName(UsersName);
	}

	@Override
	public void update(Users user) throws Exception {
		usersDao.update(user);

	}

	@Override
	public Users spUCreateUsers(String userName, String fullName, String email, String phone, String password,
			int gender, String birthday, int wardId, int districtId, int cityId, String fullAddress) throws Exception {

		return usersDao.spUCreateUsers(userName, fullName, email, phone, password, gender, birthday, wardId, districtId,
				cityId, fullAddress);
	}

	@Override
	public Users findUsersByEmail(String email, int isGoogle) throws Exception {
		return usersDao.findUsersByEmail(email, isGoogle);
	}

	@Override
	public void findUsersByIdAndUpdateIsActive(int id, int isActive) {
		usersDao.findUsersByIdAndUpdateIsActive(id, isActive);

	}

	@Override
	public Users findUsersByUsersNameAndEmail(String UsersName, String email) throws Exception {
		return usersDao.findUsersByUsersNameAndEmail(UsersName, email);
	}

	@Override
	public List<Users> getAll() throws Exception {
		return usersDao.getAll();
	}

	@Override
	public void create(Users user) throws Exception {
		usersDao.create(user);
	}

	@Override
	public Users findUsersByUsersNameAndPassword(String usersName, String password) throws Exception {
		return usersDao.findUsersByUsersNameAndPassword(usersName, password);
	}

}
