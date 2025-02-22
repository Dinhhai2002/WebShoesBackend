package com.shoes.webshoes.dao.Impl;

import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.shoes.webshoes.common.enums.StoreProcedureStatusCodeEnum;
import com.shoes.webshoes.common.exception.TechresHttpException;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.UserDao;
import com.shoes.webshoes.entity.Users;
import com.shoes.webshoes.model.StoreProcedureListResult;

@Repository("UsersDao")
@SuppressWarnings("unchecked")
@Transactional
public class UserDaoImpl extends AbstractDao<Integer, Users> implements UserDao {

	@Override
	public Users spUCreateUsers(String userName, String fullName, String email, String phone, String password,
			int gender, String birthday, int wardId, int districtId, int cityId, String fullAddress) throws Exception {
		StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_u_create_users", Users.class)
				.registerStoredProcedureParameter("userName", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("fullName", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_email", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_phone", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_password", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_gender", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_birthday", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("wardId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("districtId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("cityId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("fullAddress", String.class, ParameterMode.IN)

				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("userName", userName);
		query.setParameter("fullName", fullName);
		query.setParameter("_email", email);
		query.setParameter("_phone", phone);
		query.setParameter("_password", password);
		query.setParameter("_gender", gender);
		query.setParameter("_birthday", birthday);
		query.setParameter("wardId", wardId);
		query.setParameter("districtId", districtId);
		query.setParameter("cityId", cityId);
		query.setParameter("fullAddress", fullAddress);

		int statusCode = (int) query.getOutputParameterValue("status_code");
		String messageError = query.getOutputParameterValue("message_error").toString();

		switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
		case SUCCESS:
			return (Users) query.getResultList().stream().findFirst().orElse(null);
		case INPUT_INVALID:
			throw new TechresHttpException(HttpStatus.BAD_REQUEST, messageError);
		default:
			throw new Exception(messageError);
		}
	}

	@Override
	public Users spUUpdateUser(int id, String firstName, String lastName, String fullName, int gender, String email,
			int wardId, int cityId, int districtId, String fbUid, String ggUid, String appleUid, String phone)
			throws Exception {
		StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_u_update_user", Users.class)
				.registerStoredProcedureParameter("userName", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("fullName", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_email", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_phone", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_gender", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_birthday", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("wardId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("districtId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("cityId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("fullAddress", String.class, ParameterMode.IN)

				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("_id", id);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		query.setParameter("fullName", fullName);
		query.setParameter("_gender", gender);
		query.setParameter("_email", email);
		query.setParameter("wardId", wardId);
		query.setParameter("cityId", cityId);
		query.setParameter("districtId", districtId);
		query.setParameter("fbUid", fbUid);
		query.setParameter("ggUid", ggUid);
		query.setParameter("appleUid", appleUid);
		query.setParameter("_phone", phone);

		int statusCode = (int) query.getOutputParameterValue("status_code");
		String messageError = query.getOutputParameterValue("message_error").toString();

		switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
		case SUCCESS:
			return (Users) query.getResultList().stream().findFirst().orElse(null);
		case INPUT_INVALID:
			throw new TechresHttpException(HttpStatus.BAD_REQUEST, messageError);
		default:
			throw new Exception(messageError);
		}
	}

	@Override
	public Users findOne(int id) throws Exception {
		return this.getSession().find(Users.class, id);
	}

	@Override
	public StoreProcedureListResult<Users> spGUsers(String keyword, int status, int role, Pagination pagination)
			throws Exception {
		StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_g_users", Users.class)

				.registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("role", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)

				.registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("keySearch", keyword);
		query.setParameter("status", status);
		query.setParameter("role", role);
		query.setParameter("_limit", pagination.getLimit());
		query.setParameter("_offset", pagination.getOffset());

		int statusCode = (int) query.getOutputParameterValue("status_code");
		String messageError = query.getOutputParameterValue("message_error").toString();

		switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
		case SUCCESS:
			int totalRecord = (int) query.getOutputParameterValue("total_record");
			return new StoreProcedureListResult<>(statusCode, messageError, totalRecord, query.getResultList());
		case INPUT_INVALID:
			throw new TechresHttpException(HttpStatus.BAD_REQUEST, messageError);
		default:
			throw new Exception(messageError);
		}
	}

	@Override
	public int deleteUsers(int id) throws Exception {
		StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_u_delete_Users", Users.class)
				.registerStoredProcedureParameter("_id", Integer.class, ParameterMode.IN)

				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("_id", id);

		int statusCode = (int) query.getOutputParameterValue("status_code");
		String messageError = query.getOutputParameterValue("message_error").toString();

		switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
		case SUCCESS:
			return query.executeUpdate();
		case INPUT_INVALID:
			throw new TechresHttpException(HttpStatus.BAD_REQUEST, messageError);
		default:
			throw new Exception(messageError);
		}

	}

	@Override
	public Users findUsersByPhone(String phone) throws Exception {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Users> query = builder.createQuery(Users.class);
		Root<Users> root = query.from(Users.class);
		query.where(builder.equal(root.get("phone"), phone));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public Users findUsersByUsersName(String usersName) throws Exception {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Users> query = builder.createQuery(Users.class);
		Root<Users> root = query.from(Users.class);
		query.where(builder.equal(root.get("userName"), usersName));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public void update(Users user) throws Exception {

		this.getSession().update(user);
	}

	@Override
	public Users findUsersByEmail(String email, int isGoogle) throws Exception {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Users> query = builder.createQuery(Users.class);
		Root<Users> root = query.from(Users.class);
		Predicate condition1 = builder.equal(root.get("email"), email);
		Predicate condition2 = builder.equal(root.get("isGoogle"), isGoogle);
		Predicate combinedCondition = builder.and(condition1, condition2);

		query.where(combinedCondition);

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public void findUsersByIdAndUpdateIsActive(int id, int isActive) {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaUpdate<Users> update = builder.createCriteriaUpdate(Users.class);
		Root<Users> root = update.from(Users.class);

		update.set("isActive", isActive).where(builder.equal(root.get("id"), id));
		this.getSession().createQuery(update).executeUpdate();

	}

	@Override
	public Users findUsersByUsersNameAndEmail(String UsersName, String email) throws Exception {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Users> query = builder.createQuery(Users.class);
		Root<Users> root = query.from(Users.class);
		query.select(root).where(
				builder.and(builder.equal(root.get("userName"), UsersName), builder.equal(root.get("email"), email)));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}

	@Override
	public List<Users> getAll() throws Exception {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Users> query = builder.createQuery(Users.class);
		Root<Users> root = query.from(Users.class);

		return this.getSession().createQuery(query).getResultList();
	}

	@Override
	public void create(Users user) throws Exception {
		this.getSession().save(user);
	}

	@Override
	public Users findUsersByUsersNameAndPassword(String usersName, String password) throws Exception {
		CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Users> query = builder.createQuery(Users.class);
		Root<Users> root = query.from(Users.class);
		Predicate condition1 = builder.equal(root.get("userName"), usersName);
		Predicate condition2 = builder.equal(root.get("password"), password);
		Predicate combinedCondition = builder.and(condition1, condition2);

		query.where(combinedCondition);

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
	}

}
