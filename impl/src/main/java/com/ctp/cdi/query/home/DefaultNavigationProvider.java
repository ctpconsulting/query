package com.ctp.cdi.query.home;

public class DefaultNavigationProvider implements NavigationProvider {

    private static final long serialVersionUID = 1L;

    @Override
    public Object create() {
        return null;
    }

    @Override
    public boolean isPostback() {
        return false;
    }

    @Override
    public Object view(Object primaryKey) {
        return null;
    }

    @Override
    public Object exception() {
        return null;
    }

    @Override
    public Object search() {
        return null;
    }

}
