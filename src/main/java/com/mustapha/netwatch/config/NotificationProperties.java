package com.mustapha.netwatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "netwatch.notifications")
public class NotificationProperties {

    private final Email email = new Email();
    private final Webhook webhook = new Webhook();

    public Email getEmail() {
        return email;
    }

    public Webhook getWebhook() {
        return webhook;
    }

    public static class Email {
        private boolean enabled = false;
        private String to;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }

    public static class Webhook {
        private boolean enabled = false;
        private String url;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
