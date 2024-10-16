package com.mysite.core.beans;
//import lombok.Data;
import lombok.Data;

import java.util.List;
import java.util.ArrayList;
@Data
public class TranslationRequest {

    private final int max_tokens;
    private final String model;
    private List<Message> messages;

    public TranslationRequest(String prompt,String model, String role)
    {
        this.max_tokens=200;
        this.model=model;
        this.messages=new ArrayList<>();
        Message message=new Message();
        message.setRole(role);
        message.setContent(prompt);
        this.messages.add(message);
    }

}
