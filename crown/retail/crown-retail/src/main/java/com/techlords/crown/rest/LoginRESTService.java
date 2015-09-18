package com.techlords.crown.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.exception.CrownException;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.RoleBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.mvc.CrownModelController;
import com.techlords.crown.mvc.auth.CrownUserDetailsService;
import com.techlords.crown.mvc.util.CrownMVCHelper;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.ItemService;
import com.techlords.crown.service.WarehouseService;
import com.techlords.infra.UserDetails;

@Path("/loginservice")
public class LoginRESTService {
	@SuppressWarnings("unused")
	private static final String OK_STATUS = "{\"status\":\"OK\"}";
	private final List<WarehouseBO> retailShops = new ArrayList<WarehouseBO>();
	private int currentShop;
	private WarehouseBO currentShopBO;
	private boolean retailLogin;

	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {

		String output = "Jersey say : " + msg;

		return Response.status(200).entity(output).build();

	}

	@GET
	@Path("/retailshops")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllRetailShops() {
		retailShops.clear();
		retailShops.addAll(CrownServiceLocator.INSTANCE.getCrownService(WarehouseService.class)
				.findAllRetailShops());
		return Response.ok(retailShops).build();
	}

	@GET
	@Path("/currencies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCurrencies() {
		return Response.ok(
				CrownServiceLocator.INSTANCE.getCrownService(GeneralService.class)
						.findAllCurrencies()).build();
	}

	@GET
	@Path("/uoms")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUoms() {
		return Response.ok(
				CrownServiceLocator.INSTANCE.getCrownService(GeneralService.class)
						.findAllUnitOfMeasures()).build();
	}

	@GET
	@Path("/items")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllItems() {
		return Response.ok(
				CrownServiceLocator.INSTANCE.getCrownService(ItemService.class).findAllItems())
				.build();
	}

	@GET
	@Path("/items/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllItems(@PathParam("param")String itemCode) {
		return Response.ok(
				CrownServiceLocator.INSTANCE.getCrownService(ItemService.class).findByItemCode(itemCode))
				.build();
	}

	private void validateRetailUser(UserDetails details) throws CrownException {
		final CrownUserBO bo = (CrownUserBO) details;
		final RoleBO role = bo.getRoleBO();
		if (role == null) {
			throw new CrownException("Not a valid user account. Contact System Administrator");
		}
		if (role.getRole().equals("RETAIL_INCHARGE") && currentShop < 1) {
			throw new CrownException("Select the Shop to login!");
		}
	}

	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@FormParam("username") String username,
			@FormParam("password") String password, @FormParam("currentShop") Integer currentShop) {

		this.currentShop = currentShop;
		UserDetails details;
		try {
			final CrownUserDetailsService userDetailsService = new CrownUserDetailsService();
			details = userDetailsService.loadUserByUsername(username, password);
			validateRetailUser(details);
			CrownMVCHelper.loadStatics();

			setCurrentShopBO(CrownModelController.getAppModel(currentShop, retailShops));
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		// return HTTP response 200 in case of success
		return Response.ok(details).build();
	}

	/**
	 * @return the currentShop
	 */
	public int getCurrentShop() {
		return currentShop;
	}

	/**
	 * @param currentShop
	 *            the currentShop to set
	 */
	public void setCurrentShop(int currentShop) {
		this.currentShop = currentShop;
	}

	/**
	 * @return the currentShopBO
	 */
	public WarehouseBO getCurrentShopBO() {
		return currentShopBO;
	}

	/**
	 * @param currentShopBO
	 *            the currentShopBO to set
	 */
	public void setCurrentShopBO(WarehouseBO currentShopBO) {
		this.currentShopBO = currentShopBO;
	}

	/**
	 * @return the retailLogin
	 */
	public boolean isRetailLogin() {
		return retailLogin;
	}

	/**
	 * @param retailLogin
	 *            the retailLogin to set
	 */
	public void setRetailLogin(boolean retailLogin) {
		this.retailLogin = retailLogin;
	}

	/**
	 * @return the retailShops
	 */
	public List<WarehouseBO> getRetailShops() {
		return retailShops;
	}
}