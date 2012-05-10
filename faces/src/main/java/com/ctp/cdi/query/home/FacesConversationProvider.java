package com.ctp.cdi.query.home;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;

@FacesHome
public class FacesConversationProvider implements ConversationProvider {

    private static final long serialVersionUID = 1L;

    @Inject
    private Conversation conversation;

    @Override
    public void begin() {
        conversation.begin();
    }

    @Override
    public void end() {
        conversation.end();
    }

    @Override
    public boolean isTransient() {
        return conversation.isTransient();
    }

}
