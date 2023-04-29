package pl.project.e_invoice.service.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.project.e_invoice.integration.regonApi.client.RegonApiWebClientActions;
import pl.project.e_invoice.integration.regonApi.client.ReportType;
import pl.project.e_invoice.integration.regonApi.model.CompanyIntegration;
import pl.project.e_invoice.integration.regonApi.model.CompanyModelConverter;

@Service
@RequiredArgsConstructor
public class NipApiService {
    private final RegonApiWebClientActions regenApiClient;
    private final CompanyModelConverter modelConverter;
    private final ReportType reportType = ReportType.PublDaneRaportPrawna;

    public CompanyIntegration getFullCompanyReport(String nip) {
        try {
            String sessionId = regenApiClient.login();
            Thread.sleep(10000);
            CompanyIntegration companyByNip = findCompanyByNip(nip, sessionId);
            return getCompanyReport(companyByNip, sessionId);
        } catch (Exception e) {
        }
        return new CompanyIntegration();
    }

    private CompanyIntegration findCompanyByNip(String nip, String sessionId) throws Exception {
        String xmlPrompt = regenApiClient.search(nip, sessionId);
        return modelConverter.convert(xmlPrompt, nip);
    }

    private CompanyIntegration getCompanyReport(CompanyIntegration companyIntegration, String sessionId) throws Exception {
        String report = regenApiClient.getReport(companyIntegration.getRegon(), reportType, sessionId);
        modelConverter.convertAdditionalInfo(companyIntegration, report);

        regenApiClient.logout(sessionId);
        return companyIntegration;
    }
}
