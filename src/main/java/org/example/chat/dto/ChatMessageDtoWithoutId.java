package org.example.chat.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ChatMessageDtoWithoutId {
   private String messageText;
   private Date sentDateTime;
   private ChatUserDto sentBy;
}
