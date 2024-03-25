package org.example.chat.websocketold;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleMessage {
   private String message;
   private String recipient;
   private String author;
   private Integer count;
}
