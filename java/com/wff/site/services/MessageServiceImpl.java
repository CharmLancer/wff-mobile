package com.wff.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

	public class Message {
		private final String message;

		public Message(final String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	@Autowired
	SimpMessagingTemplate template;

	@Override
	public void send(final String message) {
		template.convertAndSend("/topic/station", new Message(message));
	}

}
