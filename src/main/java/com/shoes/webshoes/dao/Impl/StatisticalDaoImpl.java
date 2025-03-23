package com.shoes.webshoes.dao.Impl;

import java.util.List;

import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.shoes.webshoes.common.enums.StoreProcedureStatusCodeEnum;
import com.shoes.webshoes.common.exception.TechresHttpException;
import com.shoes.webshoes.dao.AbstractDao;
import com.shoes.webshoes.dao.StatisticalDao;
import com.shoes.webshoes.entity.Statistical;

@Repository("StatisticalDao")
@Transactional
public class StatisticalDaoImpl extends AbstractDao<Integer, Statistical> implements StatisticalDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> statisticalAmount(int numberWeek, String fromDate, String toDate, int type) throws Exception {
		StoredProcedureQuery query = this.getSession()
				.createStoredProcedureQuery("sp_g_amount_statistical_order", Statistical.class)

				.registerStoredProcedureParameter("numberWeek", Integer.class, ParameterMode.IN)
				.registerStoredProcedureParameter("fromDateString", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("toDateString", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("_type", Integer.class, ParameterMode.IN)

				.registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
				.registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

		query.setParameter("numberWeek", numberWeek);
		query.setParameter("fromDateString", fromDate);
		query.setParameter("toDateString", toDate);
		query.setParameter("_type", type);

		int statusCode = (int) query.getOutputParameterValue("status_code");
		String messageError = query.getOutputParameterValue("message_error").toString();

		switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
		case SUCCESS:
			return query.getResultList();
		case INPUT_INVALID:
			throw new TechresHttpException(HttpStatus.BAD_REQUEST, messageError);
		default:
			throw new Exception(messageError);
		}
	}


}
