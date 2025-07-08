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
import com.shoes.webshoes.dao.VoucherApplicationDao;
import com.shoes.webshoes.entity.VoucherApplication;
import com.shoes.webshoes.model.StoreProcedureListResult;

@Repository("VoucherApplicationDao")
@Transactional
public class VoucherApplicationDaoImpl extends AbstractDao<Integer, VoucherApplication> implements VoucherApplicationDao {
    @Override
    public void create(VoucherApplication voucherApplication) {
       this.getSession().save(voucherApplication);
    }

    @Override
    public VoucherApplication findOne(int id) {
       return this.getSession().find(VoucherApplication.class,id);
    }

    @Override
    public void update(VoucherApplication voucherApplication) {
       this.getSession().update(voucherApplication);	
    }

    @Override
    public List<VoucherApplication> getAll() {
       CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<VoucherApplication> query = builder.createQuery(VoucherApplication.class);
		Root<VoucherApplication> root = query.from(VoucherApplication.class);

		return this.getSession().createQuery(query).getResultList();
    }

    @Override
    public VoucherApplication findByName(String name) {
        CriteriaBuilder builder = this.getBuilder();
		CriteriaQuery<VoucherApplication> query = builder.createQuery(VoucherApplication.class);
		Root<VoucherApplication> root = query.from(VoucherApplication.class);
		query.where(builder.equal(root.get("name"), name));

		return this.getSession().createQuery(query).getResultList().stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
	@Override
	public StoreProcedureListResult<VoucherApplication> spGListVoucherApplication(int voucherId, int productId, int brandId, int categoryId, String keySearch,int status,Pagination pagination)
			throws Exception {
		StoredProcedureQuery query = this.getSession().createStoredProcedureQuery("sp_g_list_voucher_application", VoucherApplication.class)
				.registerStoredProcedureParameter("voucherId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("productId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("brandId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("categoryId", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("keySearch", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("status", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_limit", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)

				.registerStoredProcedureParameter("total_record", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("voucherId", voucherId);
		query.setParameter("productId", productId);
		query.setParameter("brandId", brandId);
		query.setParameter("categoryId", categoryId);
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
