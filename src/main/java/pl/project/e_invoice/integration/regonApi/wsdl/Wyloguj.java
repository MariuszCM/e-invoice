//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
//


package pl.project.e_invoice.integration.regonApi.wsdl;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pIdentyfikatorSesji" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pIdentyfikatorSesji"
})
@XmlRootElement(name = "Wyloguj")
public class Wyloguj {

    @XmlElement(nillable = true)
    protected String pIdentyfikatorSesji;

    /**
     * Gets the value of the pIdentyfikatorSesji property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPIdentyfikatorSesji() {
        return pIdentyfikatorSesji;
    }

    /**
     * Sets the value of the pIdentyfikatorSesji property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPIdentyfikatorSesji(String value) {
        this.pIdentyfikatorSesji = value;
    }

}
