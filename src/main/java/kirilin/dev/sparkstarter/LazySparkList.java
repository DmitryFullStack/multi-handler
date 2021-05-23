package kirilin.dev.sparkstarter;

import lombok.Data;
import lombok.experimental.Delegate;

import java.util.List;

@Data
public class LazySparkList implements List {

    @Delegate
    private List content;

    private String ownerId;

    private Class<?> modelClass;

    private String foreignKeyName;

    private String path2Source;

    public boolean initialized(){
        return content != null && !content.isEmpty();
    }

}
