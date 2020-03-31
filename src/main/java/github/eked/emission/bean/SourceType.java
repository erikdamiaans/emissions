package github.eked.emission.bean;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SourceType {

    private String source;
    @JsonGetter(value = "source_type")
    public String getSource() {
        return source;
    }
}
