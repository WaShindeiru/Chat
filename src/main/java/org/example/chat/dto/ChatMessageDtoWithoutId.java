package org.example.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ChatMessageDtoWithoutId {
   private String messageText;
   private LocalDateTime sentDateTime;
   private ChatUserDto sentBy;
}
