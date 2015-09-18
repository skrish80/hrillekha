package com.techlords.crown.mvc.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.ItemBO;
import com.techlords.crown.mvc.CrownLazyDataModel;
import com.techlords.crown.mvc.util.FacesUtil;
import com.techlords.crown.service.ItemService;

@SuppressWarnings("serial")
public class LazyItemDataModel extends CrownLazyDataModel<ItemBO> {
	private final ItemService service = CrownServiceLocator.INSTANCE
			.getCrownService(ItemService.class);
	final List<ItemBO> returnValue = new ArrayList<ItemBO>();

	@Override
	public List<ItemBO> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {

		if (!FacesUtil.isRenderPhase()) {
			return returnValue;
		}
		boolean pageTraversal = isOnlyPageTraversal(filters);
		returnValue.clear();
		returnValue.addAll(pageTraversal ? service
				.findAllItems(first, pageSize) : service.findAllItems(first,
				pageSize, filters));
		setRowCount(service.getFilteredItemCount());

		return returnValue;
	}
}
