package com.friends.test.automation.controller.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorResource {
	private ErrorContent content;
	private String logRef;

	public ErrorResource(ErrorContent content, String logRef) {
		this.content = content;
		this.logRef = logRef;
	}

	public static class ErrorContent {
		private List<String> message = new ArrayList<String>();

		public List<String> getMessage() {
			return message;
		}

		public void setMessage(List<String> message) {
			this.message = message;
		}

		public static ErrorContent builder() {
			return new ErrorContent();
		}

		public ErrorContent message(String message) {
			this.message.add(message);
			return this;
		}

		public ErrorContent messages(String[] messages) {
			this.message.addAll(Arrays.asList(messages));
			return this;
		}

		public ErrorContent messages(List<String> messages) {
			this.message.addAll(messages);
			return this;
		}

		public ErrorResource build(String logRef) {
			return new ErrorResource(this, logRef);
		}
	}

	public ErrorContent getContent() {
		return content;
	}

	public void setContent(ErrorContent content) {
		this.content = content;
	}

	public String getLogRef() {
		return logRef;
	}

	public void setLogRef(String logRef) {
		this.logRef = logRef;
	}
}
