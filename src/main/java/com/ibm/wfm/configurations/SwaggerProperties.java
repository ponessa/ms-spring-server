package com.ibm.wfm.configurations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("swagger")
public class SwaggerProperties {
	
	private String title;
	private String description;
	private String termsOfServiceUrl;
	private String contactName;
	private String contactUrl;
	private String contactEmail;
	private String version;
	
	public SwaggerProperties() {}
	
	public SwaggerProperties(String title, String description, String termsOfServiceUrl, String contactName,
			String contactUrl, String contactEmail, String version) {
		super();
		this.title = title;
		this.description = description;
		this.termsOfServiceUrl = termsOfServiceUrl;
		this.contactName = contactName;
		this.contactUrl = contactUrl;
		this.contactEmail = contactEmail;
		this.version = version;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTermsOfServiceUrl() {
		return termsOfServiceUrl;
	}
	public void setTermsOfServiceUrl(String termsOfServiceUrl) {
		this.termsOfServiceUrl = termsOfServiceUrl;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactUrl() {
		return contactUrl;
	}
	public void setContactUrl(String contactUrl) {
		this.contactUrl = contactUrl;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));

    }


}
