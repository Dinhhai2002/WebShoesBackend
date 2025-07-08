package com.shoes.webshoes.dao;

import java.util.List;

public interface StatisticalDao {
   List<Object> statisticalAmount(int numberWeek,String fromDate,String toDate,int type) throws Exception;
   
}
