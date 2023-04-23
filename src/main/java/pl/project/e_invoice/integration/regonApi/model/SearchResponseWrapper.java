package pl.project.e_invoice.integration.regonApi.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "root")
public class SearchResponseWrapper {
    private List<CompanyIntegration> data;


    @XmlElement(name = "dane")
    public void setData(List<CompanyIntegration> data) {
        this.data = data;
    }
}
