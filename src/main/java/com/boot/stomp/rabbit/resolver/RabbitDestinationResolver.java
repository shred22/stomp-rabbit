package com.boot.stomp.rabbit.resolver;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.messaging.simp.user.UserDestinationResult;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RabbitDestinationResolver implements UserDestinationResolver {

    private final SimpUserRegistry userRegistry;
    private String prefix = "/client";

    public RabbitDestinationResolver(SimpUserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    @Override
    public UserDestinationResult resolveDestination(Message<?> message) {
        ParseResult parseResult = parse(message);
        if (parseResult == null) {
            return null;
        }
        String user = parseResult.getUser();
        String sourceDestination = parseResult.getSourceDestination();
        Set<String> targetSet = new HashSet<>();
        targetSet.add(sourceDestination);
        String subscribeDestination = parseResult.getSubscribeDestination();
        return new UserDestinationResult(sourceDestination, targetSet, sourceDestination, user);
    }

    @Nullable
    private ParseResult parse(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        String sourceDestination = SimpMessageHeaderAccessor.getDestination(headers);
        if (sourceDestination == null) {
            return null;
        }
        SimpMessageType messageType = SimpMessageHeaderAccessor.getMessageType(headers);
        if (messageType != null) {
            switch (messageType) {
                case SUBSCRIBE:
                case UNSUBSCRIBE:
                    return parseSubscriptionMessage(message, sourceDestination);
                case MESSAGE:
                    return parseMessage(headers, sourceDestination);
            }
        }
        return null;
    }

    @Nullable
    private ParseResult parseSubscriptionMessage(Message<?> message, String sourceDestination) {
        MessageHeaders headers = message.getHeaders();
        String sessionId = SimpMessageHeaderAccessor.getSessionId(headers);
        if (sessionId == null) {
            log.error("No session id. Ignoring " + message);
            return null;
        }
        int prefixEnd = this.prefix.length() - 1;
        String actualDestination = sourceDestination.substring(prefixEnd);

        Principal principal = SimpMessageHeaderAccessor.getUser(headers);
        String user = (principal != null ? principal.getName() : null);
        Assert.isTrue(user == null || !user.contains("%2F"), "Invalid sequence \"%2F\" in user name: " + user);
        Set<String> sessionIds = Collections.singleton(sessionId);
        return new ParseResult(sourceDestination, actualDestination, sourceDestination, sessionIds, user);
    }

    private ParseResult parseMessage(MessageHeaders headers, String sourceDest) {
        int prefixEnd = this.prefix.length();
        int userEnd = sourceDest.indexOf('/', prefixEnd);
        Assert.isTrue(userEnd > 0, "Expected destination pattern \"/user/{userId}/**\"");
        String actualDest = sourceDest.substring(userEnd);
        String subscribeDest = this.prefix.substring(0, prefixEnd - 1) + actualDest;
        String userName = sourceDest.substring(prefixEnd, userEnd);
        userName = StringUtils.replace(userName, "%2F", "/");

        String sessionId = SimpMessageHeaderAccessor.getSessionId(headers);
        Set<String> sessionIds;
        if (userName.equals(sessionId)) {
            userName = null;
            sessionIds = Collections.singleton(sessionId);
        }
        else {
            sessionIds = getSessionIdsByUser(userName, sessionId);
        }

        return new ParseResult(sourceDest, actualDest, subscribeDest, sessionIds, userName);
    }

    private Set<String> getSessionIdsByUser(String userName, @Nullable String sessionId) {
        Set<String> sessionIds;
        SimpUser user = this.userRegistry.getUser(userName);
        if (user != null) {
            if (sessionId != null && user.getSession(sessionId) != null) {
                sessionIds = Collections.singleton(sessionId);
            }
            else {
                Set<SimpSession> sessions = user.getSessions();
                sessionIds = new HashSet<>(sessions.size());
                for (SimpSession session : sessions) {
                    sessionIds.add(session.getId());
                }
            }
        }
        else {
            sessionIds = Collections.emptySet();
        }
        return sessionIds;
    }
    public void setUserDestinationPrefix(String prefix) {
        Assert.hasText(prefix, "Prefix must not be empty");
        this.prefix = (prefix.endsWith("/") ? prefix : prefix + "/");
    }
    private static class ParseResult {

        private final String sourceDestination;

        private final String actualDestination;

        private final String subscribeDestination;

        private final Set<String> sessionIds;

        @Nullable
        private final String user;

        public ParseResult(String sourceDest, String actualDest, String subscribeDest,
                           Set<String> sessionIds, @Nullable String user) {

            this.sourceDestination = sourceDest;
            this.actualDestination = actualDest;
            this.subscribeDestination = subscribeDest;
            this.sessionIds = sessionIds;
            this.user = user;
        }

        public String getSourceDestination() {
            return this.sourceDestination;
        }

        public String getActualDestination() {
            return this.actualDestination;
        }

        public String getSubscribeDestination() {
            return this.subscribeDestination;
        }

        public Set<String> getSessionIds() {
            return this.sessionIds;
        }

        @Nullable
        public String getUser() {
            return this.user;
        }
    }
}
