package org.ptithcm2021.fashionshop.configure;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("storage")
@Setter
@Getter
public class StorageProperties {
    private String location;
}
