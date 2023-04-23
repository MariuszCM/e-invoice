package pl.project.e_invoice;

import javafx.application.Application;
import javafx.scene.Node;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.InjectionPointLazyFxControllerAndViewResolver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import pl.project.e_invoice.application.JavaFxApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class EInvoiceApplication {

    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }

    @Bean
    public FxWeaver fxWeaver(ConfigurableApplicationContext applicationContext) {
        return new SpringFxWeaver(applicationContext);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public <C, V extends Node> FxControllerAndView<C, V> controllerAndView(FxWeaver fxWeaver,
                                                                           InjectionPoint injectionPoint) {
        return new InjectionPointLazyFxControllerAndViewResolver(fxWeaver)
                .resolve(injectionPoint);
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("pl.project.e_invoice.integration.regonApi.wsdl");
        return marshaller;
    }

    @Bean
    public AxiomSoapMessageFactory createMessageFactory() {
        AxiomSoapMessageFactory axiomSoapMessageFactory = new AxiomSoapMessageFactory();
        axiomSoapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
        return axiomSoapMessageFactory;
    }

    @Bean
    public WebServiceMessageSender createSender() {
        HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
        httpComponentsMessageSender.setReadTimeout((int) TimeUnit.MILLISECONDS.convert(3, TimeUnit.MINUTES));
        httpComponentsMessageSender.setConnectionTimeout((int) TimeUnit.MILLISECONDS.convert(3, TimeUnit.MINUTES));
        return httpComponentsMessageSender;
    }
}
