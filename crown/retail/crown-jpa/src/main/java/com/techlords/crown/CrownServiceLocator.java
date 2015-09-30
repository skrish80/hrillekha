package com.techlords.crown;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.techlords.crown.service.CrownService;
import com.techlords.crown.service.GeneralService;
import com.techlords.crown.service.ItemService;
import com.techlords.crown.service.ReceiptService;

public final class CrownServiceLocator {

	public static final CrownServiceLocator INSTANCE = new CrownServiceLocator();
	private final BeanFactory factory;

	private CrownServiceLocator() {
		// Empty C'tor
		factory = new ClassPathXmlApplicationContext(
				"/com/techlords/crown/app-context.xml");
	}

	public final <T extends CrownService> T getCrownService(
			Class<T> serviceClass) {
		return factory.getBean(serviceClass);
	}
	
	public static void main(String[] args) {
		System.err.println(5.4444 < 5.44441);
		System.err.println(Math.round(5.334444));
		System.err.println(Math.round(5.6334444));
		NumberFormat format = DecimalFormat.getIntegerInstance(Locale.getDefault());
		format.setGroupingUsed(false);
		System.err.println(format.format(23.0d));;
		double curr = -5000;
		curr += 2000;
		System.out.println(format.format(curr));
		
		ItemService ser = INSTANCE.getCrownService(ItemService.class);
		System.err.println(ser.findAllItems());;
	}
}
