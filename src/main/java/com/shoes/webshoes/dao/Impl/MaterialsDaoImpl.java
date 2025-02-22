package com.shoes.webshoes.dao.Impl;

import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.shoes.webshoes.common.enums.StoreProcedureStatusCodeEnum;
import com.shoes.webshoes.common.exception.TechresHttpException;
import com.shoes.webshoes.common.utils.Pagination;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.MaterialsDao;
import com.shoes.webshoes.entity.Materials;
import com.shoes.webshoes.model.StoreProcedureListResult;

@Repository("MaterialsDao")
@Transactional
public class MaterialsDaoImpl extends AbstractDao<Integer, Materials> implements MaterialsDao {
    @Override
    public void create(Materials materials) {
       this.getSession().save(materials);
    }

    @Override
    public Materials findOne(int id) {
       return this.getSession().find(Materials.class,id);
    }

    @Override
    public void update(Materials materials) {
       this.getSession().update(materials);	
    }

    @Override
    public List<Materials> getAll() {
       CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Materials> query = builder.createQuery(Materials.class);
		Root<Materials> root = query.from(Materials.class);

		return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public Materials findByName(String name) {
        CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<Materials> query = builder.createQuery(Materials.class);
		Root<Materials> root = query.from(Materials.class);
		query.where(builder.equal(root.get("name"), name));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
	@Override
	public StoreProcedureListResult<Materials> spGListMaterials(String keySearch, int status, Pagination pagination)
			throws Exception {
		StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_g_list_materials", Materials.class)
				.registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)

				.registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("keySearch", keySearch);
		query.setParameter("status", status);
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
}
