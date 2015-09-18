package com.techlords.crown.mvc;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.log4j.Logger;

import com.techlords.infra.AppModel;

@ManagedBean
@SessionScoped
public class CrownModelConverter {
	
	private static final Logger LOGGER = Logger.getLogger(CrownModelConverter.class);

	public Converter getCrownModelConverter(final List<AppModel> modelObjects) {
		return new Converter() {

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null || value.equals("")) {
					return null;
				}
				Integer id = ((AppModel) value).getId();

				return (id != null) ? id.toString() : null;
			}

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {
				if (value == null) {
					return null;
				}
				int id;
				try {
					id = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					LOGGER.error(e.getMessage(), e);
					return null;
				}

				return CrownModelController.getAppModel(id, modelObjects);
			}
		};
	}
}
