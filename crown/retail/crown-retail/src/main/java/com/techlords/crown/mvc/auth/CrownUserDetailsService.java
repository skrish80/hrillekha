package com.techlords.crown.mvc.auth;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.naming.AuthenticationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.techlords.crown.CrownServiceLocator;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.WarehouseBO;
import com.techlords.crown.service.LoginService;
import com.techlords.infra.UserDetails;
import com.techlords.infra.rolemap.LinkUrl;
import com.techlords.infra.rolemap.RoleMapping;

public final class CrownUserDetailsService {
	
	private static final Logger LOGGER = Logger.getLogger(CrownUserDetailsService.class);

	private static final Map<String, List<String>> ROLE_MAPPING = new Hashtable<String, List<String>>();
	private static final String ALL_ROLES = "ALL_ROLES";

	static {
		loadRoleMapping();
	}

	private static void loadRoleMapping() {
		try {
			final JAXBContext context = JAXBContext.newInstance(
					RoleMapping.class.getPackage().getName(),
					RoleMapping.class.getClassLoader());
			final Unmarshaller unmarshaller = context.createUnmarshaller();
			final RoleMapping roleMapping = (RoleMapping) unmarshaller
					.unmarshal(CrownUserDetailsService.class
							.getResourceAsStream("role-mapping.xml"));
			for (LinkUrl url : roleMapping.getLinkUrls()) {
				ROLE_MAPPING.put(url.getLinkName(), url.getRoles());
			}
		} catch (JAXBException e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	protected void additionalAuthenticationChecks(UserDetails userDetails,
			String password) throws AuthenticationException {
		String encodedPassword = createMD5Password(password);
		if (userDetails.getPassword().equals(encodedPassword)) {
			// Good, the user is authenticated
			return;
		}
		if (!userDetails.isEnabled()) {
			throw new AuthenticationException(
					"User Account is disabled. Contact your System Administrator");
		}
		throw new AuthenticationException(
				"Password mismatch, User not authenticated...");
	}

	// private String encrypt(String plaintext, String algorithm, String
	// encoding) {
	// MessageDigest msgDigest = null;
	// String hashValue = null;
	// try {
	// msgDigest = MessageDigest.getInstance(algorithm);
	// msgDigest.update(plaintext.getBytes(encoding));
	// byte rawByte[] = msgDigest.digest();
	// hashValue = (new BASE64Encoder()).encode(rawByte);
	//
	// } catch (NoSuchAlgorithmException e) {
	// System.out.println("No Such Algorithm Exists");
	// } catch (UnsupportedEncodingException e) {
	// System.out.println("The Encoding Is Not Supported");
	// }
	// return hashValue;
	// }

	public UserDetails loadUserByUsername(String username, String password)
			throws AuthenticationException {
		LoginService loginService = CrownServiceLocator.INSTANCE
				.getCrownService(LoginService.class);
		CrownUserBO details = loginService.login(username);
		if (details == null) {
			throw new AuthenticationException("User \"" + username
					+ "\" not found");
		}
		additionalAuthenticationChecks(details, password);
		return details;
	}

	public static boolean isLinkAllowed(String linkUrl) {
		final CrownUserBO user = getCurrentUser();
		if (user == null) {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("login.jsf");
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		String role = user.getRoleBO().getRole();
		List<String> roles = ROLE_MAPPING.get(linkUrl);
		if (roles == null) {
			return true;
		}
		return roles.contains(ALL_ROLES) || roles.contains(role);
	}

	public final static CrownUserBO getCurrentUser() {
		Map<?, ?> sessionMap = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		if (sessionMap == null) {
			return null;
		}
		return (CrownUserBO) sessionMap.get("currentUser");
	}

	public final static WarehouseBO getCurrentShop() {
		Map<?, ?> sessionMap = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		if (sessionMap == null) {
			return null;
		}
		return (WarehouseBO) sessionMap.get("currentShop");
	}

	/**
	 * Encrypt the password using MD5
	 * 
	 * @param password
	 * @return
	 */
	public static String createMD5Password(String password) {
		try {
			MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
			mdAlgorithm.update(password.getBytes());

			byte[] digest = mdAlgorithm.digest();
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < digest.length; i++) {
				password = Integer.toHexString(0xFF & digest[i]);

				if (password.length() < 2) {
					password = "0" + password; //$NON-NLS-1$
				}

				hexString.append(password);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException ex) {
			LOGGER.error(ex.getMessage(), ex);
			ex.printStackTrace();
		}
		return ""; //$NON-NLS-1$
	}
}
