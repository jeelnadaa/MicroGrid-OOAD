package com.microgrid.util;

/**
 * Helper for flash message handling in HTTP requests.
 * It follows SOLID single responsibility by isolating message creation and storage.
 * It also applies GRASP Controller by keeping response attribute management in one place.
 */

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility for flash messages (similar to Flask's flash()).
 * Stores messages in the HTTP session and retrieves them once.
 */
public class FlashMessage {

    private static final String FLASH_KEY = "_flash_messages";

    private final String message;
    private final String category; // "success", "danger", "info", "warning"

    public FlashMessage(String message, String category) {
        this.message = message;
        this.category = category;
    }

    public String getMessage() { return message; }
    public String getCategory() { return category; }

    /**
     * Add a flash message to the session.
     */
    @SuppressWarnings("unchecked")
    public static void flash(HttpServletRequest request, String message, String category) {
        HttpSession session = request.getSession();
        List<FlashMessage> messages = (List<FlashMessage>) session.getAttribute(FLASH_KEY);
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(new FlashMessage(message, category));
        session.setAttribute(FLASH_KEY, messages);
    }

    /**
     * Retrieve and clear all flash messages from the session.
     */
    @SuppressWarnings("unchecked")
    public static List<FlashMessage> getMessages(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return new ArrayList<>();

        List<FlashMessage> messages = (List<FlashMessage>) session.getAttribute(FLASH_KEY);
        session.removeAttribute(FLASH_KEY);
        return messages != null ? messages : new ArrayList<>();
    }
}
