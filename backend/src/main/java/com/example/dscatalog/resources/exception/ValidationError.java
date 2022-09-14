package com.example.dscatalog.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardException{

    private List<FieldMessage> errors = new ArrayList<>();

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message){
        errors.add(new FieldMessage(fieldName,message));
    }
}
