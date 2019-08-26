package com.friends.test.automation.controller.driver;

public class StatusResource {
	private String sessionId;
	private int status;
	private StatusValue value;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public StatusValue getValue() {
		return value;
	}

	public void setValue(StatusValue value) {
		this.value = value;
	}

	public static class StatusValue {
		private StatusBuild build;
		private String message;
		private StatusOS os;
		boolean ready;

		public StatusBuild getBuild() {
			return build;
		}

		public void setBuild(StatusBuild build) {
			this.build = build;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public StatusOS getOs() {
			return os;
		}

		public void setOs(StatusOS os) {
			this.os = os;
		}

		public boolean isReady() {
			return ready;
		}

		public void setReady(boolean ready) {
			this.ready = ready;
		}

	}

	public static class StatusBuild {
		private String version;

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}

	public static class StatusOS {
		private String arch;
		private String name;
		private String version;

		public String getArch() {
			return arch;
		}

		public void setArch(String arch) {
			this.arch = arch;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}
}
