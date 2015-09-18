/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License"). You
 * may not use this file except in compliance with the License. You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.techlords.crown.retail;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.logger.web.test.JerseyClientPost;

import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.techlords.crown.business.model.InvoiceItemBO;
import com.techlords.crown.business.model.ItemBO;

/**
 * FXML Controller class
 *
 * @author Angie
 */
public class RetailInvoiceController implements Initializable, ControlledScreen {

	private ScreensController myController;
	@FXML
	private TableView<InvoiceItemBO> tableView;
	@FXML
	private TableView itemAvl;
	@FXML
	private TableView pmtView;

	@FXML
	private TextField itmScan;
	@FXML
	private TextField itmQty;
	@FXML
	private HBox itmScanBox;

	private final Client client = Client.create();

	/**
	 * Initializes the controller class.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		itemAvl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		pmtView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		itmQty.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent t) {
				char ar[] = t.getCharacter().toCharArray();
				char ch = ar[t.getCharacter().toCharArray().length - 1];
				if (!(ch >= '0' && ch <= '9')) {
					t.consume();
				}
			}
		});
		itmScan.textProperty().addListener(new ChangeListener<String>() {
			public void changed(final ObservableValue<? extends String> observableValue,
					final String oldValue, final String newValue) {
				try {
					fetchItems(itmScan.getText());
				} catch (ClientHandlerException | UniformInterfaceException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				itmScan.requestFocus();
			}
			
		});
		
	}

	private void fetchItems(String itmCode) throws JsonParseException, JsonMappingException,
			ClientHandlerException, UniformInterfaceException, IOException {
		if (itmCode.length() < 2) {
			return;
		}
		WebResource webResource = client
				.resource("http://localhost:8080/crown-retail/rest/loginservice/items");
		ClientResponse response = webResource.path(itmCode).get(ClientResponse.class);

		// if (response.getStatus() != 201) {
		// throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		// }

		System.out.println("Output from Server .... \n");
		// System.out.println(response.getEntity(String.class));
		List<ItemBO> output = JerseyClientPost.unmarshalCollection(
				response.getEntity(String.class), ItemBO.class);
		System.out.println(output.size());
		if (output.size() > 1) {
			showItemsDialog(output);
		}
		selectedItem = output.get(0);
		System.out.println("**********************");
	}

	/**
	 * @param output
	 */
	private void showItemsDialog(List<ItemBO> output) {
		Dialog<ItemBO> dialog = new Dialog<>();
		dialog.initModality(Modality.NONE);
		dialog.setTitle("Select Item");
		
		dialog.setHeaderText("Select an Item from the table.");
		dialog.setResizable(true);

		@SuppressWarnings("restriction")
		TableView<ItemBO> itemsTable = new TableView<ItemBO>(new ObservableListWrapper<ItemBO>(
				output));
		TableColumn<ItemBO, String> itemName = new TableColumn<ItemBO, String>("Item Name");
		itemName.setCellValueFactory(new PropertyValueFactory<ItemBO, String>("itemName"));

		TableColumn<ItemBO, String> itemCode = new TableColumn<ItemBO, String>("Item Code");
		itemCode.setCellValueFactory(new PropertyValueFactory<ItemBO, String>("itemCode"));

		TableColumn<ItemBO, Double> itemPrice = new TableColumn<ItemBO, Double>("Item Price");
		itemPrice.setCellValueFactory(new PropertyValueFactory<ItemBO, Double>("itemPrice"));

		TableColumn<ItemBO, Double> uomPrice = new TableColumn<ItemBO, Double>("UOM Price");
		uomPrice.setCellValueFactory(new PropertyValueFactory<ItemBO, Double>("uomPrice"));

		itemsTable.getColumns().addAll(itemName, itemCode, itemPrice, uomPrice);
		itemsTable.setFocusTraversable(true);
		itemsTable.getSelectionModel().select(0);
		BorderPane pane = new BorderPane();
		pane.setCenter(itemsTable);

		dialog.getDialogPane().setContent(pane);

		itemsTable.setRowFactory(tv -> {
			TableRow<ItemBO> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					ItemBO rowData = row.getItem();
					dialog.setResult(rowData);
				}
			});
			return row;
		});
		itemsTable.setOnKeyReleased(event -> {
			if(event.getCode() == KeyCode.ENTER) {
				dialog.setResult(itemsTable.getSelectionModel().getSelectedItem());
			}
		});

		Optional<ItemBO> result = dialog.showAndWait();
		if (result.isPresent()) {
			selectedItem = result.get();
		}
		itemEntered(null);
	}
	private ItemBO selectedItem = null;

	public void setScreenParent(ScreensController screenParent) {
		myController = screenParent;
	}

	public void qtyEntered(ActionEvent event) {
		itmQty.setText("");
		itmScan.setText("");
		itmScan.requestFocus();
	}

	public void itemEntered(ActionEvent event) {
		itmScan.setText(selectedItem.getItemName());
		itmQty.setText("1");
		itmQty.selectAll();
		itmQty.requestFocus();
	}

	@FXML
	private void goToScreen2(ActionEvent event) {
		myController.setScreen(ScreensFramework.loginScreenID);
	}
}
