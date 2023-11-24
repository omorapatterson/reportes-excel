package catalogo.reportes.core.utils;

import catalogo.reportes.core.catalogo.db.ParamsDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class S3Config {

    private String s3Id;
    private String s3Key;

    @Autowired
    private ParamsDAO paramsDAO;

    public S3Config() { }

    public S3Config(String s3Id, String s3Key) {

        this.s3Id = s3Id;
        this.s3Key = s3Key;
    }

    public String getS3Id() {
        return s3Id;
    }

    public void setS3Id(String s3Id) {
        this.s3Id = s3Id;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public S3Config s3Key(String s3Key) {
        this.s3Key = s3Key;
        return this;
    }

    public S3Config s3Id(String s3Id) {
        this.s3Id = s3Id;
        return this;
    }

}