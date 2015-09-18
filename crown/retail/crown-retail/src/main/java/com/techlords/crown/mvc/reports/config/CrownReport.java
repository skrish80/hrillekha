//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.28 at 11:05:21 PM IST 
//


package com.techlords.crown.mvc.reports.config;

import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element name="bean-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sub-title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="margin" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="detail-height" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element ref="{http://www.techlords.com/crown-report}column-params" maxOccurs="unbounded"/>
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
    "beanName",
    "title",
    "subTitle",
    "margin",
    "detailHeight",
    "columnParams"
})
@XmlRootElement(name = "crown-report")
public class CrownReport {

    @XmlElement(name = "bean-name", required = true)
    protected String beanName;
    @XmlElement(required = true)
    protected String title;
    @XmlElement(name = "sub-title", required = true)
    protected String subTitle;
    protected int margin;
    @XmlElement(name = "detail-height")
    protected int detailHeight;
    @XmlElement(name = "column-params", required = true)
    protected List<ColumnParams> columnParams;

    /**
     * Gets the value of the beanName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * Sets the value of the beanName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeanName(String value) {
        this.beanName = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the subTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * Sets the value of the subTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubTitle(String value) {
        this.subTitle = value;
    }

    /**
     * Gets the value of the margin property.
     * 
     */
    public int getMargin() {
        return margin;
    }

    /**
     * Sets the value of the margin property.
     * 
     */
    public void setMargin(int value) {
        this.margin = value;
    }

    /**
     * Gets the value of the detailHeight property.
     * 
     */
    public int getDetailHeight() {
        return detailHeight;
    }

    /**
     * Sets the value of the detailHeight property.
     * 
     */
    public void setDetailHeight(int value) {
        this.detailHeight = value;
    }

    /**
     * Gets the value of the columnParams property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the columnParams property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColumnParams().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ColumnParams }
     * 
     * 
     */
    public List<ColumnParams> getColumnParams() {
        if (columnParams == null) {
            columnParams = new ArrayList<ColumnParams>();
        }
        return this.columnParams;
    }

}
