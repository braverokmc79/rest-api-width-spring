package net.macaronics.restapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

//ObjectMapper 에 Custom Serializer를 등록해 주어야하는데
// Spring Boot에서 제공하는 @JsonComponent를 사용하면 손쉽게 등록이 가능하다.
@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        errors.getFieldErrors().forEach(e->{
            try{
                gen.writeStartObject();
                gen.writeStringField("objectName",e.getObjectName());
                gen.writeStringField("field",e.getField());
                gen.writeStringField("defaultMessage",e.getDefaultMessage());
                gen.writeStringField("code", e.getCode());
                Object rejectedValue =e.getRejectedValue();
                if(rejectedValue!=null){
                    gen.writeStringField("rejectedValue", rejectedValue.toString());
                }

                gen.writeEndObject();
            }catch (IOException e1){
                e1.printStackTrace();
            }
        });

        errors.getGlobalErrors().stream().forEach(e->{
            try{
                gen.writeStartObject();
                gen.writeStringField("objectName",e.getObjectName());
                gen.writeStringField("defaultMessage",e.getDefaultMessage());
                gen.writeStringField("code", e.getCode());
                gen.writeEndObject();
            }catch (IOException e1){
                e1.printStackTrace();
            }
        });

        gen.writeEndArray();
    }




}
