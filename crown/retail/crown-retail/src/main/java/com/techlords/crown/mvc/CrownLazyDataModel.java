package com.techlords.crown.mvc;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@SuppressWarnings("serial")
public abstract class CrownLazyDataModel<T> extends LazyDataModel<T> {

	protected boolean isOnlyPageTraversal(Map<String, Object> filters) {
		if (filters.isEmpty()) {
			return true;
		}
		final Object globalFilter = filters.get("globalFilter");
		final boolean globalFilterEmpty = (globalFilter == null)
				|| (globalFilter.toString().isEmpty());
		if (filters.size() == 1 && globalFilterEmpty) {
			return true;
		}
		return false;
	}

	/** (non-Javadoc)
	 * @see org.primefaces.model.LazyDataModel#load(int, int, java.util.List, java.util.Map)
	 */
	@Override
	public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta,
			Map<String, Object> filters) {
		// TODO Auto-generated method stub
		return super.load(first, pageSize, multiSortMeta, filters);
	}
}
