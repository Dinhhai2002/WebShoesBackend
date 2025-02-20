package com.shoes.webshoes.common.utils;

import java.io.Serializable;


public class Pagination implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int offset;
	private final int limit;
	private final int page;

	public Pagination(int page, int limit) {
		if (limit <= 0)
			limit = 50;
		if (limit > 1000)
			limit = 1000;

		this.page = page;
		this.limit = limit;
	}

	/**
	 * The current page number this pagination object represents
	 *
	 * @return the page number
	 */
	public int getPageNumber() {
		if (offset < limit || limit == 0)
			return 1;

		return (offset / limit) + 1;
	}

	/**
	 * The offset for this pagination object. The offset determines what index (0
	 * index) to start retrieving results from.
	 *
	 * @return the offset
	 */
	public int getOffset() {
		if (page < 1)
			return 0;
		return (page - 1) * limit;
	}

	/**
	 * The limit for this pagination object. The limit determines the maximum amount
	 * of results to return.
	 *
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Creates a new pagination object representing the next page
	 *
	 * @return new pagination object with offset shifted by offset+limit
	 */
	public Pagination getNext() {
		return new Pagination(offset + limit, limit);
	}

	/**
	 * Creates a new pagination object representing the previous page
	 *
	 * @return new pagination object with offset shifted by offset-limit
	 */
	public Pagination getPrevious() {
		if (limit >= offset) {
			return new Pagination(0, limit);
		} else {
			return new Pagination(offset - limit, limit);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Pagination))
			return false;

		Pagination pagination = (Pagination) o;

		return (limit == pagination.limit) && (offset == pagination.offset);
	}

	@Override
	public int hashCode() {
		int result = offset;
		result = 31 * result + limit;
		return result;
	}
}
