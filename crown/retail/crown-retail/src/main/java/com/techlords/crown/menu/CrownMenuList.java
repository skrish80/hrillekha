package com.techlords.crown.menu;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class CrownMenuList<T extends CrownMenu> extends ArrayList<T> {

	@Override
	public boolean add(T menu) {
		if (menu.isDisplayable()) {
			return super.add(menu);
		}
		return false;
	}

}
