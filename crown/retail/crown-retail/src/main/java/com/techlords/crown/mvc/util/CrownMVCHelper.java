/**
 * 
 */
package com.techlords.crown.mvc.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.AgentTypeBO;
import com.techlords.crown.business.model.CompanyTypeBO;
import com.techlords.crown.business.model.CrownEntityBO;
import com.techlords.crown.business.model.CurrencyBO;
import com.techlords.crown.business.model.CustomerTypeBO;
import com.techlords.crown.business.model.DepartmentBO;
import com.techlords.crown.business.model.DesignationBO;
import com.techlords.crown.business.model.LocationBO;
import com.techlords.crown.business.model.RoleBO;
import com.techlords.crown.business.model.StolenStockBO;
import com.techlords.crown.business.model.UomBO;
import com.techlords.crown.business.model.WarehouseStockBO;
import com.techlords.crown.mvc.flow.CrownFlowService;
import com.techlords.crown.service.AgentService;
import com.techlords.crown.service.CompanyService;
import com.techlords.crown.service.CrownUserService;
import com.techlords.crown.service.CustomerService;
import com.techlords.crown.service.DepartmentService;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.LocationService;

/**
 * @author gv
 * 
 */
public final class CrownMVCHelper {

	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#######.####");
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

	private static final List<AgentTypeBO> AGENT_TYPE_BOS = new ArrayList<AgentTypeBO>();
	private static final List<CompanyTypeBO> COMPANY_TYPE_BOS = new ArrayList<CompanyTypeBO>();
	private static final List<CustomerTypeBO> CUSTOMER_TYPE_BOS = new ArrayList<CustomerTypeBO>();
	private static final List<LocationBO> LOCATION_BOS = new ArrayList<LocationBO>();
	private static final List<CrownEntityBO> ENTITY_BOS = new ArrayList<CrownEntityBO>();
	private static final List<RoleBO> ROLE_BOS = new ArrayList<RoleBO>();
	private static final List<DesignationBO> DESIGNATION_BOS = new ArrayList<DesignationBO>();
	private static final List<DepartmentBO> DEPARTMENT_BOS = new ArrayList<DepartmentBO>();
	private static final List<CurrencyBO> CURRENCY_BOS = new ArrayList<CurrencyBO>();
	private static final List<UomBO> UOM_BOS = new ArrayList<UomBO>();

	public static boolean containsWarehouseItem(List<WarehouseStockBO> bos, WarehouseStockBO item) {
		for (final WarehouseStockBO bo : bos) {
			if (bo.getItemID().equals(item.getItemID())
					&& bo.getWarehouseID().equals(item.getWarehouseID())) {
				item.setItemQty(bo.getItemQty());
				item.setUomQty(bo.getUomQty());
				return true;
			}
		}
		return false;
	}

	public static boolean containsStolenStock(List<StolenStockBO> bos, StolenStockBO item) {
		for (final StolenStockBO bo : bos) {
			if (bo.getItem() == item.getItem() && bo.getWarehouse() == item.getWarehouse()) {
				if (bo.getStolenDate().equals(item.getStolenDate())) {
					return true;
				}
			}
		}
		return false;
	}

	public static void loadStatics() {

		getRoleBos();
		getAgentTypeBos();
		getCompanyTypeBos();
		getCustomerTypeBos();
		getDepartmentBos();
		getDesignationBos();
		getLocationBos();
		getEntityBos();
		getCurrencyBos();
		getUomBos();
	}

	public static boolean checkUniqueness(String fieldName, String fieldValue) {
		final GeneralService service = CrownServiceLocator.INSTANCE
				.getCrownService(GeneralService.class);
		final String tableName = CrownFlowService.INSTANCE.getTableName(fieldName);
		int index = fieldName.indexOf("@");
		if (index > -1) {
			fieldName = fieldName.substring(index + 1);
		}
		return service.isAvailable(tableName, fieldName, fieldValue);
	}

	public static final List<AgentTypeBO> getAgentTypeBos() {
		if (AGENT_TYPE_BOS.isEmpty()) {
			AgentService service = CrownServiceLocator.INSTANCE.getCrownService(AgentService.class);
			AGENT_TYPE_BOS.clear();
			AGENT_TYPE_BOS.addAll(service.findAllAgentTypes());
		}
		return AGENT_TYPE_BOS;
	}

	public static final List<CompanyTypeBO> getCompanyTypeBos() {
		if (COMPANY_TYPE_BOS.isEmpty()) {
			CompanyService service = CrownServiceLocator.INSTANCE
					.getCrownService(CompanyService.class);
			COMPANY_TYPE_BOS.clear();
			COMPANY_TYPE_BOS.addAll(service.searchCompanyTypes());
		}
		return COMPANY_TYPE_BOS;
	}

	public static final List<CustomerTypeBO> getCustomerTypeBos() {
		if (CUSTOMER_TYPE_BOS.isEmpty()) {
			CustomerService service = CrownServiceLocator.INSTANCE
					.getCrownService(CustomerService.class);
			CUSTOMER_TYPE_BOS.clear();
			CUSTOMER_TYPE_BOS.addAll(service.findAllCustomerTypes());
		}
		return CUSTOMER_TYPE_BOS;
	}

	public static final List<LocationBO> getLocationBos() {
		if (LOCATION_BOS.isEmpty()) {
			LocationService locationService = CrownServiceLocator.INSTANCE
					.getCrownService(LocationService.class);
			LOCATION_BOS.clear();
			LOCATION_BOS.addAll(locationService.findAllLocations());
		}
		return LOCATION_BOS;
	}

	public static final List<RoleBO> getRoleBos() {
		if (ROLE_BOS.isEmpty()) {
			CrownUserService service = CrownServiceLocator.INSTANCE
					.getCrownService(CrownUserService.class);
			DepartmentService departmentService = CrownServiceLocator.INSTANCE
					.getCrownService(DepartmentService.class);

			DESIGNATION_BOS.clear();
			DEPARTMENT_BOS.clear();
			ROLE_BOS.clear();

			DESIGNATION_BOS.addAll(service.findAllDesignations());
			ROLE_BOS.addAll(service.findAllRoles());
			DEPARTMENT_BOS.addAll(departmentService.findAllDepartments());
		}
		return ROLE_BOS;
	}

	public static final List<DesignationBO> getDesignationBos() {
		if (DESIGNATION_BOS.isEmpty()) {
			CrownUserService service = CrownServiceLocator.INSTANCE
					.getCrownService(CrownUserService.class);
			DESIGNATION_BOS.clear();
			DESIGNATION_BOS.addAll(service.findAllDesignations());
		}
		return DESIGNATION_BOS;
	}

	public static final List<DepartmentBO> getDepartmentBos() {
		if (DEPARTMENT_BOS.isEmpty()) {
			DepartmentService departmentService = CrownServiceLocator.INSTANCE
					.getCrownService(DepartmentService.class);
			DEPARTMENT_BOS.clear();
			DEPARTMENT_BOS.addAll(departmentService.findAllDepartments());
		}
		return DEPARTMENT_BOS;
	}

	public static final List<CrownEntityBO> getEntityBos() {
		if (ENTITY_BOS.isEmpty()) {
			GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			ENTITY_BOS.clear();
			ENTITY_BOS.addAll(generalService.findAllWholesaleEntities());
			ENTITY_BOS.addAll(generalService.findAllRetailEntities());

			CURRENCY_BOS.clear();
			CURRENCY_BOS.addAll(generalService.findAllCurrencies());

			UOM_BOS.clear();
			UOM_BOS.addAll(generalService.findAllUnitOfMeasures());
		}
		return ENTITY_BOS;
	}

	public static final List<CurrencyBO> getCurrencyBos() {
		if (CURRENCY_BOS.isEmpty()) {
			CURRENCY_BOS.clear();
			GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			CURRENCY_BOS.addAll(generalService.findAllCurrencies());
		}
		return CURRENCY_BOS;
	}

	public static final List<UomBO> getUomBos() {
		if (UOM_BOS.isEmpty()) {
			UOM_BOS.clear();
			GeneralService generalService = CrownServiceLocator.INSTANCE
					.getCrownService(GeneralService.class);
			UOM_BOS.addAll(generalService.findAllUnitOfMeasures());
		}
		return UOM_BOS;
	}

	public static final CurrencyBO getSCR() {
		for (CurrencyBO bo : CURRENCY_BOS) {
			if (bo.getCurrencyCode().equals("INR")) {
				return bo;
			}
		}
		GeneralService generalService = CrownServiceLocator.INSTANCE
				.getCrownService(GeneralService.class);
		return generalService.findCurrencyBO("INR");
	}
}
