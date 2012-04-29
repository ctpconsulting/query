package com.ctp.cdi.query.home;

public class DefaultConversationProvider implements ConversationProvider {

    private static final long serialVersionUID = 1L;

    @Override
    public void begin() {
    }

    @Override
    public void end() {
    }

    @Override
    public boolean isTransient() {
        return true;
    }

}
