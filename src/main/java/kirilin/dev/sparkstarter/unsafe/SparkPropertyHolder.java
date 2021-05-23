package kirilin.dev.sparkstarter.unsafe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ConfigurationProperties(prefix = "spark")
public class SparkPropertyHolder {
    private String appName;
    private String packages2Scan;

}
