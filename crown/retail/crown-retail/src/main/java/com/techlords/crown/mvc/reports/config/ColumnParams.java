//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.28 at 11:05:21 PM IST 
//


package com.techlords.crown.mvc.reports.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="column-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="column-title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="class-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="column-width" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="criteria-column" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "columnName",
    "columnTitle",
    "className",
    "columnWidth",
    "criteriaColumn"
})
@XmlRootElement(name = "column-params")
public class ColumnParams {

    @XmlElement(name = "column-name", required = true)
    protected String columnName;
    @XmlElement(name = "column-title", required = true)
    protected String columnTitle;
    @XmlElement(name = "class-name", required = true)
    protected String className;
    @XmlElement(name = "column-width")
    protected int columnWidth;
    @XmlElement(name = "criteria-column")
    protected boolean criteriaColumn;

    /**
     * Gets the value of the columnName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Sets the value of the columnName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnName(String value) {
        this.columnName = value;
    }

    /**
     * Gets the value of the columnTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumnTitle() {
        return columnTitle;
    }

    /**
     * Sets the value of the columnTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnTitle(String value) {
        this.columnTitle = value;
    }

    /**
     * Gets the value of the className property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the value of the className property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassName(String value) {
        this.className = value;
    }

    /**
     * Gets the value of the columnWidth property.
     * 
     */
    public int getColumnWidth() {
        return columnWidth;
    }

    /**
     * Sets the value of the columnWidth property.
     * 
     */
    public void setColumnWidth(int value) {
        this.columnWidth = value;
    }

    /**
     * Gets the value of the criteriaColumn property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCriteriaColumn() {
        return criteriaColumn;
    }

    /**
     * Sets the value of the criteriaColumn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCriteriaColumn(boolean value) {
        this.criteriaColumn = value;
    }

}
