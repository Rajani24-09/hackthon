package com.mysite.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
@Getter
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class }, resourceType = "mysite/components/content/translator", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TranslatorComponentModel {

    @ValueMapValue
    private String prompt;

    @ValueMapValue
    private String locale;

    @ValueMapValue
    private String translatedText;


}
